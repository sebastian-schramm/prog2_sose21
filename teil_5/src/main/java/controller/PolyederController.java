package controller;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.TriangleMesh;
import model.Polyeder;
import utilities.Parser;

import java.io.File;

public class PolyederController {

//    private StlMeshImporter stlImporter = new StlMeshImporter();
    private Polyeder polyeder = null;
    private IntegerProperty currentArea = new SimpleIntegerProperty(0);
    private IntegerProperty currentVolume = new SimpleIntegerProperty(0);

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

    public void loadFile(File file) {
        if (file != null) {
            polyeder = new Polyeder(Parser.ladeStlAusDatei(file), true);
            System.out.println("Area : " + this.polyeder.getSurface());
            System.out.println("Volume : " + this.polyeder.getVolume());
            setCurrentArea((int) this.polyeder.getSurface());
            setCurrentVolume((int) this.polyeder.getVolume());
            ModelController.getInstance().getModel().updatePane();
//        stlImporter.read(file);
        }
    }

    public void setCurrentArea(int value)
    {
        this.currentArea.set(value);
    }

    public IntegerProperty getCurrentArea() {
        return this.currentArea;
    }

    public void setCurrentVolume(int value)
    {
        this.currentVolume.set(value);
    }

    public IntegerProperty getCurrentVolume() {
        return this.currentVolume;
    }

    public TriangleMesh getMesh() {
        return polyeder.getMesh();
//        return stlImporter.getImport();
    }

}