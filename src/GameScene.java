import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
public class GameScene extends JPanel{
    public static boolean auto=false;
    private Car player;
    private ImageIcon road = new ImageIcon("/images/road1.png");
    private int currection=10;
    private int firstRoadsX=0;
    private int secondRoadsX;
    private LinkedList <Car> cars;
    private int moveCars=0;
    private float speed=4;
    private float addSpeed= 0.1F;
    private int maxSpeed=200;
    private int score=0;
    private boolean gameOver=false;
    private JLabel labelScore=new JLabel("Your score is "+score);
    private Coin[]coins= {new Coin(Window.WINDOW_WIDTH+100,68),
            new Coin(Window.WINDOW_WIDTH,68+Window.ROAD_LENGTH),
            new Coin(Window.WINDOW_WIDTH+50,68+2*Window.ROAD_LENGTH)};
    public GameScene(){
        makeGame();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void makeGame(){
        this.setBackground(new Color(70,70,70));
        this.player = new Car(0,Window.MIDDLE_ROAD,"/images/car-1.png");
        labelScore.setFont(new Font("Arial", Font.BOLD, 15));
        cars=new LinkedList<>();
        secondRoadsX=-road.getIconWidth();
        Car car1=new Car(800,Window.UP_ROAD,randomCar());
        Car car2=new Car(950,Window.MIDDLE_ROAD,randomCar());
        labelScore.setForeground(Color.white);
        this.add(labelScore);
        car2.setSpeed(8);
        cars.push(car1);
        cars.push(car2);
        new Thread(()->{
            gameOver=false;
            while (!gameOver){
                moving();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sortRoads();
                repaint();
            }
        }).start();
        new Thread(()->{
            while (!gameOver){
                Utils.playMusic("/sounds/engine.wav");
                try {
                    Thread.sleep(485);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    public String randomCar(){
        int r= (int) (Math.random()*7);
        return "/images/OCAR"+r+".png";
    }
    public void paintComponent (Graphics graphics){
        super.paintComponent(graphics);
        sortRoads();
        road.paintIcon(null,graphics,firstRoadsX,-currection);
        road.paintIcon(null,graphics,firstRoadsX,road.getIconHeight()-currection);
        road.paintIcon(null,graphics,firstRoadsX,2*road.getIconHeight()-currection);
        road.paintIcon(null,graphics,secondRoadsX,-currection);
        road.paintIcon(null,graphics,secondRoadsX,road.getIconHeight()-currection);
        road.paintIcon(null,graphics,secondRoadsX,2*road.getIconHeight()-currection);
        for (int i = 0; i < coins.length; i++) {
            coins[i].paint(graphics);
        }
        for (Car car:cars) {
            car.paint(graphics);
        }
        this.player.paint(graphics);

    }
    public boolean collision(Rectangle player){
        return (Utils.checkCollision(player, cars.get(0).getRectangle()) ||
                Utils.checkCollision(player, cars.get(1).getRectangle()));
    }
    private void getCoin(Coin coin){
        if(player.getRectangle().intersects(coin.getRectangle())){
            Utils.playMusic("/sounds/coinTake.wav");
            score++;
            labelScore.setText("Your score is "+score);
            coin.resetCoin();
        }
    }
    public void moving() {
        if (!collision(this.player.getRectangle())){
            for (int i = 0; i < coins.length; i++) {
                getCoin(coins[i]);
                coins[i].move(speed);
            }
            if (firstRoadsX <= -road.getIconWidth()) {
                firstRoadsX = road.getIconWidth();
            }
            if (secondRoadsX <= -road.getIconWidth()) {
                secondRoadsX = road.getIconWidth();
            }
            if (moveCars == 0) {
                for (Car car : cars) {
                    car.moveLeft(car.getSpeed());
                    if (car.isStart()) {
                        String randomImage=randomCar();
                        car.setOpponent(new ImageIcon(randomImage));
                        car.resetLocation();
                        if(speed<=maxSpeed ){
                            car.setSpeed((int) (speed + Math.random()* (speed)));
                            speed+=addSpeed;}
                        score++;
                        labelScore.setText("Your score is "+score);
                    }
                }
                while (cars.get(0).getY()==cars.get(1).getY()){
                    if(cars.get(0).getX()>cars.get(1).getX()){
                        cars.get(0).resetLocation();
                    }else {
                        cars.get(1).resetLocation();
                    }
                }
                moveCars = 4;
            }
            moveCars--;
            firstRoadsX-=speed;
            secondRoadsX-=speed;
            if(auto){

                for (int j = 0; j < coins.length; j++) {
                    if(coins[j].getRectangle().intersects(player.getUpRectangle()) && !collision(player.getUpRectangle())){
                        player.moveUp(Window.ROAD_LENGTH);
                        getCoin(coins[j]);
                        player.moveDown(Window.ROAD_LENGTH);
                    }
                    else if(coins[j].getRectangle().intersects(player.getDownRectangle()) && !collision(player.getDownRectangle())){
                        player.moveDown(Window.ROAD_LENGTH);
                        getCoin(coins[j]);
                        player.moveUp(Window.ROAD_LENGTH);
                    }
                    if(coins[j].getRectangle().intersects(player.getTopRectangle() ) && !collision(player.getUpRectangle()) && !collision(player.getTopRectangle())){
                        player.moveUp(Window.ROAD_LENGTH);
                        player.moveUp(Window.ROAD_LENGTH);
                        getCoin(coins[j]);
                        player.moveDown(Window.ROAD_LENGTH);
                        player.moveDown(Window.ROAD_LENGTH);
                    }
                    else if(coins[j].getRectangle().intersects(player.getLowestRectangle()) && !collision(player.getDownRectangle()) && !collision(player.getLowestRectangle())){
                        player.moveDown(Window.ROAD_LENGTH);
                        player.moveDown(Window.ROAD_LENGTH);
                        getCoin(coins[j]);
                        player.moveUp(Window.ROAD_LENGTH);
                        player.moveUp(Window.ROAD_LENGTH);
                    }
                }
                player.autoPlay(cars.get(0),cars.get(1));

            }


        }else {
            gameOver=true;
            Utils.playMusic("/sounds/boom.wav");
            player.setImage(new ImageIcon("/images/car2.png"));
            save();
        }
    }
    public void save(){
        int i=StartWindow.scores.length-1;
        String name="";

        if(Integer.valueOf(StartWindow.scores[i])<score){
            name=putName();
        }
        while (i>=0 && Integer.valueOf(StartWindow.scores[i])<score){
            try {
                String loserName=StartWindow.bestPlayers[i];
                StartWindow.bestPlayers[i+1]=loserName;
                StartWindow.bestPlayers[i]=name;

                StartWindow.scores[i+1]=StartWindow.scores[i];
                StartWindow.scores[i]=score+"";
            }catch (Exception ignore){}
            i--;
        }
        StartWindow.writeToBinaryFile(StartWindow.bestPlayers, "names.txt");
        StartWindow.writeToBinaryFile(StartWindow.scores, "score.txt");
    }
    public String putName(){
        // Show a pop-up message with a text box
        String response = JOptionPane.showInputDialog(null, "You have enter the leaderboard! \n" +
                " Please enter your name");

        // Show a pop-up message with the entered text
        JOptionPane.showMessageDialog(null, "You name: " + response+" has been saved!");
        return response;
    }
    public void sortRoads(){
        if(firstRoadsX<secondRoadsX){
            secondRoadsX=firstRoadsX+road.getIconWidth();
        }else {
            firstRoadsX=secondRoadsX+road.getIconWidth();
        }
    }
    public Car getPlayer() {
        return player;
    }
}
