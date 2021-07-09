package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import model.interfaces.GUIKonstanten;
import model.interfaces.ModelInterface;
import utilities.CreateAnchor;
import utilities.XformBox;

public class ModelController {

    private static final Group ROOT = new Group();
    private static final XformBox STL_MODEL_XFORM = new XformBox();
    private static final XformBox AXIS_MODEL_XFORM = new XformBox();
    private static final XformBox WORLD_XFORM = new XformBox();
    private static final XformBox CAMERA_XFORM = new XformBox();

    private static final PhongMaterial redMaterial = createMaterial(Color.DARKRED, Color.RED);
    private static final PhongMaterial greenMaterial = createMaterial(Color.DARKGREEN, Color.GREEN);
    private static final PhongMaterial blueMaterial = createMaterial(Color.DARKBLUE, Color.BLUE);

    private static StackPane stackPane;
    private static SubScene subScene;
    private static PerspectiveCamera camera;
    private static MeshView object;
    private static Point3D vecIni, vecPos;

    private static BooleanProperty isFill = new SimpleBooleanProperty(true);
    private static BooleanProperty isAxisVisible = new SimpleBooleanProperty(true);

    private ModelController() {
        stackPane = new StackPane();
        isFill = new SimpleBooleanProperty(true);
        isAxisVisible = new SimpleBooleanProperty(true);

        stackPane.setStyle(GUIKonstanten.SUBSCENE_COLOR);

        CreateAnchor.setConstraintsZero(stackPane);

        camera = new PerspectiveCamera(true);
        ROOT.setDepthTest(DepthTest.ENABLE);

        buildCamera();
        buildAxes();

        WORLD_XFORM.getChildren().addAll(AXIS_MODEL_XFORM);
        WORLD_XFORM.getChildren().addAll(STL_MODEL_XFORM);
        ROOT.getChildren().add(WORLD_XFORM);

        subScene = new SubScene(ROOT, GUIKonstanten.WINDOW_SIZE_X, GUIKonstanten.WINDOW_SIZE_Y, true, SceneAntialiasing.BALANCED);
        subScene.heightProperty().bind(stackPane.heightProperty());
        subScene.widthProperty().bind(stackPane.widthProperty());
        subScene.setCamera(camera);

        stackPane.getChildren().add(subScene);
    }

    private void buildCamera() {
        ROOT.getChildren().add(camera);
        CAMERA_XFORM.getChildren().add(camera);
        camera.setNearClip(ModelInterface.CAMERA_NEAR_CLIP);
        camera.setFarClip(ModelInterface.CAMERA_FAR_CLIP);
        camera.setTranslateZ(ModelInterface.CAMERA_INITIAL_DISTANCE);
        camera.setTranslateZ(ModelInterface.CAMERA_INITIAL_DISTANCE);
        camera.setFieldOfView(ModelInterface.FIELD_OF_VEW);
        CAMERA_XFORM.addRotation(ModelInterface.CAMERA_INITIAL_X_ANGLE, Rotate.X_AXIS);
        CAMERA_XFORM.addRotation(ModelInterface.CAMERA_INITIAL_Y_ANGLE, Rotate.Y_AXIS);
//        WORLD_XFORM.addRotation(-65.0, Rotate.X_AXIS);
//        WORLD_XFORM.addRotation(-15.0, Rotate.Y_AXIS);
    }

