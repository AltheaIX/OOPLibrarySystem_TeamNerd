package GUI;

import Utils.*;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
        // Root container with more space for logo
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #121212;" :
                "-fx-background-color: white;");
        root.setSpacing(20);
        root.setPadding(new Insets(30, 40, 40, 40));

        // Logo UMM - Positioned at top with proper spacing
        VBox logoSection = createLogoSection();
        root.getChildren().add(logoSection);

        // Content container for the rest
        VBox contentContainer = new VBox();
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setSpacing(25);

        // Title
        Text title = new Text("Login");
        title.setFont(FontLoader.loadPoppins(28));
        title.setFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);
        contentContainer.getChildren().add(title);

        // Login form
        VBox formBox = createLoginForm();
        contentContainer.getChildren().add(formBox);

        // Dark mode toggle at bottom
        HBox darkModeSection = createDarkModeToggle();
        contentContainer.getChildren().add(darkModeSection);

        root.getChildren().add(contentContainer);

        // Add entrance animation
        addEntranceAnimation();
    }

    private VBox createLogoSection() {
        VBox logoSection = new VBox();
        logoSection.setAlignment(Pos.CENTER);
        logoSection.setSpacing(8);
        // Reduced padding to give more space
        logoSection.setPadding(new Insets(10, 0, 15, 0));

        try {
            String logoPath = Theme.isDarkMode ?
                    "/logonightmode.png" :
                    "/logolightmode.png";

            javafx.scene.image.Image logoImage = new javafx.scene.image.Image(
                    getClass().getResourceAsStream(logoPath)
            );
            javafx.scene.image.ImageView logoView = new javafx.scene.image.ImageView(logoImage);

            // Adjusted size to prevent cropping while maintaining visibility
            logoView.setFitHeight(70);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
            // Remove any background or border effects
            logoView.setStyle("-fx-effect: null;");

            logoSection.getChildren().add(logoView);

            // University name with adjusted styling
            Label universityName = new Label("Universitas Muhammadiyah Malang");
            universityName.setFont(FontLoader.loadPoppins(12));
            universityName.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.GRAY);
            universityName.setStyle("-fx-font-weight: normal;");
            logoSection.getChildren().add(universityName);

        } catch (Exception e) {
            // Fallback jika logo tidak ditemukan
            Label logo = new Label("UMM Library");
            logo.setFont(FontLoader.loadPoppins(22));
            logo.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);
            logo.setStyle("-fx-font-weight: bold;");
            logoSection.getChildren().add(logo);

            Label subtitle = new Label("Universitas Muhammadiyah Malang");
            subtitle.setFont(FontLoader.loadPoppins(11));
            subtitle.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.GRAY);
            logoSection.getChildren().add(subtitle);
        }

        // Remove any background styling from logo section
        logoSection.setStyle("-fx-background-color: transparent;");

        return logoSection;
    }

    private VBox createLoginForm() {
        VBox formBox = new VBox(15);
        formBox.setMaxWidth(300);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(15));

        // Email
        Label emailLabel = new Label("EMAIL");
        emailLabel.setFont(FontLoader.loadPoppins(12));
        emailLabel.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.BLACK);
        TextField emailField = new TextField();
        emailField.setPromptText("hello@example.com");
        emailField.setStyle(getTextFieldStyle());
        emailField.setPrefHeight(40);

        // Password
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setFont(FontLoader.loadPoppins(12));
        passwordLabel.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.BLACK);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("*******");
        passwordField.setStyle(getTextFieldStyle());
        passwordField.setPrefHeight(40);

        // Message
        Label messageLabel = new Label();
        messageLabel.setFont(FontLoader.loadPoppins(11));
        messageLabel.setTextFill(Color.RED);

        // Login Button
        Button loginButton = createStyledButton("Login");

        // Register and Forgot links
        HBox bottomLinks = createBottomLinks();

        // Add components to form
        formBox.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, loginButton, messageLabel, bottomLinks);

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

    private HBox createBottomLinks() {
        HBox bottomLinks = new HBox(20);
        bottomLinks.setAlignment(Pos.CENTER);

        Hyperlink registerLink = new Hyperlink("Sign up");
        Hyperlink forgotLink = new Hyperlink("Forgot Password?");

        // Style links with theme colors
        registerLink.setFont(FontLoader.loadPoppins(12));
        forgotLink.setFont(FontLoader.loadPoppins(12));

        String linkColor = Theme.isDarkMode ? "#ffffff" : "#000000";
        String linkHoverColor = Theme.isDarkMode ? "#cccccc" : "#333333";

        registerLink.setStyle("-fx-text-fill: " + linkColor + "; -fx-underline: false;");
        forgotLink.setStyle("-fx-text-fill: " + linkColor + "; -fx-underline: false;");

        // Hover effects
        registerLink.setOnMouseEntered(e -> registerLink.setStyle("-fx-text-fill: " + linkHoverColor + "; -fx-underline: true;"));
        registerLink.setOnMouseExited(e -> registerLink.setStyle("-fx-text-fill: " + linkColor + "; -fx-underline: false;"));

        forgotLink.setOnMouseEntered(e -> forgotLink.setStyle("-fx-text-fill: " + linkHoverColor + "; -fx-underline: true;"));
        forgotLink.setOnMouseExited(e -> forgotLink.setStyle("-fx-text-fill: " + linkColor + "; -fx-underline: false;"));

        bottomLinks.getChildren().addAll(registerLink, new Label("•"), forgotLink);

        // Actions
        registerLink.setOnAction(e -> switchToRegisterPage());
        forgotLink.setOnAction(e -> switchToForgotPasswordPage());

        return bottomLinks;
    }

    private HBox createDarkModeToggle() {
        HBox darkModeBox = new HBox(10);
        darkModeBox.setAlignment(Pos.CENTER);
        darkModeBox.setPadding(new Insets(20, 0, 0, 0));

        Label darkModeLabel = new Label("Dark Mode");
        darkModeLabel.setFont(FontLoader.loadPoppins(12));
        darkModeLabel.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);

        // Custom switch toggle
        ToggleButton switchToggle = createCustomSwitch();

        darkModeBox.getChildren().addAll(darkModeLabel, switchToggle);
        return darkModeBox;
    }

    private ToggleButton createCustomSwitch() {
        ToggleButton toggle = new ToggleButton();
        toggle.setSelected(Theme.isDarkMode);
        toggle.setPrefSize(50, 25);

        String switchStyle = Theme.isDarkMode ?
                "-fx-background-color: #50a0ff; -fx-background-radius: 15; -fx-border-radius: 15;" :
                "-fx-background-color: #cccccc; -fx-background-radius: 15; -fx-border-radius: 15;";

        toggle.setStyle(switchStyle);
        toggle.setText("");

        // Add circle indicator
        Circle indicator = new Circle(8);
        indicator.setFill(Color.WHITE);
        indicator.setTranslateX(Theme.isDarkMode ? 10 : -10);

        StackPane switchContainer = new StackPane();
        switchContainer.getChildren().addAll(toggle, indicator);

        // Animation for switch
        toggle.setOnAction(e -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), indicator);
            if (toggle.isSelected()) {
                transition.setToX(10);
                toggle.setStyle("-fx-background-color: #50a0ff; -fx-background-radius: 15; -fx-border-radius: 15;");
            } else {
                transition.setToX(-10);
                toggle.setStyle("-fx-background-color: #cccccc; -fx-background-radius: 15; -fx-border-radius: 15;");
            }
            transition.play();

            // Delay theme change until animation completes
            transition.setOnFinished(event -> toggleDarkMode());
        });

        // Replace the toggle button with the container in the parent
        return new ToggleButton() {{
            setPrefSize(50, 25);
            setStyle(switchStyle);
            setText("");
            setSelected(Theme.isDarkMode);
            setOnAction(e -> {
                TranslateTransition transition = new TranslateTransition(Duration.millis(200), indicator);
                if (isSelected()) {
                    transition.setToX(10);
                    setStyle("-fx-background-color: #50a0ff; -fx-background-radius: 15; -fx-border-radius: 15;");
                } else {
                    transition.setToX(-10);
                    setStyle("-fx-background-color: #cccccc; -fx-background-radius: 15; -fx-border-radius: 15;");
                }
                transition.play();
                transition.setOnFinished(event -> toggleDarkMode());
            });
        }};
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(FontLoader.loadPoppins(14));
        button.setPrefWidth(250);
        button.setPrefHeight(40);

        String btnStyle = Theme.isDarkMode ?
                "-fx-background-color: white; -fx-text-fill: black;" :
                "-fx-background-color: black; -fx-text-fill: white;";
        button.setStyle(btnStyle + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");

        // Hover animations
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();

            button.setStyle("-fx-background-color: transparent; -fx-border-color: " +
                    (Theme.isDarkMode ? "white" : "black") + "; -fx-border-width: 2; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;" +
                    (Theme.isDarkMode ? "-fx-text-fill: white;" : "-fx-text-fill: black;"));
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            button.setStyle(btnStyle + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        });

        return button;
    }

    private String getTextFieldStyle() {
        return Theme.isDarkMode ?
                "-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #444; -fx-border-radius: 5; -fx-background-radius: 5;" :
                "-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;";
    }

    private void addEntranceAnimation() {
        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Slide up animation
        TranslateTransition slideUp = new TranslateTransition(Duration.millis(600), root);
        slideUp.setFromY(50);
        slideUp.setToY(0);
        slideUp.play();
    }

    private void toggleDarkMode() {
        Theme.isDarkMode = !Theme.isDarkMode;

        // Fade out current scene
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            // Create new page with updated theme
            Stage currentStage = (Stage) root.getScene().getWindow();
            LoginPage newPage = new LoginPage(currentStage);
            Scene newScene = new Scene(newPage.getView(), 400, 600);
            currentStage.setScene(newScene);
        });
        fadeOut.play();
    }

    private void switchToRegisterPage() {
        // Fade out animation before switching
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            Stage currentStage = (Stage) root.getScene().getWindow();
            RegisterPage registerPage = new RegisterPage(currentStage);
            Scene scene = new Scene(registerPage.getView(), 400, 600);
            currentStage.setScene(scene);
        });
        fadeOut.play();
    }

    private void switchToForgotPasswordPage() {
        // Fade out animation before switching
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            Stage currentStage = (Stage) root.getScene().getWindow();
            ForgotPassword forgotPasswordPage = new ForgotPassword(currentStage);
            Scene scene = new Scene(forgotPasswordPage.getView(), 400, 600);
            currentStage.setScene(scene);
        });
        fadeOut.play();
    }

    private void loginAction(TextField emailField, PasswordField passwordField, Label messageLabel) {
        String email = emailField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        messageLabel.setText(""); // Clear previous message

        if (userPasswords.containsKey(email) && userPasswords.get(email).equals(password)) {
            // Success animation
            ScaleTransition successScale = new ScaleTransition(Duration.millis(200), messageLabel);
            successScale.setFromX(0);
            successScale.setFromY(0);
            successScale.setToX(1);
            successScale.setToY(1);

            messageLabel.setText("Login berhasil!");
            messageLabel.setTextFill(Color.GREEN);
            successScale.play();

            // Delay before switching to dashboard
            successScale.setOnFinished(e -> {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(event -> {
                    String role = userRoles.get(email);
                    if ("admin".equals(role)) {
                        new DashboardAdmin().start(stage);
                    } else {
                        new DashboardUser().show(stage);
                    }
                });
                fadeOut.play();
            });
        } else {
            // Error animation
            TranslateTransition shake = new TranslateTransition(Duration.millis(100), messageLabel);
            shake.setFromX(0);
            shake.setByX(10);
            shake.setCycleCount(4);
            shake.setAutoReverse(true);

            messageLabel.setText("Email atau password salah!");
            messageLabel.setTextFill(Color.RED);
            shake.play();
        }
    }
}