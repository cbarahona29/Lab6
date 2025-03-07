package lab6s;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

public class Archivos {
    public String abrirArchivo(File archivo) throws IOException {
        if (archivo.getName().toLowerCase().endsWith(".rtf")) {
            try (FileInputStream fis = new FileInputStream(archivo)) {
                JTextPane panelTemporal = new JTextPane();
                RTFEditorKit kitRTF = new RTFEditorKit();
                Document doc = kitRTF.createDefaultDocument();
                panelTemporal.setDocument(doc);
                
                kitRTF.read(fis, doc, 0);
                
                return doc.getText(0, doc.getLength());
            } catch (BadLocationException e) {
                throw new IOException("Error al procesar el archivo RTF: " + archivo.getName(), e);
            }
        } else {
            try (FileInputStream fis = new FileInputStream(archivo)) {
                byte[] datos = new byte[(int) archivo.length()];
                fis.read(datos);
                return new String(datos, "UTF-8");
            } catch (IOException e) {
                throw new IOException("Error al abrir el archivo: " + archivo.getName(), e);
            }
        }
    }
}
