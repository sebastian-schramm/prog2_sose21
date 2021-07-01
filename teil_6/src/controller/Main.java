package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;
import utilities.FileOpenDialog;
import view.BottomViewCreator;
import view.TopViewCreator;

public class Main extends Application {

    private static BorderPane borderPane = new BorderPane();
    private static StackPane root = new StackPane();

    private static void myInit() {
//        PolyederController.getInstance().init("KugelBinary");
        PolyederController.getInstance().init();
        ModelController.getInstance().init();
        NetworkController.getInstance().init();
    }


    private void initGUI(Stage stage) {
        stage.setTitle(GUIKonstanten.MY_TITLE);
        stage.setWidth(GUIKonstanten.WINDOW_SIZE_X);
        stage.setHeight(GUIKonstanten.WINDOW_SIZE_Y);
        root.getChildren().add(borderPane);
        borderPane.setTop(TopViewCreator.createTopView(stage, this));
        borderPane.setBottom(BottomViewCreator.createBottomView(stage, this));
        borderPane.setCenter(ModelController.getInstance().getModel().getModelCreatorPane());

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            ModelController.getInstance().setSubSceneWidth(newVal.doubleValue());
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            ModelController.getInstance().setSubSceneHeight(newVal.doubleValue());
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        initGUI(stage);
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    public static void main(String[] args) {
        myInit();
        launch(args);
    }

    public void loadFile(Stage stage) {
        PolyederController.getInstance().loadFile(FileOpenDialog.openFileChooser(stage), stage);
    }
}