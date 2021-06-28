package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;
import utilities.FileOpenDialog;
import view.TopViewCreator;

public class Main extends Application {

    private static BorderPane borderPane = new BorderPane();

    private static void myInit() {
//        PolyederController.getInstance().init("KugelBinary");
        PolyederController.getInstance().init();
        ModelController.getInstance().init();
    }

    private void initGUI(Stage stage) {
        stage.setTitle(GUIKonstanten.MY_TITLE);
        borderPane.setTop(TopViewCreator.createTopView(stage, this));
        borderPane.setCenter(ModelController.getInstance().getModel().getModelCreatorPane());
    }

    @Override
    public void start(Stage stage) throws Exception {
        initGUI(stage);
        stage.setResizable(false);
        Scene scene = new Scene(borderPane, GUIKonstanten.WINDOW_SIZE_X, GUIKonstanten.WINDOW_SIZE_Y);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        myInit();
        launch(args);
    }

    public void loadFile(Stage stage) {
        PolyederController.getInstance().loadFile(FileOpenDialog.openFileChooser(stage));
    }
}