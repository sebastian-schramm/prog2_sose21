package utilities;

import controller.MyClient;
import controller.NetworkController;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Network extends Thread {

    private final StringProperty serverIpAddress = new SimpleStringProperty();
    private final StringProperty lokaleIpAddress = new SimpleStringProperty();
    private final StringProperty publicIpAddress = new SimpleStringProperty();
    private final Property<String> port = new SimpleStringProperty();

    private final StringProperty connectionStatus = new SimpleStringProperty();

    private static Socket einClient;
    private static ServerSocket derServer = null;
    private Server server = new Server();

    public Network() {
        this.serverIpAddress.setValue("");
        this.port.setValue("40404");
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            this.lokaleIpAddress.setValue(InetAddress.getLocalHost().getHostAddress());
            this.publicIpAddress.setValue(in.readLine());
        } catch (SocketException | UnknownHostException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        server.start();
    }

    public void disconnect() {
        this.connectionStatus.setValue(connectionStatusEnum.Disconnecting + "");
        try {
            server.stop();
            derServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.connectionStatus.setValue(connectionStatusEnum.Disconnected + "");
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

    public StringProperty ConnectionStatus() {
        return this.connectionStatus;
    }
}
