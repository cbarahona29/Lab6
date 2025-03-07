
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Prueba {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Editor de Texto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Área de texto
        JTextPane textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Barra de herramientas
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton boldButton = createStyledButton("B", new StyledEditorKit.BoldAction());
        boldButton.setFont(new Font("Arial", Font.BOLD, 18));

        JButton italicButton = createStyledButton("I", new StyledEditorKit.ItalicAction());
        italicButton.setFont(new Font("Arial", Font.ITALIC, 18));

        JButton underlineButton = createStyledButton("U", new StyledEditorKit.UnderlineAction());
        underlineButton.setFont(new Font("Arial", Font.PLAIN, 18));

        // ComboBox de fuentes
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        JComboBox<String> fontComboBox = new JComboBox<>(fonts);
        fontComboBox.setSelectedItem("Arial");
        fontComboBox.addActionListener(e -> textPane.setFont(new Font((String) fontComboBox.getSelectedItem(), Font.PLAIN, textPane.getFont().getSize())));

        // ComboBox de tamaño de fuente
        Integer[] sizes = {12, 16, 20, 24, 32, 48, 64};
        JComboBox<Integer> sizeComboBox = new JComboBox<>(sizes);
        sizeComboBox.setSelectedItem(18);
        sizeComboBox.addActionListener(e -> textPane.setFont(new Font(textPane.getFont().getFamily(), Font.PLAIN, (Integer) sizeComboBox.getSelectedItem())));

        // Botón de color
        JButton colorButton = new JButton("Color");
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Selecciona un color", textPane.getForeground());
            if (color != null) {
                textPane.setForeground(color);
            }
        });

        toolBar.add(boldButton);
        toolBar.add(italicButton);
        toolBar.add(underlineButton);
        toolBar.add(new JLabel("Fuente:"));
        toolBar.add(fontComboBox);
        toolBar.add(new JLabel(" Tamaño:"));
        toolBar.add(sizeComboBox);
        toolBar.add(colorButton);

        panel.add(toolBar, BorderLayout.NORTH);

        // Panel inferior con botones
        JPanel bottomPanel = new JPanel();
        JButton acceptButton = new JButton("Aceptar");
        JButton cancelButton = new JButton("Cancelar");
        bottomPanel.add(acceptButton);
        bottomPanel.add(cancelButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text, Action action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        button.setPreferredSize(new Dimension(50, 30));
        button.addActionListener(action);
        return button;
    }
}
