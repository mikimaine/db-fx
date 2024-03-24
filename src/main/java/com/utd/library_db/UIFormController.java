package com.utd.library_db;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import com.utd.library_db.loaders.*;
import com.utd.library_db.repositories.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Controller class for managing the user interface of the library database application.
 * This class handles interactions between the UI components (FXML elements) and the underlying data model.
 * It includes methods for initializing the UI, handling user input, performing CRUD operations, and managing form states.
 * The controller integrates with database tasks executed by an ExecutorService to fetch, add, update, or delete library items.
 * Various event handlers are implemented to respond to user actions such as adding, searching, editing, deleting, and clearing form fields.
 * Additionally, the controller provides methods for validating user input, updating UI components, and displaying confirmation dialogs.
 */
public class UIFormController {
    @FXML
    public DatePicker publicationDatePicker;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorsField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ComboBox<Publisher> publisherComboBox;
    @FXML
    private TableView<ItemListView> booksTable;
    @FXML
    private Label statusLabel;
    @FXML
    private GridPane formGridPane;
    @FXML
    private FlowPane authorTagsPane;
    @FXML
    private TableColumn<ItemListView, String> authorsColumn;
    @FXML
    private TableColumn<ItemListView, String> titleColumn;
    @FXML
    private TableColumn<ItemListView, String> isbnColumn;
    @FXML
    private TableColumn<ItemListView, String> descriptionColumn;
    @FXML
    private Button addSaveButton;
    @FXML
    private Pagination booksPagination;
    @FXML
    private ComboBox<String> languageComboBox;

    private FORM_STATUS currentStatus = FORM_STATUS.SEARCH_MODE;
    private ExecutorService databaseExecutor;
    private List<Publisher> publishers;
    private List<Author> authorsList;
    private ContextMenu contextMenu;

    /**
     * Sets the executor service for database operations and initiates data fetching for publishers, items, and authors.
     */
    public void setExecutorService(ExecutorService databaseExecutor) {
        this.databaseExecutor = databaseExecutor;
        getPublishers();
        getItems();
        getAuthors();
    }

    /**
     * Initializes the controller's UI components.
     * This method sets up table columns, form fields, language selection, and context menu.
     */
    @FXML
    private void initialize() {
        initializeTableColumns();
        initializeFormFields();
        initializeLanguageComboBox();
        initializeContextMenu();
    }

