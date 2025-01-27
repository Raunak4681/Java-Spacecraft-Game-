import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class CarRacingGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int carX = 100;
    private int carY = 300;
    private List<Rectangle> opponentCars;
    private boolean moveLeft, moveRight, moveUp, moveDown;
    private boolean gameOver;
    private Image playerCarImage;
    private Image opponentCarImage;
    private List<Point> stars;
    private int score;
    private int highScore;

    public CarRacingGame() {
        timer = new Timer(10, this);
        timer.start();
        opponentCars = new ArrayList<>();
        addOpponentCars();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Load images
        playerCarImage = new ImageIcon(getClass().getResource("/player_car.jpeg")).getImage();
        opponentCarImage = new ImageIcon(getClass().getResource("/opponent_car.jpeg")).getImage();

        // Create stars
        stars = new ArrayList<>();
        createStars();

        // Initialize score
        score = 0;
        highScore = 0;

        // Play background music
        playSound("/background.wav");
    }

    private void addOpponentCars() {
        opponentCars.add(new Rectangle(200, 0, 50, 100));
        opponentCars.add(new Rectangle(400, -200, 50, 100));
        opponentCars.add(new Rectangle(600, -400, 50, 100));
    }

    private void createStars() {
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(800);
            int y = rand.nextInt(600);
            stars.add(new Point(x, y));
        }
    }

    private void playSound(String soundFile) {
        try {
            URL url = getClass().getResource(soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw stars
        g.setColor(Color.WHITE);
        for (Point star : stars) {
            g.fillRect(star.x, star.y, 2, 2);
        }

        // Draw player car
        g.drawImage(playerCarImage, carX, carY, 50, 100, this);

        // Draw opponent cars
        for (Rectangle opponentCar : opponentCars) {
            g.drawImage(opponentCarImage, opponentCar.x, opponentCar.y, opponentCar.width, opponentCar.height, this);
        }

        // Draw score and high score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("High Score: " + highScore, 10, 50);

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", getWidth() / 2 - 150, getHeight() / 2);
            playSound("/crash.wav"); // Play crash sound
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (moveLeft && carX > 0) {
                carX -= 2;
            }
            if (moveRight && carX < getWidth() - 50) {
                carX += 2;
            }
            if (moveUp && carY > 0) {
                carY -= 2;
            }
            if (moveDown && carY < getHeight() - 100) {
                carY += 2;
            }

            for (Rectangle opponentCar : opponentCars) {
                opponentCar.y += 2;
                if (opponentCar.y > getHeight()) {
                    opponentCar.y = -100;
                    score += 10; // Increase score when an opponent car goes off the screen
                }

                if (new Rectangle(carX, carY, 50, 100).intersects(opponentCar)) {
                    gameOver = true;
                    timer.stop();
                    if (score > highScore) {
                        highScore = score; // Update high score
                    }
                }
            }

            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            moveLeft = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            moveRight = true;
        }
        if (key == KeyEvent.VK_UP) {
            moveUp = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            moveDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
        if (key == KeyEvent.VK_UP) {
            moveUp = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            moveDown = false;
        }
    }
}
