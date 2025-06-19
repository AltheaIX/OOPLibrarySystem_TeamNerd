package GUI;

import Utils.FontLoader;
import Utils.SessionManager;
import Utils.Theme;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Book;
import model.BookData;
import model.BorrowedBook;
import model.User;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static App.Main.BASE_URL;

public class DashboardUser {

    private BorderPane root;
    private VBox sidebar;
    private StackPane contentPane;
    private static final double MAX_CONTENT_WIDTH = 1200;

    private ObservableList<BookData> allBooks = FXCollections.observableArrayList();
    private ObservableList<BookData> filteredBooks = FXCollections.observableArrayList();
    private ObservableList<BorrowedBook> borrowedBooks = FXCollections.observableArrayList();
    private final ObservableList<LoanRecord> loanHistory = FXCollections.observableArrayList();

    public void show(Stage stage) {
        root = new BorderPane();

        sidebar = createSidebar(stage);
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(48, 40, 64, 40));
        applyThemeStyles();

        root.setLeft(sidebar);
        root.setCenter(centerContainer(contentPane));

        Scene scene = new Scene(root, 1024, 720);
        stage.setScene(scene);
        stage.setTitle("User Dashboard");
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleGlobalKeyShortcuts);
        stage.show();

        loadDashboardPage();
    }

    public void fetchBooks() {
        allBooks.clear();
        filteredBooks.clear();

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
                allBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filteredBooks.addAll(allBooks);
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
        container.setStyle("-fx-background-color: transparent;");
        StackPane.setAlignment(content, Pos.TOP_CENTER);
        return container;
    }

    private VBox createSidebar(Stage stage) {
        VBox box = new VBox(24);
        box.setPadding(new Insets(40, 24, 40, 24));
        box.setPrefWidth(220);

        box.getChildren().addAll(
                createNavButton("Dashboard", FontAwesomeSolid.HOME, this::loadDashboardPage),
                createNavButton("Books", FontAwesomeSolid.BOOK, this::loadBooksPage),
                createNavButton("Loan History", FontAwesomeSolid.CLOCK, this::loadLoanHistoryPage),
                createNavButton("Profile", FontAwesomeSolid.USER, this::loadUserPage),
                createNavButton("Settings", FontAwesomeSolid.COG, this::loadSettingsPage),
                createNavButton("Logout", FontAwesomeSolid.SIGN_OUT_ALT, () -> confirmLogout(stage))
        );

        updateSidebarStyles(box);
        return box;
    }

    private void confirmLogout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Your session will be closed.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            LoginPage login = new LoginPage(stage);
            Scene loginScene = new Scene(login.getView(), 400, 600);
            stage.setScene(loginScene);
        }
    }

    private void updateSidebarStyles(VBox sidebar) {
        if (Theme.isDarkMode) {
            sidebar.setStyle("-fx-background-color: #222;");
        } else {
            sidebar.setStyle("-fx-background-color: #f9fafb;");
        }
    }

    private Button createNavButton(String text, FontAwesomeSolid iconType, Runnable action) {
        FontIcon icon = new FontIcon(iconType);
        icon.setIconSize(20);
        icon.setIconColor(Theme.isDarkMode ? Color.WHITE : Color.web("#333333"));

        Label label = new Label(text);
        label.setFont(FontLoader.loadPoppins(14));
        label.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.web("#333333"));

        HBox content = new HBox(12, icon, label);
        content.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button();
        btn.setGraphic(content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        btn.setOnAction(e -> action.run());

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #6366f1; -fx-text-fill: white; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + (Theme.isDarkMode ? "white;" : "black;")));

        btn.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                btn.setStyle("-fx-background-color: #6366f1; -fx-text-fill: white; -fx-background-radius: 8;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + (Theme.isDarkMode ? "white;" : "black;"));
            }
        });

        return btn;
    }

    private void handleGlobalKeyShortcuts(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.F) {
            // Implement search focus logic here
            event.consume();
        }
    }

    private Color colorForText() {
        return Theme.isDarkMode ? Color.web("#e5e7eb") : Color.web("#111827");
    }

    private Color colorForSubText() {
        return Theme.isDarkMode ? Color.web("#e5e7eb") : Color.web("#111827");
    }

    private void loadDashboardPage() {
        fetchBooks();
        fetchBorrowedBooks();

        VBox container = createContentContainer();

        User user = SessionManager.getInstance().getUser();
        Label mainTitle = new Label("Welcome back, " + user.getFullName());
        mainTitle.setFont(Font.font(FontLoader.loadPoppins(42).getFamily(), FontWeight.SEMI_BOLD, 42));
        mainTitle.setTextFill(colorForText());
        container.getChildren().add(mainTitle);

        Label subtitle = new Label("Overview of your library and personal activity.");
        subtitle.setFont(FontLoader.loadPoppins(16));
        subtitle.setTextFill(colorForSubText());
        container.getChildren().add(subtitle);

        Button checkInBtn = new Button("Check In");
        applyButtonStyles(checkInBtn);
        checkInBtn.setOnAction(e -> showCheckInDialog());
        container.getChildren().add(checkInBtn);

        HBox statsRow = new HBox(24);
        statsRow.setPadding(new Insets(32, 0, 48, 0));
        statsRow.getChildren().addAll(
                createStatCard("Total Books", String.valueOf(allBooks.size())),
                createStatCard("Borrowed Books", String.valueOf(borrowedBooks.size()))
        );
        container.getChildren().add(statsRow);

        // Search bars
        HBox searchBars = new HBox(12);
        searchBars.setAlignment(Pos.CENTER_LEFT);
        searchBars.setPadding(new Insets(0, 0, 24, 0));

        TextField searchField = new TextField();
        searchField.getStyleClass().add("search-field");
        searchField.setPromptText("Search books by title...");
        searchField.setFont(FontLoader.loadPoppins(14));
        searchField.setPrefWidth(320);
        searchField.setStyle(getTextFieldStyle());
        searchField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                filterBooks(searchField.getText());
            }
        });
        Button searchBtn = new Button("Search");
        applyButtonStyles(searchBtn);
        searchBtn.setOnAction(e -> filterBooks(searchField.getText()));

        searchBars.getChildren().addAll(searchField, searchBtn);
        container.getChildren().add(searchBars);

        VBox listContainer = createBooksCatalogContent(filteredBooks);
        container.getChildren().add(listContainer);

        contentPane.getChildren().setAll(container);
    }

    public void fetchBorrowedBooks() {
        borrowedBooks.clear();
        User user = SessionManager.getInstance().getUser();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/borrow/" + user.getId()))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            System.out.println(response.body());
            BorrowedBookResponse bookResponse = mapper.readValue(response.body(), BorrowedBookResponse .class);
            for(BorrowedBook book : bookResponse.data){
                borrowedBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBooksPage() {
        fetchBooks();
        fetchBorrowedBooks();
        VBox container = createContentContainer();

        Label title = new Label("Your Books");
        title.setFont(Font.font(FontLoader.loadPoppins(36).getFamily(), FontWeight.SEMI_BOLD, 36));
        title.setTextFill(colorForText());

        container.getChildren().add(title);
        VBox catalogContent = createBooksCatalogContent(filteredBooks);
        container.getChildren().add(catalogContent);

        contentPane.getChildren().setAll(container);

        Label borrowedTitle = new Label("Borrowed Books");
        borrowedTitle.setFont(FontLoader.loadPoppins(24));
        borrowedTitle.setTextFill(colorForText());
        borrowedTitle.setPadding(new Insets(0, 0, 12, 0));
        container.getChildren().add(borrowedTitle);

        VBox borrowedBooksContainer = createContentCard();
        borrowedBooksContainer.setPadding(new Insets(16));
        borrowedBooksContainer.setSpacing(8);

        if (borrowedBooks.isEmpty()) {
            Label noBorrowed = new Label("You have no borrowed books currently.");
            noBorrowed.setFont(FontLoader.loadPoppins(14));
            noBorrowed.setTextFill(colorForSubText());
            borrowedBooksContainer.getChildren().add(noBorrowed);
        } else {
            for (BorrowedBook book : borrowedBooks) {
                HBox bookBox = new HBox(16);
                bookBox.setAlignment(Pos.CENTER_LEFT);

                ImageView cover = new ImageView(new Image("http://image.com", 50, 80, true, true, true));
                cover.setSmooth(true);

                VBox infoBox = new VBox(4);
                title.setFont(FontLoader.loadPoppins(16));
                title.setTextFill(colorForText());

                Label borrowInfo = new Label(createBorrowInfoText(book));
                borrowInfo.setFont(FontLoader.loadPoppins(12));
                borrowInfo.setTextFill(colorForSubText());

                infoBox.getChildren().addAll(title, borrowInfo);

                bookBox.getChildren().addAll(cover, infoBox);
                borrowedBooksContainer.getChildren().add(bookBox);
            }
        }
        container.getChildren().add(borrowedBooksContainer);
    }

    private boolean changePassword(ChangePasswordRequest changePasswordRequest){
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(changePasswordRequest);
            System.out.println("requestBody: " + requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/changepass"))
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("response: " + response.body());

            if(response.statusCode() == 200) {
                showAlert("Password changed successfully!");
                return true;
            }

            if(response.statusCode() == 401) {
                showAlert("Current password is incorrect!");
                return false;
            }

            showAlert("Password change failed!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void loadUserPage() {
        VBox container = createContentContainer();

        Label title = new Label("User Profile");
        title.setFont(Font.font(FontLoader.loadPoppins(36).getFamily(), FontWeight.SEMI_BOLD, 36));
        title.setTextFill(colorForText());

        container.getChildren().add(title);

        VBox card = createContentCard();
        card.setSpacing(24);
        User user = SessionManager.getInstance().getUser();
        OffsetDateTime dateTime = OffsetDateTime.parse(user.getCreatedAt());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);

        Label nameLabel = new Label("Name: " + user.getFullName());
        Label emailLabel = new Label("Email: " + user.getEmail());
        Label memberSinceLabel = new Label("Member Since: " + dateTime.format(formatter));

        for (Label lbl : new Label[]{nameLabel, emailLabel, memberSinceLabel}) {
            lbl.setFont(FontLoader.loadPoppins(18));
            lbl.setTextFill(colorForSubText());
            card.getChildren().add(lbl);
        }

        VBox pwChangeBox = new VBox(12);
        pwChangeBox.setPadding(new Insets(20, 0, 0, 0));

        Label pwLabel = new Label("Change Password");
        pwLabel.setFont(FontLoader.loadPoppins(20));
        pwLabel.setTextFill(colorForText());

        PasswordField currentPwField = new PasswordField();
        currentPwField.setPromptText("Current Password");
        currentPwField.setPromptText("********");
        currentPwField.setStyle(getTextFieldStyle());

        PasswordField newPwField = new PasswordField();
        newPwField.setPromptText("New Password");
        newPwField.setPromptText("********");
        newPwField.setStyle(getTextFieldStyle());

        PasswordField confirmPwField = new PasswordField();
        confirmPwField.setPromptText("Confirm New Password");
        confirmPwField.setPromptText("********");
        confirmPwField.setStyle(getTextFieldStyle());

        Button changePwBtn = new Button("Change Password");
        applyButtonStyles(changePwBtn);
        changePwBtn.setOnAction(e -> {
            if (!validatePasswordFields(currentPwField, newPwField, confirmPwField)) return;

            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(user.getEmail(), currentPwField.getText(), newPwField.getText());
            if(changePassword(changePasswordRequest)) {
                currentPwField.clear();
                newPwField.clear();
                confirmPwField.clear();
            }
        });

        pwChangeBox.getChildren().addAll(pwLabel, currentPwField, newPwField, confirmPwField, changePwBtn);
        card.getChildren().add(pwChangeBox);

        container.getChildren().add(card);
        contentPane.getChildren().setAll(container);
    }

    private void loadSettingsPage() {
        VBox container = createContentContainer();

        Label title = new Label("Settings");
        title.setFont(Font.font(FontLoader.loadPoppins(36).getFamily(), FontWeight.SEMI_BOLD, 36));
        title.setTextFill(colorForText());

        VBox card = createContentCard();
        card.setSpacing(24);

        Label darkModeLabel = new Label("Dark Mode");
        darkModeLabel.setFont(FontLoader.loadPoppins(18));
        darkModeLabel.setTextFill(colorForSubText());

        CheckBox darkModeToggle = new CheckBox();
        darkModeToggle.setSelected(Theme.isDarkMode);
        darkModeToggle.setStyle("-fx-cursor: hand;");
        darkModeToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            Theme.isDarkMode = isSelected;
            // Trigger UI refresh, reload current page perhaps
            Stage stage = (Stage) root.getScene().getWindow();
            show(stage);
        });

        HBox toggleBox = new HBox(10, darkModeLabel, darkModeToggle);
        toggleBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().add(toggleBox);

        container.getChildren().add(card);
        contentPane.getChildren().setAll(container);
    }

    // Helper methods

    private VBox createContentContainer() {
        VBox container = new VBox(16);
        container.setMaxWidth(MAX_CONTENT_WIDTH);
        container.setAlignment(Pos.TOP_LEFT);
        container.setPadding(new Insets(0));
        return container;
    }

    private void applyButtonStyles(Button button) {
        String baseStyle = "-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-background-radius: 8; " +
                "-fx-padding: 8 20; -fx-cursor: hand; -fx-font-size: 14px;";
        String hoverStyle = "-fx-background-color: #6366f1; -fx-text-fill: white; -fx-background-radius: 8; " +
                "-fx-padding: 8 20; -fx-cursor: hand; -fx-font-size: 14px;";
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    private VBox createStatCard(String label, String value) {
        VBox card = new VBox(8);
        card.setPrefWidth(180);
        card.setPadding(new Insets(24));
        card.setStyle(cardBackgroundStyle());

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(FontLoader.loadPoppins(28).getFamily(), FontWeight.BOLD, 28));
        valueLabel.setTextFill(colorForText());

        Label labelLabel = new Label(label.toUpperCase());
        labelLabel.setFont(FontLoader.loadPoppins(11));
        labelLabel.setTextFill(colorForSubText());
        labelLabel.setStyle("-fx-letter-spacing: 1.5;");

        card.getChildren().addAll(valueLabel, labelLabel);
        return card;
    }

    private String cardBackgroundStyle() {
        if (Theme.isDarkMode) {
            return "-fx-background-color: #2c2c2c; -fx-background-radius: 12; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10,0,0,3);";
        } else {
            return "-fx-background-color: white; -fx-background-radius: 12; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10,0,0,3);";
        }
    }

    private String getTextFieldStyle() {
        if (Theme.isDarkMode) {
            return "-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #444; " +
                    "-fx-border-radius: 6; -fx-background-radius: 6;";
        } else {
            return "-fx-border-color: #ddd; -fx-border-radius: 6; -fx-background-radius: 6;";
        }
    }

    private void refreshUI() {
        loadDashboardPage();
    }

    private boolean validatePasswordFields(PasswordField currentPw, PasswordField newPw, PasswordField confirmPw) {
        if (currentPw.getText().isEmpty() || newPw.getText().isEmpty() || confirmPw.getText().isEmpty()) {
            showAlert("Please fill in all password fields.");
            return false;
        }
        if (!newPw.getText().equals(confirmPw.getText())) {
            showAlert("New password and confirmation do not match.");
            return false;
        }
        return true;
    }

    private void showCheckInDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Check In");
        alert.setHeaderText(null);
        alert.setContentText("Check-in successful! Enjoy your reading.");
        alert.showAndWait();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private VBox createBooksCatalogContent(ObservableList<BookData> books) {
        VBox listContainer = new VBox(12);
        listContainer.setPrefWidth(MAX_CONTENT_WIDTH);
        listContainer.setPadding(new Insets(24));
        listContainer.setStyle(cardBackgroundStyle());

        Label listTitle = new Label("Books Catalog");
        listTitle.setFont(FontLoader.loadPoppins(24));
        listTitle.setTextFill(colorForText());
        listContainer.getChildren().add(listTitle);

        ListView<BookData> listView = new ListView<>(books);
        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<BookData> call(ListView<BookData> param) {
                return new ListCell<>() {
                    private final ImageView imageView = new ImageView();
                    private final Label titleLabel = new Label();
                    private final Label quantityLabel = new Label();
                    private final Button borrowReturnButton = new Button();
                    private final VBox vbox = new VBox(titleLabel, quantityLabel, new HBox(8, borrowReturnButton));
                    private final HBox hbox = new HBox(imageView, vbox);

                    {
                        imageView.setFitHeight(80);
                        imageView.setFitWidth(50);
                        imageView.setSmooth(true);
                        imageView.setPreserveRatio(true);
                        hbox.setSpacing(12);
                        vbox.setSpacing(4);

                        applyButtonStyles(borrowReturnButton);
                        borrowReturnButton.setOnAction(e -> {
                            BookData book = getItem();
                            if (book != null) showBorrowReturnDialog(book);
                        });

                        setFocusTraversable(true);
                    }

                    private void setBorrowReturnButtonText() {
                        BookData book = getItem();
                        if (book != null) {
                            if (book.getQuantity() <= 0) {
                                borrowReturnButton.setText("Book is not available.");
                                borrowReturnButton.setDisable(true);
                            } else {
                                borrowReturnButton.setText("Borrow");
                                borrowReturnButton.setDisable(false);
                            }
                        }
                    }

                    @Override
                    protected void updateItem(BookData book, boolean empty) {
                        super.updateItem(book, empty);
                        if (empty || book == null) {
                            setGraphic(null);
                        } else {
                            imageView.setImage(new Image("https://image.com/", 50, 80, true, true, true));
                            titleLabel.setText(book.getTitle());
                            titleLabel.setFont(FontLoader.loadPoppins(16));
                            titleLabel.setTextFill(colorForText());
                            quantityLabel.setText("Quantity: " + Long.toString(book.getQuantity()));

                            setBorrowReturnButtonText();
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
        listView.setPrefHeight(400);

        // Scrollbar style for dark mode
        Platform.runLater(() -> {
            listView.lookupAll(".scroll-bar").forEach(scroll -> {
                if (scroll instanceof ScrollBar) {
                    ScrollBar bar = (ScrollBar) scroll;
                    if (Theme.isDarkMode) {
                        bar.setStyle("-fx-background-color: #2e2e2e; -fx-background: #2e2e2e;");
                    } else {
                        bar.setStyle("");
                    }
                }
            });
        });

        listContainer.getChildren().add(listView);
        return listContainer;
    }

    private void borrowBook(BorrowBookRequest bookRequest) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(bookRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/borrow"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
            mapper.findAndRegisterModules();
            BorrowBookResponse bookResponse = mapper.readValue(response.body(), BorrowBookResponse.class);

            if(response.statusCode() == 200) {
                showAlert("Book reserved successfully.");
                return;
            }

            if(response.statusCode() == 400 && Objects.equals(bookResponse.message, "Book already borrowed")) {
                showAlert("You have already borrowed this book.");
                return;
            }

            if(response.statusCode() == 400 && Objects.equals(bookResponse.message, "Not enough quantity")) {
                showAlert("This book has no quantity to borrow.");
                return;
            }

            showAlert("Internal server error!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBorrowReturnDialog(BookData book) {
        TextInputDialog dialog = new TextInputDialog("14");
        dialog.setTitle("Pinjam Buku");
        dialog.setHeaderText("Pinjam buku: " + book.getTitle());
        dialog.setContentText("Masukkan jumlah hari peminjaman (1-60):");

        Optional<String> input = dialog.showAndWait();

        input.ifPresent(daysStr -> {
            try {
                int days = Integer.parseInt(daysStr);
                if (days < 1 || days > 60) {
                    showAlert("Masukkan angka antara 1 hingga 60.");
                    return;
                }

                User user = SessionManager.getInstance().getUser();
                BorrowBookRequest bookRequest = new BorrowBookRequest(user.getId(), book.getId(), days);
                borrowBook(bookRequest);
                refreshUI();
            } catch (NumberFormatException e) {
                showAlert("Input tidak valid. Masukkan angka.");
            }
        });
    }

    private BookData fetchBookById(long bookId){
        try {
            User user = SessionManager.getInstance().getUser();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/book/id/" + bookId))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            System.out.println(response.body());

            BookResponse bookResponse = mapper.readValue(response.body(), BookResponse.class);
            if(bookResponse.status == 200){
                BookData book = bookResponse.data.get(0);
                return book;
            }

            showAlert("Internal server error.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fetchLoanHistory() {
        loanHistory.clear();

        try {
            User user = SessionManager.getInstance().getUser();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/borrow/" + user.getId()))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            System.out.println(response.body());

            BorrowedBookResponse borrowedResponse = mapper.readValue(response.body(), BorrowedBookResponse.class);
            for(BorrowedBook tx : borrowedResponse.data){
                BookData book = fetchBookById(tx.getBookId());
                LocalDate borrowDate = LocalDate.parse(tx.getCreatedAt().split("T")[0]);
                LocalDate returnDate = LocalDate.parse(tx.getReturnDate());

                loanHistory.add(new LoanRecord(book.getTitle(), borrowDate, returnDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLoanHistoryPage() {
        fetchLoanHistory();
        VBox container = createContentContainer();

        Label title = new Label("Loan History");
        title.setFont(Font.font(FontLoader.loadPoppins(36).getFamily(), FontWeight.SEMI_BOLD, 36));
        title.setTextFill(colorForText());
        container.getChildren().add(title);

        VBox card = createContentCard();

        if (loanHistory.isEmpty()) {
            Label noHistory = new Label("No loan history available.");
            noHistory.setFont(FontLoader.loadPoppins(14));
            noHistory.setTextFill(colorForSubText());
            card.getChildren().add(noHistory);
        } else {
            TableView<LoanRecord> table = new TableView<>(loanHistory);

            TableColumn<LoanRecord, String> colTitle = new TableColumn<>("Title");
            colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
            colTitle.setPrefWidth(300);

            TableColumn<LoanRecord, LocalDate> colBorrow = new TableColumn<>("Borrow Date");
            colBorrow.setCellValueFactory(data -> data.getValue().borrowDateProperty());
            colBorrow.setPrefWidth(150);

            TableColumn<LoanRecord, LocalDate> colReturn = new TableColumn<>("Return Date");
            colReturn.setCellValueFactory(data -> data.getValue().returnDateProperty());
            colReturn.setPrefWidth(150);

            table.getColumns().addAll(colTitle, colBorrow, colReturn);
            table.setPrefHeight(400);
            card.getChildren().add(table);
        }
        container.getChildren().add(card);
        contentPane.getChildren().setAll(container);
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

    private void filterBooks(String text) {
        if (text == null || text.trim().isEmpty()) {
            filteredBooks.setAll(allBooks);
        } else {
            String lower = text.toLowerCase();
            filteredBooks.setAll(allBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(lower))
                    .collect(Collectors.toList()));
        }
    }

    private String createBorrowInfoText(BorrowedBook book) {
        LocalDate borrowDate = LocalDate.parse(book.getCreatedAt().split("T")[0]);
        LocalDate returnDate = LocalDate.parse(book.getReturnDate());

        long daysBorrowed = ChronoUnit.DAYS.between(borrowDate, LocalDate.now());
        return "Borrowed " + daysBorrowed + (daysBorrowed == 1 ? " day ago. " : " days ago. ") +
                "Return by " + returnDate.toString();
    }

    public static class LoanRecord {
        private final SimpleStringProperty title;
        private final SimpleObjectProperty<LocalDate> borrowDate;
        private final SimpleObjectProperty<LocalDate> returnDate;

        public LoanRecord(String title, LocalDate borrowDate, LocalDate returnDate) {
            this.title = new SimpleStringProperty(title);
            this.borrowDate = new SimpleObjectProperty<>(borrowDate);
            this.returnDate = new SimpleObjectProperty<>(returnDate);
        }

        public SimpleStringProperty titleProperty() {
            return title;
        }

        public SimpleObjectProperty<LocalDate> borrowDateProperty() {
            return borrowDate;
        }

        public SimpleObjectProperty<LocalDate> returnDateProperty() {
            return returnDate;
        }
    }
}
