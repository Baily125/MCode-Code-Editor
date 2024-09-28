import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleTerminal extends JFrame {
    private JTextArea textArea;
    private JTextField commandField;

    public SimpleTerminal() {
        setTitle("Simple Terminal");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        commandField = new JTextField();
        add(commandField, BorderLayout.SOUTH);

        commandField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandField.getText();
                executeCommand(command);
                commandField.setText("");
            }
        });
    }

    private void executeCommand(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            textArea.append(output.toString());
            textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to bottom

        } catch (IOException e) {
            textArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleTerminal terminal = new SimpleTerminal();
            terminal.setVisible(true);
        });
    }
}
