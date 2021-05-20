package utilities;

import controller.PolyederController;
import model.Triangle;
import model.interfaces.AllgemeineKonstanten;
import resources.StringKonstanten_DE;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class Parser {

    public static Triangle[] erzeugePolyeder(String dateiName) {
        return ladeStlAusDatei(dateiName);
    }

    private static Triangle[] ladeStlAusDatei(String dateiName) {
        String filePath = AllgemeineKonstanten.DEFAULT_RESOURCES_LOCATION + "" + dateiName + ".stl";
        try (FileInputStream inputStream = new FileInputStream(filePath)){
            DataInputStream input = new DataInputStream(inputStream);

            boolean isASCII = false;

            byte[] firstBlock = new byte[512];
            input.read(firstBlock);
            Charset charset = Charset.forName("UTF-8");
            CharBuffer decode = charset.decode(ByteBuffer.wrap(firstBlock, 0, 512));
            String buf = decode.toString().toLowerCase();
            StringBuffer sb = new StringBuffer();
            int inl = r


            System.out.println(buf);

//            byte[] header = new byte[80];
//            byte[] b4=new byte[4];
//            byte[] attribute=new byte[2];
//            byte[] normal = new byte[12];
//            input.read(header);
//            System.out.println(header[0]);
//            int numberTriangles = Integer.reverseBytes(input.readInt());
//            System.out.println(numberTriangles);

            System.out.println(StringKonstanten_DE.FILE_LOADING);
            System.out.println(StringKonstanten_DE.FILE_LOADING_DONE);
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            System.out.println(StringKonstanten_DE.FILE_LOADING_FAILED);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println(StringKonstanten_DE.FILE_LOADING_FAILED);
        }

//        try (FileReader reader = new FileReader(AllgemeineKonstanten.DEFAULT_RESOURCES_LOCATION + "" + dateiName + ".stl");
//            BufferedReader bufferedReader = new BufferedReader((reader))) {
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println("Line: " + line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return null;
    }
}
