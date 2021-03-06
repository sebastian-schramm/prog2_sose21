package controller;

import javafx.scene.layout.StackPane;

public class MouseController {

    double mousePosX, mousePosY;
    double mouseOldX, mouseOldY;
    double mouseDeltaX, mouseDeltaY;

    public void init() {

    }

    private MouseController() {

    }

    public void handleMouseEvents(StackPane scene) {

        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();

            ModelController.getInstance().mousePressed(mousePosX, mousePosY);
            ServerController.getInstance().sendMessage("setOnMousePressed;" + mousePosX + ";" + mousePosY);
        });

        scene.setOnMouseDragged(me -> {

            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            if (me.isPrimaryButtonDown()) {
                ModelController.getInstance().rotateWorld(mouseDeltaX, mouseDeltaY);
                ServerController.getInstance().sendMessage("setOnMouseDragged;" + mouseDeltaX + ";" + mouseDeltaY);
            } else if (me.isSecondaryButtonDown()) {
                ModelController.getInstance().moveWorld(mousePosX, mousePosY);
            }
        });

        scene.setOnScroll(me -> {
            ModelController.getInstance().zoomCamera(me.getDeltaY());
        });
    }

    public static MouseController getInstance() {
        return MouseControllerHolder.INSTANCE;
    }

    private static class MouseControllerHolder {

        private static final MouseController INSTANCE = new MouseController();
    }
}
