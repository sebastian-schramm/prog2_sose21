package controller;

public class MyEcho {
    private static java.net.Socket einClient;
    private static java.net.ServerSocket derServer = null;

    public static void main(String[] args) {
        try {
            derServer = new java.net.ServerSocket(40404);
        } catch (Exception e) {
            System.out.println("Fehler Port!");
            System.exit(0);
        }
        System.out.println("Warte auf Verbindungsanforderung");
        MyClient clientThread = new MyClient();
        clientThread.start();
        try {
            einClient = derServer.accept();
        } catch (Exception e) {
            System.out.println("Fehler accept!"); System.exit(0);
        }
        System.out.println("Client verbunden.");
        bedieneClient();
    }

    private static void bedieneClient () {
        try {
            java.io.BufferedReader ein = new java.io.BufferedReader(new java.io.InputStreamReader(einClient.getInputStream()));
            java.io.PrintWriter aus = new java.io.PrintWriter(einClient.getOutputStream(),true);
            String zeile;
            while ((zeile = ein.readLine()) != null)
                if (zeile.length() > 0)
                    aus.println(doppelEcho(zeile));
            ein.close();
            aus.close();
            einClient.close();
        }
        catch (java.io.IOException e) {System.out.println("E/A-Fehler!");}
    }

    private static String doppelEcho(String zeile) {
        StringBuffer antwort = new StringBuffer();
        for (int i=0; i < zeile.length(); ++i) {
            antwort.append(zeile.charAt(i));
            antwort.append(zeile.charAt(i));
        }
        return new String(antwort);
    }
}
