package controller;

import javafx.scene.chart.Axis;
import javafx.scene.input.PickResult;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import utilities.ClientThread;

public class MouseController {

    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private double mouseDeltaX, mouseDeltaY;
    private long nanoSec = System.currentTimeMillis();
    private long nano1Sec = System.currentTimeMillis();
    private long nano2Sec = System.currentTimeMillis();

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
            PickResult pr = me.getPickResult();


            ModelController.getInstance().mousePressed(mousePosX, mousePosY);
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
                if (nanoSec + 20 < System.currentTimeMillis() && ClientThread.getSocket() != null) {
                    ServerController.getInstance().sendString("setOnMouseDragged;" + ModelController.getInstance().getAffineString());
                    nanoSec = System.currentTimeMillis();
                }
            } else if (me.isSecondaryButtonDown()) {
                if (mouseDeltaX != 0) {
                    ModelController.getInstance().translate(Rotate.X_AXIS, mouseDeltaX);
                    if (nanoSec + 20 < System.currentTimeMillis() && ClientThread.getSocket() != null) {
                        ServerController.getInstance().sendString("translateXAxis;" + ModelController.getInstance().getTranslationString());
                        nanoSec = System.currentTimeMillis();
                    }
                }
                if (mouseDeltaY != 0) {
                    ModelController.getInstance().translate(Rotate.Y_AXIS, -mouseDeltaY);
                    if (nanoSec + 20 < System.currentTimeMillis() && ClientThread.getSocket() != null) {
                        ServerController.getInstance().sendString("translateYAxis;" + ModelController.getInstance().getTranslationString());
                        nanoSec = System.currentTimeMillis();
                    }
                }
            }
        });

        scene.setOnScroll(me -> {
            ModelController.getInstance().zoomCamera(me.getDeltaY());
        });

        scene.setOnMouseReleased(mouseEvent -> {
            if (ModelController.getInstance().getIsPicking()){
                ModelController.getInstance().setPicking(false);
            }
        });
    }

    public static MouseController getInstance() {
        return MouseControllerHolder.INSTANCE;
    }

    private static class MouseControllerHolder {

        private static final MouseController INSTANCE = new MouseController();
    }
}
