import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ToolBar {
    public ToolBar(JFrame frame) {
    
        JToolBar toolBar = new JToolBar() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(45, 45, 45)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); 
            }
        };
        
        toolBar.setPreferredSize(new Dimension(400, 40));
        toolBar.setFloatable(false); 
        
        toolBar.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        
        ImageIcon runIcon = new ImageIcon("./media/runIcon.png");
        JButton runButton = new JButton(runIcon);
        runButton.setToolTipText("Run program");
        
        runButton.setBackground(new Color(70, 70, 70)); 
        runButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        runButton.setFocusPainted(false); 

        toolBar.add(runButton);

        frame.add(toolBar, BorderLayout.NORTH);
    }
}
