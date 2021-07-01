package controller;

public class MyClient extends Thread {
    
    public static void main(String[] args) {
        MyClient clientThread = new MyClient();
        clientThread.start();
    }
    
    public void run() {
        try {//warten bis der Server gestartet ist
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ;
        }
        java.io.PrintWriter aus = null;
        java.io.BufferedReader ein = null;
        java.io.BufferedReader tastatur = null;
        tastatur = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        java.net.Socket socket = null;
        try {
            socket = new java.net.Socket("127.0.0.1",40404);}
        catch (Exception e) {
            System.out.println("Ausnahme Socket-Konstruktor.");
            System.exit(0);
        }
        try {
            ein = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            aus = new java.io.PrintWriter(socket.getOutputStream(),true);
            System.out.println("Verbunden mit Host\n" + "Bitte Text eingeben (Ende mit leerer Zeile)");
            String eineZeile;
            while (true) {
                eineZeile = tastatur.readLine();
                if (eineZeile.length() > 0) {
                    aus.println(eineZeile);
                    System.out.println("Echo: " + ein.readLine());
                } else break;
            }
            aus.close(); ein.close();
            socket.close(); tastatur.close();
        } catch (Exception e) {System.out.println(e.toString());}
    }
}
