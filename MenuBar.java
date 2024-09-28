import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class MenuBar {
    private JTextPane textPane;  // Odniesienie do JTextPane

    public MenuBar(JFrame frame, TextPanel textPanel) {
        this.textPane = textPanel.getTextPane();  // Inicjalizacja JTextPane z TextPanel

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Dodaj przycisk New
        JMenuItem newFileButton = new JMenuItem("New");
        newFileButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        // Dodaj przycisk Open
        JMenuItem openButton = new JMenuItem("Open");
        openButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();  // Wywołanie metody openFile
            }
        });

        // Dodaj przycisk Save
        JMenuItem saveButton = new JMenuItem("Save");
        saveButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();  // Wywołanie metody saveFile
            }
        });

        // Dodaj przycisk Save As
        JMenuItem saveAsButton = new JMenuItem("Save As");
        saveAsButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        // Dodaj elementy do menu
        fileMenu.add(newFileButton);
        fileMenu.add(openButton);
        fileMenu.add(saveButton);
        fileMenu.add(saveAsButton);

        // Dodaj menu do menuBar i ustaw w JFrame
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        // Jeśli użytkownik wybrał plik
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                // Oczyszczenie tekstu w JTextPane
                textPane.setText("");

                // Odczytywanie pliku linia po linii
                String line;
                while ((line = br.readLine()) != null) {
                    textPane.setText(textPane.getText() + line + "\n");  // Użycie setText z append
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
