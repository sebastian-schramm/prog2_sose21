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

    /**
     * Privater Konstruktor der PolyederController Klasse, um diese Controller Klasse als Singelton zu realisieren.
     * Ausserdem werden im privaten Konstruktor die privaten Attribute des Objekts initialisiert.
     **/
    private PolyederController() {
        this.polyeder = new Polyeder();
        this.surfaceProperty.set(0.0);
        this.volumeProperty.set(0.0);
        this.triangleAmountProperty.set(0);
    }

    /**
     * Die getInstance() Methode gibt eine Instanz der PolyederController Klasse zurueck.
     *
     * @return Instanz wird zurueckgegeben.
     **/
    public static PolyederController getInstance() {
        return PolyederControllerHolder.INSTANCE;
    }

    /**
     * Die getPolyeder() Methode gibt ein Objekt der Polyeder Klasse zurueck.
     *
     * @return polyeder - Objekt der Polyeder Klasse wird zurueckgegeben.
     **/
    public Polyeder getPolyeder() {
        return polyeder;
    }

    /**
     * Die getStage() Methode setzt das GUI Attribut Stage.
     *
     * @param stage Die Stage, die verwendet werden soll für den Polyeder.
     **/
    public void setStage(Stage stage) {
        PolyederController.stage = stage;
    }

    /**
     * Die loadFile() Methode wird verwendet um die Informationen aus dem Datei Auswaehlen Dialog, dem Polyeder hinzuzufuegen
     * und diesen zu erstellem.
     *
     * @param file Die STL-Datei, die eingelsen werden soll.
     **/
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

    /**
     * Die updateGuiProperties() Methode wird verwendet um die Informationen ueber den Polyeder, der GUI mitzuteilen und
     * diese entsprechend zu aktualisieren.
     *
     * @param filename Der Name der STL-Datei, die in der Applikation dargestellt werden soll.
     **/
    public void updateGuiProperties(String filename) {
        getTriangleAmountProperty().setValue(polyeder.getTriangleList().size());
        getVolumeProperty().set(Math.round(polyeder.getVolume() * AllgemeineKonstanten.ROUND_KOMMASTELLE) / AllgemeineKonstanten.ROUND_KOMMASTELLE);
        getSurfaceProperty().set(Math.round(polyeder.getArea() * AllgemeineKonstanten.ROUND_KOMMASTELLE) / AllgemeineKonstanten.ROUND_KOMMASTELLE);
        stage.setTitle(GUIKonstanten.MY_TITLE + filename);
    }

    /**
     * Die getSurfaceProperty() Methode gibt eine GUI Property zurueck, in der der Oberflaecheninhalt des Polyeders steht
     *
     * @return surfaceProperty - Attribut des Objekts.
     **/
    public DoubleProperty getSurfaceProperty() {
        return this.surfaceProperty;
    }

    /**
     * Die getMesh() Methode generiert und gibt ein TriangleMesh zurueck, welches den Koerper des Polyeders in der GUI repraesentiert.
     *
     * @return mesh - Ist das Trianglemesh des Polyeders.
     **/
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

    /**
     * Die getVolumeProperty() Methode gibt eine GUI Property zurueck, in der das Volumen des Polyeders steht
     *
     * @return volumeProperty - Attribut des Objekts.
     **/
    public DoubleProperty getVolumeProperty() {
        return this.volumeProperty;
    }

    /**
     * Die getTriangleAmountProperty() Methode gibt eine GUI Property zurueck, in der die Anzahl der Dreiecke des Polyeders steht.
     *
     * @return volumeProperty - IntegerProperty Attribut des Objekts.
     **/
    public IntegerProperty getTriangleAmountProperty() {
        return this.triangleAmountProperty;
    }

    /**
     * Private Klasse, die die Instanz der PolyederController Klasse erstellt.
     * */
    private static class PolyederControllerHolder {
        private static final PolyederController INSTANCE = new PolyederController();
    }

}