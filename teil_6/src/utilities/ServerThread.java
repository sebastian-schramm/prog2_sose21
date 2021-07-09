package utilities;

import controller.ModelController;
import controller.ServerController;
import model.interfaces.ServerInterface;
import view.Ausgabe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

    private boolean serverMainLoopRunning = true;
    private boolean waiting = true;

    private Socket client = null;
    private ServerSocket server = null;

    public BufferedReader ein;
    public PrintWriter aus;

    @Override
    public void run() {
        long counter = 0;

        while (serverMainLoopRunning) {
            synchronized (this) {
                while (waiting) {
                    Ausgabe.print("Wait... " + ++counter);
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (serverMainLoopRunning) {
                    Ausgabe.print("Arbeite...");
                    server = null;
                    client = null;

                    ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_START);
                    try {
                        server = new ServerSocket(Integer.parseInt(ServerController.getInstance().getPort().getValue()));
                    } catch (IOException e) {
                        Ausgabe.print(ServerInterface.SERVER_INIT_ERROR);
                    }
                    ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_WAIT_FOR_CONNECTION);

                    try {
                        client = server.accept();
                    } catch (IOException e) {
                        Ausgabe.print(ServerInterface.SERVER_ACCEPT_ERROR);
                    }

                    startListener();

                    Ausgabe.print("... Ende");
                }
            }
        }
    }

    private void startListener() {
        if (client != null && client.isConnected()) {
            Ausgabe.print("Client verbunden.");
            ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_CLIENT_CONNECTED);
            try {
                ein = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String zeile;
                System.out.println("Start listening");
                while (client.isConnected() && (zeile = ein.readLine()) != null) {
                    if (zeile.startsWith("exit")) {
                        ServerController.getInstance().disconnect();
                        Ausgabe.print("Server wird geschlossen, Verbindung getrennt");
                        break;
                    }else if(zeile.startsWith("setOnMousePressed")){
                        String[] lines = zeile.split(";");
                        ModelController.getInstance().mousePressed(Double.parseDouble(lines[1]), Double.parseDouble(lines[2]));
                    }else if(zeile.startsWith("setOnMouseDragged")){
                         String[] lines = zeile.split(";");
                        ModelController.getInstance().rotateWorld(Double.parseDouble(lines[1]), Double.parseDouble(lines[2]));
                    } else {
                        Ausgabe.print(zeile);
                    }
                }
                System.out.println("End listening");
                ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_CLOSING);
            } catch (IOException e) {
                Ausgabe.print("Das ist der Fehler hier");
                System.out.println(e);
            }
        }
    }
    

    public Socket getClient() {
        return client;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServerMainLoopRunning(boolean serverMainLoopRunning) {
        this.serverMainLoopRunning = serverMainLoopRunning;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }
}
