import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;

public class StartWindow extends JFrame {

    private boolean show = true;
    public static String[] bestPlayers;
    public static String[] scores;
    private JLabel[] scoresLabel = new JLabel[10];

    StartWindow() {
        // קוראים את נתוני השמירה בתחילת הבנאי
        if (scores == null) {
            scores = readBinaryFile("score.txt");
        }
        if (bestPlayers == null) {
            bestPlayers = readBinaryFile("names.txt");
        }

        // מאתחלים ערכי ברירת מחדל כדי למנוע קריסות
        for (int i = 0; i < 10; i++) {
            if (scores[i] == null) scores[i] = "0";
            if (bestPlayers[i] == null) bestPlayers[i] = "Player";
        }

        // בניית ממשק המשתמש עם הנתונים שנקראו
        int x = 210;
        int y = 320;
        for (int i = 0; i < scoresLabel.length; i++) {
            scoresLabel[i] = new JLabel(i + 1 + ": " + bestPlayers[i] + " " + scores[i]);
            scoresLabel[i].setBounds(x, y, 300, 50);
            scoresLabel[i].setFont(new Font("Serif", Font.BOLD, 28));
            scoresLabel[i].setForeground(Color.WHITE);
            this.add(scoresLabel[i]);
            y += 30;
        }
        scoresLabel[0].setForeground(new Color(185, 242, 255));
        scoresLabel[1].setForeground(new Color(255, 215, 0));
        scoresLabel[2].setForeground(new Color(205, 127, 50));

        this.setResizable(false);
        this.setSize(550, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        new Thread(() -> {
            while (true) {
                Utils.playMusic("/sounds/menuSound.wav");
                try {
                    Thread.sleep(89629);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Background background = new Background(this.getWidth(), this.getHeight());
        int buttonWidth = 100;
        int buttonHeight = 50;
        JButton start = new JButton("start");
        JButton rules = new JButton("Rules");
        JButton aiButton = new JButton("AI");

        start.setFont(new Font("Arial", Font.BOLD, 20));
        start.setForeground(Color.WHITE);
        start.setBackground(new Color(200, 35, 40));
        start.setFocusPainted(false);
        start.setBorder(BorderFactory.createRaisedSoftBevelBorder());

        aiButton.setFont(new Font("Arial", Font.BOLD, 20));
        aiButton.setForeground(Color.WHITE);
        aiButton.setBackground(new Color(76, 58, 110));
        aiButton.setFocusPainted(false);
        aiButton.setBorder(BorderFactory.createRaisedSoftBevelBorder());

        rules.setFont(new Font("Arial", Font.BOLD, 20));
        rules.setForeground(Color.WHITE);
        rules.setBackground(new Color(60, 40, 210));
        rules.setFocusPainted(false);
        rules.setBorder(BorderFactory.createRaisedSoftBevelBorder());

        start.setSize(buttonWidth, buttonHeight);
        start.setLocation(this.getWidth() / 2 - start.getWidth() / 2, this.getHeight() / 2 - 2 * buttonHeight);
        rules.setBounds(5, 7, buttonWidth, buttonHeight);
        aiButton.setBounds(5, 70, buttonWidth, buttonHeight);
        JLabel label = new JLabel("<html>Rules:\n<html>" +
                "Use the directional arrows to move the car to the right or left.\n" +
                "The player must avoid the moving cars on the road without colliding with them.\n" +
                "If the player collides with a car, the game ends.\n" +
                "The player must collect as many coins as possible.\n" +
                "The game continues as long as the player avoids the cars and travels the road.\n" +
                "If the player travels a certain distance without colliding with the cars, the cars move faster and more frequently on the road.\n" +
                "There will also be different types of vehicles on the road that the player must avoid.\n" +
                "The final score is calculated based on the number of coins collected by the player without colliding with the cars on the road.");
        label.setForeground(Color.white);
        label.setBounds(75, 170, 400, 250);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setVisible(false);
        this.add(label);

        start.addActionListener((e) -> startGame());

        rules.addActionListener((e) -> {
            start.setVisible(!show);
            aiButton.setVisible(!show);
            label.setVisible(show);
            for (int i = 0; i < scoresLabel.length; i++) {
                scoresLabel[i].setVisible(!show);
            }
            show = !show;
            background.transparentRectangleShowChange();
        });

        aiButton.addActionListener((e) -> {
            GameScene.auto = !GameScene.auto;
            startGame();
        });

        this.add(start);
        this.add(rules);
        this.add(aiButton);
        this.add(background);
        this.setVisible(true);
    }

    public void startGame() {
        Window window = new Window();
        this.dispose();
    }

    public static void writeToBinaryFile(String[] playerSaves, String fileName) {
        String fullPath = getPathInJarDirectory(fileName);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fullPath))) {
            objectOutputStream.writeObject(playerSaves);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] readBinaryFile(String fileName) {
        String fullPath = getPathInJarDirectory(fileName);
        File file = new File(fullPath);

        if (!file.exists()) {
            return new String[10];
        }

        String[] strings = new String[10];
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fullPath))) {
            strings = (String[]) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new String[10];
        }
        return strings;
    }

    private static String getPathInJarDirectory(String fileName) {
        try {
            File jarFile = new File(StartWindow.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String jarDir = jarFile.getParent();
            if (jarDir == null) { // Fallback for running from IDE
                return fileName;
            }
            return jarDir + File.separator + fileName;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return fileName;
        }
    }
}