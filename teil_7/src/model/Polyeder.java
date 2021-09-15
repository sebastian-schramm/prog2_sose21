package model;

import javafx.scene.shape.TriangleMesh;
import model.interfaces.AllgemeineKonstanten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Polyeder Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class Polyeder {

    private ArrayList<Triangle> triangleList;

    private Double area;
    private Double volume;

    /**
     * Der Polyeder Konstruktor initialisiert alle Attribute der Klasse.
     * */
    public Polyeder() {
        this.triangleList = new ArrayList<>(0);
        this.area = 0.0;
        this.volume = 0.0;
    }

    /**
     * InitTriangleList erstellt eine ArrayList, die die Groesse nach der Anzahl der interpretierten Dreiecke setzt.
     * @param triangleNumber Enthaellt die Dreieck Anzahl
     * */
    public void initTriangleList(int triangleNumber) {
        triangleList = new ArrayList<>(triangleNumber);
    }

    /**
     * Baut ein Dreieck auf mit dem übergebenen Dreiecken und fügt dies gleich der ArrayList aus Triangles hinzu.
     * @param vertices Gibt das Array Vertices des Dreiecks an.
     * */
    public void constructTriangle(Vertex[] vertices) {
        triangleList.add(new Triangle(vertices[0], vertices[1], vertices[2], vertices[3]));
    }

    /**
     * Berechnet aus den Flaecheninhalt jedes Dreieck in der Dreiecks Liste.
     * Dies passiert als Serielle Berechnung.
     * */
    public void surfaceSerial() {
        for (Triangle triangle : triangleList) {
            triangle.calcArea();
        }
    }

    /**
     * Berechnet aus den Flaecheninhalt jedes Dreieck in der Dreiecks Liste.
     * Dies passiert als Parallele Berechnung.
     * */
    public void surfaceThreads() {
        CountDownLatch countDownLatch = new CountDownLatch(AllgemeineKonstanten.THREAD_AMOUNT);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(AllgemeineKonstanten.THREAD_AMOUNT);

        int[] range = new int[AllgemeineKonstanten.THREAD_AMOUNT + 1];
        for (int i = 1; i < range.length - 1; ++i) {
            range[i] = range[i - 1] + triangleList.size() / AllgemeineKonstanten.THREAD_AMOUNT;
        }
        range[range.length - 1] = triangleList.size();

        for (int i = 0; i < AllgemeineKonstanten.THREAD_AMOUNT; ++i) {
            int start = range[i];
            int end = range[i + 1];

            executor.submit(() -> {
                for (int j = start; j < end; j++) {
                    triangleList.get(j).calcArea();
                }
                countDownLatch.countDown();
                return null;
            });
        }
        executor.shutdown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualisiert die Informationen des Polyeders, nachdem ein neuer Polyeder eingelesen wurde.
     * */
    public void updatePolyederInfo() {
        boolean threading = true;

        if (threading)
            surfaceThreads();
        else
            surfaceSerial();

        sortTriangles();
        calcSurface();
        calcVolume();
    }

    /**
     * Nimmt den Flächeninhalt jedes Dreiecks in der ArrayList und addiert diese zusammen.
     * Das Ergebnis wird in dem Attribut area gespeichert und ergibt den Oberflaecheninhalt.
     * */
    private void calcSurface() {
        this.area = 0.0;
        for (Triangle triangle : triangleList) {
            this.area += triangle.getArea();
        }
    }

    /**
     * Nimmt das Volumen eines Tetraeders, von jedem Dreieck in der ArrayList und addiert diese zusammen.
     * Das Ergebnis wird in dem Attribut volume gespeichert und ergibt das Volumen des Polyeders.
     * */
    private void calcVolume() {
        this.volume = 0.0;
        if (triangleList.size() > 1)
            for (Triangle triangle : triangleList) {
                this.volume += triangle.getVolume();
            }
    }

    /**
     * Methode zur Sortierung der Dreiecke in der ArrayList triangleList.
     * */
    private void sortTriangles() {
        Collections.sort(triangleList);
    }

    /**
     * Methode zur Rueckgabe der ArrayList triangleList.
     * @return triangleList Das private Attribut trinagleList wird zurueckgegeben.
     * */
    public ArrayList<Triangle> getTriangleList() {
        return this.triangleList;
    }

    /**
     * Methode um eine neue Dreiecks Liste für das Attribut triangleList zu setzen.
     *
     * @param triangleList Die Dreiecksliste die neu gesetzt werden soll.
     * */
    public void setTriangleList(ArrayList<Triangle> triangleList) {
        this.triangleList = triangleList;
    }

    /**
     * Methode zur Rueckgabe des Volumen.
     * @return volume Das private Attribut volume wird zurueckgegeben.
     * */
    public Double getVolume() {
        return volume;
    }

    /**
     * Methode zur Rueckgabe des Oberflaecheninhalts.
     * @return area Das private Attribut area wird zurueckgegeben.
     * */
    public Double getArea() {
        return area;
    }
}
