import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class MenuBar {
    private JTextPane textPane; 

    public MenuBar(JFrame frame, TextPanel textPanel) {
        this.textPane = textPanel.getTextPane(); 

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newFileButton = new JMenuItem("New");
        newFileButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem openButton = new JMenuItem("Open");
        openButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();  
            }
        });

        JMenuItem saveButton = new JMenuItem("Save");
        saveButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();  
            }
        });

        JMenuItem saveAsButton = new JMenuItem("Save As");
        saveAsButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        // Dodaj elementy do menu
        fileMenu.add(newFileButton);
        fileMenu.add(openButton);
        fileMenu.add(saveButton);
        fileMenu.add(saveAsButton);

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                // Oczyszczenie tekstu w JTextPane
                textPane.setText("");

                String line;
                while ((line = br.readLine()) != null) {
                    textPane.setText(textPane.getText() + line + "\n"); 
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save python file.");

        fileChooser.setSelectedFile(new File("script.py"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".py")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".py");
            }

            try (FileWriter fileWriter = new FileWriter(fileToSave)) {
                fileWriter.write(textPane.getText());
                JOptionPane.showMessageDialog(null,"File saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
