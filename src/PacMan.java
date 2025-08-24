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

    class Block {

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

        void updateDirection(char direction) {
            char prevdirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevdirection; // revert to previous direction if collision occurs
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize / 4;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize / 4;
            } else if (this.direction == 'L') {
                this.velocityX = -tileSize / 4;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = tileSize / 4;
                this.velocityY = 0;
            }
        }

        void reset() {
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

    private Image scaredGhostImage;

    private Image cherryImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanRightImage;
    private Image pacmanLeftImage;

    HashSet<Block> walls;
    HashSet<Block> ghosts;
    HashSet<Block> foods;
    HashSet<Block> cherries;
    HashSet<Block> powerUps;
    Block pacman;

    Timer gameloop;
    Timer cherryTimer;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;
    boolean gameStarted = false;
    boolean gamePaused = false;

    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "XA        X        X",
        "X XX XXX XAXXX XX X",
        "X  X           X  X",
        "XX X XX XXXXX XX X X",
        "X    X   X   X    X",
        "X XXXX X X X XXXX X",
        "X      X   X  A   X",
        "XXXX XXX   XXX XXXX",
        "   X  X  r  X  X   ",
        "XXXX  X bpo X  XXXX",
        "   X  X     X  X   ",
        "XXXX XXX A XXX XXXX",
        "X      X   X     AX",
        "X XXXX X X X XXXX X",
        "X    X   P   X    X",
        "XX X XX XXXXX XX X X",
        "X  X           X  X",
        "X XX XXX X XXX XX X",
        "XA       X        X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    public PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("/wall.png")).getImage();

        scaredGhostImage = new ImageIcon(getClass().getResource("/scaredGhost.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("/blueGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("/pinkGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("/orangeGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage();

        powerFoodImage = new ImageIcon(getClass().getResource("/powerFood.png")).getImage();

        cherryImage = new ImageIcon(getClass().getResource("/cherry.png")).getImage();

        loadMap();
        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }

        gameloop = new Timer(50, this);//20fps (1000/50)

        cherryTimer = new Timer(10000 + random.nextInt(5000), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameStarted && !gamePaused && !gameOver) {
                    spawnCherry();
                }
            }
        });
        // gameloop.start();
        
    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        cherries = new HashSet<Block>();
        powerUps = new HashSet<Block>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') {//wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'b') {//blue ghost
                    Block blueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(blueGhost);
                } else if (tileMapChar == 'p') {//pink ghost
                    Block pinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(pinkGhost);
                } else if (tileMapChar == 'o') {//orange ghost
                    Block orangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(orangeGhost);
                } else if (tileMapChar == 'r') {//red ghost
                    Block redGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(redGhost);
                } else if (tileMapChar == ' ') {//food
                    Block foodBlock = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(foodBlock);
                } else if (tileMapChar == 'P') {//pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == 'A') {//powerup
                    Block powerUp = new Block(null, x + 14, y + 14, 12, 12);
                    powerUps.add(powerUp);
                }
            }
        }
    }

    public void spawnCherry() {
        // Find empty spaces where cherries can spawn
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                if (tileMapChar == ' ') { // Empty space
                    int x = c * tileSize;
                    int y = r * tileSize;

                    // Check if this position is not occupied by food
                    boolean occupied = false;
                    for (Block food : foods) {
                        if (food.x == x + 14 && food.y == y + 14) {
                            occupied = true;
                            break;
                        }
                    }

                    // 5% chance to spawn cherry at this empty location
                    if (!occupied && random.nextInt(100) < 5) {
                        Block cherry = new Block(cherryImage, x, y, tileSize, tileSize);
                        cherries.add(cherry);

                        // Remove cherry after 8 seconds
                        Timer removeTimer = new Timer(8000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                cherries.remove(cherry);
                                ((Timer) e.getSource()).stop();
                            }
                        });
                        removeTimer.setRepeats(false);
                        removeTimer.start();

                        return; // Only spawn one cherry at a time
                    }
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (!gameStarted) {
            g.drawString("Press any key to START game", tileSize / 2, tileSize / 2);
        }
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        g.setColor(Color.white);
        for (Block foodBlock : foods) {
            g.fillOval(foodBlock.x, foodBlock.y, foodBlock.width, foodBlock.height);
        }
        for (Block powerUp : powerUps) {
            g.fillOval(powerUp.x, powerUp.y, powerUp.width, powerUp.height);
        }

        // Draw cherries
        for (Block cherry : cherries) {
            g.drawImage(cherry.image, cherry.x, cherry.y, cherry.width, cherry.height, null);
        }
        
        //score 
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.white);
        if (gameOver) {
            g.drawString("Score: " + String.valueOf(score), tileSize / 2, tileSize / 2);
        } else {
            g.drawString("X" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize / 2, tileSize / 2);
        }
    }

    public void move() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        //check wall condition
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        for(Block cherry : cherries) {
            if (collision(pacman, cherry)) {
                cherries.remove(cherry);
                score += 100;
                break;
            }
        }

        for (Block ghost : ghosts) {
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            //check wall condition for ghosts
            for (Block wall : walls) {
                if (ghost.y == tileSize * 9 && ghost.direction != 'D' && ghost.direction != 'U') {
                    ghost.updateDirection('U');
                }
                if (collision(ghost, wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }

            //check collision with pacman
            if (collision(pacman, ghost)) {
                lives -= 1;
                resetPositions();
                gamePaused = true;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
            }
        }
    }

    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width
                && a.x + a.width > b.x
                && a.y < b.y + b.height
                && a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gamePaused) {
            move();
        }
        repaint();
        if (gameOver) {
            gameStarted = false;
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (!gameStarted && !gameOver) {
            gameStarted = true;
            gameloop.start();
            cherryTimer.start(); // Start the cherry timer when game starts
        }

        if (gamePaused && !gameOver) {
            gamePaused = false;
            return;
        }

        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gamePaused = false;
            gameloop.start();
            cherryTimer.restart(); // Restart cherry timer when game restarts
        }

        // System.out.println("Key Released: " + e.getKeyCode());
        if (!gamePaused) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                pacman.updateDirection('U');
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                pacman.updateDirection('D');
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                pacman.updateDirection('L');
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                pacman.updateDirection('R');
            }

            if (pacman.direction == 'U') {
                pacman.image = pacmanUpImage;
            } else if (pacman.direction == 'D') {
                pacman.image = pacmanDownImage;
            } else if (pacman.direction == 'L') {
                pacman.image = pacmanLeftImage;
            } else if (pacman.direction == 'R') {
                pacman.image = pacmanRightImage;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
