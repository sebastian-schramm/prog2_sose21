package utilities;


import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardCreator {

    public static void createClipboard(String value){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(value);
        clipboard.setContent(content);
    }
}
