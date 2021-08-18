package utilities;

import controller.ServerController;
import model.interfaces.ServerInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private boolean client_main_loop_running = true;
    private static Socket socket = null;
    private static PrintWriter printWriter = null;

    public void sendeKommando(String message) {
        System.out.println("Sende Message" + message);
        if (printWriter != null)
            printWriter.println(message);
    }

    @Override
    public void run() {
//        while (client_main_loop_running) {
        try {
            socket = new Socket(ServerController.getInstance().getServerIpAddress().getValue(), Integer.parseInt(ServerController.getInstance().getPort().getValue()));
            ServerController.getInstance().getPort().setValue(Integer.parseInt(ServerController.getInstance().getPort().getValue()) + "");
            ServerController.getInstance().setConnectionStatus(ServerInterface.CONNECTED_WITH_SERVER);
        } catch (IOException e) {
            System.out.println("Ausnahme Socket-Konstruktor.");
        }
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {

        }

        sendeKommando("startClient;" + ServerController.getInstance().getLokaleIpAddress() + ";" + ServerController.getInstance().getPort());

        synchronized (ServerController.getInstance().getServerThread()) {
            ServerController.getInstance().getServerThread().notify();
        }
//        }
    }
}
