import javax.sound.sampled.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class Utils {
    public static void sleep (int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void playMusic(String resourcePath) {
        try {
            InputStream audioSrc = Utils.class.getResourceAsStream(resourcePath);

            if (audioSrc == null) {
                System.err.println("ERROR: Sound resource not found: " + resourcePath);
                return;
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.addLineListener(evt -> {
                if (evt.getType() == LineEvent.Type.STOP) {
                    evt.getLine().close();
                }
            });

        } catch (Exception e) {
            System.err.println("ERROR playing sound: " + resourcePath);
            e.printStackTrace();
        }
    }
    public static boolean checkCollision (Rectangle rect1, Rectangle rect2) {
        boolean collision = false;
        if (rect1.intersects(rect2)) {
            collision = true;
        }
        return collision;
    }

}
