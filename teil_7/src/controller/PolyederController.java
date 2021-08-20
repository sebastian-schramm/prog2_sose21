package controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import model.Polyeder;
import model.interfaces.GUIKonstanten;
import utilities.Parser;
import view.AlertMessage;

import java.io.File;

public class PolyederController {

    private final DoubleProperty surfaceProperty = new SimpleDoubleProperty();
    private final DoubleProperty volumeProperty = new SimpleDoubleProperty();
    private final IntegerProperty triangleAmountProperty = new SimpleIntegerProperty();

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

    public void loadFile(File file, Stage stage) {
        boolean threading = true;
        if (file != null) {
            Thread loadingThread = new Thread() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        //TODO Message das eine Datei geladen wird
                        Parser.ladeStlAusDatei(file);

                        if (threading)
                            polyeder.surfaceThreads();
                        else
                            polyeder.surfaceSerial();

                        polyeder.sortTriangles();
                        polyeder.calcSurface();
                        polyeder.calcVolume();

                        stage.setTitle(GUIKonstanten.MY_TITLE + file.getName());
                        ModelController.getInstance().buildModel();
                        //TODO Alert hier einbinden
                        AlertMessage.showMessage(GUIKonstanten.LOADING_FILE_COMPLETE);
                    });
                }
            };
            loadingThread.start();
        }
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