package controller;

import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import model.interfaces.ServerInterface;

public class MouseController {

    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private double mouseDeltaX, mouseDeltaY;
    private long nanoSec = System.currentTimeMillis();

    private MouseController() {

    }

    public static MouseController getInstance() {
        return MouseControllerHolder.INSTANCE;
    }

    public void handleMouseEvents(StackPane scene) {

        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
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
                if (nanoSec + ServerInterface.MESSAGE_MILLIS_WAIT < System.currentTimeMillis()) {
                    ServerController.getInstance().sendString(ServerInterface.MESSAGE_SETONMOUSEDRAGGED + ServerInterface.MESSAGE_TRENNUNG + ModelController.getInstance().getAffineToString());
                    nanoSec = System.currentTimeMillis();
                }
            } else if (me.isSecondaryButtonDown()) {
                if (mouseDeltaX != 0) {
                    ModelController.getInstance().translate(Rotate.X_AXIS, mouseDeltaX);
                    if (nanoSec + ServerInterface.MESSAGE_MILLIS_WAIT_TRANSLATE < System.currentTimeMillis()) {
                        ServerController.getInstance().sendString(ServerInterface.MESSAGE_TRANSLATE_X_AXIS + ServerInterface.MESSAGE_TRENNUNG + ModelController.getInstance().getTranslationString());
                        nanoSec = System.currentTimeMillis();
                    }
                }
                if (mouseDeltaY != 0) {
                    ModelController.getInstance().translate(Rotate.Y_AXIS, -mouseDeltaY);
                    if (nanoSec + ServerInterface.MESSAGE_MILLIS_WAIT_TRANSLATE < System.currentTimeMillis()) {
                        ServerController.getInstance().sendString(ServerInterface.MESSAGE_TRANSLATE_Y_AXIS + ServerInterface.MESSAGE_TRENNUNG + ModelController.getInstance().getTranslationString());
                        nanoSec = System.currentTimeMillis();
                    }
                }
            }
        });

        scene.setOnScroll(me -> {
            ModelController.getInstance().zoomCamera(me.getDeltaY());
        });

        scene.setOnMouseReleased(mouseEvent -> {

        });
    }

    private static class MouseControllerHolder {

        private static final MouseController INSTANCE = new MouseController();
    }
}
