package GUI;

import Utils.FontLoader;
import Utils.Theme;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen {
    private final Stage stage;

    public SplashScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // Logo UMM
        try {
            String logoPath = Theme.isDarkMode ?
                    "/logonightmode.png" :
                    "/logolightmode.png";

            Image logoImage = new Image(getClass().getResourceAsStream(logoPath));
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitHeight(120);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);

            root.getChildren().add(logoView);
            StackPane.setAlignment(logoView, Pos.CENTER);
        } catch (Exception e) {
            // Fallback jika logo tidak ditemukan
            Text appName = new Text("UMM Library");
            appName.setFont(FontLoader.loadPoppins(24));
            appName.setFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);
            root.getChildren().add(appName);
            StackPane.setAlignment(appName, Pos.CENTER);
        }

        Label loadingText = new Label("Loading...");
        loadingText.setFont(FontLoader.loadPoppins(12));
        loadingText.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.GRAY);
        root.getChildren().add(loadingText);
        StackPane.setAlignment(loadingText, Pos.BOTTOM_CENTER);
        StackPane.setMargin(loadingText, new Insets(0, 0, 30, 0));

        root.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #121212;" :
                "-fx-background-color: white;");

        Scene splashScene = new Scene(root, 400, 600);
        stage.setScene(splashScene);
        stage.show();

        // Fade In Splash
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // After delay, fade out then show login
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {
                // Fade Out Splash
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), root);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(ev -> {
                    // Load Login Page
                    LoginPage loginPage = new LoginPage(stage);
                    Scene loginScene = new Scene(loginPage.getView(), 400, 600);
                    stage.setScene(loginScene);

                    // Fade In Login Page
                    FadeTransition loginFadeIn = new FadeTransition(Duration.seconds(0.5), loginScene.getRoot());
                    loginFadeIn.setFromValue(0.0);
                    loginFadeIn.setToValue(1.0);
                    loginFadeIn.play();
                });
                fadeOut.play();
            });
        }).start();
    }
}
