package proyecto2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Prueba {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Editor de Texto");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new BorderLayout());
            
            JTextPane textPane = new JTextPane();
            DefaultStyledDocument doc = new DefaultStyledDocument();
            textPane.setDocument(doc);
            textPane.setFont(new Font("Arial", Font.PLAIN, 18));
            JScrollPane scrollPane = new JScrollPane(textPane);
            
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);
            
            JButton boldButton = createStyledButton("B", e -> new StyledEditorKit.BoldAction().actionPerformed(e));
            boldButton.setFont(new Font("Arial", Font.BOLD, 18));
            
            JButton italicButton = createStyledButton("I", e -> new StyledEditorKit.ItalicAction().actionPerformed(e));
            italicButton.setFont(new Font("Arial", Font.ITALIC, 18));
            
            JButton underlineButton = createStyledButton("U", e -> new StyledEditorKit.UnderlineAction().actionPerformed(e));
            underlineButton.setFont(new Font("Arial", Font.PLAIN, 18));
            
            toolBar.add(boldButton);
            toolBar.add(italicButton);
            toolBar.add(underlineButton);
            
            panel.add(toolBar, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            frame.add(panel);
            frame.setVisible(true);
        });
    }

    private static JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        button.setPreferredSize(new Dimension(50, 30));
        button.addActionListener(actionListener);
        return button;
    }
}
