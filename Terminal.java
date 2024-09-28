import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Terminal {
    private JTextArea textArea;
    private JTextField commandField;
    private String currentPath;

    public Terminal(JFrame frame) {
        currentPath = System.getProperty("user.dir");

        // Główne panel z BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        
        // Ustawienia etykiety "Terminal"
        JLabel label = new JLabel("Terminal");
        label.setForeground(Color.WHITE); // Ciemny motyw tekstu
        panel.add(label, BorderLayout.NORTH);

        // Ustawienia obszaru tekstowego (terminala)
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(600, 200));
        textArea.setBackground(new Color(30, 30, 30)); // Ciemny kolor tła
        textArea.setForeground(new Color(200, 200, 200)); // Jasny kolor tekstu
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Ustawienie czcionki typu monospaced

        // Dodanie paska przewijania do terminala
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Usuń ramkę
        panel.add(scrollPane, BorderLayout.CENTER);

        // Ustawienia pola tekstowego do wprowadzania komend
        commandField = new JTextField();
        commandField.setBackground(new Color(20, 20, 20)); // Ciemne tło pola tekstowego
        commandField.setForeground(new Color(230, 230, 230)); // Jasny kolor tekstu
        commandField.setCaretColor(Color.WHITE); // Kolor kursora
        commandField.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70))); // Delikatna ramka
        commandField.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Ustawienie czcionki typu monospaced
        panel.add(commandField, BorderLayout.SOUTH);

        // Obsługa polecenia po naciśnięciu Enter
        commandField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandField.getText();
                executeCommand(command);
                commandField.setText("");
            }
        });

        // Dodanie panelu terminala do okna
        panel.setBackground(new Color(40, 40, 40)); // Ciemne tło panelu
        frame.add(panel, BorderLayout.SOUTH);
        
        displayPrompt(); // Wyświetlenie prompta na starcie
    }

    private void displayPrompt() {
        textArea.append(">" + " " + currentPath + "> "); 
        textArea.setCaretPosition(textArea.getDocument().getLength()); 
    }

    private void executeCommand(String command) {
        textArea.append(command + "\n");

        if (command.startsWith("cd ")) {
            // Obsługa zmiany katalogu
            String newPath = command.substring(3).trim();
            File dir = new File(currentPath, newPath);
            if (dir.exists() && dir.isDirectory()) {
                currentPath = dir.getAbsolutePath();
                displayPrompt();
            } else {
                textArea.append("Error: Directory not found\n");
                displayPrompt();
            }
            return;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.directory(new File(currentPath));
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            textArea.append(output.toString());
            displayPrompt();

        } catch (IOException e) {
            textArea.append("Error: " + e.getMessage() + "\n");
            displayPrompt();
        }
    }
}
