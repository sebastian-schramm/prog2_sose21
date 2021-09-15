package controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import model.Polyeder;
import model.interfaces.AllgemeineKonstanten;
import model.interfaces.GUIKonstanten;
import model.interfaces.ServerInterface;
import utilities.Parser;
import view.AlertMessage;

import java.io.File;
/**
 * PolyederController Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class PolyederController {

    private static Stage stage;
    private final DoubleProperty surfaceProperty = new SimpleDoubleProperty();
    private final DoubleProperty volumeProperty = new SimpleDoubleProperty();
    private final IntegerProperty triangleAmountProperty = new SimpleIntegerProperty();
    private final Polyeder polyeder;

    private PolyederController() {
        this.polyeder = new Polyeder();
        this.surfaceProperty.set(0.0);
        this.volumeProperty.set(0.0);
        this.triangleAmountProperty.set(0);
    }

    public static PolyederController getInstance() {
        return PolyederControllerHolder.INSTANCE;
    }

    public Polyeder getPolyeder() {
        return polyeder;
    }

    public void setStage(Stage stage) {
        PolyederController.stage = stage;
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
                        ServerController.getInstance().sendString(ServerInterface.MESSAGE_UPDATE_GUI_ELEMENTS + ServerInterface.MESSAGE_TRENNUNG + file.getName());

                        ModelController.getInstance().buildModel();
                    });
                }
            };
            loadingThread.start();
        }
    }

    public void updateGuiProperties(String filename) {
        getTriangleAmountProperty().setValue(polyeder.getTriangleList().size());
        getVolumeProperty().set(Math.round(polyeder.getVolume() * AllgemeineKonstanten.ROUND_KOMMASTELLE) / AllgemeineKonstanten.ROUND_KOMMASTELLE);
        getSurfaceProperty().set(Math.round(polyeder.getArea() * AllgemeineKonstanten.ROUND_KOMMASTELLE) / AllgemeineKonstanten.ROUND_KOMMASTELLE);
        stage.setTitle(GUIKonstanten.MY_TITLE + filename);
    }

    public DoubleProperty getSurfaceProperty() {
        return this.surfaceProperty;
    }

    public TriangleMesh getMesh() {
        TriangleMesh mesh = new TriangleMesh();

        int faceCnt = 0;
        for (int x = 0; x < polyeder.getTriangleList().size(); x++) {
            for (int y = 0; y < 3; y++) {
                mesh.getTexCoords().addAll((float) polyeder.getTriangleList().get(x).getNormal().getX());
                mesh.getTexCoords().addAll((float) polyeder.getTriangleList().get(x).getNormal().getY());
                mesh.getTexCoords().addAll((float) polyeder.getTriangleList().get(x).getNormal().getZ());
            }

            for (int y = 0; y < 3; y++) {
                mesh.getPoints().addAll((float) polyeder.getTriangleList().get(x).getVertex(y).getX());
                mesh.getPoints().addAll((float) polyeder.getTriangleList().get(x).getVertex(y).getY());
                mesh.getPoints().addAll((float) polyeder.getTriangleList().get(x).getVertex(y).getZ());
            }

            mesh.getFaces().addAll(faceCnt, faceCnt, faceCnt + 1, faceCnt + 1, faceCnt + 2, faceCnt + 2);
            faceCnt += 3;
            if (x % 250000 == 0) {
                System.gc();
            }
        }
        return mesh;
    }

    public DoubleProperty getVolumeProperty() {
        return this.volumeProperty;
    }

    public IntegerProperty getTriangleAmountProperty() {
        return this.triangleAmountProperty;
    }

    private static class PolyederControllerHolder {
        private static final PolyederController INSTANCE = new PolyederController();
    }

}