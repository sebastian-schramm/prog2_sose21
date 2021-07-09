package view;

import controller.Main;
import controller.PolyederController;
import controller.ServerController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.interfaces.BottomBarInterface;
import model.interfaces.GUIKonstanten;
import utilities.LabelCreator;

public class BottomViewCreator {

    public static HBox createBottomView(Stage stage, Main main) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefHeight(BottomBarInterface.HBOX_HEIGHT);
        hBox.setSpacing(BottomBarInterface.HBOX_SPACING);
        hBox.setStyle(GUIKonstanten.BOTTOMBAR_COLOR);

        Label areaLabel = LabelCreator.createLabel(PolyederController.getInstance().getPolyeder().getSurfaceProperty().asString(), BottomBarInterface.FONT_SIZE, BottomBarInterface.LABEL_SURFACE);
        Label volumeLabel = LabelCreator.createLabel(PolyederController.getInstance().getPolyeder().getVolumeProperty().asString(), BottomBarInterface.FONT_SIZE, BottomBarInterface.LABEL_VOLUME);
        Label triangleAmountLabel = LabelCreator.createLabel(PolyederController.getInstance().getPolyeder().getTriangleAmountProperty().asString(), BottomBarInterface.FONT_SIZE, BottomBarInterface.LABEL_TRIANGLE);
        Label connectionStatus = LabelCreator.createLabel(ServerController.getInstance().getConnectionStatusProperty(), BottomBarInterface.FONT_SIZE, BottomBarInterface.LABEL_STATUS);


        hBox.getChildren().addAll(
                areaLabel,
                volumeLabel,
                triangleAmountLabel,
                connectionStatus
        );
        return hBox;
    }
}
