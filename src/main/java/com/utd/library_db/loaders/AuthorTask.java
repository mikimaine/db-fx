package com.utd.library_db.loaders;

import com.utd.library_db.database.DBTask;
import com.utd.library_db.repositories.Author;
import com.utd.library_db.repositories.AuthorRepository;
import javafx.concurrent.WorkerStateEvent;

import java.sql.Connection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class AuthorTask extends DBTask<List<Author>> {
    private static final Logger logger = Logger.getLogger(AuthorTask.class.getName());
    private final Consumer<List<Author>> onSuccessConsumer;

    public AuthorTask(Consumer<List<Author>> onSuccessConsumer) {
        this.onSuccessConsumer = onSuccessConsumer;
        setOnSucceeded(this::onSucceeded);
    }

    @Override
    protected List<Author> call() throws Exception {
        try (Connection con = getConnection()) {
            AuthorRepository authorRepository = new AuthorRepository(con);
            logger.info("Retrieved all author");
            return authorRepository.getAuthors();
        }
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
