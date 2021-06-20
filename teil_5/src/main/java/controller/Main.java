package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.Model;

public class Main extends Application {
//public class Main {

    private static void myInit() {
        PolyederController.getInstance().init("sotvl_thick_2mm");
        PolyederController.getInstance().init("22RE_Block_-_No_Support_-_Scaled");
//        PolyederController.getInstance().init("computer");
//        PolyederController.getInstance().init("KugelASCII");
//        PolyederController.getInstance().init("KugelBinary");
//        PolyederController.getInstance().init("LochBinary");
//        PolyederController.getInstance().init("LochASCII");
//        PolyederController.getInstance().init("PyramideASCII");
//        PolyederController.getInstance().init("PyramideBinary");
//        PolyederController.getInstance().init("untitled_BIG");
    }

    private void initGUI(Stage stage) {
        stage.setTitle("Hallo");
    }

    @Override
    public void start(Stage stage) throws Exception {
        initGUI(stage);
        stage.setResizable(true);

        Model model = new Model();
        Scene scene = new Scene(model.createContent(), 1280, 720);
        model.handleMouseEvents(scene);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        myInit();
        launch(args);
    }
}