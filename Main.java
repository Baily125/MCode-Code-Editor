import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Code Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TextPanel textPanel = new TextPanel(frame);

        MenuBar menuBar = new MenuBar(frame, textPanel);

        ToolBar toolBar = new ToolBar(frame);

        Terminal terminal = new Terminal(frame);

        try {
            Image icon = ImageIO.read(new File("./media/icon.png"));

            Image scaledIcon = icon.getScaledInstance(64, 64, Image.SCALE_SMOOTH);

            frame.setIconImage(scaledIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setSize(640, 480);
        frame.setVisible(true);
    }
}
