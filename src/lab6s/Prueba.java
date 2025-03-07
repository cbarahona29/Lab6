
import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Prueba {
    
    public static void main(String[] args) {
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
        
        toolBar.addSeparator(new Dimension(10, 10));
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> fontSelector = new JComboBox<>(fontNames);
        fontSelector.setSelectedItem("Arial");
        fontSelector.setMaximumSize(new Dimension(200, 30));
        fontSelector.addActionListener(e -> {
            String selectedFont = (String) fontSelector.getSelectedItem();
            MutableAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attrs, selectedFont);
            textPane.setCharacterAttributes(attrs, false);
        });
        toolBar.add(new JLabel("Fuente: "));
        toolBar.add(fontSelector);
        
        // Add font size selector
        toolBar.addSeparator(new Dimension(10, 10));
        Integer[] fontSizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
        JComboBox<Integer> sizeSelector = new JComboBox<>(fontSizes);
        sizeSelector.setSelectedItem(18);
        sizeSelector.setMaximumSize(new Dimension(80, 30));
        sizeSelector.addActionListener(e -> {
            int selectedSize = (Integer) sizeSelector.getSelectedItem();
            MutableAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontSize(attrs, selectedSize);
            textPane.setCharacterAttributes(attrs, false);
        });
        toolBar.add(new JLabel("Tamaño: "));
        toolBar.add(sizeSelector);
        
        // Create a top panel to hold toolbar and color panel
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Create color panel as a square grid
        JPanel colorPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        colorPanel.setBorder(BorderFactory.createTitledBorder("Colores"));
        colorPanel.setPreferredSize(new Dimension(150, 120));
        
        Color[] colors = {
            Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, 
            Color.ORANGE, Color.YELLOW, Color.PINK, Color.CYAN, 
            Color.MAGENTA, Color.GRAY, Color.DARK_GRAY, Color.WHITE
        };
        
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            colorButton.addActionListener(e -> applyTextColor(textPane, color));
            colorPanel.add(colorButton);
        }
        
        // Add toolbar to the left of top panel
        topPanel.add(toolBar, BorderLayout.CENTER);
        
        // Add color panel to the right of top panel
        topPanel.add(colorPanel, BorderLayout.EAST);
        
        // Add components to main panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        frame.add(panel);
        frame.setVisible(true);
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
    
    private static void applyTextColor(JTextPane textPane, Color color) {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, color);
        textPane.setCharacterAttributes(attributes, false);
    }
}