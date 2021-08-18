package view;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;

import java.io.File;

public class FileOpenDialog {
    public static File openFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(GUIKonstanten.FILE_CHOOSER_TITLE);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(GUIKonstanten.LOAD_STL, GUIKonstanten.STL_EXTENSION));
        return fileChooser.showOpenDialog(stage);
    }
}
