package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;
import utilities.FileOpenDialog;
import view.ModelCreator;
import view.TopViewCreator;

public class Main extends Application {
//public class Main {


    private static BorderPane borderPane = new BorderPane();

    private static void myInit() {
//        PolyederController.getInstance().init("KugelBinary");
        PolyederController.getInstance().init();
    }

    private void initGUI(Stage stage) {
        stage.setTitle(GUIKonstanten.MY_TITLE);
        borderPane.setTop(TopViewCreator.createTopView(stage, this));
        borderPane.setCenter(ModelCreator.getModelCreatorPane());
        ModelCreator.createContent(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        initGUI(stage);
        stage.setResizable(false);
        Scene scene = new Scene(borderPane, GUIKonstanten.WINDOW_SIZE_X, GUIKonstanten.WINDOW_SIZE_Y);
        stage.setScene(scene);
        ModelCreator.handleMouseEvents(scene, stage);
        stage.show();

//        initGUI(stage);
//        stage.setResizable(true);
//
//        Model model = new Model();
//        String dateiName = "22RE_Block_-_No_Support_-_Scaled";
//        stlImporter.read(AllgemeineKonstanten.DEFAULT_RESOURCES_LOCATION + "" + dateiName + ".stl");
//        Scene scene = new Scene(model.createContent(stlImporter.getImport()), 1280, 720);
//        stage.setScene(scene);
//        model.handleMouseEvents(scene);
//        stage.show();
    }

    public static void main(String[] args) {
        myInit();
        launch(args);
    }

    public void loadFile(Stage stage) {
        PolyederController.getInstance().loadFile(FileOpenDialog.openFileChooser(stage));
        ModelCreator.createContent(stage);
//        ModelCreator.update();
    }
}