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

    private BufferedReader inputData;
    private PrintWriter outputData;

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
                inputData = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String zeile;
                System.out.println("Start listening");
                while (client.isConnected() && (zeile = inputData.readLine()) != null) {
                    System.out.println("Received message " + zeile);
                    if (zeile.startsWith("exit")) {
                        ServerController.getInstance().disconnect();
                        Ausgabe.print("Server wird geschlossen, Verbindung getrennt");
                        break;
                    } else if (zeile.startsWith("startClient")) {
                        String[] lines = zeile.split(";");
                        ServerController.getInstance().startClient(lines[1], lines[2]);
                    } else if (zeile.startsWith("setOnMousePressed")){
                        String[] lines = zeile.split(";");
                        ModelController.getInstance().mousePressed(Double.parseDouble(lines[1]), Double.parseDouble(lines[2]));
                    } else if (zeile.startsWith("setOnMouseDragged")){
                        String[] lines = zeile.split(";");
                        ModelController.getInstance().rotateWorld(Double.parseDouble(lines[1]), Double.parseDouble(lines[2]), Double.parseDouble(lines[3]), Double.parseDouble(lines[4]), Double.parseDouble(lines[5]), Double.parseDouble(lines[6]), Double.parseDouble(lines[7]), Double.parseDouble(lines[8]), Double.parseDouble(lines[9]), Double.parseDouble(lines[10]), Double.parseDouble(lines[11]), Double.parseDouble(lines[12]));
                    } else if (zeile.startsWith("startClient")) {
                        String[] lines = zeile.split(";");
                        ServerController.getInstance().startClient(lines[1], lines[2]);

//                        sendeKommando("startClient;" + ServerController.getInstance().getLokaleIpAddress() + ";" + ServerController.getInstance().getPort());
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

    public void closeAll(){
        if (server != null)
            try {
                server.close();
            } catch (IOException e) {
                Ausgabe.print(ServerInterface.SERVER_COULD_NOT_CLOSE);
            }
        if (client != null)
            try {
                client.close();
            } catch (IOException e) {
                Ausgabe.print(ServerInterface.SERVER_CLIENT_COULD_NOT_CLOSE);
            }
        if (inputData != null) {
            try {
                inputData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputData != null) {
            outputData.close();
        }
    }
}
