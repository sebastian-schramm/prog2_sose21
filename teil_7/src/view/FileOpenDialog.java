package view;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;

import java.io.File;

/**
 * FileOpenDialog Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class FileOpenDialog {
    /**
     * Oeffnet den Explorer damit eine Datei ausgewählt werden kann, die eingeladen werden soll.
     * Dabei werden nur STL-Dateien als valide Auswahlmoeglichkeit erlaubt.
     *
     * @param stage
     * @return File
     */
    public static File openFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(GUIKonstanten.FILE_CHOOSER_TITLE);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(GUIKonstanten.LOAD_STL, GUIKonstanten.STL_EXTENSION));
        return fileChooser.showOpenDialog(stage);
    }
}