    /**
     * Initializes the table columns and sets up their cell value factories to display book data.
     */
    private void initializeTableColumns() {
        isbnColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().isbn()));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().title()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().description()));
        authorsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().authors()));
    }

    /**
     * Initializes the form fields with specific UI settings and listeners for validation and interaction.
     */
    private void initializeFormFields() {
        GridPane.setVgrow(titleField, Priority.ALWAYS);
        formGridPane.setMaxHeight(Double.MAX_VALUE);
        isbnField.focusedProperty().addListener(validateISBNFieldChange());
        initializeDatePicker();
        setDescriptionFieldSettings();
        setAuthorsFieldKeyPressedListener();
    }

    private void initializeDatePicker() {
        publicationDatePicker.setValue(LocalDate.now());
        publicationDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            @Override
            public LocalDate fromString(String string) {
                return LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });
    }
    private void initializeLanguageComboBox() {
        languageComboBox.setItems(FXCollections.observableArrayList(Util.COMMON_LANGUAGES));
    }

    /**
     * Initializes the context menu with 'Edit' and 'Delete' options.
     * 'Edit' is bound to CTRL+E and 'Delete' to the DELETE key.
     * Actions for these menu items are defined in their respective handler methods.
     */
    private void initializeContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem editMenuItem = createItem("Edit", new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        editMenuItem.setOnAction(this::handleEditStateAction);

        MenuItem deleteMenuItem = createItem("Delete", new KeyCodeCombination(KeyCode.DELETE));
        deleteMenuItem.setOnAction(actionEvent-> handleDelete());
        contextMenu.getItems().addAll(editMenuItem,deleteMenuItem);
    }

    /**
     * Handles the action to edit the selected book's data.
     * Populates the form with the selected book's data and switches to update mode.
     */
    private void handleEditStateAction(ActionEvent event) {
        ItemListView selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            populateFormWithBookData(selectedBook);
            switchToMode(FORM_STATUS.UPDATE_MODE);
        }
    }

    /**
     * Creates a menu item with specified text and keyboard accelerator.
     */
    private MenuItem createItem(String text, KeyCombination accelerator) {
        MenuItem item = new MenuItem(text);
        item.setAccelerator(accelerator);
        return item;
    }

    /**
     * Populates the form fields with data from the selected book.
     * Sets the title, ISBN, and description fields, and initializes the language selection.
     * Authors are split and added as tags to the form.
     */
    private void populateFormWithBookData(ItemListView selectedBook) {
        titleField.setText(selectedBook.title());
        isbnField.setText(selectedBook.isbn());
        descriptionField.setText(selectedBook.description());
        descriptionField.setDisable(false);
        languageComboBox.setValue("English");
        for (String author : selectedBook.authors().split(",")) {
            addTag(author);
        }
    }

    private void setDescriptionFieldSettings() {
        descriptionField.setDisable(true);
        descriptionField.setPrefRowCount(3);
    }

    /**
     * Sets a key pressed listener for the authorsField input field.
     * When the ENTER key is pressed and the field is not empty,
     * the input text is added as a tag, and the field is cleared.
     */
    private void setAuthorsFieldKeyPressedListener() {
        authorsField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !authorsField.getText().isEmpty()) {
                addTag(authorsField.getText());
                authorsField.clear();
            }
        });
    }

    /**
     * Creates a change listener for ISBN field validation.
     * The listener checks the validity of the ISBN input when the field loses focus.
     * If the input is invalid, it adds a style class to indicate the error.
     */
    private ChangeListener<Boolean> validateISBNFieldChange() {
        return (obs, oldVal, newVal) -> {
            if (!newVal) { // Focus lost
                if (!Util.validateISBN(isbnField.getText())) {
                    isbnField.getStyleClass().add("text-field-invalid");
                } else {
                    isbnField.getStyleClass().remove("text-field-invalid");
                }
            }
        };
    }

    /**
     * Adds a new tag to the author tags pane if it does not already exist.
     * Each tag is represented as an HBox containing a label and a remove button,
     * styled appropriately to appear as a 'pill'.
     */
    private void addTag(String text) {
//        authorsField.setStyle("-fx-background-color: black");
        boolean alreadyExists = authorTagsPane.getChildren().stream()
                .anyMatch(v -> v.getId().equals(text));

        if (alreadyExists) {
            return;
        }

        HBox pill = new HBox(2);
        pill.setId(text);
        pill.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(text);
//        authorTagsPane.setStyle("-fx-background-color: dodgerblue;-fx-font-size: 12; -fx-text-fill: white; -fx-padding: 5 1 5 5; -fx-border-radius: 15; -fx-background-radius: 15;");

        Button removeButton = new Button("x");
        removeButton.setOnAction(e -> authorTagsPane.getChildren().remove(pill));
        removeButton.setStyle("-fx-text-fill: white;-fx-font-size: 12;-fx-background-color: none;-fx-cursor: hand;");

        pill.getChildren().addAll(label, removeButton);
        pill.setStyle("-fx-background-radius: 15; -fx-padding: 1 1 1 5; -fx-background-color: dodgerblue;");

        authorTagsPane.getChildren().add(pill);
    }

    /**
     * Asynchronously retrieves items and updates the item list in the UI.
     * Executes ItemListTask with specified parameters and a callback to update the UI upon completion.
     */
    private void getItems() {
        ItemListTask itemListTask = new ItemListTask(this::updateItemList, null, null, null, null);
        databaseExecutor.execute(itemListTask);
    }

    /**
     * Updates the item list table in the UI, sets pagination and styles for the books table.
     * This method configures the pagination controls, applies styling to the table,
     * and loads the given list of items into the table view asynchronously.
     */
    private void updateItemList(List<ItemListView> itemLists) {
        Platform.runLater(() -> {
            booksPagination.setMaxPageIndicatorCount(5);
            booksPagination.setPageCount(1);
            Styles.toggleStyleClass(booksTable, Styles.STRIPED);
            booksTable.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
            booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
            booksPagination.setPageFactory(pageNum -> {
                booksTable.setItems(FXCollections.observableArrayList(itemLists));
                return new StackPane();
            });
            booksTable.refresh();
            booksTable.setContextMenu(contextMenu);
        });
    }

    /**
     * Initiates an asynchronous task to fetch publishers and update the publisher combo box.
     * Executes the PublisherTask in a separate thread to avoid blocking the UI thread.
     */
    private void getPublishers() {
        PublisherTask publisherTask = new PublisherTask(this::updatePublisherComboBox);
        databaseExecutor.execute(publisherTask);
    }

    /**
     * Fetches author data asynchronously and binds it to an auto-completion field.
     * Executes AuthorTask to retrieve authors and updates the authorsList and authorsField
     * with the results on the JavaFX Application thread.
     */
    private void getAuthors() {
        AuthorTask authorTask = new AuthorTask(authors -> {
            this.authorsList = authors;
            Platform.runLater(() -> {
                AutoCompletionBinding<Author> auto =TextFields.bindAutoCompletion(authorsField, FXCollections.observableArrayList(authors));
            });
        });
        databaseExecutor.execute(authorTask);
    }

    /**
     * Updates the publisher combo box with a list of publishers.
     * This method assigns the given publishers list to a class field and
     * sets the combo box items on the JavaFX Application thread.
     */
    private void updatePublisherComboBox(List<Publisher> publishers) {
        this.publishers = publishers;
        Platform.runLater(() -> publisherComboBox.setItems(FXCollections.observableArrayList(publishers)));
    }

    /**
     * Switches the form to a specified mode (Add, Update, or Search),
     * updating the UI elements accordingly to reflect the current status.
     */
    private void switchToMode(FORM_STATUS status) {
        switch (status) {
            case ADD_MODE -> {
                currentStatus = status;
                addSaveButton.setText("Save");
                statusLabel.setText("Save Mode");
                descriptionField.setDisable(false);
            }
            case UPDATE_MODE -> {
                currentStatus = status;
                addSaveButton.setText("Update");
                statusLabel.setText("Update Mode");
                descriptionField.setDisable(false);
            }
            case SEARCH_MODE -> handleClear();
        }

    }

    /**
     * Validates book item data and processes it using the provided BiConsumer.
     * Shows an error alert for validation issues or proceeds with processing the book item for saving or updating.
     * The processItem BiConsumer is expected to handle the actual save or update logic.
     */
    private void validateAndProcessBookItem(BiConsumer<ItemInterface, Boolean> processItem) {
        Alert validation = new Alert(Alert.AlertType.ERROR);
        validation.setHeaderText("Validation Error");

        // Common data extraction
        String title = titleField.getText();
        int[] authorIds = Util.getAuthorIdsFromNameList(getSelectedAuthors(), authorsList);
        String isbn = isbnField.getText();
        Date publicationDate = Date.valueOf(publicationDatePicker.getValue());
        String language = languageComboBox.getValue();
        String description = descriptionField.getText();

        // Common validation
        if (publisherComboBox.getValue() == null) {
            validation.setContentText("Publisher can't be empty");
            validation.show();
            return;
        }
        int publisherId = publisherComboBox.getValue().id();

        if (LocalDate.now().isBefore(publicationDatePicker.getValue())) {
            validation.setContentText("Publication date can't be in the future");
            validation.show();
            return;
        }
        String validationResult = Util.validateBookData(isbn, title, authorIds, publisherId, language, description);
        if (!validationResult.isEmpty()) {
            validation.setContentText(validationResult);
            validation.show();
            return;
        }

        // Process item based on the operation (update or save)
        if (processItem != null) {
            boolean isUpdate = booksTable.getSelectionModel().getSelectedItem() != null;
            ItemInterface item = isUpdate ?
                    new ItemUpdate(booksTable.getSelectionModel().getSelectedItem().id(), title, description, isbn, authorIds, language, publisherId, publicationDate) :
                    new ItemCreate(title, description, isbn, authorIds, publicationDate);
            processItem.accept(item, isUpdate);
        }
    }

    /**
     * Executes either an update or save operation for a book item based on the provided flag.
     * Uses asynchronous tasks to perform the database operations.
     */
    private void handleUpdateOrSave(ItemInterface item, boolean isUpdate) {

        if (isUpdate) {
            UpdateItemTask updateTask = new UpdateItemTask(taskFinished, (ItemUpdate) item);
            databaseExecutor.execute(updateTask);
            return;
        }

        AddItemTask addTask = new AddItemTask(taskFinished, (ItemCreate) item);
        databaseExecutor.execute(addTask);
    }

    private void validateBookItemAndUpdate() {
        validateAndProcessBookItem(this::handleUpdateOrSave);
    }

    private void validateBookItemAndSave() {
        validateAndProcessBookItem(this::handleUpdateOrSave);
    }

    /**
     * Handles the completion of a background task, showing an error alert if the task failed,
     * or performing UI cleanup on success. If the task result is false, it displays an error alert;
     * otherwise, it clears the form fields on the JavaFX Application thread.
     */
    Consumer<Boolean> taskFinished = (Boolean result) -> {
        if(!result) {
            Alert validation = new Alert(Alert.AlertType.ERROR);
            validation.setContentText("There was an error to execute this operation. Please try again.");
            validation.show();
            return;
        }
        Platform.runLater(this::handleClear);
    };

    private Integer getPublisherId(String publisherName) {
        if (publishers != null) {
            Optional<Publisher> optionalPublisher = this.publishers.stream()
                    .filter(v -> v.name().equals(publisherName))
                    .findFirst();

            if (optionalPublisher.isPresent()) {
                return optionalPublisher.get().id();
            } else {
                System.out.println("Publisher not found");
            }
        }
        return null;
    }

    private String[] getSelectedAuthors() {
        List<String> authorList = authorTagsPane.getChildren().stream()
                .map(Node::getId)
                .toList();
        return authorList.toArray(new String[0]);
    }

    /**
     * Displays a confirmation dialog to confirm deletion of the selected item.
     * If confirmed, initiates an asynchronous task to delete the item.
     */
    private void showConfirmationDialog(ItemListView item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete '" + item.title() + "' book?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            DeleteItemListTask deleteItemListTask = new DeleteItemListTask((Boolean done) -> getItems(), item.id());
            databaseExecutor.execute(deleteItemListTask);
        }
    }

    @FXML
    private void handleAddSaveButton() {
        if (currentStatus == FORM_STATUS.SEARCH_MODE) {
            // We are on SEARCH_MODE, so switch to add mode
            switchToMode(FORM_STATUS.ADD_MODE);
        } else if (currentStatus == FORM_STATUS.UPDATE_MODE) {
            // We are on UPDATE_MODE, so lets save the changes to the database
            validateBookItemAndUpdate();
        } else {
            // If we reach this, it means we are on ADD_MODE so let's validate and save the new data
            validateBookItemAndSave();
        }
    }

    @FXML
    private void handleSearch() {
        statusLabel.setText("Searching");
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String[] authors = getSelectedAuthors();
        Integer publisherId = getPublisherId(String.valueOf(publisherComboBox.getValue()));

        ItemListTask itemListTask = new ItemListTask(this::updateItemList, publisherId, isbn, title, authors);
        databaseExecutor.execute(itemListTask);
        statusLabel.setText("Search Mode");
    }

    @FXML
    private void handleDelete() {
        ItemListView selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showConfirmationDialog(selected);
        }
    }

    /**
     * Clears the form and resets the UI to its initial state.
     * Invokes resetForm to clear form fields, fetches items to refresh the table,
     * clears any table selection, and resets UI elements to default search mode settings.
     */
    @FXML
    private void handleClear() {
        resetForm();
        getItems();
        booksTable.getSelectionModel().clearSelection();
        isbnField.getStyleClass().remove("text-field-invalid");
        currentStatus = FORM_STATUS.SEARCH_MODE;
        descriptionField.setDisable(true);
        addSaveButton.setText("Add");
        statusLabel.setText("Search Mode");
    }

    /**
     * Resets all form fields to their default empty or null states.
     */
    private void resetForm() {
        publisherComboBox.setValue(null);
        titleField.setText(null);
        authorsField.setText(null);
        authorTagsPane.getChildren().removeAll();
        authorTagsPane.getChildren().clear();
        isbnField.setText(null);
        descriptionField.setText(null);
    }
}