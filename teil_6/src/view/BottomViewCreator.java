package view;

import controller.Main;
import controller.NetworkController;
import controller.PolyederController;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;
import utilities.LabelCreator;

public final class BottomViewCreator {

    private BottomViewCreator() {

    }

    public static HBox createBottomView (Stage stage, Main meinMain) {
        HBox hBox = new HBox();
        hBox.setMaxHeight(GUIKonstanten.BOTTOMBAR_HEIGHT);

        Label volumeLabel = LabelCreator.createLabel(PolyederController.getInstance().volumeProperty().asString(), 12, GUIKonstanten.LABEL_VOLUME);
        Label areaLabel = LabelCreator.createLabel(PolyederController.getInstance().surfaceProperty().asString(), 12, GUIKonstanten.LABEL_SURFACE);
        Label triangleAmountLabel = LabelCreator.createLabel(PolyederController.getInstance().triangleAmountProperty().asString(), 12, GUIKonstanten.LABEL_TRIANGLE);
        Label connectionStatus = LabelCreator.createLabel(NetworkController.getInstance().getNetwork().ConnectionStatus(), 12, GUIKonstanten.LABEL_STATUS);

        hBox.getChildren().addAll(volumeLabel, areaLabel, triangleAmountLabel, connectionStatus);

        return hBox;
    }
}
