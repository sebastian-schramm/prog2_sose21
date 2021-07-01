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

        Label volumeLabel = LabelCreator.createLabel(PolyederController.getInstance().volumeProperty().asString(), 12, "Volume: ");
        Label areaLabel = LabelCreator.createLabel(PolyederController.getInstance().surfaceProperty().asString(), 12, "Surface: ");
        Label triangleAmountLabel = LabelCreator.createLabel(PolyederController.getInstance().triangleAmountProperty().asString(), 12, "Triangles: ");
        Label connectionStatus = LabelCreator.createLabel(NetworkController.getInstance().getNetwork().getConnectionStatus(), 12, "Status: ");

        hBox.getChildren().addAll(volumeLabel, areaLabel, triangleAmountLabel, connectionStatus);

        return hBox;
    }
}
