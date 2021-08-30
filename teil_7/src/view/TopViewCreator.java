package view;

import controller.Main;
import controller.ModelController;
import controller.ServerController;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.interfaces.MenuBarInterface;
import utilities.ClipboardCreator;
import utilities.MenuTextFieldCreator;

public final class TopViewCreator {

    public static VBox createTopView(Stage stage, Main main) {
        VBox vBox = new VBox();
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu(MenuBarInterface.MENU_FILE);
        MenuItem menuFileOpen = new MenuItem(MenuBarInterface.MENU_FILE_OPEN);Menu menuControls = new Menu(MenuBarInterface.MENU_CONTROL);
        CheckMenuItem menuItemShowFaces = new CheckMenuItem(MenuBarInterface.MENU_CONTROL_FACES);
        CheckMenuItem menuItemShowAxis = new CheckMenuItem(MenuBarInterface.MENU_CONTROL_AXIS);

        Menu menuHelp = new Menu(MenuBarInterface.MENU_HELP);
        MenuItem menuItemAbout = new MenuItem(MenuBarInterface.MENU_ABOUT);

        //Network control bla
        Menu menuNetwork = new Menu(MenuBarInterface.MENU_NETWORK);
        CheckMenuItem menuItemConnectToServer = new CheckMenuItem(MenuBarInterface.MENU_NETWORK_CONNECT);
        CustomMenuItem menuItemServerIP = MenuTextFieldCreator.customMenuItem(ServerController.getInstance().getServerIpAddress());
        CustomMenuItem menuItemPort = MenuTextFieldCreator.customMenuItem(ServerController.getInstance().getPort());

        MenuItem menuItemShowLocalIpAddress = new MenuItem();
        menuItemShowLocalIpAddress.textProperty().bind(Bindings.concat(MenuBarInterface.LABEL_LOCALE, ServerController.getInstance().getLokaleIpAddress()));
        MenuItem menuItemShowPublicIpAddress = new MenuItem();
        menuItemShowPublicIpAddress.textProperty().bind(Bindings.concat(MenuBarInterface.LABEL_SERVER, ServerController.getInstance().getPublicIpAddress()));

        menuFile.getItems().addAll(menuFileOpen);

        menuControls.getItems().addAll(
                menuItemShowFaces,
                menuItemShowAxis
        );

        menuHelp.getItems().addAll(menuItemAbout);

        menuNetwork.getItems().addAll(
                menuItemConnectToServer,
                menuItemServerIP,
                menuItemPort,
                menuItemShowLocalIpAddress,
                menuItemShowPublicIpAddress
        );

        menuBar.getMenus().addAll(
                menuFile,
                menuControls,
                menuNetwork,
                menuHelp
        );

        menuFileOpen.setOnAction(e -> main.loadFile(stage));

        menuItemShowFaces.setSelected(ModelController.getInstance().isFill().getValue());
        menuItemShowFaces.setOnAction(e -> ModelController.getInstance().setDrawModeFill(!ModelController.getInstance().isFill().getValue()));

        menuItemShowAxis.setSelected(ModelController.getInstance().isAxisVisible().getValue());
        menuItemShowAxis.setOnAction(e -> ModelController.getInstance().setAxisVisible(!ModelController.getInstance().isAxisVisible().getValue()));

        menuItemConnectToServer.setOnAction(e -> {
            if (menuItemConnectToServer.isSelected()) {
                menuItemServerIP.getContent().setDisable(true);
                menuItemPort.getContent().setDisable(true);
                ServerController.getInstance().connect();
            } else {
                menuItemServerIP.getContent().setDisable(false);
                menuItemPort.getContent().setDisable(false);
                ServerController.getInstance().disconnect();
            }
        });

        menuItemShowLocalIpAddress.setOnAction(e -> {
            ClipboardCreator.createClipboard(ServerController.getInstance().getLokaleIpAddress().getValue());
        });

        menuItemShowPublicIpAddress.setOnAction(e -> {
            ClipboardCreator.createClipboard(ServerController.getInstance().getPublicIpAddress().getValue());
        });

        menuItemAbout.setOnAction(actionEvent -> {
            AlertMessage.aboutMessage(MenuBarInterface.ABOUT_MESSAGE, MenuBarInterface.ABOUT_HEADER, MenuBarInterface.MENU_ABOUT);
        });

        vBox.getChildren().add(menuBar);
        return vBox;
    }
}
