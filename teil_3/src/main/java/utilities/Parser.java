package utilities;

import controller.PolyederController;
import controller.TriangleController;
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
import java.util.ArrayList;
import java.util.Locale;

public class Parser {

    public static Triangle[] erzeugePolyeder(String dateiName, int flag)
    {
        if (flag == 0){
            return ladeStlAusDatei(dateiName);
        }
        else
        {
            return readAsciiFile(dateiName);
        }
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



            System.out.println(buf); // Ausgabe

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

    private static Triangle[] readAsciiFile(String dateiName)
    {
        String filePath = AllgemeineKonstanten.DEFAULT_RESOURCES_LOCATION + "" + dateiName + ".stl";
        String word;
        File file = new File(filePath);
        ArrayList <String> relevantData = new ArrayList();
        String vertex = AllgemeineKonstanten.VERTEX;
        String facetNormal = AllgemeineKonstanten.FACET_NORMAL;
        int count = 0;
        if (file.canRead() && file.isFile())
        {
            try {
                BufferedReader input = new BufferedReader(new FileReader(file));
                while ((word = input.readLine()) != null)
                {
                    word = word.stripLeading();
                    if (word.contains(vertex) || word.contains(facetNormal))
                    {
                        if (count < 4)
                        {
                            count += 1;
                            relevantData.add(word);
                        }
                        else
                        {
                            TriangleController.getInstance().constructTriangle(relevantData);
                            relevantData.clear();
                            relevantData.add(word);
                            count = 1;
                        }
                    }
                }
                input.close();
                TriangleController.getInstance().constructTriangle(relevantData);
            } catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
        else
        {
            System.err.println("Die Datei ist nicht korrekt angeben");
            System.exit(0);
        }
        return null;
    }
}
