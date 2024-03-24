package com.utd.library_db.loaders;

import com.utd.library_db.database.DBTask;
import com.utd.library_db.repositories.Publisher;
import com.utd.library_db.repositories.PublisherRepository;
import javafx.concurrent.WorkerStateEvent;

import java.sql.Connection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class PublisherTask extends DBTask<List<Publisher>> {
    private static final Logger logger = Logger.getLogger(PublisherTask.class.getName());
    private final Consumer<List<Publisher>> onSuccessConsumer;

    public PublisherTask(Consumer<List<Publisher>> onSuccessConsumer) {
        this.onSuccessConsumer = onSuccessConsumer;
        setOnSucceeded(this::onSucceeded);
    }

    @Override
    protected List<Publisher> call() throws Exception {
        try (Connection con = getConnection()) {
            PublisherRepository publisherRepository = new PublisherRepository(con);
            logger.info("Retrieved all publishers");
            return publisherRepository.getPublishers();
        }
    }

    private void onSucceeded(WorkerStateEvent event) {
        if (event.getEventType() == WorkerStateEvent.WORKER_STATE_SUCCEEDED && onSuccessConsumer != null) {
            onSuccessConsumer.accept(getValue());
        }
    }
}
