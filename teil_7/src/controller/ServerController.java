package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Triangle;
import model.interfaces.ServerInterface;
import utilities.ClientThread;
import utilities.ServerThread;
import view.Ausgabe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * ServerController Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class ServerController {

    private final StringProperty serverIpAddress = new SimpleStringProperty();
    private final StringProperty lokaleIpAddress = new SimpleStringProperty();
    private final StringProperty publicIpAddress = new SimpleStringProperty();
    private final StringProperty port = new SimpleStringProperty();

    private final StringProperty connectionStatus = new SimpleStringProperty();

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ServerController() {
        this.serverIpAddress.setValue(ServerInterface.SERVER_IP);
        this.lokaleIpAddress.setValue(ServerInterface.LOCALE_IP);
        this.publicIpAddress.setValue(ServerInterface.PUBLIC_IP);
        this.port.setValue(ServerInterface.PORT_NUMBER);

        setConnectionStatus(ServerInterface.OFFLINE);

        try {
            URL whatismyip = new URL(ServerInterface.URL_TO_GET_PUBIC_IP);
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            this.lokaleIpAddress.setValue(InetAddress.getLocalHost().getHostAddress());
            this.publicIpAddress.setValue(in.readLine());
        } catch (MalformedURLException e) {
            Ausgabe.print(ServerInterface.WEBSITE_COULD_NOT_BE_FOUND);
        } catch (IOException e) {
            Ausgabe.print(ServerInterface.WEBSITE_IP_NOT_FOUND);
        }
    }

    public void startClient(String ip, String port) {
        System.out.println("Try to start Client");
        System.out.println(ip);
        System.out.println(port);
        this.serverIpAddress.setValue(ip);
        this.port.setValue(port);

        if (clientThread == null) {
            System.out.println("Start ClientThread");
            clientThread = new ClientThread();
            clientThread.start();
        }
    }

    public void startServer(String ip, String port) {
        System.out.println("Try to start Client");
        System.out.println(ip);
        System.out.println(port);
        this.serverIpAddress.setValue(ip);
        this.port.setValue(port);

        if (serverThread == null) {
            System.out.println("Start ClientThread");
            serverThread = new ServerThread();
            serverThread.start();
        }
    }

    public void connect() {
        Ausgabe.print(ServerInterface.CHECK_IF_IP_IS_SET);

        if (this.serverIpAddress.getValue().equals("")) {
            Ausgabe.print(ServerInterface.CHECK_NO_IP_FOUND);
            serverThread = new ServerThread();
            serverThread.start();
            serverThread.setServerMainLoopRunning(true);
//            serverThread.setWaiting(false);

//            synchronized (serverThread) {
//                serverThread.notify();
//            }

        } else {
            setConnectionStatus(ServerInterface.SERVER_START);
            Ausgabe.print(ServerInterface.CHECK_IP_FOUND);
            clientThread = new ClientThread();

            clientThread.start();
        }
    }

    public void disconnect() {
        if (serverThread != null) {
            setConnectionStatus(ServerInterface.SERVER_CLOSING);
            serverThread.setServerMainLoopRunning(false);
//        serverThread.setWaiting(true);
            serverThread.closeAll();
            clientThread.closeAll();

            synchronized (serverThread) {
                serverThread.notify();
            }
            setConnectionStatus(ServerInterface.OFFLINE);
        }
    }

    public void close() {
        if (serverThread != null) {
            serverThread.setServerMainLoopRunning(false);
            serverThread.setWaiting(false);
            serverThread.closeAll();

            synchronized (serverThread) {
                serverThread.notify();
            }
        }
    }

    public void sendTriangleList(ArrayList<Triangle> triangleList) {
        if (clientThread != null)
            clientThread.sendeMesh(triangleList);
    }

    public void sendString(String affine) {
        if (clientThread != null && ClientThread.getSocket() != null)
            clientThread.sendeRotation(affine);
    }

    public StringProperty getLokaleIpAddress() {
        return this.lokaleIpAddress;
    }

    public StringProperty getPublicIpAddress() {
        return this.publicIpAddress;
    }

    public StringProperty getPort() {
        return this.port;
    }

    public StringProperty getServerIpAddress() {
        return this.serverIpAddress;
    }

    public ServerThread getServerThread() {
        return this.serverThread;
    }

    public ClientThread getClientThread() {
        return this.clientThread;
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
