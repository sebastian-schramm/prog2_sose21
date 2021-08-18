package utilities;

import controller.PolyederController;
import model.Vertex;
import model.interfaces.AllgemeineKonstanten;
import resources.StringKonstanten_DE;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Parser {

    private static int[] index;
    private static int counter;
    private static int triangleNumber;

    /**
     * Liest eine Datei aus und überprüft ob es sich dabei um ein ASCII oder Binary Format handelt
     * @param file
     */
    public static void ladeStlAusDatei(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)){
            DataInputStream input = new DataInputStream(inputStream);

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
                System.out.println("Solid am anfang gefunden, könnte ASCII sein!");
                line = input.readLine();
                if (line.toLowerCase().startsWith(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[0]))
                    System.out.println("Startet nicht mit facet");

                long lineCount = getLineCount(file);
                triangleNumber = (int) ((lineCount - 2 ) / 7);
                PolyederController.getInstance().setTriangleAmount(triangleNumber);

                if ((triangleNumber * 7L + 2) != lineCount)
                    throw new Exception(StringKonstanten_DE.FILE_LINE_COUNT_NOT_VALID);

                PolyederController.getInstance().getPolyeder().initTriangleList(triangleNumber);
                readASCIIFile(file);
            } else {
                System.out.println("Kein Solid am anfang gefunden, könnte Binary sein!");
                triangleNumber = Integer.reverseBytes(input.readInt());
                PolyederController.getInstance().setTriangleAmount(triangleNumber);

                if (!(fileSize == (triangleNumber * 50L + 84)))
                    throw new Exception(StringKonstanten_DE.FILE_LINE_COUNT_NOT_VALID);

                PolyederController.getInstance().getPolyeder().initTriangleList(triangleNumber);
                readBinaryFile(file);
            }
            System.out.println(triangleNumber + " Dreiecke gefunden");

            //TODO Alert hier einbinden
//            Ausgabe.loadingFileComplete();

            return;
        } catch (FileNotFoundException e) {
//            Ausgabe.loadingFileFailed();
        } catch (IOException e) {
//            Ausgabe.loadingFileFailed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PolyederController.getInstance().getPolyeder().initTriangleList(triangleNumber);
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
        Charset charset = StandardCharsets.UTF_8;
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
                PolyederController.getInstance().getPolyeder().constructTriangle(vertices);
//                TriangleController.getInstance().constructTriangle(vertices);

                input.read(attribute);
            }
        }
    }

    private static void readASCIIFile(File filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader((reader))) {
//            Ausgabe.foundASCIIFile();
            
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
                    PolyederController.getInstance().getPolyeder().constructTriangle(vertices);
//                    TriangleController.getInstance().constructTriangle(vertices);

                if (i %250000 == 0)
                        executor.submit(() -> {
//                            System.gc();
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
    }

    private static Vertex stringToVertexFacet(String line) {
        return getVertex(line.stripLeading().substring(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[2].length() + 1).stripLeading());
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

        return new Vertex((float) Double.parseDouble(value.substring(0, index[0])), (float) Double.parseDouble(value.substring(index[0] + 1, index[1])), (float) Double.parseDouble(value.substring(index[1] + 1)));
    }

}