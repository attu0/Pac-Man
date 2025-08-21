import javax.swing.JFrame;

public class app{
    public static void main(String[] args){
        int columCount = 21;
        int rowCount = 19;
        int tileSize = 32;
        int boardWidth = columCount * tileSize;
        int boardHeight = rowCount * tileSize;

        javax.swing.JFrame frame = new JFrame("Pac Man");
        frame.setSize(boardWidth,boardHeight);
        // frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}