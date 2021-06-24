package controller;


import javafx.scene.shape.TriangleMesh;
import model.Polyeder;
import utilities.Parser;

import java.io.File;

public class PolyederController {


    private Polyeder polyeder = null;

    public void init(String dateiName) {
        long time = System.currentTimeMillis();
//        this.polyeder = new Polyeder(Parser.ladeStlAusDatei(dateiName));
        System.out.println("Dauer des einlesens : " + (System.currentTimeMillis()-time));

        time = System.currentTimeMillis();
        System.out.println("Area : " + this.polyeder.getSurface(false));
//        System.out.println("Fläche : " + this.polyeder.getSurface(true));
        System.out.println("Volume : " + this.polyeder.getVolume());
        System.out.println("Time Parallel " + (System.currentTimeMillis()-time));

        System.out.println("-----------------------------------------");
    }

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
        polyeder = new Polyeder(Parser.ladeStlAusDatei(file));
        System.out.println("Area : " + this.polyeder.getSurface(false));
        System.out.println("Volume : " + this.polyeder.getVolume());
//        stlImporter.read(file);
    }

    public TriangleMesh getMesh() {
        return polyeder.getMesh();
//        return stlImporter.getImport();
    }

}