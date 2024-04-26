import javax.swing.*;

public class Frame{
    public static void main(String[] args) {
        int frameWidth = 360;
        int frameHeight = 640;

        JFrame frame = new JFrame("Bird Game");
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        bird bird = new bird();
        frame.add(bird);
        frame.pack();
        bird.requestFocus();
        frame.setVisible(true);
    }
}
