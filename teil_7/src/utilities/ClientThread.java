package utilities;

import controller.ServerController;
import javafx.application.Platform;
import model.Triangle;
import model.interfaces.ServerInterface;
import view.AlertMessage;
import view.Ausgabe;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientThread extends Thread {

    private static Socket socket = null;
    private static PrintWriter printWriter = null;
    private boolean client_main_loop_running = true;

    public static Socket getSocket() {
        return socket;
    }

    public void sendeMesh(ArrayList<Triangle> triangleArrayList) {
        System.out.println("Mesh wird gesendet");
        try {
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            if (triangleArrayList != null) {
                objectOutputStream.writeObject(triangleArrayList);
            }
        } catch (IOException e) {
            Ausgabe.print("Fehler beim Versenden der Trianglemesh");
            e.printStackTrace();
        }
    }

    public void sendeRotation(String message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            if (message != null) {
                objectOutputStream.writeObject(message);
            }
        } catch (SocketException ex) {
            Ausgabe.print("Keine Verbindung mehr");

        } catch (IOException e) {
            Ausgabe.print("Fehler beim Versenden der Affine");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (client_main_loop_running) {
            socket = null;
            printWriter = null;
            System.out.println("Verbinde Client");
            try {
                socket = new Socket(ServerController.getInstance().getServerIpAddress().getValue(), Integer.parseInt(ServerController.getInstance().getPort().getValue()));
//            ServerController.getInstance().getPort().setValue(Integer.parseInt(ServerController.getInstance().getPort().getValue()) + "");
                ServerController.getInstance().setConnectionStatus(ServerInterface.CONNECTED_WITH_SERVER);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.out.println("Ausnahme Socket-Konstruktor.");
            } catch (ConnectException e) {
                System.out.println("con ref");
                Platform.runLater(() -> {
                    AlertMessage.errorMessage("Port stimmt nicht");
                });
                break;

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ausnahme Socket-Konsdtruktor.");
            }
            try {
                printWriter = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {

            }
            ServerController.getInstance().getPort().setValue("40405");
            System.out.println("Alles fertig");
            ServerController.getInstance().startServer(ServerController.getInstance().getLokaleIpAddress().getValue(), String.valueOf(Integer.parseInt(ServerController.getInstance().getPort().getValue())));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendeRotation("startClient;" + ServerController.getInstance().getLokaleIpAddress().getValue() + ";" + Integer.parseInt(ServerController.getInstance().getPort().getValue()));

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
