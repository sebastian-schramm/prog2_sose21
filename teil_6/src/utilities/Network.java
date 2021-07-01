package utilities;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
        this.connectionStatus.setValue(connectionStatusEnum.Offline + "");
    }

    public void connect() {
        this.connectionStatus.setValue(connectionStatusEnum.Connecting + "");
//        try {
//            derServer = new ServerSocket(Integer.parseInt(port.getValue()));
//        } catch (Exception e) {
//            System.out.println(connectionStatusEnum.Connecting + "");
////            System.exit(0);
//        }
////        MyClient clientThread = new MyClient();
////        clientThread.start();
//        try {
//            einClient = derServer.accept();
//        } catch (Exception e) {
//            System.out.println("Fehler accept!"); System.exit(0);
//        }
        this.connectionStatus.setValue(connectionStatusEnum.Connected + "");
    }

    public void disconnect() {
        this.connectionStatus.setValue(connectionStatusEnum.Disconnecting + "");

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

    public StringProperty getConnectionStatus() {
        return this.connectionStatus;
    }

    public enum connectionStatusEnum {
        Offline,
        Connecting,
        Connected,
        Disconnecting,
        Disconnected,
        WrongPort;
    }
}
