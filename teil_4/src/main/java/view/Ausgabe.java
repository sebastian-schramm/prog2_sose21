package view;

import model.interfaces.AllgemeineKonstanten;
import resources.StringKonstanten_DE;

public class Ausgabe {

    public static void loadingFile(String dateiName) {
        System.out.println(dateiName + " " + StringKonstanten_DE.FILE_LOADING);
    }

    public static void loadingFileComplete() {
        System.out.println(StringKonstanten_DE.FILE_LOADING_DONE);
    }

    public static void loadingFileFailed() {
        System.out.println(StringKonstanten_DE.FILE_LOADING_FAILED);
    }

    public static void trianglesFound(int number) {
        System.out.println(number + "" + StringKonstanten_DE.TRIANGLES_FOUND);
    }

    public static void foundASCIIFile() {
        System.out.println(StringKonstanten_DE.FOUND_ASCII_FILE);
    }

    public static void foundBinaryFile() {
        System.out.println(StringKonstanten_DE.FOUND_BINARY_FILE);
    }
}
