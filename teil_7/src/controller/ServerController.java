package controller;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.interfaces.ServerInterface;

public class ServerController {

    private final StringProperty serverIpAddress = new SimpleStringProperty();
    private final StringProperty lokaleIpAddress = new SimpleStringProperty();
    private final StringProperty publicIpAddress = new SimpleStringProperty();
    private final Property<String> port = new SimpleStringProperty();

    private final StringProperty connectionStatus = new SimpleStringProperty();

    private ServerController() {
        this.serverIpAddress.setValue(ServerInterface.SERVER_IP);
        this.lokaleIpAddress.setValue(ServerInterface.LOCALE_IP);
        this.publicIpAddress.setValue(ServerInterface.PUBLIC_IP);
        this.port.setValue(ServerInterface.PORT_NUMBER);

        setConnectionStatus(ServerInterface.OFFLINE);
    }

    public StringProperty getLokaleIpAddress() {
        return this.lokaleIpAddress;
    }

    public StringProperty getPublicIpAddress() {
        return this.publicIpAddress;
    }

    public Property<String> getPort() {
        return this.port;
    }

    public Property<String> getServerIpAddress() {
        return this.serverIpAddress;
    }

    public void setConnectionStatus(String status) {
        Platform.runLater(() -> {
            this.connectionStatus.setValue(status);
        });
    }

    public StringProperty getConnectionStatusProperty() {
        return this.connectionStatus;
    }

    public static ServerController getInstance() {
        return ServerControllerHolder.INSTANCE;
    }

    private static class ServerControllerHolder {
        private static final ServerController INSTANCE = new ServerController();
    }
}
