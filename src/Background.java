import javax.swing.*;
import java.awt.*;

public class Background extends JPanel {
    private boolean showTransparentRectangle=true;
    private ImageIcon icon=new ImageIcon("/images/menuRealistic.png");
    Background(int width,int height){
        icon=new ImageIcon(icon.getImage().getScaledInstance(width, height,Image.SCALE_SMOOTH));

        this.setSize(width,height);
        this.setLayout(null);
        this.setLocation(0,0);
    }
    public void transparentRectangleShowChange(){
        showTransparentRectangle=!showTransparentRectangle;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        icon.paintIcon(null,g,0,0);
        if(showTransparentRectangle){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
            g2d.setColor(Color.BLACK);
                g2d.fillRect(175, 325, 210, 320);}

    }
}
