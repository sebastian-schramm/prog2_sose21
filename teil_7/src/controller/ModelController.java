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
import javafx.scene.transform.Transform;
import model.interfaces.GUIKonstanten;
import model.interfaces.ModelInterface;
import model.interfaces.ServerInterface;
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

    private static PhongMaterial createMaterial(Color diffuseColor, Color specularColor) {
        PhongMaterial material = new PhongMaterial(diffuseColor);
        material.setSpecularColor(specularColor);
        return material;
    }

    public static ModelController getInstance() {
        return ModelControllerHolder.INSTANCE;
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
        camera.setTranslateZ(-object.getBoundsInLocal().getHeight() - object.getBoundsInLocal().getDepth() - object.getBoundsInLocal().getWidth());

        object.setMaterial(new PhongMaterial(Color.BLUE));
        setDrawModeFill(isFill.getValue());
        object.setCullFace(CullFace.NONE);

        STL_MODEL_XFORM.getChildren().clear();
        STL_MODEL_XFORM.getChildren().add(object);
        STL_MODEL_XFORM.setVisible(true);
    }

    public void centerModel() {
        object.setTranslateX(-object.getBoundsInLocal().getCenterX());
        object.setTranslateY(-object.getBoundsInLocal().getCenterY());
        object.setTranslateZ(-object.getBoundsInLocal().getCenterZ());
    }

    public void rotateWorld(Double mouseDeltaX, Double mouseDeltaY) {
        WORLD_XFORM.addRotation(mouseDeltaX * ModelInterface.MOUSE_SPEED * ModelInterface.ROTATION_SPEED, Rotate.Y_AXIS);
        WORLD_XFORM.addRotation(mouseDeltaY * ModelInterface.MOUSE_SPEED * ModelInterface.ROTATION_SPEED, Rotate.X_AXIS);
    }

    public void rotateWorld(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        WORLD_XFORM.addRotation(mxx, mxy, mxz, tx, myx, myy, myz, ty, mzx, mzy, mzz, tz);
    }

    public Transform getWorldAffine() {
        return WORLD_XFORM.getTransforms().get(0);
    }

    public String getAffineToString() {
        return getWorldAffine().getMxx() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getMxy() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getMxz() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getTx() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getMyx() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getMyy() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getMyz() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getTy() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getMzx() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getMzy() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getMzz() + ServerInterface.MESSAGE_TRENNUNG + getWorldAffine().getTz();
    }

    public String getTranslationString() {
        return STL_MODEL_XFORM.getT().getX() + ServerInterface.MESSAGE_TRENNUNG + STL_MODEL_XFORM.getT().getY() + ServerInterface.MESSAGE_TRENNUNG + STL_MODEL_XFORM.getT().getZ();
    }

    public void translate(Point3D axis, double mouseDelta) {
        STL_MODEL_XFORM.translate(axis.multiply(mouseDelta * ModelInterface.MOUSE_SPEED * ModelInterface.ROTATION_SPEED));
    }

    public void translate(double x, double y, double z) {
        STL_MODEL_XFORM.translate(x, y, z);
    }

    public void resetRotation() {
        WORLD_XFORM.reset();
    }

    public void zoomCamera(Double mouseDeltaY) {
        double newZ = camera.getTranslateZ() + mouseDeltaY * ModelInterface.MOUSE_SPEED * ModelInterface.ZOOM_SPEED;
        if (newZ <= 0)
            camera.setTranslateZ(newZ);
    }

    public void setDrawModeFill(Boolean isFill) {
        if (object != null) {
            if (isFill)
                object.setDrawMode(DrawMode.FILL);
            else
                object.setDrawMode(DrawMode.LINE);
        }
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

    private static class ModelControllerHolder {
        private static final ModelController INSTANCE = new ModelController();
    }
}
