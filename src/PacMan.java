import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U';// U D L R
        int velocityX = 0;
        int velocityY = 0;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction){
            char prevdirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for(Block wall:walls){
                if(collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevdirection; // revert to previous direction if collision occurs
                    updateVelocity();
                }
            }
        }
        void updateVelocity(){
            if(this.direction =='U'){
                this.velocityX = 0;
                this.velocityY = -tileSize/4;
            }else if(this.direction =='D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }else if(this.direction =='L'){
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }else if(this.direction =='R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }
        void reset(){
            this.x = startX;
            this.y = startY;
        }
    }

    private int columCount = 19;
    private int rowCount = 21;
    private int tileSize = 32;
    private int boardWidth = columCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image pinkGhostImage;
    private Image orangeGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanRightImage;
    private Image pacmanLeftImage;

    HashSet<Block> walls;
    HashSet<Block> ghosts;
    HashSet<Block> foods;
    Block pacman;

    Timer gameloop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;
    boolean gameStarted = false;

    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "X       bpo       X",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    public PacMan() {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("/blueGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("/pinkGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("/orangeGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage();

        loadMap();
        for(Block ghost:ghosts){
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        gameloop = new Timer(50, this);//20fps (1000/50)
        // gameloop.start();
    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for(int r =0;r<rowCount;r++){
            for(int c =0;c<columCount;c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if(tileMapChar == 'X'){//wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }else if(tileMapChar == 'b'){//blue ghost
                    Block blueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(blueGhost);
                }else if(tileMapChar == 'p'){//pink ghost
                    Block pinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(pinkGhost);
                }else if(tileMapChar == 'o'){//orange ghost
                    Block orangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(orangeGhost);
                }else if(tileMapChar == 'r'){//red ghost
                    Block redGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(redGhost);
                }else if(tileMapChar == ' '){//food
                    Block foodBlock = new Block(null, x+14, y+14, 4,4);
                    foods.add(foodBlock);
                }else if(tileMapChar == 'P'){//pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(!gameStarted){
            g.drawString("Press any key to START game", tileSize/2, tileSize/2);
        }
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        for(Block ghost:ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        for(Block wall:walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        g.setColor(Color.white);
        for(Block foodBlock:foods){
            g.fillRect(foodBlock.x, foodBlock.y, foodBlock.width, foodBlock.height);
        }
        //score 
        g.setFont(new Font("Arial",Font.BOLD,18));
        g.setColor(Color.white);
        if(gameOver){
            g.drawString("Score: " + String.valueOf(score),tileSize/2,tileSize/2);
        }else{
            g.drawString("X"+String.valueOf(lives)+" Score: "+String.valueOf(score), tileSize/2, tileSize/2);
        }
    }

    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        //check wall condition
        for(Block wall:walls){
            if(collision(pacman, wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
        Block foodEaten = null;
        for(Block food:foods){
            if(collision(pacman, food)){
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        for(Block ghost:ghosts){
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            //check wall condition for ghosts
            for(Block wall:walls){
                if(ghost.y==tileSize*9 && ghost.direction!='D' && ghost.direction!='U'){
                    ghost.updateDirection('U');
                }
                if(collision(ghost, wall)){
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }

            //check collision with pacman
            if(collision(pacman, ghost)){
                lives -= 1;
                resetPositions();   
                if(lives ==0) {
                    gameOver = true;
                    return;
                }
            }
        }
    }

    public boolean collision(Block a, Block b){
        return a.x < b.x + b.width && 
               a.x + a.width > b.x && 
               a.y < b.y + b.height && 
               a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX =0;
        pacman.velocityY =0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameStarted){
            move();
        }
        repaint();
        if(gameOver){
            gameStarted = false;
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        if (!gameStarted && !gameOver) {
            gameStarted = true;
            gameloop.start();
        }

        if(gameOver){
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameloop.start();
        }

        // System.out.println("Key Released: " + e.getKeyCode());
        if(e.getKeyCode()==KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
        }else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
        }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
        }

        if(pacman.direction =='U'){
            pacman.image = pacmanUpImage;
        }else if(pacman.direction =='D'){
            pacman.image = pacmanDownImage;
        }else if(pacman.direction =='L'){
            pacman.image = pacmanLeftImage;
        }else if(pacman.direction =='R'){
            pacman.image = pacmanRightImage;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
