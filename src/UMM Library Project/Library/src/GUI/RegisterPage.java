package GUI;

import Utils.Theme;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Utils.FontLoader;
import javafx.util.Duration;
import dto.CreateUserRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static App.Main.BASE_URL;

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

    private boolean register(CreateUserRequest newUser) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            String requestBody = mapper.writeValueAsString(newUser);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(requestBody);
            System.out.println(response.statusCode());

            if(response.statusCode() == 200) {
                showAlert("User created successfully!");
                return true;
            }

            if(response.statusCode() == 400) {
                showAlert("Email already exists!");
                return false;
            }

            showAlert("User created failed!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createUI() {
        // Title
        Text title = new Text("Create new Account");
        title.setFont(FontLoader.loadPoppins(24));
        title.setFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);

        // Back to Login
        Hyperlink loginLink = new Hyperlink("Already Registered? Login");
        loginLink.setFont(FontLoader.loadPoppins(12));
        loginLink.setStyle("-fx-text-fill: " + (Theme.isDarkMode ? "white;" : "black;"));

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

        // Major
        Label majorLabel = new Label("MAJOR");
        majorLabel.setFont(FontLoader.loadPoppins(12));
        majorLabel.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.BLACK);
        TextField majorField = new TextField();
        majorField.setPromptText("Your major");
        majorField.setStyle(Theme.isDarkMode ? "-fx-background-color: #2a2a2a; -fx-text-fill: white;" : "");


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
                majorLabel, majorField,
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

        // Event: submit
        signupButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String major = majorField.getText();

            // Validasi simple
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || major.isEmpty()) {
                showAlert("All fields are required!");
            } else {
                CreateUserRequest request = new CreateUserRequest(name, email, password, major);
                register(request);
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