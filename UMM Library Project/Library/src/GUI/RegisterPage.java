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

public class RegisterPage {
    private VBox root;
    private Stage stage;

    public RegisterPage(Stage stage) {
        this.stage = stage;
        createUI();
    }

    public VBox getView() {
        return root;
    }

    private void createUI() {
        // Title
        Text title = new Text("Create new Account");
        title.setFont(FontLoader.loadPoppins(24));
        title.setFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);


        Hyperlink loginLink = new Hyperlink("Already Registered? Login");
        loginLink.setFont(FontLoader.loadPoppins(12));

        // Name
        Label nameLabel = new Label("NAME");
        nameLabel.setFont(FontLoader.loadPoppins(12));
        nameLabel.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.BLACK);
        TextField nameField = new TextField();
        nameField.setPromptText("Your name");
        nameField.setStyle(Theme.isDarkMode ? "-fx-background-color: #2a2a2a; -fx-text-fill: white;" : "");


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
        passwordField.setStyle(Theme.isDarkMode ? "-fx-background-color: #2a2a2a; -fx-text-fill: #ffffff;" : "");

        // Date of Birth
        Label dobLabel = new Label("DATE OF BIRTH");
        dobLabel.setFont(FontLoader.loadPoppins(12));
        dobLabel.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.BLACK);
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle(Theme.isDarkMode ? "-fx-control-inner-background: #2a2a2a; -fx-text-fill: #ffffff;" : "");


        // Sign Up Button
        Button signupButton = new Button("sign up");
        signupButton.setFont(FontLoader.loadPoppins(14));
        String btnBase = Theme.isDarkMode ?
                "-fx-background-color: white; -fx-text-fill: black;" :
                "-fx-background-color: black; -fx-text-fill: white;";
        signupButton.setStyle(btnBase + "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;");

        // Hover style
        signupButton.setOnMouseEntered(e -> signupButton.setStyle(
                "-fx-background-color: transparent; -fx-border-color: gray; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;" +
                        (Theme.isDarkMode ? "-fx-text-fill: white;" : "-fx-text-fill: black;")));

        signupButton.setOnMouseExited(e -> signupButton.setStyle(btnBase + "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;"));

        // Form Layout
        VBox formBox = new VBox(10,
                title, loginLink,
                nameLabel, nameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                dobLabel, datePicker,
                signupButton
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

        // Event: back to login
        loginLink.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage.getView(), 400, 600));
        });
        loginLink.setOnAction(e -> {
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

        // Event: submit (nanti bisa dipakai simpan user)
        signupButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String dob = datePicker.getValue() != null ? datePicker.getValue().toString() : "";

            // Validasi simple
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty()) {
                showAlert("All fields are required!");
            } else {
                showAlert("Account created successfully!");
                // TODO: simpan user ke service
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}