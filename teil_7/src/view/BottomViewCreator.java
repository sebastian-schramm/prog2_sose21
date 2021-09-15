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
/**
 * BottomViewCreator Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class BottomViewCreator {

    /**
     * Erzeugt den unteren Teil der View, in dem vier informationen ausgeben werden,
     * die Laufend aktualisiert werden können.
     *
     * @return hBox - Enthaelt den kompletten Inhalt der Statusbar.
     */
    public static HBox createBottomView() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefHeight(BottomBarInterface.HBOX_HEIGHT);
        hBox.setSpacing(BottomBarInterface.HBOX_SPACING);
        hBox.setStyle(GUIKonstanten.BOTTOMBAR_COLOR);

        Label areaLabel = LabelCreator.createLabel(PolyederController.getInstance().getSurfaceProperty().asString(), GUIKonstanten.FONT_SIZE, BottomBarInterface.LABEL_SURFACE);
        Label volumeLabel = LabelCreator.createLabel(PolyederController.getInstance().getVolumeProperty().asString(), GUIKonstanten.FONT_SIZE, BottomBarInterface.LABEL_VOLUME);
        Label triangleAmountLabel = LabelCreator.createLabel(PolyederController.getInstance().getTriangleAmountProperty().asString(), GUIKonstanten.FONT_SIZE, BottomBarInterface.LABEL_TRIANGLE);
        Label connectionStatus = LabelCreator.createLabel(ServerController.getInstance().getConnectionStatusProperty(), GUIKonstanten.FONT_SIZE, BottomBarInterface.LABEL_STATUS);


        hBox.getChildren().addAll(
                areaLabel,
                volumeLabel,
                triangleAmountLabel,
                connectionStatus
        );
        return hBox;
    }
}
