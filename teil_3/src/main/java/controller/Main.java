package controller;

import model.Triangle;

public class Main {

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        PolyederController.getInstance().init("KugelASCII");
//        PolyederController.getInstance().init("KugelBinary");
//        PolyederController.getInstance().init("LochBinary");
//        PolyederController.getInstance().init("LochASCII");
    }
}
