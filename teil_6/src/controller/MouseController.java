package controller;

public class MouseController {

    public void init() {

    }

    private MouseController() {

    }

    public static MouseController getInstance() {
        return MouseController.MouseControllerHolder.INSTANCE;
    }

    private static class MouseControllerHolder {
        private static final MouseController INSTANCE = new MouseController();
    }
}
