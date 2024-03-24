package com.utd.library_db.loaders;

import com.utd.library_db.database.DBTask;
import com.utd.library_db.repositories.ItemListView;
import com.utd.library_db.repositories.ItemRepository;
import javafx.concurrent.WorkerStateEvent;

import java.sql.Connection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ItemListTask extends DBTask<List<ItemListView>> {
    private static final Logger logger = Logger.getLogger(ItemListTask.class.getName());
    private Consumer<List<ItemListView>> onSuccessConsumer;
    private final Integer publisherId;
    private final String isbn;
    private final String title;
    private final String[] authors;

    public ItemListTask(Consumer<List<ItemListView>> onSuccessConsumer, Integer publisherId,
                        String isbn, String title, String[] authors) {
        setupSuccessConsumer(onSuccessConsumer);
        this.publisherId = publisherId;
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
    }

    @Override
    protected List<ItemListView> call() throws Exception {
        try (Connection con = getConnection()) {
            ItemRepository itemRepository = new ItemRepository(con);
            List<ItemListView> items = itemRepository.filterItems(publisherId, isbn, title, authors);
            logger.info("Retrieved all books");
            return items;
        }
    }

    private void setupSuccessConsumer(Consumer<List<ItemListView>> onSuccessConsumer) {
        this.onSuccessConsumer = onSuccessConsumer;
        setOnSucceeded(this::onSucceeded);
    }

    private void onSucceeded(WorkerStateEvent event) {
        if (event.getEventType() == WorkerStateEvent.WORKER_STATE_SUCCEEDED && onSuccessConsumer != null) {
            onSuccessConsumer.accept(getValue());
        }
    }
}
