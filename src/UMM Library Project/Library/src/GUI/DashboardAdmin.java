package GUI;

import Utils.FontLoader;
import Utils.Theme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//library icon awesome jangan lupa add di project structure, ada di resource root
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class DashboardAdmin {
    private BorderPane root;
    private VBox sidebar;
    private StackPane contentPane;
    private static final double MAX_CONTENT_WIDTH = 1200;

    // Dummy data for books
    private ObservableList<Book> booksData = FXCollections.observableArrayList();
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactionsData = FXCollections.observableArrayList();

    public void start(Stage stage) {
        initializeDummyData();

        root = new BorderPane();
        sidebar = createSidebar(stage);
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(64, 40, 64, 40));
        applyThemeStyles();

        root.setLeft(sidebar);
        root.setCenter(centerContainer(contentPane));

        Scene scene = new Scene(root, 1400, 800);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard - Library Management System");
        stage.show();

        loadDashboardContent();
    }

    private void initializeDummyData() {
        // Initialize books data
        booksData.addAll(
                new Book("B001", "To Kill a Mockingbird", "Harper Lee", "Fiction", "Available", LocalDate.now()),
                new Book("B002", "1984", "George Orwell", "Fiction", "Borrowed", LocalDate.now().minusDays(5)),
                new Book("B003", "Pride and Prejudice", "Jane Austen", "Romance", "Available", LocalDate.now()),
                new Book("B004", "The Catcher in the Rye", "J.D. Salinger", "Fiction", "Overdue", LocalDate.now().minusDays(10)),
                new Book("B005", "Lord of the Flies", "William Golding", "Fiction", "Borrowed", LocalDate.now().minusDays(3)),
                new Book("B006", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "Available", LocalDate.now()),
                new Book("B007", "Moby Dick", "Herman Melville", "Adventure", "Overdue", LocalDate.now().minusDays(12)),
                new Book("B008", "War and Peace", "Leo Tolstoy", "Historical", "Available", LocalDate.now())
        );

        // Initialize users data
        usersData.addAll(
                new User("U001", "John Doe", "john@email.com", "Active", 2),
                new User("U002", "Jane Smith", "jane@email.com", "Active", 1),
                new User("U003", "Bob Johnson", "bob@email.com", "Inactive", 0),
                new User("U004", "Alice Brown", "alice@email.com", "Active", 3),
                new User("U005", "Charlie Wilson", "charlie@email.com", "Active", 1)
        );

        // Initialize transactions data
        transactionsData.addAll(
                new Transaction("T001", "U001", "B002", LocalDate.now().minusDays(5), LocalDate.now().plusDays(9), "Active"),
                new Transaction("T002", "U004", "B004", LocalDate.now().minusDays(10), LocalDate.now().minusDays(3), "Overdue"),
                new Transaction("T003", "U002", "B005", LocalDate.now().minusDays(3), LocalDate.now().plusDays(11), "Active"),
                new Transaction("T004", "U001", "B007", LocalDate.now().minusDays(12), LocalDate.now().minusDays(5), "Overdue")
        );
    }

    private void applyThemeStyles() {
        if (Theme.isDarkMode) {
            root.setStyle("-fx-background-color: #121212;");
            contentPane.setStyle("-fx-background-color: #1e1e1e;");
            sidebar.setStyle("-fx-background-color: #222;");
        } else {
            root.setStyle("-fx-background-color: #ffffff;");
            contentPane.setStyle("-fx-background-color: white;");
            sidebar.setStyle("-fx-background-color: #f9fafb;");
        }
    }

    private StackPane centerContainer(Region content) {
        StackPane container = new StackPane(content);
        container.setMaxWidth(MAX_CONTENT_WIDTH);
        container.setPrefWidth(MAX_CONTENT_WIDTH);
        container.setStyle("-fx-background-color: transparent;");
        StackPane.setAlignment(content, Pos.TOP_CENTER);
        return container;
    }

    private VBox createSidebar(Stage stage) {
        VBox box = new VBox(24);
        box.setPadding(new Insets(40, 24, 40, 24));
        box.setPrefWidth(220);

        Button dashboardBtn = createNavButton("Dashboard", FontAwesomeSolid.HOME, this::loadDashboardContent);
        Button booksBtn = createNavButton("Books", FontAwesomeSolid.BOOK, this::loadBooksContent);
        Button usersBtn = createNavButton("Users", FontAwesomeSolid.USERS, this::loadUsersContent);
        Button transactionsBtn = createNavButton("Transactions", FontAwesomeSolid.FILE_ALT, this::loadTransactionsContent);
        Button reportsBtn = createNavButton("Reports", FontAwesomeSolid.CHART_BAR, this::loadReportsContent);
        Button settingsBtn = createNavButton("Settings", FontAwesomeSolid.COG, this::loadSettingsContent);
        Button logoutBtn = createNavButton("Logout", FontAwesomeSolid.SIGN_OUT_ALT, () -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage.getView(), 400, 600));
        });


        box.getChildren().addAll(dashboardBtn, booksBtn, usersBtn, transactionsBtn, reportsBtn, settingsBtn, logoutBtn);
        updateSidebarStyles(box);
        return box;
    }

    private void updateSidebarStyles(VBox sidebar) {
        if (Theme.isDarkMode) {
            sidebar.setStyle("-fx-background-color: #222;");
        } else {
            sidebar.setStyle("-fx-background-color: #f9fafb;");
        }
    }

    private Button createNavButton(String label, FontAwesomeSolid iconType, Runnable onClick) {
        // Buat ikon
        FontIcon icon = new FontIcon(iconType);
        icon.setIconSize(18);

        // Set warna ikon sesuai tema
        if (Theme.isDarkMode) {
            icon.setIconColor(Color.web("#ffffff")); // putih untuk dark mode
        } else {
            icon.setIconColor(Color.web("#333333")); // abu gelap untuk light mode
        }

        // Buat label
        Label textLabel = new Label(label);
        textLabel.setFont(FontLoader.loadPoppins(14));
        textLabel.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.web("#333"));

        HBox content = new HBox(10, icon, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button();
        btn.setGraphic(content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        btn.setOnAction(e -> onClick.run());

        return btn;
    }


    // === DASHBOARD CONTENT ===
    private void loadDashboardContent() {
        VBox container = createContentContainer();
        Label title = createTitleLabel("Library Dashboard");

        // Statistics cards
        HBox metrics = new HBox(24);
        metrics.setPadding(new Insets(40, 0, 48, 0));

        long totalBooks = booksData.size();
        long availableBooks = booksData.stream().filter(b -> "Available".equals(b.getStatus())).count();
        long borrowedBooks = booksData.stream().filter(b -> "Borrowed".equals(b.getStatus())).count();
        long overdueBooks = booksData.stream().filter(b -> "Overdue".equals(b.getStatus())).count();
        long totalUsers = usersData.size();
        long activeUsers = usersData.stream().filter(u -> "Active".equals(u.getStatus())).count();
        long totalFines = calculateTotalFines();

        metrics.getChildren().addAll(
                createMetricCard("Total Books", String.valueOf(totalBooks)),
                createMetricCard("Available Books", String.valueOf(availableBooks)),
                createMetricCard("Borrowed Books", String.valueOf(borrowedBooks)),
                createMetricCard("Overdue Books", String.valueOf(overdueBooks))
        );

        HBox metrics2 = new HBox(24);
        metrics2.setPadding(new Insets(0, 0, 48, 0));
        metrics2.getChildren().addAll(
                createMetricCard("Total Users", String.valueOf(totalUsers)),
                createMetricCard("Active Users", String.valueOf(activeUsers)),
                createMetricCard("Library Visitors", "1,234"),
                createMetricCard("Total Fines", "Rp " + String.format("%,d", totalFines))
        );

        // Charts section
        HBox chartsBox = new HBox(24);
        chartsBox.setPadding(new Insets(0, 0, 40, 0));

        VBox chartContainer1 = createChartContainer("Books Borrowed This Month", createBooksChart());
        VBox chartContainer2 = createChartContainer("Books by Genre", createGenreChart());

        chartsBox.getChildren().addAll(chartContainer1, chartContainer2);

        container.getChildren().addAll(title, metrics, metrics2, chartsBox);
        contentPane.getChildren().setAll(container);
    }

    private long calculateTotalFines() {
        return transactionsData.stream()
                .filter(t -> "Overdue".equals(t.getStatus()))
                .mapToLong(t -> {
                    long daysOverdue = Math.max(0, LocalDate.now().toEpochDay() - t.getDueDate().toEpochDay());
                    return daysOverdue * 500; // 500 rupiah per day
                })
                .sum();
    }

    private LineChart<Number, Number> createBooksChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day");
        yAxis.setLabel("Books Borrowed");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setPrefHeight(300);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        // Dummy data for the past 30 days
        for (int i = 1; i <= 30; i++) {
            series.getData().add(new XYChart.Data<>(i, (int)(Math.random() * 20) + 5));
        }
        lineChart.getData().add(series);

        return lineChart;
    }

    //    dummy pie chart
    private PieChart createGenreChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Fiction", 45),
                new PieChart.Data("Non-Fiction", 25),
                new PieChart.Data("Romance", 15),
                new PieChart.Data("Adventure", 10),
                new PieChart.Data("Historical", 5)
        );

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setPrefHeight(300);
        pieChart.setLegendVisible(true);
        pieChart.setAnimated(false);

        return pieChart;
    }

    private VBox createChartContainer(String title, javafx.scene.Node chart) {
        VBox container = new VBox(16);
        container.setPrefWidth(580);
        container.setPadding(new Insets(24));
        container.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #2c2c2c; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8,0,0,2);"
                : "-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 8,0,0,2);");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(FontLoader.loadPoppins(18).getFamily(), FontWeight.SEMI_BOLD, 18));
        titleLabel.setTextFill(Theme.isDarkMode ? Color.web("#e5e7eb") : Color.web("#111827"));

        container.getChildren().addAll(titleLabel, chart);
        return container;
    }

    // === BOOKS MANAGEMENT ===
    private void loadBooksContent() {
        VBox container = createContentContainer();
        Label title = createTitleLabel("Books Management");

        // Action buttons
        HBox actionButtons = new HBox(12);
        actionButtons.setPadding(new Insets(0, 0, 24, 0));

        Button addBookBtn = createActionButton("+ Add Book", this::showAddBookDialog);
        Button refreshBtn = createActionButton("🔄 Refresh", this::loadBooksContent);

        actionButtons.getChildren().addAll(addBookBtn, refreshBtn);

        // Books table
        TableView<Book> booksTable = createBooksTable();
        VBox tableContainer = createTableContainer(booksTable);

        container.getChildren().addAll(title, actionButtons, tableContainer);
        contentPane.getChildren().setAll(container);
    }

    private TableView<Book> createBooksTable() {
        TableView<Book> table = new TableView<>();
        table.setItems(booksData);
        table.setPrefHeight(500);

        TableColumn<Book, String> idCol = new TableColumn<>("Book ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(150);

        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(100);

        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        // Custom cell factory for status column to show colored status
        statusCol.setCellFactory(column -> new TableCell<Book, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "Available":
                            setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
                            break;
                        case "Borrowed":
                            setStyle("-fx-text-fill: #2563eb; -fx-font-weight: bold;");
                            break;
                        case "Overdue":
                            setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        TableColumn<Book, String> dateCol = new TableColumn<>("Date Added");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        dateCol.setPrefWidth(120);

        TableColumn<Book, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttonsBox = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4;");
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4;");

                editBtn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showEditBookDialog(book);
                });

                deleteBtn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showDeleteConfirmation(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }
        });

        table.getColumns().addAll(idCol, titleCol, authorCol, genreCol, statusCol, dateCol, actionCol);
        return table;
    }

    // === USERS MANAGEMENT ===
    private void loadUsersContent() {
        VBox container = createContentContainer();
        Label title = createTitleLabel("Users Management");

        // User statistics
        HBox userStats = new HBox(24);
        userStats.setPadding(new Insets(0, 0, 32, 0));

        long activeUsers = usersData.stream().filter(u -> "Active".equals(u.getStatus())).count();
        long inactiveUsers = usersData.stream().filter(u -> "Inactive".equals(u.getStatus())).count();

        userStats.getChildren().addAll(
                createMiniStatCard("Active Users", String.valueOf(activeUsers)),
                createMiniStatCard("Inactive Users", String.valueOf(inactiveUsers)),
                createMiniStatCard("Total Users", String.valueOf(usersData.size()))
        );

        // Users table
        TableView<User> usersTable = createUsersTable();
        VBox tableContainer = createTableContainer(usersTable);

        container.getChildren().addAll(title, userStats, tableContainer);
        contentPane.getChildren().setAll(container);
    }

    private TableView<User> createUsersTable() {
        TableView<User> table = new TableView<>();
        table.setItems(usersData);
        table.setPrefHeight(400);

        TableColumn<User, String> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(250);

        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        TableColumn<User, Integer> booksCol = new TableColumn<>("Books Borrowed");
        booksCol.setCellValueFactory(new PropertyValueFactory<>("booksBorrowed"));
        booksCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, nameCol, emailCol, statusCol, booksCol);
        return table;
    }

    // === TRANSACTIONS MANAGEMENT ===
    private void loadTransactionsContent() {
        VBox container = createContentContainer();
        Label title = createTitleLabel("Transactions & Fines");

        // Action buttons with search
        HBox actionButtons = new HBox(12);
        actionButtons.setPadding(new Insets(0, 0, 24, 0));
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        Button addTransactionBtn = createActionButton("+ Add Transaction", this::showAddTransactionDialog);
        Button refreshBtn = createActionButtonWithIcon("Refresh", FontAwesomeSolid.SYNC_ALT, this::loadTransactionsContent);

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by User ID...");
        searchField.setPrefWidth(200);
        searchField.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #3c3c3c; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-background-radius: 6;" :
                "-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 6; -fx-border-color: #d1d5db; -fx-border-radius: 6;");

        Button searchBtn = createActionButtonWithIcon("Search", FontAwesomeSolid.SEARCH, () -> filterTransactionsByUserId(searchField.getText()));
        Button clearBtn = createActionButton("Clear", () -> {
            searchField.clear();
            filterTransactionsByUserId("");
        });

        actionButtons.getChildren().addAll(addTransactionBtn, refreshBtn,
                new Label("  "), searchField, searchBtn, clearBtn);

        // Fine statistics
        HBox fineStats = new HBox(24);
        fineStats.setPadding(new Insets(0, 0, 32, 0));

        long overdueCount = transactionsData.stream().filter(t -> "Overdue".equals(t.getStatus())).count();
        long totalFines = calculateTotalFines();

        fineStats.getChildren().addAll(
                createMiniStatCard("Active Loans", String.valueOf(transactionsData.stream().filter(t -> "Active".equals(t.getStatus())).count())),
                createMiniStatCard("Overdue Loans", String.valueOf(overdueCount)),
                createMiniStatCard("Total Fines", "Rp " + String.format("%,d", totalFines))
        );

        // Transactions table
        transactionsTable = createTransactionsTable();
        VBox tableContainer = createTableContainer(transactionsTable);

        container.getChildren().addAll(title, actionButtons, fineStats, tableContainer);
        contentPane.getChildren().setAll(container);
    }

    private TableView<Transaction> transactionsTable;
    private ObservableList<Transaction> filteredTransactionsData = FXCollections.observableArrayList();

    private TableView<Transaction> createTransactionsTable() {
        TableView<Transaction> table = new TableView<>();
        filteredTransactionsData.setAll(transactionsData);
        table.setItems(filteredTransactionsData);
        table.setPrefHeight(400);

        TableColumn<Transaction, String> idCol = new TableColumn<>("Transaction ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(120);

        TableColumn<Transaction, String> userCol = new TableColumn<>("User ID");
        userCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userCol.setPrefWidth(80);

        TableColumn<Transaction, String> bookCol = new TableColumn<>("Book ID");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookCol.setPrefWidth(80);

        TableColumn<Transaction, LocalDate> borrowCol = new TableColumn<>("Borrow Date");
        borrowCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        borrowCol.setPrefWidth(120);

        TableColumn<Transaction, LocalDate> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueCol.setPrefWidth(120);

        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        // Custom cell factory for status column
        statusCol.setCellFactory(column -> new TableCell<Transaction, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "Active":
                            setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
                            break;
                        case "Overdue":
                            setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                            break;
                        case "Returned":
                            setStyle("-fx-text-fill: #2563eb; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        TableColumn<Transaction, String> fineCol = new TableColumn<>("Fine");
        fineCol.setPrefWidth(100);
        fineCol.setCellFactory(column -> new TableCell<Transaction, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Transaction transaction = (Transaction) getTableRow().getItem();
                    if ("Overdue".equals(transaction.getStatus())) {
                        long daysOverdue = Math.max(0, LocalDate.now().toEpochDay() - transaction.getDueDate().toEpochDay());
                        long fine = daysOverdue * 500;
                        setText("Rp " + String.format("%,d", fine));
                        setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                    } else {
                        setText("Rp 0");
                        setStyle("-fx-text-fill: #16a34a;");
                    }
                }
            }
        });

        TableColumn<Transaction, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(180);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final Button payFineBtn = new Button("Pay Fine");
            private final HBox buttonsBox = new HBox(5, editBtn, deleteBtn, payFineBtn);

            {
                editBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4; -fx-font-size: 10;");
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4; -fx-font-size: 10;");
                payFineBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4; -fx-font-size: 10;");

                editBtn.setOnAction(e -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    showEditTransactionDialog(transaction);
                });

                deleteBtn.setOnAction(e -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    showDeleteTransactionConfirmation(transaction);
                });

                payFineBtn.setOnAction(e -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    payFine(transaction);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    // Only show Pay Fine button for overdue transactions
                    if ("Overdue".equals(transaction.getStatus())) {
                        buttonsBox.getChildren().setAll(editBtn, deleteBtn, payFineBtn);
                    } else {
                        buttonsBox.getChildren().setAll(editBtn, deleteBtn);
                    }
                    setGraphic(buttonsBox);
                }
            }
        });

        table.getColumns().addAll(idCol, userCol, bookCol, borrowCol, dueCol, statusCol, fineCol, actionCol);
        return table;
    }

    // Add these new methods:
    private void filterTransactionsByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            filteredTransactionsData.setAll(transactionsData);
        } else {
            filteredTransactionsData.setAll(
                    transactionsData.stream()
                            .filter(t -> t.getUserId().toLowerCase().contains(userId.toLowerCase()))
                            .collect(java.util.stream.Collectors.toList())
            );
        }
    }

    private void showAddTransactionDialog() {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add New Transaction");
        dialog.setHeaderText("Enter transaction details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField userIdField = new TextField();
        TextField bookIdField = new TextField();
        DatePicker borrowDatePicker = new DatePicker(LocalDate.now());
        DatePicker dueDatePicker = new DatePicker(LocalDate.now().plusDays(14));
        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("Active", "Overdue", "Returned");
        statusField.setValue("Active");

        grid.add(new Label("User ID:"), 0, 0);
        grid.add(userIdField, 1, 0);
        grid.add(new Label("Book ID:"), 0, 1);
        grid.add(bookIdField, 1, 1);
        grid.add(new Label("Borrow Date:"), 0, 2);
        grid.add(borrowDatePicker, 1, 2);
        grid.add(new Label("Due Date:"), 0, 3);
        grid.add(dueDatePicker, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(statusField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String newId = "T" + String.format("%03d", transactionsData.size() + 1);
                return new Transaction(newId, userIdField.getText(), bookIdField.getText(),
                        borrowDatePicker.getValue(), dueDatePicker.getValue(), statusField.getValue());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(transaction -> {
            transactionsData.add(transaction);
            filteredTransactionsData.setAll(transactionsData);
            showAlert("Success", "Transaction added successfully!");
        });
    }

    private void showEditTransactionDialog(Transaction transaction) {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Edit Transaction");
        dialog.setHeaderText("Edit transaction details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField userIdField = new TextField(transaction.getUserId());
        TextField bookIdField = new TextField(transaction.getBookId());
        DatePicker borrowDatePicker = new DatePicker(transaction.getBorrowDate());
        DatePicker dueDatePicker = new DatePicker(transaction.getDueDate());
        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("Active", "Overdue", "Returned");
        statusField.setValue(transaction.getStatus());

        grid.add(new Label("User ID:"), 0, 0);
        grid.add(userIdField, 1, 0);
        grid.add(new Label("Book ID:"), 0, 1);
        grid.add(bookIdField, 1, 1);
        grid.add(new Label("Borrow Date:"), 0, 2);
        grid.add(borrowDatePicker, 1, 2);
        grid.add(new Label("Due Date:"), 0, 3);
        grid.add(dueDatePicker, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(statusField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                transaction.setUserId(userIdField.getText());
                transaction.setBookId(bookIdField.getText());
                transaction.setBorrowDate(borrowDatePicker.getValue());
                transaction.setDueDate(dueDatePicker.getValue());
                transaction.setStatus(statusField.getValue());
                return transaction;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            filteredTransactionsData.setAll(transactionsData);
            transactionsTable.refresh();
            showAlert("Success", "Transaction updated successfully!");
            loadTransactionsContent(); // Refresh to update statistics
        });
    }

    private void showDeleteTransactionConfirmation(Transaction transaction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText("Are you sure you want to delete this transaction?");
        alert.setContentText("Transaction ID: " + transaction.getId() +
                "\nUser ID: " + transaction.getUserId() +
                "\nBook ID: " + transaction.getBookId());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                transactionsData.remove(transaction);
                filteredTransactionsData.setAll(transactionsData);
                showAlert("Success", "Transaction deleted successfully!");
                loadTransactionsContent(); // Refresh to update statistics
            }
        });
    }

    private void payFine(Transaction transaction) {
        if (!"Overdue".equals(transaction.getStatus())) {
            showAlert("Information", "This transaction doesn't have any fine to pay.");
            return;
        }

        long daysOverdue = Math.max(0, LocalDate.now().toEpochDay() - transaction.getDueDate().toEpochDay());
        long fine = daysOverdue * 500;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pay Fine");
        alert.setHeaderText("Confirm fine payment");
        alert.setContentText("Transaction ID: " + transaction.getId() +
                "\nFine Amount: Rp " + String.format("%,d", fine) +
                "\nDays Overdue: " + daysOverdue +
                "\n\nMark this fine as paid?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Change status to "Returned" to indicate fine is paid
                transaction.setStatus("Returned");
                transactionsTable.refresh();
                showAlert("Success", "Fine payment recorded successfully!\nTransaction status updated to 'Returned'.");
                loadTransactionsContent(); // Refresh to update statistics
            }
        });
    }

    // === REPORTS PAGE ===
    private void loadReportsContent() {
        VBox container = createContentContainer();
        Label title = createTitleLabel("Reports & Analytics");

        HBox chartStats = new HBox(24);
        chartStats.setPadding(new Insets(0, 0, 32, 0));

        //data static yang sama
        int[] visitorData = {45, 52, 38, 61, 43, 55, 47, 39, 58, 44, 50, 42, 56, 49, 53, 41, 48, 59, 46, 51, 40, 57, 43, 54, 48, 45, 52, 47, 49, 55};
        int[] borrowData = {12, 15, 9, 18, 11, 16, 13, 8, 17, 10, 14, 12, 19, 13, 15, 11, 12, 16, 14, 13, 9, 18, 11, 15, 12, 13, 16, 14, 13, 17};

        int totalVisitors = 0;
        int totalBorrows = 0;

        for (int i = 0; i < 30; i++) {
            totalVisitors += visitorData[i];
            totalBorrows += borrowData[i];
        }

        chartStats.getChildren().addAll(
                createMiniStatCard("Total Visitors", String.valueOf(totalVisitors)),
                createMiniStatCard("Total Borrows", String.valueOf(totalBorrows))
        );

        // Charts section
        HBox chartsBox = new HBox(24);
        chartsBox.setPadding(new Insets(0, 0, 40, 0));

        VBox chartContainer1 = createChartContainer("Daily Activity", createActivityChart());

        chartsBox.getChildren().addAll(chartContainer1);

        container.getChildren().addAll(title, chartStats, chartsBox);
        contentPane.getChildren().setAll(container);
    }

    private LineChart<Number, Number> createActivityChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day");
        yAxis.setLabel("Activity Count");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setPrefHeight(300);
        lineChart.setAnimated(false);

        XYChart.Series<Number, Number> visitorseries = new XYChart.Series<>();
        visitorseries.setName("Visitors");
        XYChart.Series<Number, Number> borrowSeries = new XYChart.Series<>();
        borrowSeries.setName("Books Borrowed");

        // Data tetap tanpa random
        int[] visitorData = {45, 52, 38, 61, 43, 55, 47, 39, 58, 44, 50, 42, 56, 49, 53, 41, 48, 59, 46, 51, 40, 57, 43, 54, 48, 45, 52, 47, 49, 55};
        int[] borrowData = {12, 15, 9, 18, 11, 16, 13, 8, 17, 10, 14, 12, 19, 13, 15, 11, 12, 16, 14, 13, 9, 18, 11, 15, 12, 13, 16, 14, 13, 17};

        for (int i = 0; i < 30; i++) {
            visitorseries.getData().add(new XYChart.Data<>(i + 1, visitorData[i]));
            borrowSeries.getData().add(new XYChart.Data<>(i + 1, borrowData[i]));
        }

        lineChart.getData().addAll(visitorseries, borrowSeries);
        return lineChart;
    }

    // === SETTINGS PAGE ===
    private void loadSettingsContent() {
        VBox container = createContentContainer();
        Label title = createTitleLabel("Settings");

        VBox card = createContentCard();
        card.setSpacing(32);

        // Dark mode toggle
        Label darkModeLabel = new Label("Dark Mode");
        darkModeLabel.setFont(FontLoader.loadPoppins(18));
        darkModeLabel.setTextFill(Theme.isDarkMode ? Color.web("#d1d5db") : Color.web("#374151"));

        CheckBox darkModeToggle = new CheckBox();
        darkModeToggle.setSelected(Theme.isDarkMode);
        darkModeToggle.setStyle("-fx-cursor: hand;");

        darkModeToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            Theme.isDarkMode = isSelected;
            start((Stage) root.getScene().getWindow()); // Reload UI
        });

        HBox toggleBox = new HBox(10, darkModeLabel, darkModeToggle);
        toggleBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().add(toggleBox);
        container.getChildren().addAll(title, card);
        contentPane.getChildren().setAll(container);
    }


    // === HELPER METHODS ===
    private Button createActionButton(String text, Runnable onClick) {
        Button btn = new Button(text);
        btn.setFont(FontLoader.loadPoppins(14));
        btn.setPadding(new Insets(8, 16, 8, 16));
        btn.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;" :
                "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
        btn.setOnAction(e -> onClick.run());
        return btn;
    }

    private Button createActionButtonWithIcon(String text, FontAwesomeSolid iconType, Runnable onClick) {
        // Create icon
        FontIcon icon = new FontIcon(iconType);
        icon.setIconSize(14);

        // Set icon color based on theme
        if (Theme.isDarkMode) {
            icon.setIconColor(Color.WHITE);
        } else {
            icon.setIconColor(Color.WHITE);
        }

        // Create label
        Label textLabel = new Label(text);
        textLabel.setFont(FontLoader.loadPoppins(14));
        textLabel.setTextFill(Color.WHITE);

        HBox content = new HBox(8, icon, textLabel);
        content.setAlignment(Pos.CENTER);

        Button btn = new Button();
        btn.setGraphic(content);
        btn.setPadding(new Insets(8, 16, 8, 16));
        btn.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;" :
                "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
        btn.setOnAction(e -> onClick.run());
        return btn;
    }

    private VBox createTableContainer(TableView<?> table) {
        VBox container = new VBox();
        container.setPadding(new Insets(24));
        container.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #2c2c2c; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8,0,0,2);"
                : "-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 8,0,0,2);");

        container.getChildren().add(table);
        return container;
    }

    private void showAddBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");
        dialog.setHeaderText("Enter book details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        ComboBox<String> genreField = new ComboBox<>();
        genreField.getItems().addAll("Fiction", "Non-Fiction", "Romance", "Adventure", "Historical", "Science", "Biography");
        genreField.setValue("Fiction");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2);
        grid.add(genreField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String newId = "B" + String.format("%03d", booksData.size() + 1);
                return new Book(newId, titleField.getText(), authorField.getText(),
                        genreField.getValue(), "Available", LocalDate.now());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(book -> {
            booksData.add(book);
            showAlert("Success", "Book added successfully!");
        });
    }

    private void showEditBookDialog(Book book) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Edit Book");
        dialog.setHeaderText("Edit book details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        ComboBox<String> genreField = new ComboBox<>();
        genreField.getItems().addAll("Fiction", "Non-Fiction", "Romance", "Adventure", "Historical", "Science", "Biography");
        genreField.setValue(book.getGenre());

        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("Available", "Borrowed", "Overdue");
        statusField.setValue(book.getStatus());

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2);
        grid.add(genreField, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setGenre(genreField.getValue());
                book.setStatus(statusField.getValue());
                return book;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            showAlert("Success", "Book updated successfully!");
            loadBooksContent(); // Refresh the table
        });
    }

    private void showDeleteConfirmation(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Book");
        alert.setHeaderText("Are you sure you want to delete this book?");
        alert.setContentText("Book: " + book.getTitle() + " by " + book.getAuthor());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                booksData.remove(book);
                showAlert("Success", "Book deleted successfully!");
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox createMetricCard(String label, String value) {
        VBox card = new VBox(8);
        card.setPrefWidth(280);
        card.setPadding(new Insets(24));
        card.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #2c2c2c; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8,0,0,2);"
                : "-fx-background-color: #f8fafc; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8,0,0,2);");

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font(FontLoader.loadPoppins(32).getFamily(), FontWeight.BOLD, 32));
        valLbl.setTextFill(Theme.isDarkMode ? Color.web("#d1d5db") : Color.web("#111827"));

        Label labelLbl = new Label(label.toUpperCase());
        labelLbl.setFont(FontLoader.loadPoppins(12));
        labelLbl.setTextFill(Theme.isDarkMode ? Color.web("#9ca3af") : Color.web("#6b7280"));
        labelLbl.setStyle("-fx-letter-spacing: 1.5;");

        card.getChildren().addAll(valLbl, labelLbl);
        return card;
    }

    private VBox createMiniStatCard(String label, String value) {
        VBox card = new VBox(6);
        card.setPrefWidth(180);
        card.setPadding(new Insets(20));
        card.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #3b82f6; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.5), 8,0,0,4);"
                : "-fx-background-color: #dbeafe; -fx-background-radius: 10;");

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font(FontLoader.loadPoppins(24).getFamily(), FontWeight.BOLD, 24));
        valLbl.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.web("#1e40af"));

        Label labelLbl = new Label(label);
        labelLbl.setFont(FontLoader.loadPoppins(14));
        labelLbl.setTextFill(Theme.isDarkMode ? Color.web("#bfdbfe") : Color.web("#1e40af"));

        card.getChildren().addAll(valLbl, labelLbl);
        return card;
    }

    private Label createTitleLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font(FontLoader.loadPoppins(48).getFamily(), FontWeight.SEMI_BOLD, 48));
        lbl.setTextFill(Theme.isDarkMode ? Color.web("#e5e7eb") : Color.web("#111827"));
        lbl.setPadding(new Insets(0, 0, 16, 0));
        return lbl;
    }

    private Label createInfoLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(FontLoader.loadPoppins(16));
        lbl.setTextFill(Theme.isDarkMode ? Color.web("#9ca3af") : Color.web("#6b7280"));
        lbl.setPadding(new Insets(0, 0, 16, 0));
        return lbl;
    }

    private VBox createContentCard() {
        VBox card = new VBox();
        card.setPadding(new Insets(32));
        if (Theme.isDarkMode) {
            card.setStyle("-fx-background-color: #2c2c2c; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10,0,0,3);");
        } else {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10,0,0,3);");
        }
        card.setMaxWidth(MAX_CONTENT_WIDTH);
        return card;
    }

    private VBox createContentContainer() {
        VBox container = new VBox(16);
        container.setMaxWidth(MAX_CONTENT_WIDTH);
        container.setAlignment(Pos.TOP_LEFT);
        container.setPadding(new Insets(0));
        return container;
    }

    // === DATA CLASSES ===
    public static class Book {
        private String id;
        private String title;
        private String author;
        private String genre;
        private String status;
        private LocalDate dateAdded;

        public Book(String id, String title, String author, String genre, String status, LocalDate dateAdded) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.status = status;
            this.dateAdded = dateAdded;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getGenre() { return genre; }
        public void setGenre(String genre) { this.genre = genre; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getDateAdded() {
            return dateAdded.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        public void setDateAdded(LocalDate dateAdded) { this.dateAdded = dateAdded; }
    }

    public static class User {
        private String id;
        private String name;
        private String email;
        private String status;
        private int booksBorrowed;

        public User(String id, String name, String email, String status, int booksBorrowed) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.status = status;
            this.booksBorrowed = booksBorrowed;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public int getBooksBorrowed() { return booksBorrowed; }
        public void setBooksBorrowed(int booksBorrowed) { this.booksBorrowed = booksBorrowed; }
    }

    public static class Transaction {
        private String id;
        private String userId;
        private String bookId;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private String status;

        public Transaction(String id, String userId, String bookId, LocalDate borrowDate, LocalDate dueDate, String status) {
            this.id = id;
            this.userId = userId;
            this.bookId = bookId;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
            this.status = status;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }

        public LocalDate getBorrowDate() { return borrowDate; }
        public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}