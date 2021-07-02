package utilities;

import controller.ModelController;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.ConnectionStatusEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server extends Thread {

    private final StringProperty serverIpAddress = new SimpleStringProperty();
    private final StringProperty lokaleIpAddress = new SimpleStringProperty();
    private final StringProperty publicIpAddress = new SimpleStringProperty();
    private final Property<String> port = new SimpleStringProperty();

    private final StringProperty connectionStatus = new SimpleStringProperty();

    private Socket client = null;
    private ServerSocket server = null;

    private boolean serverMainLoopRunning = true;
    private boolean waiting = true;

    private BufferedReader ein;
    private PrintWriter aus;

    @Override
    public void run() {
        System.out.println("Prüfe ob eine IP für den Server gesetzt wurde...");
        long counter = 0;
        while (serverMainLoopRunning) {
            // Überprüfen, ob pausieren angesagt ist:
            synchronized (this) {
                while (waiting) {
                    System.out.println("Wait..." + ++counter);
                    try {
                        wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (serverMainLoopRunning) {
                    if (serverIpAddress.getValue().equals("")) {
                        System.out.println("Keine IP angegeben, Starte Server!");
                        startServer();
                    } else {
                        System.out.println("Versuche eine Verbindung zum Server herzustellen!");
                        startClient();
                    }
                }
            }
        }
    }

    public Server() {
        ConnectionStatus(ConnectionStatusEnum.OFFLINE);
        this.serverIpAddress.setValue("");
        this.port.setValue("40404");
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            this.serverIpAddress.setValue(InetAddress.getLocalHost().getHostAddress());
            this.lokaleIpAddress.setValue(InetAddress.getLocalHost().getHostAddress());
            this.publicIpAddress.setValue(in.readLine());
        } catch (SocketException | UnknownHostException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.start();

        synchronized (this) {
            notify();
        }
    }

    public void sendMessage(String message) {
        if (client != null) {
            System.out.println("Send message : " + message);
            aus.println(message);
        }
    }

    private void startServer() {
        System.out.println("Server wird gestartet");
        ConnectionStatus(ConnectionStatusEnum.SERVER_START);
        try {
            server = new ServerSocket(Integer.parseInt(port.getValue()));
        } catch (Exception e) {
            System.out.println("Fehler Port!");
            server = null;
        }

        System.out.println("Warte auf Verbindungsanforderung");
        try {
            client = server.accept();
        } catch (Exception e) {
            System.out.println("Fehler accept!");
        }

        startListener();
        ConnectionStatus(ConnectionStatusEnum.SERVER_STARTED);
    }

    private void startClient() {
        System.out.println("Client wird gestartet");
        ConnectionStatus(ConnectionStatusEnum.CONNECTING);
        try {
            client = new Socket(serverIpAddress.getValue(), Integer.parseInt(port.getValue()));
        } catch (Exception e) {
            System.out.println("Server nicht gefunden.");
        }

        startListener();
        ConnectionStatus(ConnectionStatusEnum.CONNECTED);
    }

    private void startListener() {
        if (client != null && client.isConnected()) {
            System.out.println("Client verbunden.");
            ConnectionStatus(ConnectionStatusEnum.CONNECTED);
            try {
                ein = new BufferedReader(new InputStreamReader(client.getInputStream()));
                aus = new PrintWriter(client.getOutputStream(), true);
                String zeile;
                System.out.println("Start listening");
                while (client.isConnected()) {
                    if (!(zeile = ein.readLine()).contains("exit")) {
                        String[] lines;
                        if (zeile.startsWith("PrimaryButtonDown")) {
                            lines = zeile.split(";");
                            ModelController.getInstance().getModel().rotateWorld(Double.parseDouble(lines[1]), Double.parseDouble(lines[2]));
                        }
                        System.out.println("Echo: " + zeile);
                    } else {
                        disconnect();
                        break;
                    }
                }
                System.out.println("End listening");
                ConnectionStatus(ConnectionStatusEnum.DISCONNECTED);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void connect() {
        waiting = false;
        synchronized (this) {
            notify();
        }
    }

    public void disconnect() {
        sendMessage("exit");
        waiting = true;
        try {
            if (client != null)
                client.close();
            if (server != null)
                server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            notify();
        }
        System.out.println("Notify erfolgreich");
    }

    public void close() {
        sendMessage("exit");
        serverMainLoopRunning = false;
        waiting = false;
        try {
            if (client != null)
                client.close();
            if (server != null)
                server.close();
            if (ein != null)
                ein.close();
            if (aus != null)
                aus.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            notify();
        }
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
            case SERVER_START:
                text = "Server wird gestartet";
                break;
            case SERVER_STARTED:
                text = "Server gestartet";
                break;
            case SERVER_CLOSING:
                text = "Server wird geschlossen";
                break;
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
            case WRONG_PORT:
                text = "Flascher Port";
                break;
            default:
                this.connectionStatus.setValue("Bambus");
                break;
        }
        String finalText = text;
        Platform.runLater(
            () -> {
                connectionStatus.setValue(finalText);
            }
        );
        return this.connectionStatus;
    }
}
