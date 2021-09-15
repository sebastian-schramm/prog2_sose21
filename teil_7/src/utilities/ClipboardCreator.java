package utilities;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
/**
 * ClipboardCreator Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class ClipboardCreator {

    /**
     * Die createClipboard() Methode baut ein Clipboard auf mit unterschiedlichen Werten.
     *
     * @param value Der Wert, der im Clipboard dargestellt werden soll.
     **/
    public static void createClipboard(String value) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(value);
        clipboard.setContent(content);
    }
}
