package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.FileOpenDialog;
import view.GUICreator;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(GUICreator.createMainFrame(stage, this), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
//            ServerController.getInstance().close();
        });
    }

    public void loadFile(Stage stage) {
        PolyederController.getInstance().getPolyeder().loadFile(FileOpenDialog.openFileChooser(stage), stage);
    }

}
