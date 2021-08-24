package controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import model.Polyeder;
import model.interfaces.AllgemeineKonstanten;
import model.interfaces.GUIKonstanten;
import utilities.Parser;
import view.AlertMessage;

import java.io.File;

public class PolyederController {

    private final DoubleProperty surfaceProperty = new SimpleDoubleProperty();
    private final DoubleProperty volumeProperty = new SimpleDoubleProperty();
    private final IntegerProperty triangleAmountProperty = new SimpleIntegerProperty();

    private static Stage stage;
    private Polyeder polyeder;

    private PolyederController() {
        polyeder = new Polyeder();
        this.surfaceProperty.set(0.0);
        this.volumeProperty.set(0.0);
        this.triangleAmountProperty.set(0);
    }

    public Polyeder getPolyeder() {
        return polyeder;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadFile(File file) {
        if (file != null) {
            Thread loadingThread = new Thread() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        Parser.ladeStlAusDatei(file);
                        polyeder.updatePolyederInfo();
                        updateGuiProperties(file.getName());

                        ServerController.getInstance().sendTriangleList(polyeder.getTriangleList());
                        ServerController.getInstance().sendString("updateGUIElements;" + file.getName());

                        ModelController.getInstance().buildModel();
                        AlertMessage.showMessage(GUIKonstanten.LOADING_FILE_COMPLETE);
                    });
                }
            };
            loadingThread.start();
        }
    }

    public void updateGuiProperties(String filename){
        getTriangleAmountProperty().setValue(polyeder.getTriangleList().size());
        getVolumeProperty().set(Math.round(polyeder.getVolume() * AllgemeineKonstanten.ROUND_KOMMASTELLE)/AllgemeineKonstanten.ROUND_KOMMASTELLE);
        getSurfaceProperty().set(Math.round(polyeder.getArea() * AllgemeineKonstanten.ROUND_KOMMASTELLE)/AllgemeineKonstanten.ROUND_KOMMASTELLE);
        stage.setTitle(GUIKonstanten.MY_TITLE + filename);
    }

    public DoubleProperty getSurfaceProperty() {
        return this.surfaceProperty;
    }

    public DoubleProperty getVolumeProperty() {
        return this.volumeProperty;
    }

    public IntegerProperty getTriangleAmountProperty() {
        return this.triangleAmountProperty;
    }

    public static PolyederController getInstance() {
        return PolyederControllerHolder.INSTANCE;
    }

    private static class PolyederControllerHolder {
        private static final PolyederController INSTANCE = new PolyederController();
    }

}