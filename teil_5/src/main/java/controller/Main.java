package controller;

import model.Triangle;

public class Main {

    public static void main(String[] args) {
        init();
    }

    private static void init() {
//        PolyederController.getInstance().init("KugelASCII");
//        PolyederController.getInstance().init("KugelBinary");
//        PolyederController.getInstance().init("LochBinary");
//        PolyederController.getInstance().init("LochASCII");
        PolyederController.getInstance().init("PyramideASCII");
        PolyederController.getInstance().init("PyramideBinary");
        PolyederController.getInstance().init("untitled_BIG");
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
