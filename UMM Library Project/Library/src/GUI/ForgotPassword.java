package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Utils.FontLoader;

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

        Label instruction = new Label("Enter your registered email to reset password");
        instruction.setFont(FontLoader.loadPoppins(12));

        // Email field
        Label emailLabel = new Label("EMAIL");
        emailLabel.setFont(FontLoader.loadPoppins(12));
        TextField emailField = new TextField();
        emailField.setPromptText("hello@example.com");

        // Reset button
        Button resetButton = new Button("Send Reset Link");
        resetButton.setFont(FontLoader.loadPoppins(14));
        resetButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10 20;");
        resetButton.setMaxWidth(Double.MAX_VALUE);

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

        root = new VBox(formBox);
        root.setAlignment(Pos.CENTER);

        // Event: back
        backToLogin.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage.getView(), 400, 600));
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

