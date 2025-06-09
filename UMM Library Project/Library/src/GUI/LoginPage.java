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

public class LoginPage {
    private VBox root;
    private Stage stage;

    public LoginPage(Stage stage) {
        this.stage = stage;
        createUI();
    }

    public VBox getView() {
        return root;
    }

    private void createUI() {
        // Title
        Text title = new Text("Login");
        title.setFont(FontLoader.loadPoppins(28));

        // Email
        Label emailLabel = new Label("EMAIL");
        emailLabel.setFont(FontLoader.loadPoppins(12));
        TextField emailField = new TextField();
        emailField.setPromptText("hello@example.com");

        // Password
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setFont(FontLoader.loadPoppins(12));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("*******");

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setFont(FontLoader.loadPoppins(14));
        loginButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10 20;");

        // Register and Forgot
        HBox bottomText = new HBox(10);
        Hyperlink registerLink = new Hyperlink("Sign up");
        Hyperlink forgotLink = new Hyperlink("Forgot Password?");
        registerLink.setFont(FontLoader.loadPoppins(11));
        forgotLink.setFont(FontLoader.loadPoppins(11));
        bottomText.setAlignment(Pos.CENTER);
        bottomText.getChildren().addAll(registerLink, forgotLink);

        // Container
        VBox formBox = new VBox(10, title,
                emailLabel, emailField,
                passwordLabel, passwordField,
                loginButton,
                bottomText);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(40));
        formBox.setMaxWidth(300);

        // Root
        root = new VBox(formBox);
        root.setAlignment(Pos.CENTER);

        // Event: navigate to RegisterPage
        registerLink.setOnAction(e -> {
            RegisterPage registerPage = new RegisterPage(stage);
            stage.setScene(new Scene(registerPage.getView(), 400, 600));
        });

        // Event: navigate to ForgotPasswordPage
        forgotLink.setOnAction(e -> {
            ForgotPassword forgotPasswordPage = new ForgotPassword(stage);
            stage.setScene(new Scene(forgotPasswordPage.getView(), 400, 600));
        });
    }
}
