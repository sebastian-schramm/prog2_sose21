package view;

import controller.PolyederController;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

public class Model {

    private MeshView meshView;
    private Box testBox;

    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

    private double mousePosX, mousePosY = 0;
    private static final double AXIS_LENGTH = 250.0;

    public Parent createContent() throws Exception {
        testBox = new Box(10, 10, 10);
        testBox.getTransforms().addAll(rotateZ, rotateY, rotateX);
        testBox.setMaterial(new PhongMaterial(Color.PERU));
        TriangleMesh triangleMesh = PolyederController.getInstance().getPolyeder().getMesh();
        meshView = new MeshView(triangleMesh);
        meshView.getTransforms().addAll(rotateZ, rotateY, rotateX);

        meshView.setMaterial(new PhongMaterial(Color.RED));
        meshView.setDrawMode(DrawMode.LINE);
        meshView.setDrawMode(DrawMode.FILL);

        PerspectiveCamera camera = new PerspectiveCamera(true);
//        camera.getTransforms().addAll(
//                new Rotate(5, Rotate.Y_AXIS),
//                new Rotate(-110, Rotate.X_AXIS),
//                new Translate(0, 0, -80)
//        );
        camera.setNearClip(0.1);
        camera.setFarClip(100000);
        camera.setTranslateZ(-400);

        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(meshView);
        root.getChildren().add(testBox);

        SubScene subScene = new SubScene(root, 1280, 720, true, SceneAntialiasing.BALANCED);
//        SubScene subScene = new SubScene(root, 1280, 720);
        subScene.setFill(Color.TRANSPARENT);
        subScene.setCamera(camera);

        root.setScaleX(1);
        root.setScaleY(1);
        root.setScaleZ(1);

//        SubScene subScene = new SubScene(root, 1280, 720, true,
//                SceneAntialiasing.BALANCED);
//        subScene.setFill(Color.TRANSPARENT);

        return new Group(subScene);
    }

    public void handleMouseEvents(Scene scene) {
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent me) -> {
            double dx = (mousePosX - me.getSceneX()) ;
            double dy = (mousePosY - me.getSceneY());
            if (me.isPrimaryButtonDown()) {
//                meshView.setRotate(dx);
                rotateX.setAngle(rotateX.getAngle() - (dy / testBox.getHeight() * 360) * (Math.PI / 180));
                rotateY.setAngle(rotateY.getAngle() - (dx / testBox.getWidth() * -360) * (Math.PI / 180));

            }
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });
    }
}
