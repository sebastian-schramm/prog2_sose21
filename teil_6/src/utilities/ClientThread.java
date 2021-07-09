package utilities;

import controller.ServerController;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import view.Ausgabe;

public class ClientThread extends Thread {
    
    private boolean client_main_loop_running = true;
    private static Socket socket = null;
    private static PrintWriter printWriter = null;

    public void sendeKommando(String message) {
        printWriter.println(message);

    }

    @Override
    public void run() {
//        while (client_main_loop_running) {
            try {
                socket = new Socket(ServerController.getInstance().getServerIpAddress().getValue(), Integer.parseInt(ServerController.getInstance().getPort().getValue()));
            } catch (IOException e) {
                System.out.println("Ausnahme Socket-Konstruktor.");
            }
            try {
                 printWriter = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
            }
//        }
    }

    private static void starten() {

    }
}
