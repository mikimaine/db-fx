package com.utd.library_db.loaders;

import com.utd.library_db.database.DBTask;
import com.utd.library_db.repositories.PublisherCreate;
import com.utd.library_db.repositories.PublisherRepository;
import javafx.concurrent.WorkerStateEvent;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Task for adding a publisher to the database asynchronously.
 * Extends DBTask to use database connection handling and asynchronous execution capabilities.
 */
public class AddPublisherTask extends DBTask<Boolean> {
    private static final Logger logger = Logger.getLogger(AddPublisherTask.class.getName());
    private Consumer<Boolean> onSuccessConsumer;
    private final PublisherCreate newItem;

    /**
     * Constructor for AddPublisherTask.
     * @param onSuccessConsumer Consumer callback to be called upon successful execution of the task.
     * @param newItem The new publisher to be added to the database.
     */
    public AddPublisherTask(Consumer<Boolean> onSuccessConsumer, PublisherCreate newItem) {
        setupSuccessConsumer(onSuccessConsumer);
        this.newItem = newItem;

    }

    /**
     * The main execution method of the task, adding a publisher to the database.
     * @return true if the operation is successful.
     * @throws Exception if there are any issues executing the task.
     */
    @Override
    protected Boolean call() throws Exception {
        try (Connection con = getConnection()) {
            PublisherRepository publisherRepository = new PublisherRepository(con);
            publisherRepository.addPublisher(newItem);
            logger.info("New publisher added");
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
