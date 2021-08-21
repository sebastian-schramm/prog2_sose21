package utilities;

import controller.ModelController;
import controller.PolyederController;
import controller.ServerController;
import javafx.application.Platform;
import javafx.scene.shape.TriangleMesh;
import model.Polyeder;
import model.Triangle;
import model.interfaces.ServerInterface;
import view.Ausgabe;

import java.awt.geom.AffineTransform;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private boolean serverMainLoopRunning = true;
    private boolean waiting = true;

    private Socket client = null;
    private ServerSocket server = null;

    private BufferedReader inputData;
    private PrintWriter outputData;
    private ObjectInputStream objectInputStream;

    @Override
    public void run() {
        long counter = 0;

        while (serverMainLoopRunning) {
            Ausgabe.print("Laufen... " + ++counter);
            synchronized (this) {
//                while (waiting) {
//                    Ausgabe.print("Wait... " + ++counter);
//                    try {
//                        wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                if (serverMainLoopRunning) {
//                    server = null;
                    client = null;

                    ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_START);
                    if (server == null)
                        try {
                            Ausgabe.print("Starte server...");
                            server = new ServerSocket(Integer.parseInt(ServerController.getInstance().getPort().getValue()));
                        } catch (IOException e) {
                            Ausgabe.print(ServerInterface.SERVER_INIT_ERROR);
                        }
                    ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_WAIT_FOR_CONNECTION);

                    try {
                        System.out.println("Warte auf client...");
                        client = server.accept();
                    } catch (IOException e) {
                        Ausgabe.print(ServerInterface.SERVER_ACCEPT_ERROR);
                    }
                    System.out.println("Client verbunden...");


//                    startListener();
                    startObjectListener();

                    Ausgabe.print("... Ende");
                }
            }
        }
        System.out.println("ende loop");
    }

    //TODO entfernen
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

    private void startObjectListener(){
        if (client != null && client.isConnected()) {
            try {
                objectInputStream = new ObjectInputStream(client.getInputStream());
                Object object;
                try {
                    while ((object = objectInputStream.readObject()) != null) {
                        if (object.getClass().isInstance(new String())) {
                            String zeile = (String) object;
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
                            } else {
                                Ausgabe.print(zeile);
                            }
                        } else if (object.getClass().isInstance(new ArrayList<Triangle>())) {
                            System.out.println("Triangle Arraylist");
                            PolyederController.getInstance().getPolyeder().setTriangleList((ArrayList<Triangle>) object);
                            Platform.runLater(() -> {
                                PolyederController.getInstance().getPolyeder().updatePolyederInfo();
                                ModelController.getInstance().buildModel();
                            });
                        } else {
                            System.out.println("Nichts gefunden");
                        }
                        objectInputStream = new ObjectInputStream(client.getInputStream());
                    }
                }
                catch (ClassNotFoundException e) {
                    Ausgabe.print("classnotfoundexception");
                }
            }
            catch (IOException e){
                Ausgabe.print("IOException in ServerThread");
                e.printStackTrace();
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
