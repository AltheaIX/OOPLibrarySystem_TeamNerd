package GUI;

import Utils.FontLoader;
import Utils.Theme;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

        Text appName = new Text("UMM Library");
        appName.setFont(FontLoader.loadPoppins(24));
        appName.setFill(Theme.isDarkMode ? Color.WHITE : Color.BLACK);

        Label loadingText = new Label("Loading...");
        loadingText.setFont(FontLoader.loadPoppins(12));
        loadingText.setTextFill(Theme.isDarkMode ? Color.LIGHTGRAY : Color.GRAY);

        root.getChildren().addAll(appName, loadingText);
        StackPane.setAlignment(appName, Pos.CENTER);
        StackPane.setAlignment(loadingText, Pos.BOTTOM_CENTER);
        StackPane.setMargin(loadingText, new javafx.geometry.Insets(0, 0, 30, 0));

        root.setStyle(Theme.isDarkMode ?
                "-fx-background-color: #121212;" :
                "-fx-background-color: white;");

        Scene splashScene = new Scene(root, 400, 600);
        stage.setScene(splashScene);
        stage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // After delay, go to login
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                LoginPage loginPage = new LoginPage(stage);
                Scene loginScene = new Scene(loginPage.getView(), 400, 600);
                stage.setScene(loginScene);
            });
        }).start();
    }
}