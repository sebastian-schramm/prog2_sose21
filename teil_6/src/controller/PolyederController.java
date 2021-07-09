package controller;

import model.Polyeder;

public class PolyederController {

    private Polyeder polyeder;

    private PolyederController() {
        polyeder = new Polyeder();
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