package utilities;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.ConnectionStatusEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Network {

    private final StringProperty serverIpAddress = new SimpleStringProperty();
    private final StringProperty lokaleIpAddress = new SimpleStringProperty();
    private final StringProperty publicIpAddress = new SimpleStringProperty();
    private final Property<String> port = new SimpleStringProperty();

    private final StringProperty connectionStatus = new SimpleStringProperty();

    private static Socket einClient;
    private static ServerSocket derServer = null;
    private Server server;

    public Network() {
        ConnectionStatus(ConnectionStatusEnum.OFFLINE);
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

    public void startServer() {
        server = new Server();
//        server.start();
    }

    public void connect() {
        server.start();
    }

    public void disconnect() {
        ConnectionStatus(ConnectionStatusEnum.DISCONNECTING);
        try {
            server.stop();
            derServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConnectionStatus(ConnectionStatusEnum.DISCONNECTED);
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

    public StringProperty ConnectionStatus(ConnectionStatusEnum status) {
        String text = "";
        switch (status) {
            case OFFLINE:
                text = "Nicht gestartet";
                break;
            case CONNECTING:
                text = "Verbindung wird hergestellt";
                break;
            case CONNECTED:
                text = "Ist Verbunden";
                break;
            case DISCONNECTING:
                text = "Verbindung wird getrennt";
                break;
            case DISCONNECTED:
                text = "Verbindung ist getrennt";
                break;
            case WRONGPORT:
                text = "Flascher Port";
                break;
            default:
                this.connectionStatus.setValue("Bambus");
        }
        this.connectionStatus.setValue(text);
        return this.connectionStatus;
    }


}
