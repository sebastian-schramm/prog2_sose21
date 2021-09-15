package controller;

import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import model.interfaces.ServerInterface;
import model.interfaces.ModelInterface;
/**
 * MouseController Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class MouseController {

    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private double mouseDeltaX, mouseDeltaY;
    private long nanoSec = System.currentTimeMillis();

    /**
     * Privater Konstruktor der MouseController Klasse, um diese Controller Klasse als Singelton zu realisieren.
     * */
    private MouseController() {

    }

    /**
     * Die getInstance() Methode gibt eine Instanz der MouseController Klasse zurueck.
     *
     * @return Instanz wird zurueckgegeben.
     * */
    public static MouseController getInstance() {
        return MouseControllerHolder.INSTANCE;
    }

    /**
     * Die handleMouseEvents() Methode verwaltet die Events, die gefeuert werden wenn man in der GUI die Maus verwendet.
     *
     * @param scene Ist das GUI Element in der die Mouse Events abfeuert.
     * */
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

            if (me.isShiftDown() && me.isControlDown())
                ;
            else if (me.isShiftDown()) {
                mouseDeltaX *= ModelInterface.SHIFT_MULTIPLIER;
                mouseDeltaY *= ModelInterface.SHIFT_MULTIPLIER;
            } else if (me.isControlDown()) {
                mouseDeltaX *= ModelInterface.CONTROL_MULTIPLIER;
                mouseDeltaY *= ModelInterface.CONTROL_MULTIPLIER;
            }

            if (me.isPrimaryButtonDown()) {
                ModelController.getInstance().rotateWorld(Rotate.X_AXIS, mouseDeltaY * ModelInterface.MOUSE_SPEED * ModelInterface.ROTATION_SPEED);
                ModelController.getInstance().rotateWorld(Rotate.Y_AXIS, mouseDeltaX * ModelInterface.MOUSE_SPEED * ModelInterface.ROTATION_SPEED);

                if (nanoSec + ServerInterface.MESSAGE_MILLIS_WAIT < System.currentTimeMillis()) {
                    ServerController.getInstance().sendString(ServerInterface.MESSAGE_SET_ON_MOUSE_DRAGGED + ServerInterface.MESSAGE_TRENNUNG + ModelController.getInstance().getAffineToString());
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
            if (me.isShiftDown() && me.isControlDown())
                ModelController.getInstance().zoomCamera(me.getDeltaY());
            else if (me.isShiftDown()) {
                ModelController.getInstance().zoomCamera(me.getDeltaY() * ModelInterface.SHIFT_MULTIPLIER);
            } else if (me.isControlDown()) {
                ModelController.getInstance().zoomCamera(me.getDeltaY() * ModelInterface.CONTROL_MULTIPLIER);
            } else
                ModelController.getInstance().zoomCamera(me.getDeltaY());
        });

        scene.setOnMouseReleased(mouseEvent -> {

        });
    }

    /**
     * Private Klasse, die die Instanz der MouseController Klasse erstellt.
     * */
    private static class MouseControllerHolder {

        private static final MouseController INSTANCE = new MouseController();
    }
}
