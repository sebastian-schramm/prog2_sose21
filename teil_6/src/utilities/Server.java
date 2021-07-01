package utilities;

import controller.NetworkController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.ConnectionStatusEnum;

import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private ServerSocket derServer;
    private Socket einClient;
    private StringProperty port = new SimpleStringProperty("0000");

    public Server() {
        NetworkController.getInstance().getNetwork().ConnectionStatus(ConnectionStatusEnum.OFFLINE);
    }

    @Override
    public void run() {
        NetworkController.getInstance().getNetwork().ConnectionStatus(ConnectionStatusEnum.CONNECTING);
        try {
            derServer = new ServerSocket(Integer.parseInt(port.getValue()));
        } catch (Exception e) {
//            System.out.println(Network.connectionStatusEnum.Connecting + " failed");
//            System.exit(0);
        }
//        MyClient clientThread = new MyClient();
//        clientThread.start();
        try {
            einClient = derServer.accept();
        } catch (Exception e) {
            System.out.println("Fehler accept!");
            System.exit(0);
        }
//        while (true) {
//            System.out.println("Hello");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        NetworkController.getInstance().getNetwork().ConnectionStatus(ConnectionStatusEnum.CONNECTED);
    }
}
