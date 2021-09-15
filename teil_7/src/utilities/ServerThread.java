package utilities;

import controller.ModelController;
import controller.PolyederController;
import controller.ServerController;
import javafx.application.Platform;
import model.Triangle;
import model.interfaces.ServerInterface;
import view.AlertMessage;
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

    private ObjectInputStream objectInputStream;

    /**
     * Die run() Methode startet den ServerSocket und wartet auf eine eingehende verbindung.
     */
    @Override
    public void run() {
        while (serverMainLoopRunning) {
            synchronized (this) {
                if (waiting) {
                    client = null;

                    ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_START);
                    if (server == null)
                        try {
                            server = new ServerSocket(Integer.parseInt(ServerController.getInstance().getPort().getValue()));

                            ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_WAIT_FOR_CONNECTION);

                        } catch (IOException e) {
                            Ausgabe.print(ServerInterface.SERVER_INIT_ERROR);
                            ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_PORT_ALREADY_USED);
                        }

                    try {
                        client = server.accept();
                        ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_CONNECTION_DETECTED);
                    } catch (IOException e) {
                        Ausgabe.print(ServerInterface.SERVER_ACCEPT_ERROR);
                        ServerController.getInstance().setConnectionStatus(ServerInterface.SERVER_CONNECTION_FAILED);
                    }

                    startObjectListener();
                }
            }
        }
    }

    /**
     * Die startObjectListener() Methode wertet die empfangenen Nachrichten aus.
     * Handelt es sich um ein String, so wird geprueft welcher Befehl am Anfang steht und die entsprechende Methode aufgerufen.
     * Wenn es eine ArrayList ist, wird diese dem Polyeder uebergeben und neu dargestellt.
     */
    private void startObjectListener() {
        if (client != null && client.isConnected()) {
            try {
                objectInputStream = new ObjectInputStream(client.getInputStream());
                Object object;
                try {
                    while ((object = objectInputStream.readObject()) != null) {
                        if (object.getClass().isInstance(new String())) {
                            String zeile = (String) object;
                            String[] lines = zeile.split(ServerInterface.MESSAGE_TRENNUNG);
                            switch (lines[0]) {
                                case ServerInterface.MESSAGE_EXIT:
                                    ServerController.getInstance().disconnect();
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
                            Platform.runLater(() -> {
                                AlertMessage.errorMessage(ServerInterface.CONNECTION_CLOSED, ServerInterface.CONNECTION_CLOSED_BY_CLIENT);
                            });
                            break;
                        }
                    }
                } catch (ClassNotFoundException e) {
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * Die setServerMainLoopRunning() Methode setzt einen boolean Wert fuer den MainLoop des Servers.
     *
     * @param serverMainLoopRunning Der Status des MainLoops vom Server.
     **/
    public void setServerMainLoopRunning(boolean serverMainLoopRunning) {
        this.serverMainLoopRunning = serverMainLoopRunning;
    }

    /**
     * Die closeAll() Methode schliesst den Server und den Client.
     */
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
    }
}
