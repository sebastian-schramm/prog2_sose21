package controller;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.TriangleMesh;
import model.Polyeder;
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



    public void loadFile(File file) {
        if (file != null) {
            polyeder = new Polyeder(Parser.ladeStlAusDatei(file),true);
            System.out.println("Area : " + this.polyeder.getSurface());
            System.out.println("Volume : " + this.polyeder.getVolume());
            setVolumeProperty(this.polyeder.getVolume());
            setSurfaceProperty(this.polyeder.getSurface());
            setTriangleAmountProperty(this.polyeder.getTriangleListSize());
            ModelController.getInstance().getModel().updatePane();
//        stlImporter.read(file);
        }
    }

    public TriangleMesh getMesh() {
        return polyeder.getMesh();
//        return stlImporter.getImport();
    }

}