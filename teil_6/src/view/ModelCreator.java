package view;

import controller.PolyederController;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import model.interfaces.GUIKonstanten;
import utilities.XformBox;

public class ModelCreator {
    private static BorderPane borderPane;
    public static SubScene subScene;

    private final Group root = new Group();
    private MeshView object = new MeshView();
    private final XformBox objectGroup = new XformBox();
    private final XformBox axisGroup = new XformBox();
    private final XformBox world = new XformBox();

    private final XformBox cameraXform = new XformBox();

    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 0.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 0.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 2500000.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;

    private static PerspectiveCamera camera;
    private static Enum viewPoint;

    double mousePosX, mousePosY;
    double mouseOldX, mouseOldY;
    double mouseDeltaX, mouseDeltaY;

    private boolean isFill = true;
    private boolean isAxisVisible = true;

    public ModelCreator() {
        borderPane = new BorderPane();

        camera = new PerspectiveCamera(true);
        root.setDepthTest(DepthTest.ENABLE);

        buildCamera();
        buildAxes();
        world.getChildren().addAll(axisGroup);
        world.getChildren().addAll(objectGroup);
        root.getChildren().add(world);

        subScene = new SubScene(root, GUIKonstanten.WINDOW_SIZE_X, (GUIKonstanten.WINDOW_SIZE_Y-GUIKonstanten.MENUBAR_HEIGHT-GUIKonstanten.BOTTOMBAR_HEIGHT*2), true, SceneAntialiasing.BALANCED);
        handleMouseEvents(subScene, world);
        subScene.setFill(Color.GREY);
        subScene.setCamera(camera);

        borderPane.setCenter(subScene);
    }

    public BorderPane getModelCreatorPane() {
        return borderPane;
    }

    public void updatePane() {
        buildModel();
    }

    private void buildCamera() {
        root.getChildren().add(camera);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.addRotation(CAMERA_INITIAL_X_ANGLE, Rotate.X_AXIS);
        cameraXform.addRotation(CAMERA_INITIAL_Y_ANGLE, Rotate.Y_AXIS);

    }

    private void buildAxes() {
        final PhongMaterial redMaterial = createMaterial(Color.DARKRED, Color.RED);
        final PhongMaterial greenMaterial = createMaterial(Color.DARKGREEN, Color.GREEN);
        final PhongMaterial blueMaterial = createMaterial(Color.DARKBLUE, Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
    }

    private void buildModel() {
        object = new MeshView(PolyederController.getInstance().getMesh());
        object.setMaterial(new PhongMaterial(Color.RED));
        setDrawModeFill(isFill);
        object.setCullFace(CullFace.NONE);

        objectGroup.getChildren().clear();
        objectGroup.getChildren().add(object);
        objectGroup.setVisible(true);
    }

    private PhongMaterial createMaterial(Color diffuseColor, Color specularColor) {
        PhongMaterial material = new PhongMaterial(diffuseColor);
        material.setSpecularColor(specularColor);
        return material;
    }

    private void handleMouseEvents(SubScene scene, final Node root) {
        scene.setOnMousePressed( me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene.setOnMouseDragged( me -> {
            double modifier = 1.0;

            if (me.isControlDown()) {
                modifier = CONTROL_MULTIPLIER;
            }
            if (me.isShiftDown()) {
                modifier = SHIFT_MULTIPLIER;
            }

            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            if (me.isPrimaryButtonDown()) {
                world.addRotation(-mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED, Rotate.Y_AXIS);
                world.addRotation(mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED, Rotate.X_AXIS);
//                objectGroup.addRotation(-mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED, Rotate.Y_AXIS);
//                objectGroup.addRotation(mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED, Rotate.X_AXIS);
            } else if (me.isSecondaryButtonDown()) {
                objectGroup.setTranslateX(objectGroup.getTranslateX() + mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED);
                objectGroup.setTranslateZ(objectGroup.getTranslateZ() - mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED);
//                camera.setTranslateX(camera.getTranslateX() - mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED);
//                camera.setTranslateY(camera.getTranslateY() - mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED);
            }
        });

        scene.setOnScroll(me -> {
            double modifier = 1.0;

            double newZ = camera.getTranslateZ() + me.getDeltaY() * MOUSE_SPEED * modifier;
            if (newZ <= 0)
                camera.setTranslateZ(newZ);
        });
    }

    public static void setViewPoint(viewPoints pos) {
        viewPoint = pos;
        if (viewPoint == viewPoints.xPos)
            setRotate(0, 0, 90);
        else if (viewPoint == viewPoints.xNeg)
            setRotate(0, 0, -90);
        else if (viewPoint == viewPoints.yPos)
            setRotate(90, 0, 0);
        else if (viewPoint == viewPoints.yNeg)
            setRotate(-90, 180, 0);
        else if (viewPoint == viewPoints.zPos)
            setRotate(0, 0, 0);
        else if (viewPoint == viewPoints.zNeg)
            setRotate(0, 180, 0);
    }

    private static void setRotate(double x, double y, double z) {
//        xRotate.setAngle(x);
//        yRotate.setAngle(y);
//        zRotate.setAngle(z);
    }

    public static enum viewPoints {
        xPos,
        xNeg,
        yPos,
        yNeg,
        zPos,
        zNeg;
    }

    public void setDrawModeFill(boolean isFill) {
        if (isFill)
            object.setDrawMode(DrawMode.FILL);
        else
            object.setDrawMode(DrawMode.LINE);

        this.isFill = isFill;
    }

    public boolean isFill()
    {
        return isFill;
    }

    public void setAxisVisible(boolean isVisible) {
        axisGroup.setVisible(isVisible);

        this.isAxisVisible = isVisible;
    }

    public boolean isAxisVisible()
    {
        return isAxisVisible;
    }
}
