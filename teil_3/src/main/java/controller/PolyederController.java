package controller;

import model.Polyeder;
import utilities.Parser;

import java.io.File;

public class PolyederController {

    private Polyeder polyeder = null;

    public void init(String dateiName) {
        this.polyeder = new Polyeder(Parser.ladeStlAusDatei(dateiName));
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