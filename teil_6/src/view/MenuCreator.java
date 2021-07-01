package view;

import controller.Main;
import controller.ModelController;
import controller.NetworkController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;
import utilities.LabelCreator;

import javax.naming.Binding;

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

        //Network control bla
        Menu menuNetwork = new Menu("Network");
        CheckMenuItem menuItemConnectToServer = new CheckMenuItem(GUIKonstanten.MENU_NETWORK_CONNECT);
        CustomMenuItem menuItemServerIP = new CustomMenuItem();
        TextField menuTextFieldServerIP = new TextField();
        menuTextFieldServerIP.setPrefWidth(150);
        menuTextFieldServerIP.textProperty().bindBidirectional(NetworkController.getInstance().getNetwork().getPort());
        menuItemServerIP.setContent(menuTextFieldServerIP);
        menuItemServerIP.setHideOnClick(false);

        CustomMenuItem menuItemPort = new CustomMenuItem();
        TextField menuTextFieldPort = new TextField();
        menuTextFieldPort.setPrefWidth(150);
        menuTextFieldPort.textProperty().bindBidirectional(NetworkController.getInstance().getNetwork().getServerIpAddress());
        menuItemPort.setContent(menuTextFieldPort);
        menuItemPort.setHideOnClick(false);

        MenuItem menuItemShowLocalIpAddress = new MenuItem();
            menuItemShowLocalIpAddress.textProperty().bind(NetworkController.getInstance().getNetwork().getLokaleIpAddress());
        MenuItem menuItemShowPublicIpAddress = new MenuItem();
            menuItemShowPublicIpAddress.textProperty().bind(NetworkController.getInstance().getNetwork().getPublicIpAddress());


        menuFile.getItems().add(menuFileOpen);
        menuViewPoint.getItems().addAll(menuXPos, menuXNeg, menuYPos, menuYNeg, menuZPos, menuZNeg);
        menuControls.getItems().addAll(menuItemShowFaces, menuItemShowAxis);
        menuNetwork.getItems().addAll(menuItemConnectToServer, menuItemServerIP, menuItemPort, menuItemShowPublicIpAddress, menuItemShowLocalIpAddress);

        menuBar.getMenus().addAll(menuFile, menuViewPoint, menuControls, menuNetwork);

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

        menuItemConnectToServer.setOnAction(e -> {
            if (menuItemConnectToServer.isSelected()) {
                menuTextFieldServerIP.setDisable(true);
                menuTextFieldPort.setDisable(true);
                NetworkController.getInstance().getNetwork().connect();
            } else {
                menuTextFieldServerIP.setDisable(false);
                menuTextFieldPort.setDisable(false);
                NetworkController.getInstance().getNetwork().disconnect();
            }
        });

//        menuTextFieldPort.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("textfield changed from " + oldValue + " to " + newValue);
//            System.out.println(NetworkController.getInstance().getNetwork().getPort());
//        });

        return menuBar;
    }
}
