package GUI;

import Utils.Theme;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Utils.FontLoader;
import javafx.util.Duration;

public class ForgotPassword {
    private VBox root;
    private Stage stage;

    public ForgotPassword(Stage stage) {
        this.stage = stage;
        createUI();
    }

    public VBox getView() {
        return root;
    }

    private void createUI() {
        // Title
        Text title = new Text("Forgot Password");
        title.setFont(FontLoader.loadPoppins(24));
        title.setFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);

        Label instruction = new Label("Enter your registered email to reset password");
        instruction.setFont(FontLoader.loadPoppins(12));
        instruction.setTextFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);

        // Email field
        Label emailLabel = new Label("EMAIL");
        emailLabel.setFont(FontLoader.loadPoppins(12));
        emailLabel.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.BLACK);
        TextField emailField = new TextField();
        emailField.setPromptText("hello@example.com");
        emailField.setStyle(Theme.isDarkMode ? "-fx-background-color: #2a2a2a; -fx-text-fill: white;" : "");

        // Reset button
        Button resetButton = new Button("Send Reset Link");
        resetButton.setFont(FontLoader.loadPoppins(14));
        String btnBase = Theme.isDarkMode ?
                "-fx-background-color: white; -fx-text-fill: black;" :
                "-fx-background-color: black; -fx-text-fill: white;";
        resetButton.setStyle(btnBase + "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;");

        // Hover style
        resetButton.setOnMouseEntered(e -> resetButton.setStyle(
                "-fx-background-color: transparent; -fx-border-color: gray; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;" +
                        (Theme.isDarkMode ? "-fx-text-fill: white;" : "-fx-text-fill: black;")));

        resetButton.setOnMouseExited(e -> resetButton.setStyle(btnBase + "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;"));

        // Back to login
        Hyperlink backToLogin = new Hyperlink("Back to Login");
        backToLogin.setFont(FontLoader.loadPoppins(11));

        // Layout
        VBox formBox = new VBox(10,
                title,
                instruction,
                emailLabel,
                emailField,
                resetButton,
                backToLogin
        );
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(40));
        formBox.setMaxWidth(300);

        // Root
        root = new VBox(formBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #121212; -fx-text-fill: white;" :
                "-fx-background-color: white; -fx-text-fill: black;");

        // Event: back
        backToLogin.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> {
                LoginPage loginpage = new LoginPage(stage);
                Scene registerScene = new Scene(loginpage.getView(), 400, 600);
                stage.setScene(registerScene);
            });
            fadeOut.play();
        });

        // Event: send reset (dummy)
        resetButton.setOnAction(e -> {
            String email = emailField.getText();
            if (email.isEmpty()) {
                showAlert("Please enter your email.");
            } else {
                // TODO: Kirim reset link jika pakai database/SMTP
                showAlert("A reset link has been sent to: " + email);
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}