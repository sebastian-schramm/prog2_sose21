package view;

import controller.Main;
import controller.PolyederController;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class ModelCreator {
    private static BorderPane borderPane = new BorderPane();
    private static MeshView meshView;
    private static Enum viewPoint = viewPoints.yPos;
    private static SubScene subScene;
    private static boolean isFill = false;
    private static Group root;

    private static Translate pivot = new Translate();
    private static Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    private static Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
    private static Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);

    private static double mousePosX, mousePosY = 0;

    public static void createContent(TriangleMesh triangleMesh) throws Exception {

    }

    public static void createContent(Stage stage) {
        borderPane.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(0), Insets.EMPTY)));

//        TriangleMesh triangleMesh = PolyederController.getInstance().getMesh();
//        meshView = new MeshView(PolyederController.getInstance().getMesh());
//        meshView.getTransforms().addAll(rotateZ, rotateY, rotateX);
//
//        meshView.setMaterial(new PhongMaterial(Color.RED));
////        meshView.setDrawMode(DrawMode.LINE);
//        meshView.setDrawMode(DrawMode.FILL);
//        meshView.setCullFace(CullFace.NONE);
//
//        camera = new PerspectiveCamera(true);
//        camera.setNearClip(0.1);
//        camera.setFarClip(100000);
//        camera.setTranslateZ(cameraTransalteZ);
//
//        Group root = new Group();
//        root.getChildren().add(camera);
//        root.getChildren().add(meshView);
//
//        SubScene subScene = new SubScene(root, 1280, 720, true, SceneAntialiasing.BALANCED);
////        SubScene subScene = new SubScene(root, 1280, 720);
//        subScene.setFill(Color.TRANSPARENT);
//        subScene.setCamera(camera);

//        TriangleMesh triangleMesh = PolyederController.getInstance().getMesh();
        meshView = new MeshView(PolyederController.getInstance().getMesh());
        meshView.setMaterial(new PhongMaterial(Color.RED));
        meshView.setDrawMode(DrawMode.LINE);
//        meshView.setDrawMode(DrawMode.FILL);
        meshView.setCullFace(CullFace.NONE);

        // Create and position camera
        PerspectiveCamera camera = setUpCamera();

        // Build the Scene Graph
        root = setUpRoot(camera, meshView, stage);

        // Use a SubScene
        subScene = new SubScene(root, 1280, 720, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);

        borderPane.setCenter(subScene);
    }

    public static BorderPane getModelCreatorPane() {
        return borderPane;
    }

    public static void handleMouseEvents(Scene scene, Stage stage) {
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent me) -> {
            double dx = (mousePosX - me.getSceneX());
            double dy = (mousePosY - me.getSceneY());
            if (me.isPrimaryButtonDown()) {
                xRotate.setAngle(xRotate.getAngle() + (dy / 10 * 360) * (Math.PI / 180));
                yRotate.setAngle(yRotate.getAngle() + (dx / 10 * -360) * (Math.PI / 180));
            }
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });




        /*scene.setOnScroll((ScrollEvent.SCROLL, event -> {
            root.translateZProperty().set(root.getTranslateZ() + event.getDeltaY());
            System.out.println(root.getTranslateZ() + event.getDeltaY());
        });*/
    }

    public static void showFill()
    {
        meshView.setDrawMode(DrawMode.FILL);
        isFill = true;
    }

    public static boolean isFill()
    {
        return isFill;
    }

    public static void showLine()
    {
        meshView.setDrawMode(DrawMode.LINE);
        isFill = false;
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
        xRotate.setAngle(x);
        yRotate.setAngle(y);
        zRotate.setAngle(z);
    }

    public static enum viewPoints {
        xPos,
        xNeg,
        yPos,
        yNeg,
        zPos,
        zNeg;
    }

    private static PerspectiveCamera setUpCamera ()
    {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (
                pivot,
                yRotate,
                xRotate,
                zRotate,
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(0, 0, -50)
        );
        camera.setFarClip(100000);
        return camera;
    }

    private static Group setUpRoot (PerspectiveCamera camera, MeshView meshview, Stage stage)
    {
        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(meshview);

        root.getChildren().stream()
                .filter(node -> !(node instanceof Camera))
                .forEach(node ->
                        node.setOnMouseClicked(event -> {
                            pivot.setX(node.getTranslateX());
                            pivot.setY(node.getTranslateY());
                            pivot.setZ(node.getTranslateZ());
                        })
                );

        stage.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            //TODO Nicht fertig

            double deltaY = (scrollEvent.getDeltaY())/2;
            camera.setTranslateX(camera.getTranslateX()+deltaY/2);
            camera.setTranslateZ(camera.getTranslateZ() + deltaY);
            System.out.println(camera.getTranslateZ() + deltaY);
        });

        return root;
    }


}
