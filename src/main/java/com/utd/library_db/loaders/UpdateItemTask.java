package com.utd.library_db.loaders;

import com.utd.library_db.database.DBTask;
import com.utd.library_db.repositories.ItemCreate;
import com.utd.library_db.repositories.ItemRepository;
import com.utd.library_db.repositories.ItemUpdate;
import javafx.concurrent.WorkerStateEvent;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class UpdateItemTask extends DBTask<Boolean> {
    private static final Logger logger = Logger.getLogger(UpdateItemTask.class.getName());
    private Consumer<Boolean> onSuccessConsumer;
    private final ItemUpdate updateItem;

    public UpdateItemTask(Consumer<Boolean> onSuccessConsumer, ItemUpdate newItem) {
        setupSuccessConsumer(onSuccessConsumer);
        this.updateItem = newItem;

    }

    @Override
    protected Boolean call() throws Exception {
        try (Connection con = getConnection()) {
            ItemRepository itemRepository = new ItemRepository(con);
            itemRepository.updateItem(updateItem);
            logger.info("Update item");
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
