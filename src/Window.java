import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements KeyListener {
    public static final int WINDOW_WIDTH =1000;
    public static final int WINDOW_HEIGHT =600;
    public static final int ROAD_LENGTH =195;
    public static final int MIDDLE_ROAD=263;

    public static final int UP_ROAD=MIDDLE_ROAD-ROAD_LENGTH;
    public static final int DOWN_ROAD=MIDDLE_ROAD+ROAD_LENGTH;

    private GameScene gameScene = new GameScene();
    private final int PLAYER_SPEED =60;
    public static int highestScore=0;

    public Window (){
        this.setResizable(false);
        this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.addKeyListener(this);
        this.add(gameScene);
        this.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        switch (e.getKeyCode()){
            case 37->gameScene.getPlayer().moveLeft(PLAYER_SPEED);
            case 38->gameScene.getPlayer().moveUp(ROAD_LENGTH);
            case 39->gameScene.getPlayer().moveRight(PLAYER_SPEED);
            case 40->gameScene.getPlayer().moveDown(ROAD_LENGTH);
        }
        if(gameScene.isGameOver()){
            GameScene gameScene1=new GameScene();
            gameScene.setVisible(false);
            gameScene=gameScene1;
            gameScene.setVisible(true);
            this.add(gameScene);
        }
        repaint();
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
