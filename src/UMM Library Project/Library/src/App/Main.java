package App;

import GUI.SplashScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public final static String BASE_URL = "https://b21f-180-248-29-175.ngrok-free.app";

    @Override
    public void start(Stage stage) {
        SplashScreen splash = new SplashScreen(stage);
        splash.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}