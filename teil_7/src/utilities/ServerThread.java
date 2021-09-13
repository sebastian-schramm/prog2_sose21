package utilities;

import controller.ModelController;
import controller.PolyederController;
import controller.ServerController;
import javafx.application.Platform;
import model.Triangle;
import model.interfaces.ServerInterface;
import view.Ausgabe;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * ServerThread Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
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
                if (waiting) {
//                    server = null;
                    client = null;

                    ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_START);
                    if (server == null)
                        try {
                            Ausgabe.print("Starte server...");
                            server = new ServerSocket(Integer.parseInt(ServerController.getInstance().getPort().getValue()));

                            ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_WAIT_FOR_CONNECTION);

                            try {
                                System.out.println("Warte auf client...");
                                client = server.accept();
                                ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_CONNECTION_DETECTED);
                            } catch (IOException e) {
                                Ausgabe.print(ServerInterface.SERVER_ACCEPT_ERROR);
                                ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_CONNECTION_FAILED);
                            }
                            System.out.println("Client verbunden...");

                        } catch (IOException e) {
                            Ausgabe.print(ServerInterface.SERVER_INIT_ERROR);
                            ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_PORT_ALREADY_USED);
                        }

                    startObjectListener();


                    Ausgabe.print("... Ende");
                }
            }
        }
        System.out.println("ende loop");
    }

    private void startObjectListener() {
        if (client != null && client.isConnected()) {
            try {
                objectInputStream = new ObjectInputStream(client.getInputStream());
                Object object;
                try {
                    while ((object = objectInputStream.readObject()) != null) {
                        if (object.getClass().isInstance(new String())) {
                            String zeile = (String) object;
                            Ausgabe.print(zeile);
                            String[] lines = zeile.split(ServerInterface.MESSAGE_TRENNUNG);
                            switch (lines[0]) {
                                case ServerInterface.MESSAGE_EXIT:
                                    ServerController.getInstance().disconnect();
                                    Ausgabe.print("Server wird geschlossen, Verbindung getrennt");
                                    break;
                                case ServerInterface.MESSAGE_START_CLIENT:
                                    ServerController.getInstance().startClient(lines[1], lines[2]);
                                    break;
                                case ServerInterface.MESSAGE_SET_ON_MOUSE_DRAGGED:
                                    ModelController.getInstance().rotateWorld(Double.parseDouble(lines[1]), Double.parseDouble(lines[2]), Double.parseDouble(lines[3]), Double.parseDouble(lines[4]), Double.parseDouble(lines[5]), Double.parseDouble(lines[6]), Double.parseDouble(lines[7]), Double.parseDouble(lines[8]), Double.parseDouble(lines[9]), Double.parseDouble(lines[10]), Double.parseDouble(lines[11]), Double.parseDouble(lines[12]));
                                    break;
                                case ServerInterface.MESSAGE_UPDATE_GUI_ELEMENTS:
                                    Platform.runLater(() -> {
                                        PolyederController.getInstance().updateGuiProperties(lines[1]);
                                    });
                                    break;
                                case ServerInterface.MESSAGE_TRANSLATE_X_AXIS:
                                case ServerInterface.MESSAGE_TRANSLATE_Y_AXIS:
                                    ModelController.getInstance().translate(Double.parseDouble(lines[1]), Double.parseDouble(lines[2]), Double.parseDouble(lines[3]));
                                    break;
                            }
                        } else if (object.getClass().isInstance(new ArrayList<Triangle>())) {
                            PolyederController.getInstance().getPolyeder().setTriangleList((ArrayList<Triangle>) object);
                            Platform.runLater(() -> {
                                PolyederController.getInstance().getPolyeder().updatePolyederInfo();
                                ModelController.getInstance().buildModel();
                            });
                        }
                        try {
                            objectInputStream = new ObjectInputStream(client.getInputStream());
                        } catch (SocketException e) {
                            Ausgabe.print("Verbindung abgebrochen");
                            break;
                        }
                    }
                } catch (ClassNotFoundException e) {
                    Ausgabe.print("classnotfoundexception");
                }
            } catch (IOException e) {
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

    public void closeAll() {
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
