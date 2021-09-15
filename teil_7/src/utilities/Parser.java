package utilities;

import controller.PolyederController;
import javafx.application.Platform;
import model.Vertex;
import model.interfaces.AllgemeineKonstanten;
import model.interfaces.GUIKonstanten;
import resources.StringKonstanten_DE;
import view.AlertMessage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Parser Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class Parser {

    private static int[] index;
    private static int counter;
    private static int triangleNumber;

    /**
     * Liest eine Datei aus und überprüft, ob es sich dabei um ein ASCII oder Binary Format handelt.
     *
     * @param file
     */
    public static void ladeStlAusDatei(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            DataInputStream input = new DataInputStream(inputStream);

            triangleNumber = 0;
            long fileSize = file.length();
            byte[] firstBlock = new byte[80];

            //Liest die ersten 6 Bytes aus der Datei aus
            input.read(firstBlock);
            String block = readBlock(firstBlock);
            String line;

            if (block.contains(AllgemeineKonstanten.ASCII_STL_START_LINE)) {
                input.readLine();
                line = input.readLine();
                if (line.toLowerCase().startsWith(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[0]))
                    throw new Exception(StringKonstanten_DE.FILE_FIRST_LINE_NOT_VALID);

                long lineCount = getLineCount(file);
                triangleNumber = (int) ((lineCount - 2) / 7);
                PolyederController.getInstance().getTriangleAmountProperty().setValue(triangleNumber);

                if ((triangleNumber * 7L + 2) != lineCount)
                    throw new Exception(StringKonstanten_DE.FILE_LINE_COUNT_NOT_VALID);

                PolyederController.getInstance().getPolyeder().initTriangleList(triangleNumber);
                readASCIIFile(file);
            } else {
                triangleNumber = Integer.reverseBytes(input.readInt());
                PolyederController.getInstance().getTriangleAmountProperty().setValue(triangleNumber);

                if (!(fileSize == (triangleNumber * 50L + 84)))
                    throw new Exception(StringKonstanten_DE.FILE_LINE_COUNT_NOT_VALID);

                PolyederController.getInstance().getPolyeder().initTriangleList(triangleNumber);
                readBinaryFile(file);
            }
            AlertMessage.showMessage(GUIKonstanten.LOADING_FILE_COMPLETE);

            return;
        } catch (FileNotFoundException e) {
            AlertMessage.showMessage(GUIKonstanten.LOADING_FILE_FAILED);
        } catch (IOException e) {
            AlertMessage.showMessage(GUIKonstanten.LOADING_FILE_FAILED);
        } catch (Exception e) {
            AlertMessage.showMessage(GUIKonstanten.LOADING_FILE_FAILED);
        }

        PolyederController.getInstance().getPolyeder().initTriangleList(triangleNumber);
    }

    /**
     * Gibt die anzahl der Zeilen zurück
     *
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

    /**
     * Liest einen Byte Block aus einer Datei aus und wandelt die Daten in Strings um.
     *
     * @param firstBlock Der Byteblock der ausgelesen werden soll.
     * @return Der dekodierte Inhalt des ausgelesenen Blocks als String.
     */
    private static String readBlock(byte[] firstBlock) {
        Charset charset = StandardCharsets.UTF_8;
        CharBuffer decode = charset.decode(ByteBuffer.wrap(firstBlock, 0, firstBlock.length));
        return decode.toString().toLowerCase();
    }

    /**
     * Liest eine STL-Datei aus, die im Binaerformat vorhanden ist.
     *
     * @param filePath Der Dateipfad zur STL-Datei
     * @throws IOException
     */
    private static void readBinaryFile(File filePath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(filePath);
             BufferedInputStream input = new BufferedInputStream(inputStream)) {
            byte[] header = new byte[80];
            byte[] b4 = new byte[4];
            byte[] attribute = new byte[2];
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

                input.read(attribute);
            }
        }
    }

    /**
     * Diese ueberladene Methode getVertex() liesst aus einen Byte Block die Koordinaten eines Vertex aus und baut diesen auf.
     *
     * @param attribute Der Byteblock, der interpretiert wird.
     * @return Ein neues Vertex Objekt.
     **/
    private static Vertex getVertex(byte[] attribute) {
        ByteBuffer bb = ByteBuffer.wrap(attribute).order(ByteOrder.LITTLE_ENDIAN);

        return new Vertex(bb.getFloat(), bb.getFloat(), bb.getFloat());
    }

    /**
     * Liest eine STL-Datei aus, die im Asciiformat vorhanden ist.
     *
     * @param filePath Der Dateipfad zur STL-Datei
     * @throws IOException
     */
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

                if (i % 250000 == 0)
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

    /**
     * Baut einen String auf, der die Normale eines Dreiecks enthaelt.
     *
     * @param line Der String aus der ASCII Datei, der die Normale enthaelt.
     * @return Gibt den fertiggestellten Vertex zurueck.
     **/
    private static Vertex stringToVertexNormal(String line) {
        return getVertex(line.stripLeading().substring(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[0].length() + 1).stripLeading());
    }

    /**
     * Baut einen String auf, der den Vertex eines Dreiecks enthaelt.
     *
     * @param line Der String aus der ASCII Datei, der die Normale enthaelt.
     * @return Gibt den fertiggestellten Vertex zurueck.
     **/
    private static Vertex stringToVertexFacet(String line) {
        return getVertex(line.stripLeading().substring(AllgemeineKonstanten.ASCII_TRIANGLE_PATTERN[2].length() + 1).stripLeading());
    }

    /**
     * Diese ueberladene Methode getVertex() liesst aus einen String die Koordinaten eines Vertex aus und baut diesen auf.
     *
     * @param value Der String, der interpretiert wird.
     * @return Ein neues Vertex Objekt.
     **/
    private static Vertex getVertex(String value) {
        index = new int[2];
        counter = 0;
        for (int i = 0; i < value.length() && counter < index.length; ++i)
            if (Character.isWhitespace(value.charAt(i)) && !(Character.isWhitespace(value.charAt(i + 1))))
                index[counter++] = i;

        return new Vertex((float) Double.parseDouble(value.substring(0, index[0])), (float) Double.parseDouble(value.substring(index[0] + 1, index[1])), (float) Double.parseDouble(value.substring(index[1] + 1)));
    }

}