package GUI;

import Utils.*;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class LoginPage {
    private VBox root;
    private Stage stage;

    // Dummy users
    private final Map<String, String> userPasswords = new HashMap<>();
    private final Map<String, String> userRoles = new HashMap<>();

    public LoginPage(Stage stage) {
        this.stage = stage;
        initDummyUsers();
        createUI();
    }

    public VBox getView() {
        return root;
    }

    private void initDummyUsers() {
        userPasswords.put("admin@example.com", "admin123");
        userRoles.put("admin@example.com", "admin");

        userPasswords.put("user@example.com", "user123");
        userRoles.put("user@example.com", "user");
    }

    private void createUI() {
        // Root container
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #121212;" :
                "-fx-background-color: white;");
        root.setSpacing(24);
        root.setPadding(new Insets(20, 40, 40, 40));

        // Sticky Top Navigation Bar
        HBox navBar = createStickyTopNav();
        root.getChildren().add(navBar);

        // Title
        Text title = new Text("Login");
        title.setFont(FontLoader.loadPoppins(28));
        title.setFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);
        root.getChildren().add(title);

        // Login form
        VBox formBox = createLoginForm();
        root.getChildren().add(formBox);
    }

    private HBox createStickyTopNav() {
        HBox navBar = new HBox();
        navBar.setPadding(new Insets(12, 24, 12, 24));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setSpacing(20);
        navBar.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #1e1e1e; -fx-border-color: #333;" :
                "-fx-background-color: #f9fafb; -fx-border-color: #e5e7eb;");
        navBar.setPrefHeight(60);

        Label logo = new Label("UMM Library");
        logo.setFont(FontLoader.loadPoppins(24));
        logo.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        CheckBox darkModeToggle = new CheckBox("Dark Mode");
        darkModeToggle.setFont(FontLoader.loadPoppins(12));
        darkModeToggle.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);
        darkModeToggle.setSelected(Theme.isDarkMode);
        darkModeToggle.setOnAction(e -> toggleDarkMode());

        navBar.getChildren().addAll(logo, spacer, darkModeToggle);
        return navBar;
    }

    private VBox createLoginForm() {
        VBox formBox = new VBox(10);
        formBox.setMaxWidth(300);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(40));

        // Email
        Label emailLabel = new Label("EMAIL");
        emailLabel.setFont(FontLoader.loadPoppins(12));
        emailLabel.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.BLACK);
        TextField emailField = new TextField();
        emailField.setPromptText("hello@example.com");
        emailField.setStyle(Theme.isDarkMode ? "-fx-background-color: #2a2a2a; -fx-text-fill: white;" : "");

        // Password
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setFont(FontLoader.loadPoppins(12));
        passwordLabel.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.BLACK);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("*******");
        passwordField.setStyle(Theme.isDarkMode ? "-fx-background-color: #2a2a2a; -fx-text-fill: white;" : "");

        // Message
        Label messageLabel = new Label();
        messageLabel.setFont(FontLoader.loadPoppins(11));
        messageLabel.setTextFill(Color.RED);

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setFont(FontLoader.loadPoppins(14));
        String btnBase = Theme.isDarkMode ?
                "-fx-background-color: white; -fx-text-fill: black;" :
                "-fx-background-color: black; -fx-text-fill: white;";
        loginButton.setStyle(btnBase + "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;");

        // Hover style
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: transparent; -fx-border-color: gray; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;" +
                        (Theme.isDarkMode ? "-fx-text-fill: white;" : "-fx-text-fill: black;")));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(btnBase + "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;"));

        // Register and Forgot
        HBox bottomText = new HBox(10);
        Hyperlink registerLink = new Hyperlink("Sign up");
        Hyperlink forgotLink = new Hyperlink("Forgot Password?");
        registerLink.setFont(FontLoader.loadPoppins(11));
        forgotLink.setFont(FontLoader.loadPoppins(11));
        registerLink.setTextFill(Theme.isDarkMode ? Color.LIGHTBLUE : Color.BLUE);
        forgotLink.setTextFill(Theme.isDarkMode ? Color.LIGHTBLUE : Color.BLUE);
        bottomText.setAlignment(Pos.CENTER);
        bottomText.getChildren().addAll(registerLink, forgotLink);

        // Add components to form
        formBox.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, loginButton, messageLabel, bottomText);

        // Register page transition
        registerLink.setOnAction(e -> switchToRegisterPage());

        // Forgot Password transition
        forgotLink.setOnAction(e -> switchToForgotPasswordPage());

        // Login action
        loginButton.setOnAction(e -> loginAction(emailField, passwordField, messageLabel));

        // Enter key triggers login
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginAction(emailField, passwordField, messageLabel);
            }
        });

        return formBox;
    }

    private void toggleDarkMode() {
        Theme.isDarkMode = !Theme.isDarkMode;
        Stage currentStage = (Stage) root.getScene().getWindow();
        LoginPage newPage = new LoginPage(currentStage);
        Scene newScene = new Scene(newPage.getView(), 400, 600);
        currentStage.setScene(newScene);
    }

    private void switchToRegisterPage() {
        Stage currentStage = (Stage) root.getScene().getWindow();
        RegisterPage registerPage = new RegisterPage(currentStage);
        Scene scene = new Scene(registerPage.getView(), 400, 600);
        currentStage.setScene(scene);
    }

    private void switchToForgotPasswordPage() {
        Stage currentStage = (Stage) root.getScene().getWindow();
        ForgotPassword forgotPasswordPage = new ForgotPassword(currentStage);
        Scene scene = new Scene(forgotPasswordPage.getView(), 400, 600);
        currentStage.setScene(scene);
    }

    private void loginAction(TextField emailField, PasswordField passwordField, Label messageLabel) {
        String email = emailField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        messageLabel.setText(""); // Clear previous message

        if (userPasswords.containsKey(email) && userPasswords.get(email).equals(password)) {
            String role = userRoles.get(email);
            if ("admin".equals(role)) {
                new DashboardAdmin().start(stage);
            } else {
                new DashboardUser ().show(stage);
            }
        } else {
            messageLabel.setText("Email atau password salah!");
        }
    }
}