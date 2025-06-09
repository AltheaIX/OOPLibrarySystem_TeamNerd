package Utils;

import javafx.scene.text.Font;

public class FontLoader {
    public static Font loadPoppins(double size) {
        return Font.loadFont(FontLoader.class.getResourceAsStream("/Poppins-Regular.ttf"), size);
    }
}
