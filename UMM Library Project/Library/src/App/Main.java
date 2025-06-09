package App;

import GUI.SplashScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SplashScreen splash = new SplashScreen(stage);
        splash.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}