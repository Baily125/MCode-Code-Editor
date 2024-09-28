import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;
import java.awt.event.KeyEvent;  
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class TextPanel {
    private JTextPane textPane;  
    private JTextArea lineNumberArea;  

    private static final Set<String> pythonKeywords = new HashSet<>();
    private static final Set<String> pythonBooleans = new HashSet<>();

    static {
        pythonKeywords.add("def");
        pythonKeywords.add("class");
        pythonKeywords.add("import");
        pythonKeywords.add("as");
        pythonKeywords.add("from");
        pythonKeywords.add("if");
        pythonKeywords.add("else");
        pythonKeywords.add("elif");
        pythonKeywords.add("while");
        pythonKeywords.add("for");
        pythonKeywords.add("return");
        pythonKeywords.add("try");
        pythonKeywords.add("except");
        pythonKeywords.add("finally");
        pythonKeywords.add("raise");
        pythonKeywords.add("with");
        pythonKeywords.add("lambda");
        pythonKeywords.add("global");
        pythonKeywords.add("nonlocal");
        pythonKeywords.add("pass");
        pythonKeywords.add("break");
        pythonKeywords.add("continue");
        
        pythonBooleans.add("True");
        pythonBooleans.add("False");
    }

    public TextPanel(JFrame frame) {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Stylizacja JTextPane (tekst)
        textPane = new JTextPane();
        textPane.setFont(new Font("Consolas", Font.PLAIN, 16));  
        textPane.setMargin(new Insets(5, 5, 5, 5));
        textPane.setBackground(new Color(30, 30, 30)); // Ciemne tło
        textPane.setForeground(new Color(230, 230, 230)); // Jasny tekst
        textPane.setCaretColor(Color.WHITE); // Kolor kursora

        // Popup Menu dla opcji edycji (Copy, Cut, Paste)
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem pasteItem = new JMenuItem("Paste");

        copyItem.addActionListener(e -> textPane.copy());
        cutItem.addActionListener(e -> textPane.cut());
        pasteItem.addActionListener(e -> textPane.paste());

        popupMenu.add(copyItem);
        popupMenu.add(cutItem);
        popupMenu.add(pasteItem);

        textPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                highlightSyntax(textPane); 
            }
        });

        JScrollPane textScrollPane = new JScrollPane(textPane);
        textScrollPane.setBorder(BorderFactory.createEmptyBorder());  

        // Stylizacja JTextArea dla numerów linii
        lineNumberArea = new JTextArea("1");
        lineNumberArea.setEditable(false);
        lineNumberArea.setFont(textPane.getFont());
        lineNumberArea.setBackground(new Color(45, 45, 45)); // Ciemne tło dla numerów linii
        lineNumberArea.setForeground(new Color(200, 200, 200)); // Jasny tekst dla numerów linii
        lineNumberArea.setBorder(BorderFactory.createEmptyBorder());
        lineNumberArea.setMargin(new Insets(5, 5, 5, 5));

        JScrollPane lineNumberScrollPane = new JScrollPane(lineNumberArea);
        lineNumberScrollPane.setBorder(BorderFactory.createEmptyBorder());  
        lineNumberScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        lineNumberScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        textPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            private void updateLineNumbers() {
                int lineCount = textPane.getDocument().getDefaultRootElement().getElementCount();
                StringBuilder lineNumbers = new StringBuilder();
                for (int i = 1; i <= lineCount; i++) {
                    lineNumbers.append(String.format("%4d%n", i)); 
                }
                lineNumberArea.setText(lineNumbers.toString());
            }
        });

        mainPanel.add(lineNumberScrollPane, BorderLayout.WEST);
        mainPanel.add(textScrollPane, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        JScrollBar textVerticalScrollBar = textScrollPane.getVerticalScrollBar();
        JScrollBar lineNumberVerticalScrollBar = lineNumberScrollPane.getVerticalScrollBar();
        textVerticalScrollBar.addAdjustmentListener(e -> lineNumberVerticalScrollBar.setValue(textVerticalScrollBar.getValue()));
    }

    // Funkcja podświetlająca składnię
    public static void highlightSyntax(JTextPane textPane) {
        StyledDocument doc = textPane.getStyledDocument();
        StyleContext context = StyleContext.getDefaultStyleContext();
    
        try {
            String text = doc.getText(0, doc.getLength());
    
            AttributeSet defAttr = context.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE);
            doc.setCharacterAttributes(0, text.length(), defAttr, true);
    
            highlightPattern(doc, "\\b(" + String.join("|", pythonKeywords) + ")\\b", Color.BLUE, true, text);
            highlightPattern(doc, "\\b(" + String.join("|", pythonBooleans) + ")\\b", Color.MAGENTA, true, text);
            highlightPattern(doc, "\".*?\"|'.*?'", Color.GREEN, true, text);
            highlightPattern(doc, "\\b\\d+(?!\\w)", Color.ORANGE, true, text);
            highlightPattern(doc, "#.*", Color.GRAY, true, text);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private static void highlightPattern(StyledDocument doc, String pattern, Color color, Boolean bold, String text) {
        StyleContext context = StyleContext.getDefaultStyleContext();
        AttributeSet attr = context.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);

        if (bold) {
            attr = context.addAttribute(attr, StyleConstants.Bold, true);
        }

        while (m.find()) {
            doc.setCharacterAttributes(m.start(), m.end() - m.start(), attr, true);
        }
    }

    public JTextPane getTextPane() {
        return textPane;
    }
}
