package com.utd.library_db;

import com.utd.library_db.database.DBSetupTask;
import com.utd.library_db.database.DatabaseThreadFactory;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Logger;

import atlantafx.base.theme.*;

public class LibraryBookEntryApp extends Application {

    private static final Logger logger = Logger.getLogger(LibraryBookEntryApp.class.getName());
    private ExecutorService databaseExecutor;

    private Future<?> databaseSetupFuture;

    @Override
    public void init() throws Exception {
        databaseExecutor = Executors.newFixedThreadPool(10, new DatabaseThreadFactory());
        // run the database setup in parallel to the JavaFX application setup.
        DBSetupTask setup = new DBSetupTask();
        databaseSetupFuture = databaseExecutor.submit(setup);
    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException, ExecutionException {
        // wait for the database setup to complete cleanly before showing any UI.
        // a real app might use a preloader or show a splash screen if this
        // was to take a long time rather than just pausing the JavaFX application thread.
        databaseSetupFuture.get();

        final ProgressIndicator databaseActivityIndicator = new ProgressIndicator();
        databaseActivityIndicator.setVisible(true);

        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryBookEntryApp.class.getResource("ui-form-view.fxml"));
        BorderPane borderPane = fxmlLoader.load();

        UIFormController helloController = fxmlLoader.getController();
        logger.info(fxmlLoader.getController() + " fxmlLoader...");
        helloController.setExecutorService(databaseExecutor);
        final int SCREEN_SIZE_WIDTH = 1280;
        final int SCREEN_SIZE_HEIGHT = 768;
        final boolean DEPTH_BUFFER = false;

        Scene scene = new Scene(borderPane, SCREEN_SIZE_WIDTH, SCREEN_SIZE_HEIGHT, DEPTH_BUFFER, getAntialiasing());
        loadAppIcon(stage);
        stage.setTitle("Library Book Entry");
        stage.setScene(scene);
        stage.show();
    }

    private void loadAppIcon(Stage stage) {
        stage.getIcons().add(new Image(Objects.requireNonNull(LibraryBookEntryApp.class.getResourceAsStream("apple-books-app-icon.png"))));
    }

    /**
     * Determines the level of antialiasing to be used for rendering scenes based on platform capabilities.
     *
     * @return SceneAntialiasing.BALANCED if 3D scenes are supported by the platform, otherwise SceneAntialiasing.DISABLED.
     * This method checks if the platform supports 3D scenes using the ConditionalFeature.SCENE3D flag.
     * If 3D scenes are supported, it opts for a balanced level of antialiasing to smooth out the edges in the scene,
     * providing a compromise between performance and visual quality. If 3D is not supported, it disables antialiasing
     * to potentially improve performance, as antialiasing can be resource-intensive.
     */
    private SceneAntialiasing getAntialiasing() {
        return Platform.isSupported(ConditionalFeature.SCENE3D)
                ? SceneAntialiasing.BALANCED
                : SceneAntialiasing.DISABLED;
    }

    // shutdown the program.
    @Override
    public void stop() throws Exception {
        databaseExecutor.shutdown();
        if (!databaseExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
            logger.info("Database execution thread timed out after 3 seconds rather than shutting down cleanly.");
        }
    }

    public static void main(String[] args) {
        launch();
    }

}