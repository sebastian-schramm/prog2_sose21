package utilities;

import controller.ServerController;
import javafx.application.Platform;
import model.Triangle;
import model.interfaces.ServerInterface;
import view.AlertMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
/**
 * ClientThread Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class ClientThread extends Thread {

    private static Socket socket = null;
    private static PrintWriter printWriter = null;
    private boolean client_main_loop_running = true;

    public static Socket getSocket() {
        return socket;
    }

    public void sendeMesh(ArrayList<Triangle> triangleArrayList) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            if (triangleArrayList != null) {
                objectOutputStream.writeObject(triangleArrayList);
            }
        } catch (IOException e) {
            AlertMessage.errorMessage(ServerInterface.SERVER_COULD_NOT_SEND_TRIANGLES, ServerInterface.SERVER_ERROR);
        }
    }

    public void sendeMessage(String message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            if (message != null) {
                objectOutputStream.writeObject(message);
            }
        } catch (SocketException ex) {
//            AlertMessage.errorMessage(ServerInterface.CONNECTION_CLOSED, ServerInterface.SERVER_ERROR);
        } catch (IOException e) {
//            AlertMessage.errorMessage(ServerInterface.CONNECTION_CLOSED, ServerInterface.SERVER_ERROR);
        }
    }

    @Override
    public void run() {
        while (client_main_loop_running) {
            socket = null;
            printWriter = null;
            try {
                socket = new Socket(ServerController.getInstance().getServerIpAddress().getValue(), Integer.parseInt(ServerController.getInstance().getPort().getValue()));
                ServerController.getInstance().setConnectionStatus(ServerInterface.CONNECTED_WITH_SERVER);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (ConnectException e) {
                Platform.runLater(() -> {
                    AlertMessage.errorMessage(ServerInterface.CONNECTION_CLOSED, ServerInterface.CONNECTION_CLOSED_BY_CLIENT);
                });
                break;

            } catch (IOException e) {
                Platform.runLater(() -> {
                    AlertMessage.errorMessage(ServerInterface.CONNECTION_CLOSED, ServerInterface.CONNECTION_CLOSED_BY_CLIENT);
                });
            }
            try {
                printWriter = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {

            }
            ServerController.getInstance().getPort().setValue(Integer.parseInt(ServerController.getInstance().getPort().getValue()) + 1 + "");
            ServerController.getInstance().startServer(ServerController.getInstance().getLokaleIpAddress().getValue(), String.valueOf(Integer.parseInt(ServerController.getInstance().getPort().getValue())));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendeMessage(ServerInterface.MESSAGE_START_CLIENT + ServerInterface.MESSAGE_TRENNUNG + ServerController.getInstance().getLokaleIpAddress().getValue() + ServerInterface.MESSAGE_TRENNUNG + Integer.parseInt(ServerController.getInstance().getPort().getValue()));

            synchronized (ServerController.getInstance().getServerThread()) {
                ServerController.getInstance().getServerThread().notify();
            }
        }
    }

    public void closeAll() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (printWriter != null)
            printWriter.close();
    }
}
