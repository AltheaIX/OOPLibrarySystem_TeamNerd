package GUI;

import Utils.FontLoader;
import Utils.Theme;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.*;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

//library icon awesome jangan lupa add di project structure, ada di resource root
import model.*;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import static App.Main.BASE_URL;

public class DashboardAdmin {
    private BorderPane root;
    private VBox sidebar;
    private StackPane contentPane;
    private static final double MAX_CONTENT_WIDTH = 1200;

    // Dummy data for books
    private ObservableList<BookData> booksData = FXCollections.observableArrayList();
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactionsData = FXCollections.observableArrayList();

    public void start(Stage stage) {
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
        // Button reportsBtn = createNavButton("Reports", FontAwesomeSolid.CHART_BAR, this::loadReportsContent);
        Button settingsBtn = createNavButton("Settings", FontAwesomeSolid.COG, this::loadSettingsContent);
        Button logoutBtn = createNavButton("Logout", FontAwesomeSolid.SIGN_OUT_ALT, () -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage.getView(), 400, 600));
        });


        box.getChildren().addAll(dashboardBtn, booksBtn, usersBtn, transactionsBtn, settingsBtn, logoutBtn);
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
        fetchTransactions();
        fetchBook();
        fetchUsers();

        VBox container = createContentContainer();
        Label title = createTitleLabel("Library Dashboard");

        // Statistics cards
        HBox metrics = new HBox(24);
        metrics.setPadding(new Insets(40, 0, 48, 0));

        long totalBooks = booksData.size();
        long totalUsers = usersData.size();
        long totalTransactions = transactionsData.size();

        metrics.getChildren().addAll(
                createMetricCard("Total Books", String.valueOf(totalBooks)),
                createMetricCard("Total Users", String.valueOf(totalUsers)),
                createMetricCard("Total Transactions", String.valueOf(totalTransactions))
        );

        container.getChildren().addAll(title, metrics);
        contentPane.getChildren().setAll(container);
    }

    private long calculateTotalFines() {
        return transactionsData.stream()
                .filter(t -> "Overdue".equals(t.getStatus()))
                .mapToLong(t -> {
                    long daysOverdue = Math.max(0, LocalDate.now().toEpochDay() - LocalDate.parse(t.getReturnDate()).toEpochDay());
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

    private void fetchBook(){
        booksData.clear();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/book"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());
            mapper.findAndRegisterModules();
            BookResponse bookResponse = mapper.readValue(response.body(), BookResponse.class);
            for(BookData book : bookResponse.data){
                booksData.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === BOOKS MANAGEMENT ===
    private void loadBooksContent() {
        fetchBook();

        VBox container = createContentContainer();
        Label title = createTitleLabel("Books Management");

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search Books");
        searchField.setMaxWidth(200);

        // Action buttons
        HBox actionButtons = new HBox(12);
        actionButtons.setPadding(new Insets(0, 0, 24, 0));

        Button addBookBtn = createActionButton("+ Add Book", this::showAddBookDialog);
        Button refreshBtn = createActionButtonWithIcon("Refresh", FontAwesomeSolid.SYNC_ALT, this::loadBooksContent);

        actionButtons.getChildren().addAll(searchField, addBookBtn, refreshBtn);

        // Books table
        TableView<BookData> booksTable = createBooksTable();

        // === Filter & Sort Setup ===
        FilteredList<BookData> filteredData = new FilteredList<>(booksData, b -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(book -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return book.getTitle().toLowerCase().contains(lower) ||
                        book.getAuthor().toLowerCase().contains(lower);
            });
        });
        SortedList<BookData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(booksTable.comparatorProperty());
        booksTable.setItems(sortedData);
        // === END Filter & Sort Setup ===

        VBox tableContainer = createTableContainer(booksTable);
        container.getChildren().addAll(title, actionButtons, tableContainer);
        contentPane.getChildren().setAll(container);
    }
    private TableView<BookData> createBooksTable() {
        TableView<BookData> table = new TableView<>();
        table.setItems(booksData);
        table.setPrefHeight(500);

        TableColumn<BookData, String> idCol = new TableColumn<>("Book ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<BookData, String> isbnCol = new TableColumn<>("Isbn");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnCol.setPrefWidth(100);

        TableColumn<BookData, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<BookData, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(150);

        TableColumn<BookData, String> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        TableColumn<BookData, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttonsBox = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4;");
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4;");

                editBtn.setOnAction(e -> {
                    BookData book = getTableView().getItems().get(getIndex());
                    showEditBookDialog(book);
                });

                deleteBtn.setOnAction(e -> {
                    BookData book = getTableView().getItems().get(getIndex());
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

        table.getColumns().addAll(idCol, isbnCol, titleCol, authorCol, quantityCol, actionCol);
        return table;
    }

    private void fetchUsers(){
        usersData.clear();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());
            mapper.findAndRegisterModules();
            UserResponse userResponse = mapper.readValue(response.body(), UserResponse.class);
            for(User user : userResponse.data){
                usersData.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === USERS MANAGEMENT ===
    private TableView<User> usersTable;
    private ObservableList<User> filteredUsersData = FXCollections.observableArrayList();

    private void loadUsersContent() {
        fetchUsers();
        VBox container = createContentContainer();
        Label title = createTitleLabel("Users Management");

        // Action buttons with search
        HBox actionButtons = new HBox(12);
        actionButtons.setPadding(new Insets(0, 0, 24, 0));
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        Button addUserBtn = createActionButton("+ Add User", this::showAddUserDialog);
        Button refreshBtn = createActionButtonWithIcon("Refresh", FontAwesomeSolid.SYNC_ALT, this::loadUsersContent);

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by User ID...");
        searchField.setPrefWidth(200);
        searchField.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #3c3c3c; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-background-radius: 6;" :
                "-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 6; -fx-border-color: #d1d5db; -fx-border-radius: 6;");

        Button searchBtn = createActionButtonWithIcon("Search", FontAwesomeSolid.SEARCH, () -> filterUsersByUserId(searchField.getText()));
        Button clearBtn = createActionButton("Clear", () -> {
            searchField.clear();
            filterUsersByUserId("");
        });

        actionButtons.getChildren().addAll(addUserBtn, refreshBtn,
                new Label("  "), searchField, searchBtn, clearBtn);

        // User statistics
        HBox userStats = new HBox(24);
        userStats.setPadding(new Insets(0, 0, 32, 0));;

        userStats.getChildren().add(
                createMiniStatCard("Total Users", String.valueOf(usersData.size()))
        );

        // Users table
        usersTable = createUsersTable();
        VBox tableContainer = createTableContainer(usersTable);

        container.getChildren().addAll(title, actionButtons, userStats, tableContainer);
        contentPane.getChildren().setAll(container);
    }

    private TableView<User> createUsersTable() {
        TableView<User> table = new TableView<>();
        filteredUsersData.setAll(usersData);
        table.setItems(filteredUsersData);
        table.setPrefHeight(400);

        TableColumn<User, String> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameCol.setPrefWidth(200);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(250);

        TableColumn<User, String> majorCol = new TableColumn<>("Major");
        majorCol.setCellValueFactory(new PropertyValueFactory<>("major"));
        majorCol.setPrefWidth(150);

        TableColumn<User, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttonsBox = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4;");
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 4;");

                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    showEditUserDialog(user);
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    showDeleteUserConfirmation(user);
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

        table.getColumns().addAll(idCol, nameCol, emailCol, majorCol, actionCol);
        return table;
    }

    private void fetchTransactions(){
        transactionsData.clear();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/transaction"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());
            mapper.findAndRegisterModules();
            TransactionResponse txResp = mapper.readValue(response.body(), TransactionResponse.class);
            for(Transaction tx : txResp.data){
                transactionsData.add(tx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === TRANSACTIONS MANAGEMENT ===
    private void loadTransactionsContent() {
        fetchTransactions();

        VBox container = createContentContainer();
        Label title = createTitleLabel("Transactions & Fines");

        // Action buttons with search
        HBox actionButtons = new HBox(12);
        actionButtons.setPadding(new Insets(0, 0, 24, 0));
        actionButtons.setAlignment(Pos.CENTER_LEFT);

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

        actionButtons.getChildren().addAll(refreshBtn,
                new Label("  "), searchField, searchBtn, clearBtn);

        // Fine statistics
        HBox fineStats = new HBox(24);
        fineStats.setPadding(new Insets(0, 0, 32, 0));

        long totalFines = calculateTotalFines();

        fineStats.getChildren().addAll(
                createMiniStatCard("Active Loans", String.valueOf(transactionsData.stream().filter(t -> "borrowed".equals(t.getStatus())).count())),
                createMiniStatCard("Returned Loans", String.valueOf(transactionsData.stream().filter(t -> "returned".equals(t.getStatus())).count()))
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
        borrowCol.setCellValueFactory(cellData -> {
            String createdAt = cellData.getValue().getCreatedAt().split("T")[0];
            LocalDate date = LocalDate.parse(createdAt);
            return new ReadOnlyObjectWrapper<>(date);
        });
        borrowCol.setPrefWidth(120);

        TableColumn<Transaction, LocalDate> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
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
                    setText(status.substring(0,1).toUpperCase() + status.substring(1).toLowerCase());

                    Transaction transaction = getTableView().getItems().get(getIndex());
                    LocalDate returnDate = LocalDate.parse(transaction.getReturnDate());

                    if(status.equals("borrowed")){
                        if(returnDate.isBefore(LocalDate.now())){
                            transaction.setStatus("Overdue");
                            setText("Overdue");
                            setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                            return;
                        }
                    }

                    switch (status) {
                        case "borrowed":
                            setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
                            break;
                        case "returned":
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
                        long daysOverdue = Math.max(0, LocalDate.now().toEpochDay() - LocalDate.parse(transaction.getReturnDate()).toEpochDay());
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
        statusField.getItems().addAll("Borrowed", "Overdue", "Returned");
        statusField.setValue("Borrowed");

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
//                String newId = "T" + String.format("%03d", transactionsData.size() + 1);
//                return new Transaction(newId, userIdField.getText(), bookIdField.getText(),
//                        borrowDatePicker.getValue(), dueDatePicker.getValue(), statusField.getValue());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(transaction -> {
            transactionsData.add(transaction);
            filteredTransactionsData.setAll(transactionsData);
            showAlert("Success", "Transaction added successfully!");
        });
    }

    private void updateTransaction(long id, UpdateTransactionRequest txUpdate){
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(txUpdate);
            System.out.println("requestBody: " + requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/transaction/" + id))
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                showAlert("Success", "Transaction updated successfully!");
                return;
            } else {
                showAlert("Error", "Transaction update failed!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
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
        DatePicker dueDatePicker = new DatePicker(LocalDate.parse(transaction.getReturnDate()));
        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("Borrowed", "Returned");
        statusField.setValue(transaction.getStatus().substring(0,1).toUpperCase() + transaction.getStatus().substring(1).toLowerCase());

        grid.add(new Label("User ID:"), 0, 0);
        grid.add(userIdField, 1, 0);
        grid.add(new Label("Book ID:"), 0, 1);
        grid.add(bookIdField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                UpdateTransactionRequest updateTransactionRequest = new UpdateTransactionRequest(Long.parseLong(userIdField.getText()), Long.parseLong(bookIdField.getText()), dueDatePicker.getValue().format(formatter), statusField.getValue());
                updateTransaction(Long.parseLong(transaction.getId()), updateTransactionRequest);
                loadTransactionsContent();
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

    private boolean deleteTransaction(long id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/transaction/" + id))
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());
            mapper.findAndRegisterModules();
            DeleteBookResponse deleteResp = mapper.readValue(response.body(), DeleteBookResponse.class);

            if (deleteResp.status == 200){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
                if(deleteTransaction(Long.parseLong(transaction.getId()))){
                    showAlert("Success", "Transaction deleted successfully!");
                } else {
                    showAlert("Error", "Transaction could not be deleted!");
                }
                loadTransactionsContent(); // Refresh to update statistics
            }
        });
    }

    private void returnBook(ReturnBookRequest returnBookRequest) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(returnBookRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/return"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            mapper.findAndRegisterModules();
            System.out.println(response.body());

            if(response.statusCode() == 200) {
                showAlert("Success", "Fine payment recorded successfully!\nTransaction status updated to 'Returned'.");
                return;
            }

            showAlert("Error", "Internal server error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void payFine(Transaction transaction) {
        if (!"Overdue".equals(transaction.getStatus())) {
            showAlert("Information", "This transaction doesn't have any fine to pay.");
            return;
        }

        long daysOverdue = Math.max(0, LocalDate.now().toEpochDay() - LocalDate.parse(transaction.getReturnDate()).toEpochDay());
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
                ReturnBookRequest returnBookRequest = new ReturnBookRequest(Integer.parseInt(transaction.getId()));
                returnBook(returnBookRequest);
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

    private boolean addBook(CreateBookRequest newBook){
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(newBook);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/book"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            if(response.statusCode() == 200) {
                showAlert("Success", "Book created successfully!");
                return true;
            } else {
                showAlert("Error", "Book created failed!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAddBookDialog() {
        Dialog<BookData> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");
        dialog.setHeaderText("Enter book details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField isbnField = new TextField();
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField quantityField = new TextField(String.valueOf(0));

        grid.add(new Label("Isbn:"), 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(authorField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                CreateBookRequest createBookRequest = new CreateBookRequest(authorField.getText(), titleField.getText(), isbnField.getText(), Long.parseLong(quantityField.getText()));
                addBook(createBookRequest);
                loadBooksContent();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(book -> {
            booksData.add(book);
            showAlert("Success", "Book added successfully!");
        });
    }

    private boolean updateBook(long id, UpdateBookRequest bookUpdate){
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(bookUpdate);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/book/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                showAlert("Success", "Book updated successfully!");
                return true;
            } else {
                showAlert("Error", "Book update failed!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showEditBookDialog(BookData book) {
        Dialog<BookData> dialog = new Dialog<>();
        dialog.setTitle("Edit Book");
        dialog.setHeaderText("Edit book details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField isbnField = new TextField(book.getIsbn());
        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        TextField quantityField = new TextField(String.valueOf(book.getQuantity()));

        grid.add(new Label("Isbn:"), 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(authorField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                UpdateBookRequest bookRequest = new UpdateBookRequest(authorField.getText(), titleField.getText(), isbnField.getText(), Long.parseLong(quantityField.getText()));
                updateBook(book.getId(), bookRequest);
                loadBooksContent();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            showAlert("Success", "Book updated successfully!");
            loadBooksContent(); // Refresh the table
        });
    }

    private boolean deleteBook(BookData book) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/book/" + book.getId()))
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());
            mapper.findAndRegisterModules();
            DeleteBookResponse deleteResp = mapper.readValue(response.body(), DeleteBookResponse.class);

            if (deleteResp.status == 200){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showDeleteConfirmation(BookData book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Book");
        alert.setHeaderText("Are you sure you want to delete this book?");
        alert.setContentText("Book: " + book.getTitle() + " by " + book.getAuthor());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if(deleteBook(book)){
                    showAlert("Success", "Book deleted successfully!");
                    loadBooksContent();
                } else {
                    showAlert("Error", "Book could not be deleted!");
                }
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

    private void filterUsersByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            filteredUsersData.setAll(usersData);
            return;
        }

        try {
            long idToSearch = Long.parseLong(userId.trim());

            filteredUsersData.setAll(
                    usersData.stream()
                            .filter(u -> u.getId() == idToSearch) // assumes getId() returns long or Long
                            .collect(Collectors.toList())
            );
        } catch (NumberFormatException e) {
            // Optional: clear the list or show all if input is not a valid number
            filteredUsersData.clear();
            System.out.println("Invalid ID input: " + userId);
        }
    }

    private boolean addUser(CreateUserRequest newUser){
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(newUser);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());

            if(response.statusCode() == 200) {
                showAlert("Success", "User created successfully!");
                return true;
            } else {
                showAlert("Error", "User created failed!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter user details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField passwordField = new PasswordField();
        TextField majorField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("Major:"), 0, 3);
        grid.add(majorField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                CreateUserRequest newUser = new CreateUserRequest(nameField.getText(), emailField.getText(), passwordField.getText(), majorField.getText());
                addUser(newUser);
                loadUsersContent();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            if (user != null) {
                usersData.add(user);
                filteredUsersData.setAll(usersData);
                showAlert("Success", "User added successfully!");
            }
        });
    }

    private boolean updateUser(long id, UpdateUserRequest userUpdate){
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(userUpdate);
            System.out.println("requestBody: " + requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/" + id))
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                showAlert("Success", "User updated successfully!");
                return true;
            } else {
                showAlert("Error", "User update failed!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showEditUserDialog(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Edit user details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(user.getFullName());
        TextField emailField = new TextField(user.getEmail());
        TextField majorField = new TextField(user.getMajor());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Major:"), 0, 2);
        grid.add(majorField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                UpdateUserRequest userRequest = new UpdateUserRequest(nameField.getText(), emailField.getText(), majorField.getText());
                updateUser(user.getId(), userRequest);
                loadUsersContent();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result != null) {
                filteredUsersData.setAll(usersData);
                usersTable.refresh();
                showAlert("Success", "User updated successfully!");
                loadUsersContent(); // Refresh to update statistics
            }
        });
    }

    private boolean deleteUser(User user) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/" + user.getId()))
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());
            mapper.findAndRegisterModules();
            DeleteBookResponse deleteResp = mapper.readValue(response.body(), DeleteBookResponse.class);

            if (deleteResp.status == 200){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showDeleteUserConfirmation(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("User ID: " + user.getId() +
                "\nName: " + user.getFullName() +
                "\nEmail: " + user.getEmail());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteUser(user);
                showAlert("Success", "User deleted successfully!");
                loadUsersContent(); // Refresh to update statistics
            }
        });
    }
}