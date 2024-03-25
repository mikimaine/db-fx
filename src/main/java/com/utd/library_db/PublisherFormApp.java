package com.utd.library_db;

import com.utd.library_db.loaders.AddPublisherTask;
import com.utd.library_db.repositories.PublisherCreate;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class PublisherFormApp  {

    private final Consumer<Boolean> onSuccessAddPublisher;
    private final ExecutorService databaseExecutor;

    public  PublisherFormApp(ExecutorService databaseExecutor, Consumer<Boolean> onSuccessAddPublisher) {
        this.databaseExecutor = databaseExecutor;
        this.onSuccessAddPublisher =onSuccessAddPublisher;
        showPublisherForm();
    }
    private void showPublisherForm() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        TextField nameField = new TextField();
        TextField phoneNumberField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneNumberField, 1, 1);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            if (validatePublisher(nameField.getText(), phoneNumberField.getText())) {
                // Proceed with saving if validation passes
                savePublisher(nameField.getText(), phoneNumberField.getText());
                stage.close();
            }
        });

        grid.add(saveButton, 1, 2);

        Scene scene = new Scene(grid);
        stage.setTitle("Add Publisher");
        stage.setScene(scene);
        stage.showAndWait();
    }

    // Implement the savePublisher method to save the publisher to the database
    private void savePublisher(String name, String phoneNumber) {
        AddPublisherTask addPublisherTask = new AddPublisherTask((Boolean done) -> {
            onSuccessAddPublisher.accept(true);
        }, new PublisherCreate(name,phoneNumber));
        databaseExecutor.execute(addPublisherTask);
    }

    private boolean validatePublisher(String name, String phoneNumber) {
        if (name.isEmpty()) {
            showAlert("Name cannot be empty.");
            return false;
        }

        if (name.length() > 45) {
            showAlert("Name cannot be more than 45 characters.");
            return false;
        }

        if (phoneNumber.isEmpty()) {
            showAlert("Phone number cannot be empty.");
            return false;
        }

        if (phoneNumber.length() > 12) {
            showAlert("Phone number cannot be more than 12 characters.");
            return false;
        }
        // This regex will need to be adjusted to fit the specific phone number format requirements you have
        if (!phoneNumber.matches("\\d{10,12}")) {
            showAlert("Phone number must be 10 to 12 digits.");
            return false;
        }

        return true;
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setContentText(content);
        alert.showAndWait();
    }

}
