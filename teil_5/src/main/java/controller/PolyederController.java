package controller;


import javafx.scene.shape.TriangleMesh;
import model.Polyeder;
import utilities.Parser;

import java.io.File;

public class PolyederController {

//    private StlMeshImporter stlImporter = new StlMeshImporter();
    private Polyeder polyeder = null;

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
            polyeder = new Polyeder(Parser.ladeStlAusDatei(file));
            System.out.println("Area : " + this.polyeder.getSurface(false));
            System.out.println("Volume : " + this.polyeder.getVolume());
            ModelController.getInstance().getModel().updatePane();
//        stlImporter.read(file);
        }
    }

    public TriangleMesh getMesh() {
        return polyeder.getMesh();
//        return stlImporter.getImport();
    }

}