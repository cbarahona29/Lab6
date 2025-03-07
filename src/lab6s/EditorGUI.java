package lab6s;

import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class EditorGUI {
    
    public JTextPane panelTexto = new JTextPane();
    private JTree arbolArchivos;
    private DefaultTreeModel modeloArbol;
    private final String rutaDirectorio = "Archivos";
    private final RTFEditorKit kitRTF = new RTFEditorKit();
    private String archivoActual = null; 

    public EditorGUI() {
        JFrame ventana = new JFrame("Editor de Texto");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 600);
        ventana.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        panelTexto.setEditorKit(kitRTF);
        panelTexto.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane panelDesplazamiento = new JScrollPane(panelTexto);

        JToolBar barraHerramientas = new JToolBar();
        barraHerramientas.setFloatable(false);

        JButton botonNegrita = crearBotonEstilo("B", e -> new StyledEditorKit.BoldAction().actionPerformed(e));
        botonNegrita.setFont(new Font("Arial", Font.BOLD, 18));

        JButton botonCursiva = crearBotonEstilo("I", e -> new StyledEditorKit.ItalicAction().actionPerformed(e));
        botonCursiva.setFont(new Font("Arial", Font.ITALIC, 18));

        JButton botonSubrayado = crearBotonEstilo("U", e -> new StyledEditorKit.UnderlineAction().actionPerformed(e));
        botonSubrayado.setFont(new Font("Arial", Font.PLAIN, 18));

        barraHerramientas.add(botonNegrita);
        barraHerramientas.add(botonCursiva);
        barraHerramientas.add(botonSubrayado);

        barraHerramientas.addSeparator(new Dimension(10, 10));
        String[] nombresFuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> selectorFuente = new JComboBox<>(nombresFuentes);
        selectorFuente.setSelectedItem("Arial");
        selectorFuente.setMaximumSize(new Dimension(200, 30));
        selectorFuente.addActionListener(e -> {
            String fuenteSeleccionada = (String) selectorFuente.getSelectedItem();
            MutableAttributeSet atributos = new SimpleAttributeSet();
            StyleConstants.setFontFamily(atributos, fuenteSeleccionada);
            panelTexto.setCharacterAttributes(atributos, false);
        });
        barraHerramientas.add(new JLabel("Fuente: "));
        barraHerramientas.add(selectorFuente);

        barraHerramientas.addSeparator(new Dimension(10, 10));
        Integer[] tamanosFuente = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
        JComboBox<Integer> selectorTamano = new JComboBox<>(tamanosFuente);
        selectorTamano.setSelectedItem(18);
        selectorTamano.setMaximumSize(new Dimension(80, 30));
        selectorTamano.addActionListener(e -> {
            int tamanoSeleccionado = (Integer) selectorTamano.getSelectedItem();
            MutableAttributeSet atributos = new SimpleAttributeSet();
            StyleConstants.setFontSize(atributos, tamanoSeleccionado);
            panelTexto.setCharacterAttributes(atributos, false);
        });
        barraHerramientas.add(new JLabel("Tamaño: "));
        barraHerramientas.add(selectorTamano);

        barraHerramientas.addSeparator(new Dimension(10, 10));
        
        JButton botonNuevo = new JButton("Nuevo");
        botonNuevo.setPreferredSize(new Dimension(80, 30));
        botonNuevo.addActionListener(e -> {
            panelTexto.setText("");
            archivoActual = null;
            JOptionPane.showMessageDialog(ventana, "Nuevo documento creado");
        });
        barraHerramientas.add(botonNuevo);
        
        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.setPreferredSize(new Dimension(80, 30));
        botonGuardar.addActionListener(e -> {
            if (archivoActual == null) {
                guardarComo(ventana);
            } else {
                File archivoSalida = new File(rutaDirectorio + File.separator + archivoActual);
                guardarArchivo(archivoSalida, ventana);
            }
        });
        barraHerramientas.add(botonGuardar);
        
        JButton botonGuardarComo = new JButton("Guardar Como");
        botonGuardarComo.setPreferredSize(new Dimension(120, 30));
        botonGuardarComo.addActionListener(e -> {
            guardarComo(ventana);
        });
        barraHerramientas.add(botonGuardarComo);
        
        JButton botonEliminar = new JButton("Eliminar");
        botonEliminar.setPreferredSize(new Dimension(80, 30));
        botonEliminar.addActionListener(e -> {
            DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) arbolArchivos.getLastSelectedPathComponent();
            if (nodoSeleccionado != null && nodoSeleccionado.getUserObject() instanceof String && !nodoSeleccionado.isRoot()) {
                String nombreArchivo = nodoSeleccionado.getUserObject().toString();
                File archivoSeleccionado = new File(rutaDirectorio + File.separator + nombreArchivo);
                
                int confirmacion = JOptionPane.showConfirmDialog(
                    ventana,
                    "¿Está seguro que desea eliminar el archivo: " + nombreArchivo + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    if (archivoSeleccionado.exists() && archivoSeleccionado.isFile()) {
                        if (archivoSeleccionado.delete()) {
                            JOptionPane.showMessageDialog(ventana, "Archivo eliminado correctamente");
                            actualizarArbolArchivos();
                            
                            if (nombreArchivo.equals(archivoActual)) {
                                archivoActual = null;
                                panelTexto.setText("");
                            }
                        } else {
                            JOptionPane.showMessageDialog(ventana, 
                                "No se pudo eliminar el archivo", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(ventana, 
                    "Seleccione un archivo para eliminar", 
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        barraHerramientas.add(botonEliminar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JPanel panelColores = new JPanel(new GridLayout(3, 4, 5, 5));
        panelColores.setBorder(BorderFactory.createTitledBorder("Colores"));
        panelColores.setPreferredSize(new Dimension(150, 120));

        Color[] colores = {
            Color.BLACK, Color.RED, Color.BLUE, Color.GREEN,
            Color.ORANGE, Color.YELLOW, Color.PINK, Color.CYAN,
            Color.MAGENTA, Color.GRAY, Color.DARK_GRAY, Color.WHITE
        };

        for (Color color : colores) {
            JButton botonColor = new JButton();
            botonColor.setBackground(color);
            botonColor.setPreferredSize(new Dimension(30, 30));
            botonColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            botonColor.addActionListener(e -> aplicarColorTexto(panelTexto, color));
            panelColores.add(botonColor);
        }

        panelSuperior.add(barraHerramientas, BorderLayout.CENTER);
        panelSuperior.add(panelColores, BorderLayout.EAST);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelDesplazamiento, BorderLayout.CENTER);

        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        
        arbolArchivos = crearArbol();
        JScrollPane panelArbol = new JScrollPane(arbolArchivos);
        panelArbol.setPreferredSize(new Dimension(200, 0));
        panel.add(panelArbol, BorderLayout.WEST);

        ventana.add(panel);
        ventana.setVisible(true);
    }
    
    private void guardarComo(JFrame ventana) {
        String nombre = JOptionPane.showInputDialog("Escriba el nombre del archivo");
        if (nombre != null && !nombre.trim().isEmpty()) {
            if (!nombre.toLowerCase().endsWith(".rtf")) {
                nombre += ".rtf";
            }
            
            File directorio = new File(rutaDirectorio);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            
            File archivoSalida = new File(rutaDirectorio + File.separator + nombre);
            if (guardarArchivo(archivoSalida, ventana)) {
                archivoActual = nombre; 
            }
        }
    }
    
    private boolean guardarArchivo(File archivo, JFrame ventana) {
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            kitRTF.write(fos, panelTexto.getDocument(), 0, panelTexto.getDocument().getLength());
            JOptionPane.showMessageDialog(ventana,
                    "Archivo guardado en:\n" + archivo.getAbsolutePath());
            
            actualizarArbolArchivos();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ventana,
                    "Error al guardar el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static JButton crearBotonEstilo(String texto, ActionListener accionListener) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        boton.setPreferredSize(new Dimension(50, 30));
        boton.addActionListener(accionListener);
        return boton;
    }

    private static void aplicarColorTexto(JTextPane panelTexto, Color color) {
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setForeground(atributos, color);
        panelTexto.setCharacterAttributes(atributos, false);
    }

    private JTree crearArbol() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Archivos");
        File carpeta = new File(rutaDirectorio);
        
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        
        popularNodoArbol(raiz, carpeta);
        
        modeloArbol = new DefaultTreeModel(raiz);
        JTree arbol = new JTree(modeloArbol);
        
        arbol.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
            if (nodoSeleccionado != null && nodoSeleccionado.getUserObject() instanceof String && !nodoSeleccionado.isRoot()) {
                String nombreArchivo = nodoSeleccionado.getUserObject().toString();
                File archivoSeleccionado = new File(rutaDirectorio + File.separator + nombreArchivo);
                if (archivoSeleccionado.exists() && archivoSeleccionado.isFile()) {
                    try {
                        abrirArchivoRTF(archivoSeleccionado);
                        archivoActual = nombreArchivo; // Actualizar el nombre del archivo actual
                    } catch (IOException ex) {
                        Logger.getLogger(EditorGUI.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, 
                                "Error al abrir el archivo: " + ex.getMessage(), 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        return arbol;
    }
    
    private void popularNodoArbol(DefaultMutableTreeNode nodo, File dir) {
        File[] archivos = dir.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    DefaultMutableTreeNode nodoArchivo = new DefaultMutableTreeNode(archivo.getName());
                    nodo.add(nodoArchivo);
                }
            }
        }
    }
    
    private void actualizarArbolArchivos() {
        DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modeloArbol.getRoot();
        raiz.removeAllChildren();
        
        File carpeta = new File(rutaDirectorio);
        popularNodoArbol(raiz, carpeta);
        
        modeloArbol.reload();
        arbolArchivos.expandPath(arbolArchivos.getPathForRow(0));
    }
    
    private void abrirArchivoRTF(File archivo) throws IOException {
        try (FileInputStream fis = new FileInputStream(archivo)) {
            panelTexto.setText("");
            kitRTF.read(fis, panelTexto.getDocument(), 0);
        } catch (BadLocationException ex) {
            throw new IOException("Error al leer el archivo RTF: " + ex.getMessage(), ex);
        }
    }
}
