# CS6360 DB-FX: A JavaFX Database Management Application

DB-FX is a JavaFX-based application designed to manage and interact with a database system. It provides a user-friendly interface to perform CRUD (Create, Read, Update, Delete) operations on various entities like books, authors, and publishers.


## Features

- **CRUD Operations**: Manage books, authors, and publishers with full create, read, update, and delete capabilities.
- **Search and Filter**: Advanced search and filtering options to easily navigate through the data.
- **Data Validation**: Ensures the integrity of the data entered into the database with comprehensive validation checks.
- **Asynchronous Processing**: Utilizes background tasks to maintain a responsive UI during database operations. The application leverages advanced thread management features to handle database transactions and UI updates efficiently, ensuring a smooth user experience.
- **Thread Management**: Incorporates advanced threading features to manage database connections and operations, preventing UI freezing and enhancing performance.
- **Customizable UI**: A flexible user interface that adapts to different data management needs.

Note: Database and stored procedure related files can be found in the `./db` directory
- `procs-insertBookData.sql`
- `procs-updateBookData.sql`
- `schema.sql`

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/mikimaine/db-fx.git
   cd db-fx
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn javafx:run
   ```

## Usage

- **Main Interface**: The application's main window displays a tabular view of the database records. You can switch between tabs to view books, authors, or publishers.
- **Add/Edit Data**: Use the form fields provided to add or edit records. Validation is performed to ensure data consistency.
- **Delete Data**: Select a record from the table and use the delete option to remove it from the database.
- **Search/Filter**: Utilize the search bar to filter the displayed records based on relevant criteria.

## Contributing

Contributions to the DB-FX project are welcome. Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your improvements (`git commit -am 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Thank you to all the contributors who have helped to build this application.

---