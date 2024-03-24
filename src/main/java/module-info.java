module com.example.library_db {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires atlantafx.base;
    requires sproket.github.io.persism;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.zondicons;
    requires org.jetbrains.annotations;

    opens com.utd.library_db to javafx.fxml;
    exports com.utd.library_db;
    exports com.utd.library_db.repositories;
    opens com.utd.library_db.repositories to javafx.fxml;
    exports com.utd.library_db.database;
    opens com.utd.library_db.database to javafx.fxml;
    exports com.utd.library_db.loaders;
    opens com.utd.library_db.loaders to javafx.fxml;
}