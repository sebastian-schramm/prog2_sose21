package controller;

import javafx.scene.layout.StackPane;

public class MouseController {

    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private double mouseDeltaX, mouseDeltaY;
    private long nanoSec = System.currentTimeMillis();

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
//            ServerController.getInstance().sendMessage("setOnMousePressed;" + mousePosX + ";" + mousePosY);
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
                if (nanoSec + 20 < System.currentTimeMillis()) {
                    ServerController.getInstance().sendMessage("setOnMouseDragged;" + ModelController.getInstance().getAffine().get(0).getMxx() + ";" + ModelController.getInstance().getAffine().get(0).getMxy() + ";" + ModelController.getInstance().getAffine().get(0).getMxz() + ";" + ModelController.getInstance().getAffine().get(0).getTx() + ";" + ModelController.getInstance().getAffine().get(0).getMyx() + ";" + ModelController.getInstance().getAffine().get(0).getMyy() + ";" + ModelController.getInstance().getAffine().get(0).getMyz() + ";" + ModelController.getInstance().getAffine().get(0).getTy() + ";" + ModelController.getInstance().getAffine().get(0).getMzx() + ";" + ModelController.getInstance().getAffine().get(0).getMzy() + ";" + ModelController.getInstance().getAffine().get(0).getMzz() + ";" + ModelController.getInstance().getAffine().get(0).getTz());
                    nanoSec = System.currentTimeMillis();
                }
            } else if (me.isSecondaryButtonDown()) {
//                ModelController.getInstance().resetRotation();
//                ModelController.getInstance().moveWorld(mousePosX, mousePosY);
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
