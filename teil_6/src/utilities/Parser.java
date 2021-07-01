package utilities;

import controller.TriangleController;
import model.Triangle;
import model.Vertex;
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

    private static String[] coords;
    private static int[] index;
    private static int counter;
    private static int triangleNumber;

    /**
     * Liest eine Datei aus und überprüft ob es sich dabei um ein ASCII oder Binary Format handelt
     * @param file
     * @return
     */
    public static ArrayList<Triangle> ladeStlAusDatei(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)){
            Ausgabe.loadingFile("");
            DataInputStream input = new DataInputStream(inputStream);

            boolean isASCII = false;
            triangleNumber = 0;
            long fileSize = file.length();
            byte[] firstBlock = new byte[80];

            System.out.println("Filesize in Bytes : " + fileSize);

            //Liest die ersten 6 Bytes aus der Datei aus
            input.read(firstBlock);
            String block = readBlock(firstBlock);
            String line;

            if (block.contains(AllgemeineKonstanten.ASCII_STL_START_LINE)) {
                input.readLine();
                System.out.println("Solid am anfang gefunden, koennte ASCII sein!");
                line = input.readLine();
                if (line.toLowerCase().startsWith(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[0]))
                    System.out.println("Startet nicht mit facet");

                long lineCount = getLineCount(file);
                triangleNumber = (int) ((lineCount - 2 ) / 7);

                if ((triangleNumber * 7 + 2) != lineCount)
                    throw new Exception(StringKonstanten_DE.FILE_LINE_COUNT_NOT_VALID);

                TriangleController.getInstance().initTriangle(triangleNumber);
                readASCIIFile(file);
            } else {
                System.out.println("Kein Solid am anfang gefunden, koennte Binary sein!");
                triangleNumber = Integer.reverseBytes(input.readInt());

                if (!(fileSize == (triangleNumber * 50 + 84)))
                    throw new Exception(StringKonstanten_DE.FILE_LINE_COUNT_NOT_VALID);

                TriangleController.getInstance().initTriangle(triangleNumber);
                readBinaryFile(file);
            }
            System.out.println(triangleNumber + " Dreiecke gefunden");

            Ausgabe.loadingFileComplete();

            return TriangleController.getInstance().getTriangleList();
        } catch (FileNotFoundException e) {
            Ausgabe.loadingFileFailed();
        } catch (IOException e) {
            Ausgabe.loadingFileFailed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TriangleController.getInstance().initTriangle(triangleNumber);
        return TriangleController.getInstance().getTriangleList();
    }

    /**
     * Gibt die anzahl der Zeilen zurück
     * @param filePath
     * @return lineCounter
     * @throws Exception
     */
    private static long getLineCount(File filePath) throws Exception {
        long lineCounter = 0;
        try (FileReader reader = new FileReader(filePath + "");
             BufferedReader bufferedReader = new BufferedReader((reader))) {
            String line, last = "";
            while ((line = bufferedReader.readLine()) != null) {
                ++lineCounter;
                last = line;
            }
            if (lineCounter == 0)
                throw new Exception(StringKonstanten_DE.FILE_EMPTY);
            if (!last.toLowerCase().startsWith(AllgemeineKonstanten.ASCII_STL_END_LINE))
                throw new Exception(StringKonstanten_DE.FILE_LAST_LINE_NOT_VALID);
        }
        return lineCounter;
    }

    private static String readBlock(byte[] firstBlock) {
        Charset charset = Charset.forName("UTF-8");
        CharBuffer decode = charset.decode(ByteBuffer.wrap(firstBlock, 0, firstBlock.length));
        return decode.toString().toLowerCase();
    }

    private static void readBinaryFile(File filePath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(filePath);
             BufferedInputStream input = new BufferedInputStream(inputStream)) {
            byte[] header = new byte[80];
            byte[] b4=new byte[4];
            byte[] attribute=new byte[2];
            byte[] normal = new byte[12];

            input.read(header);
            input.read(b4);

            Vertex[] vertices;
            while (input.available() > 0) {
                vertices = new Vertex[AllgemeineKonstanten.TRIANGLE_VERTICES];

                for (int i = 0; i < 4; ++i) {
                    input.read(normal);
                    vertices[i] = getVertex(normal);
                }
                TriangleController.getInstance().constructTriangle(vertices);

                input.read(attribute);
            }
        }
    }

    private static void readASCIIFile(File filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader((reader))) {
            Ausgabe.foundASCIIFile();
            
            CountDownLatch countDownLatch = new CountDownLatch(AllgemeineKonstanten.THREAD_AMOUNT);
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(AllgemeineKonstanten.THREAD_AMOUNT);

            Vertex[] vertices;

            bufferedReader.readLine();
            for (int i = 0; i < triangleNumber; i++) {
                vertices = new Vertex[4];
                vertices[0] = stringToVertexNormal(bufferedReader.readLine());
                bufferedReader.readLine();
                vertices[1] = stringToVertexFacet(bufferedReader.readLine());
                vertices[2] = stringToVertexFacet(bufferedReader.readLine());
                vertices[3] = stringToVertexFacet(bufferedReader.readLine());
                bufferedReader.readLine();
                bufferedReader.readLine();

//                for (int n = 0; n < 10; ++n)
                    TriangleController.getInstance().constructTriangle(vertices);

                if (i %250000 == 0)
                        executor.submit(() -> {
                            System.gc();
                            countDownLatch.countDown();
                            return null;
                        });
            }
            System.gc();
            System.gc();
            executor.shutdown();
        }
    }

    private static Vertex stringToVertexNormal(String line) {
        return getVertex(line.stripLeading().substring(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[0].length() + 1).stripLeading());
//        coords = line.stripLeading().substring(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[0].length() + 1).stripLeading().split(" ");
//        return new Vertex(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]));
    }

    private static Vertex stringToVertexFacet(String line) {
        return getVertex(line.stripLeading().substring(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[2].length() + 1).stripLeading());
//        coords = line.substring(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[2].length() + 1).stripLeading().split(" ");
//        return new Vertex(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]));
    }

    private static Vertex getVertex(byte[] attribute) {
        ByteBuffer bb = ByteBuffer.wrap(attribute).order(ByteOrder.LITTLE_ENDIAN);

        return new Vertex(bb.getFloat(), bb.getFloat(),bb.getFloat());
    }

    private static Vertex getVertex(String value) {
        index = new int[2];
        counter = 0;
        for(int i = 0; i < value.length(); ++i)
            if(Character.isWhitespace(value.charAt(i)))
                index[counter++] = i;

        return new Vertex((float) Double.parseDouble(value.substring(0, index[0])), (float) Double.parseDouble(value.substring(index[0] + 1, index[1])), (float) Double.parseDouble(value.substring(index[1] + 1, value.length())));
    }

    private static Vertex stringToVertex(String line) {
        coords = line.replace(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[0], "").replace(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[2], "").stripLeading().split(" ");
        return new Vertex(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]));
    }
}