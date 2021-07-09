package controller;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.interfaces.ServerInterface;
import utilities.ServerThread;
import view.Ausgabe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import utilities.ClientThread;

public class ServerController extends Thread {

    private final StringProperty serverIpAddress = new SimpleStringProperty();
    private final StringProperty lokaleIpAddress = new SimpleStringProperty();
    private final StringProperty publicIpAddress = new SimpleStringProperty();
    private final Property<String> port = new SimpleStringProperty();

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

        serverThread = new ServerThread();
        serverThread.setWaiting(true);
        serverThread.start();

        synchronized (serverThread) {
            serverThread.notify();
        }

        clientThread = new ClientThread();
//        clientThread.start();
//        synchronized (clientThread) {
//            notify();
//        }
    }

    public void connect() {
        Ausgabe.print(ServerInterface.CHECK_IF_IP_IS_SET);

        if (this.serverIpAddress.getValue().equals("")) {
            Ausgabe.print(ServerInterface.CHECK_NO_IP_FOUND);
            serverThread.setWaiting(false);

            synchronized (serverThread) {
                serverThread.notify();
            }

        } else {
            setConnectionStatus(ServerInterface.SERVER_START);
            Ausgabe.print(ServerInterface.CHECK_IP_FOUND);

           
            clientThread.start();
//            synchronized (serverThread) {
//                serverThread.notify();
//            }
        }
    }

    public void disconnect() {
        setConnectionStatus(ServerInterface.SERVER_CLOSING);
        serverThread.setWaiting(true);
        closeAllServerThings();

        synchronized (serverThread) {
            serverThread.notify();
        }
        setConnectionStatus(ServerInterface.OFFLINE);
    }

    public void close() {
        serverThread.setServerMainLoopRunning(false);
        serverThread.setWaiting(false);
        closeAllServerThings();

        synchronized (serverThread) {
            serverThread.notify();
        }
    }

    private void closeAllServerThings() {
        if (serverThread.getServer() != null)
            try {
            serverThread.getServer().close();
        } catch (IOException e) {
            Ausgabe.print(ServerInterface.SERVER_COULD_NOT_CLOSE);
        }
        if (serverThread.getClient() != null)
            try {
            serverThread.getClient().close();
        } catch (IOException e) {
            Ausgabe.print(ServerInterface.SERVER_CLIENT_COULD_NOT_CLOSE);
        }
        if (serverThread.ein != null) {
            try {
                serverThread.ein.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverThread.aus != null) {
            serverThread.aus.close();
        }
    }

    public void sendMessage(String message) {
        clientThread.sendeKommando(message);
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
