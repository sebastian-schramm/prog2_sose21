package controller;

import model.Polyeder;
import utilities.Parser;



public class PolyederController {

    private Polyeder polyeder = null;

    public void init(String dateiName) {
        long time;
        time = System.currentTimeMillis();
        this.polyeder = new Polyeder(Parser.ladeStlAusDatei(dateiName));

        System.out.println(time);
        time = System.currentTimeMillis();

        System.out.println(this.polyeder.getSurfaceSerial());
        System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        System.out.println(this.polyeder.getSurfaceSerial());
        System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        System.out.println(this.polyeder.getSurfaceSerial());
        System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        System.out.println(this.polyeder.getSurfaceSerial());
        System.out.println(System.currentTimeMillis()-time);

        System.out.println("-----------------------------------------");
        time = System.currentTimeMillis();
        System.out.println(this.polyeder.getSurfaceThreads());
        System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        System.out.println(this.polyeder.getSurfaceThreads());
        System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        System.out.println(this.polyeder.getSurfaceThreads());
        System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        System.out.println(this.polyeder.getSurfaceThreads());
        System.out.println(System.currentTimeMillis()-time);

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