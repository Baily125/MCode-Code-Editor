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

        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel label = new JLabel("Terminal");
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(600, 200));
        textArea.setBackground(new Color(30, 30, 30)); 
        textArea.setForeground(new Color(200, 200, 200)); 
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        panel.add(scrollPane, BorderLayout.CENTER);

        commandField = new JTextField();
        commandField.setBackground(new Color(20, 20, 20)); 
        commandField.setForeground(new Color(230, 230, 230)); 
        commandField.setCaretColor(Color.WHITE); 
        commandField.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70))); 
        commandField.setFont(new Font("Monospaced", Font.PLAIN, 14)); 
        panel.add(commandField, BorderLayout.SOUTH);


        commandField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandField.getText();
                executeCommand(command);
                commandField.setText("");
            }
        });

        panel.setBackground(new Color(40, 40, 40)); 
        frame.add(panel, BorderLayout.SOUTH);
        
        displayPrompt(); /
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
