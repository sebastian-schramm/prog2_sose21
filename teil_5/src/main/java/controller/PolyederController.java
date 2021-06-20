package controller;

import model.Polyeder;
import utilities.Parser;

public class PolyederController {

    private Polyeder polyeder = null;

    public void init(String dateiName) {
        long time = System.currentTimeMillis();
        this.polyeder = new Polyeder(Parser.ladeStlAusDatei(dateiName));
        System.out.println("Dauer des einlesens : " + (System.currentTimeMillis()-time));

        time = System.currentTimeMillis();
//        System.out.println("Fläche : " + this.polyeder.getSurface(false));
        System.out.println("Fläche : " + this.polyeder.getSurface(true));
        System.out.println("Volumen : " + this.polyeder.getVolume());
        System.out.println("Time Parallel " + (System.currentTimeMillis()-time));

        System.out.println("-----------------------------------------");
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
}