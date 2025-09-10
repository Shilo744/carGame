import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Coin {
    private int x;
    private int y;
    private static ImageIcon coin=new ImageIcon("/images/coin0.png");

    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
        resetCoin();
    }
    public void move(double speed){
        this.x-=speed;
        if(x<=-coin.getIconWidth()){
        resetCoin();
        }
    }
    public void resetCoin(){
        int start=67;
                 Random random=new Random();
                 x=Window.WINDOW_WIDTH;
            int choose=random.nextInt(1,4);
            if(choose==1){
                y=start;
            }
            if(choose==2){
                y=start+Window.ROAD_LENGTH;
            }
            if(choose==3){
                y=start+2*Window.ROAD_LENGTH;
            }
    }
    public Rectangle getRectangle(){
        return new Rectangle(x,y,coin.getIconWidth(),coin.getIconHeight());
    }
    public void paint(Graphics graphics){
        coin.paintIcon(null,graphics,x,y);
    }
}
