package controller;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import model.Polyeder;
import model.interfaces.GUIKonstanten;
import utilities.Parser;

import java.io.File;

public class PolyederController {

//    private StlMeshImporter stlImporter = new StlMeshImporter();
    private Polyeder polyeder = null;
    private DoubleProperty volumeProperty = new SimpleDoubleProperty(0);
    private DoubleProperty surfaceProperty = new SimpleDoubleProperty(0);
    private IntegerProperty triangleAmountProperty = new SimpleIntegerProperty(0);

    public void init() {
        polyeder = new Polyeder();
    }

    private PolyederController() {

    }

    public static PolyederController getInstance() {
        return PolyederControllerHolder.INSTANCE;
    }

    private static class PolyederControllerHolder {
        private static final PolyederController INSTANCE = new PolyederController();
    }

    public Polyeder getPolyeder() {
        return polyeder;
    }

    public DoubleProperty volumeProperty() {
        return this.volumeProperty;
    }

    public void setVolumeProperty(double volumeProperty) { this.volumeProperty.set(Math.round(volumeProperty*1000.0)/1000.0); }

    public DoubleProperty surfaceProperty() {
        return this.surfaceProperty;
    }

    public void setSurfaceProperty(double surfaceProperty) { this.surfaceProperty.set(Math.round(surfaceProperty*1000.0)/1000.0); }

    public IntegerProperty triangleAmountProperty() { return this.triangleAmountProperty; }

    public void setTriangleAmountProperty (int triangleAmountProperty) { this.triangleAmountProperty.set(triangleAmountProperty); }



    public void loadFile(File file, Stage stage) {
        if (file != null) {
            polyeder = new Polyeder(Parser.ladeStlAusDatei(file),true);
            System.out.println(GUIKonstanten.LABEL_SURFACE + this.polyeder.getSurface());
            System.out.println(GUIKonstanten.LABEL_VOLUME + this.polyeder.getVolume());
            setVolumeProperty(this.polyeder.getVolume());
            setSurfaceProperty(this.polyeder.getSurface());
            setTriangleAmountProperty(this.polyeder.getTriangleListSize());
            System.out.println(file.getName());
            stage.setTitle(GUIKonstanten.MY_TITLE + file.getName());
            ModelController.getInstance().getModel().updatePane();
//        stlImporter.read(file);
        }
    }

    public TriangleMesh getMesh() {
        return polyeder.getMesh();
//        return stlImporter.getImport();
    }

}