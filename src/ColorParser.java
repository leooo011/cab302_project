import java.awt.*;

public class ColorParser {
    public static String rgbTohex(Color color) {
        String hex = "#" + Integer.toHexString(color.getRGB()).substring(2);
        return hex.toUpperCase();
    }
    public static Color hexToRgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }

}
