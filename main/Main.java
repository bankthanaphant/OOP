package main;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Game");

        Gamepanel gamepanel = new Gamepanel();
        window.add(gamepanel);
        window.pack();
        window.setLocationRelativeTo(null);

        gamepanel.stratgThread();

        window.setVisible(true);
    }
}
