package view;

import controller.Main;
import controller.ModelController;
import controller.ServerController;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.interfaces.GUIKonstanten;
import model.interfaces.MenuBarInterface;

public final class TopViewCreator {

    public static VBox createTopView(Stage stage, Main main) {
        VBox vBox = new VBox();
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu(MenuBarInterface.MENU_FILE);
        MenuItem menuFileOpen = new MenuItem(MenuBarInterface.MENU_FILE_OPEN);

        Menu menuViewPoint = new Menu(MenuBarInterface.MENU_VIEWPOINT);

        Menu menuControls = new Menu(MenuBarInterface.MENU_CONTROL);
        CheckMenuItem menuItemShowFaces = new CheckMenuItem(MenuBarInterface.MENU_CONTROL_FACES);
        CheckMenuItem menuItemShowAxis = new CheckMenuItem(MenuBarInterface.MENU_CONTROL_AXIS);

        //Network control bla
        Menu menuNetwork = new Menu(MenuBarInterface.MENU_NETWORK);
        CheckMenuItem menuItemConnectToServer = new CheckMenuItem(MenuBarInterface.MENU_NETWORK_CONNECT);
        CustomMenuItem menuItemServerIP = new CustomMenuItem();
        TextField menuTextFieldServerIP = new TextField();
        menuTextFieldServerIP.setPrefWidth(150);
        menuTextFieldServerIP.textProperty().bindBidirectional(ServerController.getInstance().getServerIpAddress());
        menuItemServerIP.setContent(menuTextFieldServerIP);
        menuItemServerIP.setHideOnClick(false);

        CustomMenuItem menuItemPort = new CustomMenuItem();
        TextField menuTextFieldPort = new TextField();
        menuTextFieldPort.setPrefWidth(150);
        menuTextFieldPort.textProperty().bindBidirectional(ServerController.getInstance().getPort());
        menuItemPort.setContent(menuTextFieldPort);
        menuItemPort.setHideOnClick(false);

        MenuItem menuItemShowLocalIpAddress = new MenuItem();
        menuItemShowLocalIpAddress.textProperty().bind(Bindings.concat("Lokale: ", ServerController.getInstance().getLokaleIpAddress()));
        MenuItem menuItemShowPublicIpAddress = new MenuItem();
        menuItemShowPublicIpAddress.textProperty().bind(Bindings.concat("Public: ", ServerController.getInstance().getPublicIpAddress()));

        menuFile.getItems().addAll(
                menuFileOpen
        );

        menuViewPoint.getItems().addAll(

        );

        menuControls.getItems().addAll(
                menuItemShowFaces,
                menuItemShowAxis
        );
        menuNetwork.getItems().addAll(
                menuItemConnectToServer,
                menuItemServerIP,
                menuItemPort,
                menuItemShowLocalIpAddress,
                menuItemShowPublicIpAddress
        );

        menuBar.getMenus().addAll(
                menuFile,
                menuViewPoint,
                menuControls,
                menuNetwork
        );

        menuFileOpen.setOnAction(e -> main.loadFile(stage));

        menuItemShowFaces.setSelected(ModelController.getInstance().isFill().getValue());
        menuItemShowFaces.setOnAction(e -> ModelController.getInstance().setDrawModeFill(!ModelController.getInstance().isFill().getValue()));

        menuItemShowAxis.setSelected(ModelController.getInstance().isAxisVisible().getValue());
        menuItemShowAxis.setOnAction(e -> ModelController.getInstance().setAxisVisible(!ModelController.getInstance().isAxisVisible().getValue()));

        menuItemConnectToServer.setOnAction(e -> {
            if (menuItemConnectToServer.isSelected()) {
                menuTextFieldServerIP.setDisable(true);
                menuTextFieldPort.setDisable(true);
                ServerController.getInstance().connect();
            } else {
                menuTextFieldServerIP.setDisable(false);
                menuTextFieldPort.setDisable(false);
                ServerController.getInstance().disconnect();
            }
        });

        menuItemShowLocalIpAddress.setOnAction(e -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(ServerController.getInstance().getLokaleIpAddress().getValue());
            clipboard.setContent(content);
        });

        menuItemShowPublicIpAddress.setOnAction(e -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(ServerController.getInstance().getPublicIpAddress().getValue());
            clipboard.setContent(content);

        });

        vBox.getChildren().add(menuBar);
        return vBox;
    }
}
