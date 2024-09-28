import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ToolBar {
    public ToolBar(JFrame frame) {
        // Tworzenie toolbara
        JToolBar toolBar = new JToolBar() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(45, 45, 45)); // Ciemny kolor tła
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Zaokrąglone rogi
            }
        };
        
        // Ustawienie preferowanego rozmiaru
        toolBar.setPreferredSize(new Dimension(400, 40));
        toolBar.setFloatable(false); // Zablokowanie możliwości przeciągania
        
        // Ustawienie odstępów
        toolBar.setBorder(new EmptyBorder(5, 5, 5, 5)); // Padding wewnętrzny
        
        // Dodanie przycisku
        ImageIcon runIcon = new ImageIcon("./media/runIcon.png");
        JButton runButton = new JButton(runIcon);
        runButton.setToolTipText("Run program");
        
        // Stylizacja przycisku
        runButton.setBackground(new Color(70, 70, 70)); // Kolor przycisku
        runButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Usuń ramkę przycisku
        runButton.setFocusPainted(false); // Usuń obrys przy skupieniu

        toolBar.add(runButton);

        // Dodanie toolbara do ramki
        frame.add(toolBar, BorderLayout.NORTH);
    }
}
