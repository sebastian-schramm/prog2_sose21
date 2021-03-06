package view;

import controller.Main;
import controller.ModelController;
import javafx.event.Event;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;

public final class MenuCreator {

    private MenuCreator() {

    }

    public static MenuBar createMenu(Stage stage, Main meinMain) {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu(GUIKonstanten.MENU_FILE);
        MenuItem menuFileOpen = new MenuItem(GUIKonstanten.MENU_FILE_OPEN);

        Menu menuViewPoint = new Menu("View Point");
        MenuItem menuXPos = new MenuItem("xPos");
        MenuItem menuXNeg = new MenuItem("xNeg");
        MenuItem menuYPos = new MenuItem("yPos");
        MenuItem menuYNeg = new MenuItem("yNeg");
        MenuItem menuZPos = new MenuItem("zPos");
        MenuItem menuZNeg = new MenuItem("zNeg");

        Menu menuControls = new Menu(GUIKonstanten.MENU_CONTROL);
        CheckMenuItem menuItemShowFaces = new CheckMenuItem(GUIKonstanten.MENU_CONTROL_FACES);
        CheckMenuItem menuItemShowAxis = new CheckMenuItem(GUIKonstanten.MENU_CONTROL_AXIS);

        menuFile.getItems().add(menuFileOpen);
        menuViewPoint.getItems().addAll(menuXPos, menuXNeg, menuYPos, menuYNeg, menuZPos, menuZNeg);
        menuControls.getItems().addAll(menuItemShowFaces, menuItemShowAxis);

        menuBar.getMenus().addAll(menuFile, menuViewPoint, menuControls);

        menuFileOpen.setOnAction(e -> {
            meinMain.loadFile(stage);
        });

        menuXPos.setOnAction(e -> {
            ModelCreator.setViewPoint(ModelCreator.viewPoints.xPos);
        });

        menuXNeg.setOnAction(e -> {
            ModelCreator.setViewPoint(ModelCreator.viewPoints.xNeg);
        });

        menuYPos.setOnAction(e -> {
            ModelCreator.setViewPoint(ModelCreator.viewPoints.yPos);
        });

        menuYNeg.setOnAction(e -> {
            ModelCreator.setViewPoint(ModelCreator.viewPoints.yNeg);
        });

        menuZPos.setOnAction(e -> {
            ModelCreator.setViewPoint(ModelCreator.viewPoints.zPos);
        });

        menuZNeg.setOnAction(e -> {
            ModelCreator.setViewPoint(ModelCreator.viewPoints.zNeg);
        });

        menuItemShowFaces.setSelected(ModelController.getInstance().getModel().isFill());

        menuItemShowFaces.setOnAction(e -> {
            ModelController.getInstance().getModel().setDrawModeFill(!ModelController.getInstance().getModel().isFill());
        });

        menuItemShowAxis.setSelected(ModelController.getInstance().getModel().isAxisVisible());

        menuItemShowAxis.setOnAction(e -> {
            ModelController.getInstance().getModel().setAxisVisible(!ModelController.getInstance().getModel().isAxisVisible());
        });

        return menuBar;
    }
}
