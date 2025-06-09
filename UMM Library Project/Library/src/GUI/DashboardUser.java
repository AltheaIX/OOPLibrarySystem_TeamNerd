package GUI;

import Utils.FontLoader;
import Utils.Theme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardUser  {

    private BorderPane root;
    private VBox sidebar;
    private StackPane contentPane;
    private static final double MAX_CONTENT_WIDTH = 1200;

    public void show(Stage stage) {
        root = new BorderPane();

        sidebar = createSidebar(stage);
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(64, 40, 64, 40));
        applyThemeStyles();

        root.setLeft(sidebar);
        root.setCenter(centerContainer(contentPane));

        Scene scene = new Scene(root, 1024, 720);
        stage.setScene(scene);
        stage.setTitle("User  Dashboard");
        stage.show();

        loadDashboardPage();
    }

    // === Theming ===

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

    // === Layout Helpers ===

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

        Button btnDashboard = createNavButton("🏠 Dashboard", this::loadDashboardPage);
        Button btnBooks = createNavButton("📚 Books", this::loadBooksPage);
        Button btnFavourites = createNavButton("❤ Favourites", this::loadFavouritesPage);
        Button btnUser  = createNavButton("👤 User", this::loadUserPage);
        Button btnSettings = createNavButton("⚙️ Settings", this::loadSettingsPage);
        Button btnLogout = createNavButton("⬆ Logout", () -> {
            LoginPage login = new LoginPage(stage);
            stage.setScene(new Scene(login.getView(), 400, 600));
        });

        box.getChildren().addAll(btnDashboard, btnBooks, btnFavourites, btnUser , btnSettings, btnLogout);
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

    private Button createNavButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setFont(FontLoader.loadPoppins(16));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        updateNavButtonStyle(btn, false);
        btn.setOnMouseEntered(e -> updateNavButtonStyle(btn, true));
        btn.setOnMouseExited(e -> updateNavButtonStyle(btn, false));
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void updateNavButtonStyle(Button btn, boolean hover) {
        if (Theme.isDarkMode) {
            if (hover) {
                btn.setStyle("-fx-background-color: #5750de; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #d1d5db; -fx-padding: 10 20; -fx-background-radius: 8;");
            }
        } else {
            if (hover) {
                btn.setStyle("-fx-background-color: #4338ca; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #374151; -fx-padding: 10 20; -fx-background-radius: 8;");
            }
        }
    }

    // === PAGE CONTENTS ===

    /** DASHBOARD PAGE **/
    private void loadDashboardPage() {
        VBox container = createContentContainer();

        Label mainTitle = new Label("Welcome back, user");
        mainTitle.setFont(Font.font(FontLoader.loadPoppins(48).getFamily(), FontWeight.SEMI_BOLD, 48));
        mainTitle.setTextFill(colorForText());
        container.getChildren().add(mainTitle);

        Label subtitle = new Label("Overview of your library and book recommendations.");
        subtitle.setFont(FontLoader.loadPoppins(18));
        subtitle.setTextFill(colorForSubText());
        container.getChildren().add(subtitle);

        HBox stats = new HBox(24);
        stats.setPadding(new Insets(40, 0, 48, 0));
        stats.getChildren().addAll(
                createStatCard("Total Books", "123"),
                createStatCard("Borrowed Books", "456"),
                createStatCard("Total Users", "789"),
                createStatCard("Books Reported", "100")
        );
        container.getChildren().add(stats);

        Label recTitle = new Label("Books Recommendation");
        recTitle.setFont(FontLoader.loadPoppins(28));
        recTitle.setTextFill(colorForText());
        recTitle.setPadding(new Insets(0, 0, 16, 0));
        container.getChildren().add(recTitle);

        VBox recommendationCard = new VBox();
        recommendationCard.setPadding(new Insets(24));
        recommendationCard.setMaxWidth(MAX_CONTENT_WIDTH);
        recommendationCard.setStyle(cardBackgroundStyle());

        HBox bookCoversBox = new HBox(16);
        bookCoversBox.setPadding(new Insets(0));
        bookCoversBox.setStyle("-fx-background-color: transparent;");

        String[] bookUrls = {
                "https://m.media-amazon.com/images/I/51UoqRAxwEL.jpg",
                "https://m.media-amazon.com/images/I/41jEbK-jG+L.jpg",
                "https://m.media-amazon.com/images/I/41j0Fk4tACL.jpg",
                "https://m.media-amazon.com/images/I/51MYszsRkYL.jpg"
        };

        for (String url : bookUrls) {
            ImageView iv = new ImageView(new Image(url, 120, 180, true, true));
            iv.setSmooth(true);
            iv.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8,0,0,2);");
            Rectangle clip = new Rectangle(120, 180);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            iv.setClip(clip);
            bookCoversBox.getChildren().add(iv);
        }

        recommendationCard.getChildren().add(bookCoversBox);
        container.getChildren().add(recommendationCard);

        contentPane.getChildren().setAll(container);
    }

    /** BOOKS PAGE **/
    private void loadBooksPage() {
        VBox container = createContentContainer();

        Label title = new Label("Your Books");
        title.setFont(Font.font(FontLoader.loadPoppins(36).getFamily(), FontWeight.SEMI_BOLD, 36));
        title.setTextFill(colorForText());
        container.getChildren().add(title);

        VBox card = createContentCard();
        card.setSpacing(16);

        String[] exampleBooks = {
                "The Great Gatsby", "1984", "Clean Code", "Effective Java"
        };
        for (String book : exampleBooks) {
            Label label = new Label("• " + book);
            label.setFont(FontLoader.loadPoppins(18));
            label.setTextFill(colorForSubText());
            card.getChildren().add(label);
        }

        container.getChildren().add(card);
        contentPane.getChildren().setAll(container);
    }

    /** FAVOURITES PAGE **/
    private void loadFavouritesPage() {
        VBox container = createContentContainer();

        Label title = new Label("Favourites");
        title.setFont(Font.font(FontLoader.loadPoppins(36).getFamily(), FontWeight.SEMI_BOLD, 36));
        title.setTextFill(colorForText());
        container.getChildren().add(title);

        VBox card = createContentCard();
        card.setSpacing(16);

        String[] favBooks = {
                "To Kill a Mockingbird", "Pride and Prejudice", "The Lord of the Rings"
        };
        for (String fav : favBooks) {
            Label label = new Label("♥ " + fav);
            label.setFont(FontLoader.loadPoppins(18));
            label.setTextFill(colorForSubText());
            card.getChildren().add(label);
        }

        container.getChildren().add(card);
        contentPane.getChildren().setAll(container);
    }

    /** USER PAGE **/
    private void loadUserPage() {
        VBox container = createContentContainer();

        Label title = new Label("User  Profile");
        title.setFont(Font.font(FontLoader.loadPoppins(36).getFamily(), FontWeight.SEMI_BOLD, 36));
        title.setTextFill(colorForText());
        container.getChildren().add(title);

        VBox card = createContentCard();
        card.setSpacing(24);

        Label nameLabel = new Label("Name: John Doe");
        Label emailLabel = new Label("Email: johndoe@example.com");
        Label memberSinceLabel = new Label("Member Since: January 2022");

        for (Label lbl : new Label[]{nameLabel, emailLabel, memberSinceLabel}) {
            lbl.setFont(FontLoader.loadPoppins(18));
            lbl.setTextFill(colorForSubText());
            card.getChildren().add(lbl);
        }

        container.getChildren().add(card);
        contentPane.getChildren().setAll(container);
    }

    /** SETTINGS PAGE **/
    private void loadSettingsPage() {
        VBox container = createContentContainer();

        Label title = new Label("Settings");
        title.setFont(Font.font(FontLoader.loadPoppins(36).getFamily(), FontWeight.SEMI_BOLD, 36));
        title.setTextFill(colorForText());
        container.getChildren().add(title);

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
            show((Stage) root.getScene().getWindow());
        });

        HBox toggleBox = new HBox(10, darkModeLabel, darkModeToggle);
        toggleBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().add(toggleBox);

        String[] options = {
                "Email Notifications",
                "Auto-Backup Data",
                "Usage Reports"
        };
        for (String option : options) {
            Label lbl = new Label("• " + option);
            lbl.setFont(FontLoader.loadPoppins(18));
            lbl.setTextFill(colorForSubText());
            card.getChildren().add(lbl);
        }

        container.getChildren().add(card);
        contentPane.getChildren().setAll(container);
    }

    // --- Helper methods ---

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

    private Color colorForText() {
        return Theme.isDarkMode ? Color.web("#e5e7eb") : Color.web("#111827");
    }

    private Color colorForSubText() {
        return Theme.isDarkMode ? Color.web("#9ca3af") : Color.web("#6b7280");
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
        return Theme.isDarkMode ?
                "-fx-background-color: #2c2c2c; -fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10,0,0,3);" :
                "-fx-background-color: white; -fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10,0,0,3);";
    }
}