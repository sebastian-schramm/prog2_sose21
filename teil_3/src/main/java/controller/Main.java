package controller;

public class Main {

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        PolyederController.getInstance().init("LochASCII");
        PolyederController.getInstance().init("Loch");
    }
}
