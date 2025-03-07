/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6s;

import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author danilos
 */
public class EditorGUI {
    
    public JTextPane textPanel = new JTextPane();

    public EditorGUI() {
        JFrame frame = new JFrame("Editor de Texto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        DefaultStyledDocument doc = new DefaultStyledDocument();
        textPanel.setDocument(doc);
        textPanel.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(textPanel);

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
            textPanel.setCharacterAttributes(attrs, false);
        });
        toolBar.add(new JLabel("Fuente: "));
        toolBar.add(fontSelector);

        toolBar.addSeparator(new Dimension(10, 10));
        Integer[] fontSizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
        JComboBox<Integer> sizeSelector = new JComboBox<>(fontSizes);
        sizeSelector.setSelectedItem(18);
        sizeSelector.setMaximumSize(new Dimension(80, 30));
        sizeSelector.addActionListener(e -> {
            int selectedSize = (Integer) sizeSelector.getSelectedItem();
            MutableAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontSize(attrs, selectedSize);
            textPanel.setCharacterAttributes(attrs, false);
        });
        toolBar.add(new JLabel("Tamaño: "));
        toolBar.add(sizeSelector);

           toolBar.addSeparator(new Dimension(10, 10));
        JButton guardarButton = new JButton("Guardar");
        guardarButton.setPreferredSize(new Dimension(80, 30));
        guardarButton.addActionListener(e -> {

            String name;
            name = JOptionPane.showInputDialog("Escriba el nombre del archivo");
            if (name != null) {
                File archivoSalida = new File(name + ".rtf");
                try (FileOutputStream fos = new FileOutputStream(archivoSalida)) {
                    RTFEditorKit rtfKit = new RTFEditorKit();
                    rtfKit.write(fos, textPanel.getDocument(), 0, textPanel.getDocument().getLength());
                    JOptionPane.showMessageDialog(frame,
                            "Archivo guardado en:\n" + archivoSalida.getAbsolutePath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                            "Error al guardar el archivo: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        toolBar.add(guardarButton);

        JPanel topPanel = new JPanel(new BorderLayout());
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
            colorButton.addActionListener(e -> applyTextColor(textPanel, color));
            colorPanel.add(colorButton);
        }

        topPanel.add(toolBar, BorderLayout.CENTER);
        topPanel.add(colorPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JTree fileTree = createTree();
        JScrollPane treeScrollPane = new JScrollPane(fileTree);
        treeScrollPane.setPreferredSize(new Dimension(200, 0));
        panel.add(treeScrollPane, BorderLayout.WEST);

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

    private JTree createTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Archivos");
        File folder = new File("Archivos"); // Cambia esta ruta si es necesario
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file.getName());
                    root.add(fileNode);
                }
            }
        }

        JTree tree = new JTree(root);
        Archivos arch = new Archivos();
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                String fileName = selectedNode.getUserObject().toString();
                File selectedFile = new File("C:\\Users\\river\\OneDrive\\Documentos\\NetBeansProjects\\Lab6\\" + fileName);
                if (selectedFile.exists()) {
                    try {
                        textPanel.setText(arch.openFile(selectedFile));
                    } catch (IOException ex) {
                        Logger.getLogger(EditorGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        return tree;
    }
}
