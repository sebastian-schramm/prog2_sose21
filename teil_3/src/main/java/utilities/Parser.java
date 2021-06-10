package utilities;

import controller.TriangleController;
import model.Triangle;
import model.interfaces.AllgemeineKonstanten;
import resources.StringKonstanten_DE;
import view.Ausgabe;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Parser {

    public static ArrayList<Triangle> ladeStlAusDatei(String dateiName) {
        String filePath = AllgemeineKonstanten.DEFAULT_RESOURCES_LOCATION + "" + dateiName + ".stl";

        try (FileInputStream inputStream = new FileInputStream(filePath)){
            Ausgabe.loadingFile();
            DataInputStream input = new DataInputStream(inputStream);

            boolean isASCII = false;
            byte[] firstBlock = new byte[128];
            input.read(firstBlock);
            String block = readBlock(firstBlock);

            if (block.contains(AllgemeineKonstanten.SOLID))
                readASCIIFile(filePath);
            else
                readBinaryFile(filePath);

            ArrayList<Triangle> triangles = TriangleController.getInstance().getTriangleList();

            Ausgabe.loadingFileComplete();
            Ausgabe.trianglesFound(triangles.size());

            return triangles;
        } catch (FileNotFoundException e) {
            Ausgabe.loadingFileFailed();
        } catch (IOException e) {
            Ausgabe.loadingFileFailed();
        }

        return TriangleController.getInstance().getTriangleList();
    }

    private static String readBlock(byte[] firstBlock) {
        Charset charset = Charset.forName("UTF-8");
        CharBuffer decode = charset.decode(ByteBuffer.wrap(firstBlock, 0, 128));
        return decode.toString().toLowerCase();
    }

    //TODO
    private static boolean isValidASCII(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader((reader))) {
            String line;
            int lineCounter = 0;
            while ((line = bufferedReader.readLine()) != null) {
                ++lineCounter;
            }
            bufferedReader.lines();
            System.out.println(bufferedReader.lines());
        }
        return false;
    }

    private static void readBinaryFile(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        DataInputStream input = new DataInputStream(inputStream);
        Ausgabe.foundBinaryFile();

        byte[] header = new byte[80];
        byte[] b4=new byte[4];
        byte[] attribute=new byte[2];
        byte[] normal = new byte[12];

        input.read(header);

//        int triangleNumber = Integer.reverseBytes(input.readInt());
//        System.out.println("Dreiecke gefunden: " + triangleNumber);
        input.read(b4);
        int temp = ByteBuffer.wrap(b4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.println("Dreiecke gefunden: " + temp);
        float temp2;

        String[] relevantData = new String[4];

        while (input.available() > 0) {
            for (int i = 0; i < 4; ++i) {
                for (int n = 0; n < 3; ++n) {
                    input.read(b4);
                    temp2 = ByteBuffer.wrap(b4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//                    System.out.print(temp2 + " ");
                }
//                System.out.println();
            }
//            System.out.println();

            input.read(attribute);
        }
    }

    private static void readASCIIFile(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader((reader))) {
            Ausgabe.foundASCIIFile();

            String line;
            String[] relevantData = new String[4];

            int counter = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(AllgemeineKonstanten.FACET_NORMAL) || line.contains(AllgemeineKonstanten.VERTEX)) {
                    relevantData[counter++] = line;
                }
                if (counter >= 4) {
                    TriangleController.getInstance().constructTriangle(relevantData);
                    counter = 0;
                }
            }
        }
    }

    private static int getTrianglesNumber(String filePath) throws IOException {
        int counter = 0;
        try (FileReader reader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader((reader))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ++counter;
            }
        }
        return counter/7;
    }
}
//            for (int i = 0; i < triangles.size(); ++i) {
//                System.out.print(triangles.get(i).getNormal().getX() + " ");
//                System.out.print(triangles.get(i).getNormal().getY() + " ");
//                System.out.print(triangles.get(i).getNormal().getZ() + "\n");
//                for (int n = 0; n < 3; ++n) {
//                    System.out.print(triangles.get(i).getVertex(n).getX() + " ");
//                    System.out.print(triangles.get(i).getVertex(n).getY() + " ");
//                    System.out.print(triangles.get(i).getVertex(n).getZ() + "\n");
//                }
//                System.out.println();
//            }