package GUI;

import Utils.FontLoader;
import Utils.Theme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardAdmin {
    private BorderPane root;
    private VBox sidebar;
    private StackPane contentPane;
    private static final double MAX_CONTENT_WIDTH = 1200;

    public void start(Stage stage) {
        root = new BorderPane();

        sidebar = createSidebar(stage);
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(64, 40, 64, 40));
        applyThemeStyles();

        root.setLeft(sidebar);
        root.setCenter(centerContainer(contentPane));

        Scene scene = new Scene(root, 1024, 720);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();

        loadDashboardContent();
    }

    // Apply dark or light styles to containers
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

    // Sidebar with navigation buttons and logout
    private VBox createSidebar(Stage stage) {
        VBox box = new VBox(24);
        box.setPadding(new Insets(40, 24, 40, 24));
        box.setPrefWidth(220);

        Button dashboardBtn = createNavButton("🏠 Dashboard", this::loadDashboardContent);
        Button booksBtn = createNavButton("📚 Books", this::loadBooksContent);
        Button usersBtn = createNavButton("👥 Users", this::loadUsersContent);
        Button reportsBtn = createNavButton("📄 Reports", this::loadReportsContent);
        Button settingsBtn = createNavButton("⚙️ Settings", this::loadSettingsContent);
        Button logoutBtn = createNavButton("🚪 Logout", () -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage.getView(), 400, 600));
        });

        box.getChildren().addAll(dashboardBtn, booksBtn, usersBtn, reportsBtn, settingsBtn, logoutBtn);
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

    private Button createNavButton(String text, Runnable onClick) {
        Button btn = new Button(text);
        btn.setFont(FontLoader.loadPoppins(16));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        updateNavButtonStyle(btn, false);
        btn.setOnMouseEntered(e -> updateNavButtonStyle(btn, true));
        btn.setOnMouseExited(e -> updateNavButtonStyle(btn, false));
        btn.setOnAction(e -> onClick.run());
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

    // Admin Dashboard with system stats
    private void loadDashboardContent() {
        VBox container = createContentContainer();

        Label title = createTitleLabel("Admin Dashboard");

        HBox metrics = new HBox(24);
        metrics.setPadding(new Insets(40, 0, 48, 0));

        metrics.getChildren().addAll(
                createMetricCard("Active Users", "1,234"),
                createMetricCard("Sessions Today", "567"),
                createMetricCard("System Health", "Good"),
                createMetricCard("Errors Reported", "4")
        );

        container.getChildren().addAll(title, metrics);
        contentPane.getChildren().setAll(container);
    }

    // Metric card styling
    private VBox createMetricCard(String label, String value) {
        VBox card = new VBox(8);
        card.setPrefWidth(200);
        card.setPadding(new Insets(24));
        card.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #2c2c2c; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8,0,0,2);"
                : "-fx-background-color: #f3f4f6; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8,0,0,2);");

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font(FontLoader.loadPoppins(28).getFamily(), FontWeight.BOLD, 28));
        valLbl.setTextFill(Theme.isDarkMode ? Color.web("#d1d5db") : Color.web("#111827"));

        Label labelLbl = new Label(label.toUpperCase());
        labelLbl.setFont(FontLoader.loadPoppins(11));
        labelLbl.setTextFill(Theme.isDarkMode ? Color.web("#9ca3af") : Color.web("#6b7280"));
        labelLbl.setStyle("-fx-letter-spacing: 1.5;");

        card.getChildren().addAll(valLbl, labelLbl);
        return card;
    }

    // Books management page
    private void loadBooksContent() {
        VBox container = createContentContainer();

        Label title = createTitleLabel("Books Management");

        VBox card = createContentCard();
        card.setSpacing(16);

        Label overview = createInfoLabel("Manage your books collection here.");

        HBox stats = new HBox(20);
        stats.getChildren().addAll(
                createMiniStatCard("Books Added", "1,200"),
                createMiniStatCard("Currently Borrowed", "450"),
                createMiniStatCard("Overdue Books", "27")
        );

        card.getChildren().addAll(overview, stats);
        container.getChildren().addAll(title, card);
        contentPane.getChildren().setAll(container);
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

    private VBox createMiniStatCard(String label, String value) {
        VBox card = new VBox(6);
        card.setPrefWidth(160);
        card.setPadding(new Insets(16));
        card.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #3b82f6; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.5), 8,0,0,4);"
                : "-fx-background-color: #dbeafe; -fx-background-radius: 10;");

        Label valLbl = new Label(value);
        valLbl.setFont(Font.font(FontLoader.loadPoppins(22).getFamily(), FontWeight.BOLD, 22));
        valLbl.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.web("#1e40af"));

        Label labelLbl = new Label(label);
        labelLbl.setFont(FontLoader.loadPoppins(14));
        labelLbl.setTextFill(Theme.isDarkMode ? Color.web("#bfdbfe") : Color.web("#1e40af"));

        card.getChildren().addAll(valLbl, labelLbl);
        return card;
    }

    // Users management page
    private void loadUsersContent() {
        VBox container = createContentContainer();

        Label title = createTitleLabel("Users Management");

        VBox card = createContentCard();
        card.setSpacing(16);

        Label overview = createInfoLabel("Manage users and roles here.");

        HBox stats = new HBox(20);
        stats.getChildren().addAll(
                createMiniStatCard("Active Users", "850"),
                createMiniStatCard("Inactive Users", "120"),
                createMiniStatCard("Admin Users", "15")
        );

        Label rolesLbl = createInfoLabel("Role Distribution: Admin 2%, User 98%");

        card.getChildren().addAll(overview, stats, rolesLbl);
        container.getChildren().addAll(title, card);
        contentPane.getChildren().setAll(container);
    }

    // Reports page with simple line chart
    private void loadReportsContent() {
        VBox container = createContentContainer();

        Label title = createTitleLabel("Reports");

        VBox card = createContentCard();
        card.setSpacing(16);

        Label overview = createInfoLabel("View system activity and issue reports.");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day");
        yAxis.setLabel("Error Count");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setPrefHeight(300);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setStyle(Theme.isDarkMode ?
                "-fx-background-color: transparent; -fx-text-fill: white;" :
                "-fx-background-color: transparent;");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 5));
        series.getData().add(new XYChart.Data<>(2, 3));
        series.getData().add(new XYChart.Data<>(3, 8));
        series.getData().add(new XYChart.Data<>(4, 2));
        series.getData().add(new XYChart.Data<>(5, 6));
        lineChart.getData().add(series);

        card.getChildren().addAll(overview, lineChart);
        container.getChildren().addAll(title, card);
        contentPane.getChildren().setAll(container);
    }

    // Settings page with dark mode toggle and dummy options
    private void loadSettingsContent() {
        VBox container = createContentContainer();

        Label title = createTitleLabel("Settings");

        VBox card = createContentCard();
        card.setSpacing(24);

        Label darkModeLabel = new Label("Dark Mode");
        darkModeLabel.setFont(FontLoader.loadPoppins(18));
        darkModeLabel.setTextFill(Theme.isDarkMode ? Color.web("#d1d5db") : Color.web("#374151"));

        CheckBox darkModeToggle = new CheckBox();
        darkModeToggle.setSelected(Theme.isDarkMode);
        darkModeToggle.setStyle("-fx-cursor: hand;");

        darkModeToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            Theme.isDarkMode = isSelected;
            start((Stage) root.getScene().getWindow());
        });

        HBox toggleBox = new HBox(10, darkModeLabel, darkModeToggle);
        toggleBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().add(toggleBox);

        String[] options = {
                "Notifications: Enabled",
                "Email Preferences",
                "Privacy Settings"
        };
        for (String option : options) {
            Label lbl = new Label("• " + option);
            lbl.setFont(FontLoader.loadPoppins(18));
            lbl.setTextFill(Theme.isDarkMode ? Color.web("#9ca3af") : Color.web("#4b5563"));
            card.getChildren().add(lbl);
        }

        container.getChildren().addAll(title, card);
        contentPane.getChildren().setAll(container);
    }

    // --- Helpers ---

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
}
