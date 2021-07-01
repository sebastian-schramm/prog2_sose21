package controller;

public class PaketEmpfaenger extends Thread {
    public void run() {
        try {
            java.net.DatagramSocket socket = new java.net.DatagramSocket(6666);
            byte[] puffer = new byte[80];
            java.net.DatagramPacket paket = new java.net.DatagramPacket(puffer, puffer.length);
            while (true) {
                socket.receive(paket);
                System.out.println("Empfaenger: " + new String(puffer, 0, paket.getLength()));
            }
        } catch (Exception e) {System.out.println(e.getMessage());}
    }
}

