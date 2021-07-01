package utilities;

import controller.NetworkController;

import java.net.ServerSocket;

public class Server extends Thread {

    public Server() {
        NetworkController.getInstance().getNetwork().ConnectionStatus().setValue(ConnectionStatusMessage(connectionStatusEnum.Offline));
    }

    @Override
    public void run() {
        NetworkController.getInstance().getNetwork().ConnectionStatus().setValue(ConnectionStatusMessage(connectionStatusEnum.Connecting));
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
        NetworkController.getInstance().getNetwork().ConnectionStatus().setValue(ConnectionStatusMessage(connectionStatusEnum.Connected));
    }

    private static String ConnectionStatusMessage(connectionStatusEnum stautus) {
        switch (stautus) {

        }
        return "";
    }

    public static enum connectionStatusEnum {
        Offline,
        Connecting,
        Connected,
        Disconnecting,
        Disconnected,
        WrongPort;
    }
}
