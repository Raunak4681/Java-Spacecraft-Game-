import javax.swing.JFrame;

public class GameLauncher {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Racing Game");
        CarRacingGame game = new CarRacingGame();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.requestFocusInWindow(); // Ensure the game panel has focus for key events
    }
}
