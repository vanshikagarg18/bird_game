import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class bird extends JPanel implements ActionListener,KeyListener{
    int frameWidth = 360;
    int frameHeight = 640;



    Image backImage;
    Image birdImage;
    Image upperPipeImage;
    Image lowerPipeImage;



    int birdX = frameWidth/8;
    int birdY = frameHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;



    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }


    int pipeX = frameWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;


    class pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        pipe(Image img){
            this.img = img;
        }
    }

    Bird bird;
    Timer gameloop;
    Timer placePipesTimer;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<pipe> pipes;
    Random random = new Random();


    boolean isGameOver = false;
    double score = 0;

    bird(){
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);     

                 
        backImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();


        bird = new Bird(birdImage);
        pipes = new ArrayList<pipe>();

        placePipesTimer = new Timer(2000,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        placePipesTimer.start();


        // this -> refers to bird class
        gameloop = new Timer(1000/60,this);
        gameloop.start();
    }

    public void placePipes(){
        int randomPipeY = (int)(pipeY-pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openSpace = frameHeight/4;

        pipe toppipe = new pipe(upperPipeImage);
        toppipe.y = randomPipeY;
        pipes.add(toppipe);

        pipe bottompipe = new pipe(lowerPipeImage);
        bottompipe.y = toppipe.y + pipeHeight + openSpace;
        pipes.add(bottompipe);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // Your code here
        g.drawImage(backImage, 0, 0, frameWidth, frameHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);


        for (int i = 0; i < pipes.size(); i++) {
            pipe p = pipes.get(i);
            g.drawImage(p.img, p.x, p.y, p.width, p.height, null);
        }



        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        if(isGameOver){
            g.drawString("Game Over"+ String.valueOf((int) score),10,35);
        }else{
            g.drawString(String.valueOf((int) score),10,35);
        }
    }

    public void move(){
        velocityY += gravity;
        bird.y += velocityY;
        birdY = Math.max(bird.y,0);

        for (int i = 0; i < pipes.size(); i++) {
            pipe p = pipes.get(i);
            p.x += velocityX;

            if(!p.passed && bird.x > p.x + p.width){
                p.passed = true;
                score += 0.5;
            } 


            if(collision(bird, p)){
                isGameOver = true;
            }
        }

        if(bird.y >= frameHeight){
            isGameOver = true;
        }

        if (bird.y <= 0) {
            isGameOver = true;
        }

        

    }

    boolean collision(Bird a, pipe b) {
        return a.x < b.x + b.width && 
               a.x + a.width > b.x && 
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(isGameOver){
            gameloop.stop();
            placePipesTimer.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if(isGameOver){
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                isGameOver = false;
                gameloop.start();
                placePipesTimer.start();
            }
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
