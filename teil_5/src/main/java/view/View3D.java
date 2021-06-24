package view;


import controller.PolyederController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import model.Polyeder;
import model.Triangle;
import model.Vertex;

import javax.swing.event.DocumentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class View3D {

    public Parent intCreateContent() throws Exception
    {
        Sphere sphere = new Sphere(2.5);
        sphere.setMaterial(new PhongMaterial(Color.FORESTGREEN));

        sphere.setTranslateZ(7);
        sphere.setTranslateX(2);

        Box box = new Box(5, 5, 5);
        box.setMaterial(new PhongMaterial(Color.RED));

        Translate pivot = new Translate();
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (
                pivot,
                yRotate,
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(0, 0, -50)
        );

        // animate the camera position.
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(yRotate.angleProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(15),
                        new KeyValue(yRotate.angleProperty(), 360)
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(box);
        root.getChildren().add(sphere);
//        root.getChildren().add(createHUD());

        // set the pivot for the camera position animation base upon mouse clicks on objects
        root.getChildren().stream()
                .filter(node -> !(node instanceof Camera))
                .forEach(node ->
                        node.setOnMouseClicked(event -> {
                            pivot.setX(node.getTranslateX());
                            pivot.setY(node.getTranslateY());
                            pivot.setZ(node.getTranslateZ());
                        })
                );

        // Use a SubScene
        SubScene subScene = new SubScene(
                root,
                300,300,
                true,
                SceneAntialiasing.BALANCED
        );
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);
        //group.getChildren().add(createHUD());

        return group;
    }


    public MenuBar createHUD()
    {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuFileOpen = new MenuItem("Open");
        Menu menuEdit = new Menu("EDIT");
        MenuItem menuEditOpen = new MenuItem("EDIT_SEARCH");
        Menu menuView = new Menu("VIEW");
        MenuItem menuViewOpen = new MenuItem("Open");

        menuFile.getItems().add(menuFileOpen);
        menuEdit.getItems().add(menuEditOpen);
        menuView.getItems().add(menuViewOpen);
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);

        /*BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);

        SubScene scene = new SubScene(borderPane, 300,300);*/

        return menuBar;
    }

/*
    for (int j = 0; j < 3; j++) {
                       double x = tr.getVertex(j).getX();
                       double y = tr.getVertex(j).getY();
                       double z = tr.getVertex(j).getZ();
                       System.out.println("Die Cords: " + "X: "+ x + " " + "Y: " + y + " " + "Z: " + z);

   double xc = tr.getVertex(j-1).getX();
                double yc = tr.getVertex(j-1).getY();
                double zc = tr.getVertex(j-1).getZ();

                if (x == xc)
                {
                    validPositions.add(y);
                    validPositions.add(z);
                    break;
                }
                if (y == yc)
                {
                    validPositions.add(x);
                    validPositions.add(z);
                    break;
                }
                if (z == zc)
                {
                    validPositions.add(x);
                    validPositions.add(y);
                    break;
                }

            }*/


}
