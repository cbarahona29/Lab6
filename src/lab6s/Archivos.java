/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6s;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

/**
 *
 * @author danilos
 */
public class Archivos {

    public String openFile(File file) throws IOException {
        // Crea un flujo de entrada para el archivo
        try {
            FileReader reader = new FileReader(file);
            StringBuilder content = new StringBuilder();
            int caracter = 0;

            // Lee el archivo línea por línea
            while ((caracter = reader.read()) != -1) {
                content.append((char) caracter).append("\n");
            }

            // Coloca el contenido del archivo en el JTextPane
            return String.valueOf(content);
        } catch (IOException e) {
            throw new IOException("Error al abrir el archivo: " + file.getName(), e);
        }
    }
}
