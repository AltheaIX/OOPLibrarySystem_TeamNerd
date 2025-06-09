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

        Hyperlink loginLink = new Hyperlink("Already Registered? Login");
        loginLink.setFont(FontLoader.loadPoppins(12));

        // Name
        Label nameLabel = new Label("NAME");
        nameLabel.setFont(FontLoader.loadPoppins(12));
        TextField nameField = new TextField();
        nameField.setPromptText("Your name");

        // Email
        Label emailLabel = new Label("EMAIL");
        emailLabel.setFont(FontLoader.loadPoppins(12));
        TextField emailField = new TextField();
        emailField.setPromptText("hello@reallygreatsite.com");

        // Password
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setFont(FontLoader.loadPoppins(12));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("*******");

        // Date of Birth
        Label dobLabel = new Label("DATE OF BIRTH");
        dobLabel.setFont(FontLoader.loadPoppins(12));
        DatePicker datePicker = new DatePicker();

        // Sign Up Button
        Button signupButton = new Button("sign up");
        signupButton.setFont(FontLoader.loadPoppins(14));
        signupButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10 20;");
        signupButton.setMaxWidth(Double.MAX_VALUE);

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

        root = new VBox(formBox);
        root.setAlignment(Pos.CENTER);

        // Event: back to login
        loginLink.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage.getView(), 400, 600));
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

