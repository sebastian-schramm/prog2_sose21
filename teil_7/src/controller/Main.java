package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.FileOpenDialog;
import view.GUICreator;
/**
 * Main Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Startet die GUI Applikation
     * @param args Umgebungsvarabeln des Systems
     **/
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Die Methode start() ist eine ueberschriebene Methode aus der JavaFX Libary und baut die Scene auf der Applikation.
     **/
    @Override
    public void start(Stage stage) {
        init(stage);
        Scene scene = new Scene(GUICreator.createMainFrame(stage, this), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            ServerController.getInstance().disconnect();
        });
    }

    /**
     * Die Methode init() baut die stage mit ihrem Inhalt auf.
     *
     * @param stage Die stage in der sich die Applikation befinden soll.
     **/
    public void init(Stage stage) {
        PolyederController.getInstance().setStage(stage);
    }

    /**
     * Die Methode loadFile() wird ausgeführt, wenn der Datei Auswaehlen Dialog gestartet werden soll in der Stage.
     * @param stage Die aktuelle Stage des Programms.
     **/
    public void loadFile(Stage stage) {
        PolyederController.getInstance().loadFile(FileOpenDialog.openFileChooser(stage));
    }

}
