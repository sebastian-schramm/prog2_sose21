package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.Ausgabe;
import view.FileOpenDialog;
import view.GUICreator;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
//        Drag3DObject drag3DObject = new Drag3DObject();
//        drag3DObject.start(stage);

        Scene scene = new Scene(GUICreator.createMainFrame(stage, this), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            Ausgabe.print("Programm wird beendet");
            ServerController.getInstance().close();
        });
    }

    private static void myInit() {

    }

    public void loadFile(Stage stage) {
        PolyederController.getInstance().getPolyeder().loadFile(FileOpenDialog.openFileChooser(stage), stage);
    }
}
