package view;

import controller.Main;
import controller.PolyederController;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilities.LabelCreator;

public final class BottomViewCreator {

    private BottomViewCreator() {

    }

    public static HBox createBottomView (Stage stage, Main meinMain) {
        HBox hBox = new HBox();

        Label volumeLabel = LabelCreator.createLabel(PolyederController.getInstance().getPolyeder().aktuelleGesundheitProperty().asString(), 11);
        Label areaLabel = LabelCreator.createLabel(PolyederController.getInstance().getPolyeder().aktuelleGesundheitProperty().asString(), 11);
//        Label volumeLabel = LabelCreator.createLabel(PolyederController.getInstance().getPolyeder().getVolumeProperty(), 11);
//        Label areaLabel = LabelCreator.createLabel(PolyederController.getInstance().getPolyeder().getSurfaceProperty(), 11);
//        System.out.println(PolyederController.getInstance().getPolyeder().getVolumeProperty().getValue());
//        MenuBar menu = MenuCreator.createMenu(stage, meinMain);

        hBox.getChildren().addAll(volumeLabel, areaLabel);

        return hBox;
    }
}
