package controller;

public class PaketSender {

    public static void main(String[] args) throws java.io.IOException {
        PaketEmpfaenger empfaenger = new PaketEmpfaenger();
        empfaenger.start();
        java.net.DatagramSocket sock = new java.net.DatagramSocket();
        java.net.InetAddress addr = java.net.InetAddress.getByName("localhost");
        while (true) {
            String zufallsString = zufallString();
            System.out.println("Sender: " + zufallsString);
            byte[] Text = zufallsString.getBytes();
            java.net.DatagramPacket Paket = new java.net.DatagramPacket(
                    Text, zufallsString.length(),addr,6666);
            sock.send(Paket);
        }
    }

    private static String zufallString() {
        StringBuilder text = new StringBuilder();
        for (int i=0; i < 20; ++i)
            text.append((char)(int)(Math.random()*26+97));
        return text.toString();
    }
}