    private void buildAxes() {
        final Box xAxis = new Box(ModelInterface.AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, ModelInterface.AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, ModelInterface.AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        AXIS_MODEL_XFORM.getChildren().addAll(xAxis, yAxis, zAxis);
        AXIS_MODEL_XFORM.setVisible(true);
    }

    public void buildModel() {
        object = new MeshView(PolyederController.getInstance().getPolyeder().getMesh());

        centerModel();
        camera.setTranslateZ(-object.getBoundsInLocal().getHeight()-object.getBoundsInLocal().getDepth()-object.getBoundsInLocal().getWidth());

        object.setMaterial(new PhongMaterial(Color.BLUE));
        setDrawModeFill(isFill.getValue());
        object.setCullFace(CullFace.NONE);

        STL_MODEL_XFORM.getChildren().clear();
        STL_MODEL_XFORM.getChildren().add(object);
        STL_MODEL_XFORM.setVisible(true);
    }

    private static PhongMaterial createMaterial(Color diffuseColor, Color specularColor) {
        PhongMaterial material = new PhongMaterial(diffuseColor);
        material.setSpecularColor(specularColor);
        return material;
    }

    public void centerModel() {
        object.setTranslateX(-object.getBoundsInLocal().getCenterX());
        object.setTranslateY(-object.getBoundsInLocal().getCenterY());
        object.setTranslateZ(-object.getBoundsInLocal().getCenterZ());
    }

    public void centerModelOnPane() {
        object.setTranslateX(-object.getBoundsInLocal().getCenterX());
        object.setTranslateY(-object.getBoundsInLocal().getCenterY());
        object.setTranslateZ(-object.getBoundsInLocal().getCenterZ() + object.getBoundsInLocal().getCenterZ());
    }

    public void mousePressed(Double mousePosX, Double mousePosY) {
        vecIni = unProjectDirection(mousePosX, mousePosY, subScene.getWidth(), subScene.getHeight());
    }

    public void rotateWorld(Double mouseDeltaX, Double mouseDeltaY) {
        WORLD_XFORM.addRotation(mouseDeltaX * ModelInterface.MOUSE_SPEED * ModelInterface.ROTATION_SPEED, Rotate.Y_AXIS);
        WORLD_XFORM.addRotation(mouseDeltaY * ModelInterface.MOUSE_SPEED * ModelInterface.ROTATION_SPEED, Rotate.X_AXIS);
    }

    public void moveWorld(Double mousePosX, Double mousePosY) {
        vecPos = unProjectDirection(mousePosX, mousePosY, subScene.getWidth(), subScene.getHeight());
        Point3D p = vecPos.subtract(vecIni).multiply(-camera.getTranslateZ());
        STL_MODEL_XFORM.getTransforms().add(new Translate(p.getX(),p.getY(),p.getZ()));
        vecIni=vecPos;

//        STL_MODEL_XFORM.setTranslateX(STL_MODEL_XFORM.getTranslateX() + mouseDeltaX);
//        STL_MODEL_XFORM.setTranslateZ(STL_MODEL_XFORM.getTranslateZ() - mouseDeltaY);
//        WORLD_XFORM.setTranslateX(mouseDeltaX);
//        WORLD_XFORM.setTranslateY(mouseDeltaY);
    }
    public Point3D unProjectDirection(double sceneX, double sceneY, double sWidth, double sHeight) {
        double tanHFov = Math.tan(Math.toRadians(camera.getFieldOfView()) * 0.5f);
        Point3D vMouse = new Point3D(tanHFov*(2*sceneX/sWidth-1), tanHFov*(2*sceneY/sWidth-sHeight/sWidth), 1);

        Point3D result = localToSceneDirection(vMouse);
        return result.normalize();
    }

    public Point3D localToScene(Point3D pt) {
        Point3D res = camera.localToParentTransformProperty().get().transform(pt);
        if (camera.getParent() != null) {
            res = camera.getParent().localToSceneTransformProperty().get().transform(res);
        }
        return res;
    }

    public Point3D localToSceneDirection(Point3D dir) {
        Point3D res = localToScene(dir);
        return res.subtract(localToScene(new Point3D(0, 0, 0)));
    }

    public void zoomCamera(Double mouseDeltaY) {
        double modifier = 1.0;

        double newZ = camera.getTranslateZ() + mouseDeltaY * ModelInterface.MOUSE_SPEED * modifier;
        if (newZ <= 0)
            camera.setTranslateZ(newZ);
    }

    public void setDrawModeFill(Boolean isFill) {
        if (isFill)
            object.setDrawMode(DrawMode.FILL);
        else
            object.setDrawMode(DrawMode.LINE);

        ModelController.isFill.set(isFill);
    }

    public BooleanProperty isFill() {
        return isFill;
    }

    public void setAxisVisible(Boolean isAxisVisible) {
        AXIS_MODEL_XFORM.setVisible(isAxisVisible);

        ModelController.isAxisVisible.set(isAxisVisible);
    }

    public BooleanProperty isAxisVisible() {
        return isAxisVisible;
    }

    public StackPane getModelStackPane() {
        return stackPane;
    }

    public static ModelController getInstance() {
        return ModelControllerHolder.INSTANCE;
    }

    private static class ModelControllerHolder {
        private static final ModelController INSTANCE = new ModelController();
    }
}
