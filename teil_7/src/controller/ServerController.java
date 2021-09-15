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

    /**
     * Privater Konstruktor der ServerController Klasse, um diese Controller Klasse als Singelton zu realisieren.
     * Ausserdem werden im privaten Konstruktor die privaten Attribute des Objekts initialisiert.
     **/
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

    /**
     * Die Methode startClient() baut fuer eine Verbindung von zwei Applikationen die Clients auf.
     * Des Weiteren werden die String Properties, die in der GUI Informationen ueber die Verbindung zeigen, gesetzt.
     *
     * @param ip   Die IP-Adressen String, der in der GUI angezeigt wird.
     * @param port Der Ziel Port String, der in der GUI angezeigt wird.
     **/
    public void startClient(String ip, String port) {
        this.serverIpAddress.setValue(ip);
        this.port.setValue(port);

        if (clientThread == null) {
            clientThread = new ClientThread();
            clientThread.start();
        }
    }

    /**
     * Die Methode startServer() baut fuer eine Verbindung von zwei Applikationen die Server auf. Des Weiteren werden die
     * String Properties, die in der GUI Informationen ueber die Verbindung zeigen, gesetzt.
     *
     * @param ip   Die IP-Adresse String, der in der GUI angezeigt wird.
     * @param port Der Ziel Port String, der in der GUI angezeigt wird.
     **/
    public void startServer(String ip, String port) {
        this.serverIpAddress.setValue(ip);
        this.port.setValue(port);

        if (serverThread == null) {
            serverThread = new ServerThread();
            serverThread.start();
        }
    }

    /**
     * Die Methode connect() baut eine Verbindung von zwei Applikationen auf.
     **/
    public void connect() {
        if (this.serverIpAddress.getValue().equals("")) {
            serverThread = new ServerThread();
            serverThread.start();
            serverThread.setServerMainLoopRunning(true);
        } else {
            setConnectionStatus(ServerInterface.SERVER_START);
            clientThread = new ClientThread();
            clientThread.start();
        }
    }

    /**
     * Die Methode disconnect() schliesst die Verbindung zwischen zwei Applikationen.
     **/
    public void disconnect() {
        if (serverThread != null) {
            setConnectionStatus(ServerInterface.SERVER_CLOSING);
            serverThread.setServerMainLoopRunning(false);
            serverThread.closeAll();
            clientThread.closeAll();

            synchronized (serverThread) {
                serverThread.notify();
            }
            setConnectionStatus(ServerInterface.OFFLINE);
        }
    }

    /**
     * Die Methode sendTriangleList() gibt an den clientThread die Liste von Dreiecken weiter.
     *
     * @param triangleList Die Dreieckliste vom Polyeder
     **/
    public void sendTriangleList(ArrayList<Triangle> triangleList) {
        if (clientThread != null)
            clientThread.sendeMesh(triangleList);
    }

    /**
     * Die Methode sendString() gibt an den clientThread einen Befehl weiter.
     *
     * @param affine Der Befehl der an den clientThread weitergegeben wird.
     **/
    public void sendString(String affine) {
        if (clientThread != null && ClientThread.getSocket() != null)
            clientThread.sendeMessage(affine);
    }

    /**
     * Die getLokaleIpAddress() Methode gibt eine String Property zurueck, die die lokale Ip Adresse des Anwenders beinhaltet.
     *
     * @return String Property der lokalen IP Adresse.
     **/
    public StringProperty getLokaleIpAddress() {
        return this.lokaleIpAddress;
    }

    /**
     * Die getPublicIpAddress() Methode gibt eine String Property zurueck, die die oeffentliche Ip Adresse des Anwenders beinhaltet.
     *
     * @return String Property der oeffentliche IP Adresse.
     **/
    public StringProperty getPublicIpAddress() {
        return this.publicIpAddress;
    }

    /**
     * Die getPort() Methode gibt eine String Property zurueck, die den offenen Port des Anwenders beinhaltet.
     *
     * @return String Property des Ports.
     **/
    public StringProperty getPort() {
        return this.port;
    }

    /**
     * Die getServerIpAddress() Methode gibt eine String Property zurueck, die die Ip Adresse des Servers beinhaltet.
     *
     * @return String Property der Server IP Adresse.
     **/
    public StringProperty getServerIpAddress() {
        return this.serverIpAddress;
    }

    /**
     * Die getServerThread() Methode gibt ein Objekt der Klasse ServerThread zurueck.
     *
     * @return Private Attribut serverThread.
     **/
    public ServerThread getServerThread() {
        return this.serverThread;
    }

    /**
     * Die setConnectionStatus() Methode setzt eine String Property, ob eine Verbindung aufgebaut wurde.
     *
     * @param status Der Status der Verbindung.
     **/
    public void setConnectionStatus(String status) {
        Platform.runLater(() -> {
            this.connectionStatus.setValue(status);
        });
    }

    /**
     * Die getServerThread() Methode gibt ein Objekt der Klasse ServerThread zurueck.
     *
     * @return Private Attribut serverThread.
     **/
    public StringProperty getConnectionStatusProperty() {
        return this.connectionStatus;
    }

    /**
     * Die getInstance() Methode gibt eine Instanz der ServerController Klasse zurueck.
     *
     * @return Instanz wird zurueckgegeben.
     **/
    public static ServerController getInstance() {
        return ServerControllerHolder.INSTANCE;
    }

    /**
     * Private Klasse, die die Instanz der ServerController Klasse erstellt.
     */
    private static class ServerControllerHolder {
        private static final ServerController INSTANCE = new ServerController();
    }
}
