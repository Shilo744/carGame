import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * זוהי מחלקה מותאמת אישית שיורשת מ-javax.swing.ImageIcon.
 * המטרה שלה היא "לדרוס" את הבנאי שמקבל מחרוזת (String)
 * כדי שיטען משאבים מתוך ה-JAR במקום ממערכת הקבצים.
 */
public class ImageIcon extends javax.swing.ImageIcon {

    /**
     * הבנאי החדש והמשופר שלנו.
     * הוא מקבל נתיב למשאב וטוען אותו בצורה בטוחה.
     * @param resourcePath הנתיב למשאב, למשל "/images/car.png"
     */
    public ImageIcon(String resourcePath) {
        // קורא לבנאי של המחלקה המקורית, אבל עם URL ולא עם String
        super(getImageUrl(resourcePath));
    }

    // בנאים נוספים כדי לשמור על תאימות עם הקוד המקורי
    public ImageIcon() {
        super();
    }

    public ImageIcon(Image image) {
        super(image);
    }

    public ImageIcon(URL location) {
        super(location);
    }

    private static URL getImageUrl(String resourcePath) {
        // חשוב להשתמש ב-class כלשהו מהפרויקט כדי לקבל את הנתיב הנכון
        URL url = ImageIcon.class.getResource(resourcePath);
        if (url != null) {
            return url;
        } else {
            System.err.println("FATAL ERROR: Resource not found at path: " + resourcePath);
            return ImageIcon.class.getResource("/images/_empty.png");
        }
    }
}