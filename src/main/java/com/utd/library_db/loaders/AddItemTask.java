package com.utd.library_db.loaders;

import com.utd.library_db.database.DBTask;
import com.utd.library_db.repositories.ItemCreate;
import com.utd.library_db.repositories.ItemRepository;
import javafx.concurrent.WorkerStateEvent;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Task for adding a new book item to the database asynchronously.
 * Extends DBTask to use database connection handling and asynchronous execution capabilities.
 */
public class AddItemTask extends DBTask<Boolean> {
    private static final Logger logger = Logger.getLogger(AddItemTask.class.getName());
    private Consumer<Boolean> onSuccessConsumer;
    private final ItemCreate newItem;

    /**
     * Constructor for AddItemTask.
     * @param onSuccessConsumer Consumer callback to be called upon successful execution of the task.
     * @param newItem The new item to be added to the database.
     */
    public AddItemTask(Consumer<Boolean> onSuccessConsumer, ItemCreate newItem) {
        setupSuccessConsumer(onSuccessConsumer);
        this.newItem = newItem;

    }

    /**
     * The main execution method of the task, adding an item to the database.
     * @return true if the operation is successful.
     * @throws Exception if there are any issues executing the task.
     */
    @Override
    protected Boolean call() throws Exception {
        try (Connection con = getConnection()) {
            ItemRepository itemRepository = new ItemRepository(con);
            itemRepository.addItem(newItem);
            logger.info("New item added");
        }
        return true;
    }

    /**
     * Sets up the onSuccess consumer and the succeeded event handler for the task.
     * @param onSuccessConsumer The consumer to call on success.
     */
    private void setupSuccessConsumer(Consumer<Boolean> onSuccessConsumer) {
        this.onSuccessConsumer = onSuccessConsumer;
        setOnSucceeded(this::onSucceeded);
    }

    /**
     * Called when the task has succeeded.
     * @param event The event associated with the task's success.
     */
    private void onSucceeded(WorkerStateEvent event) {
        if (event.getEventType() == WorkerStateEvent.WORKER_STATE_SUCCEEDED && onSuccessConsumer != null) {
            onSuccessConsumer.accept(getValue());
        }
    }
}
