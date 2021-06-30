package model;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.TriangleMesh;
import model.interfaces.AllgemeineKonstanten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

public class Polyeder extends Thread {
    private ArrayList<Triangle> triangleList;

    private Double area = 0.0;
    private Double volume = 0.0;
//    private StringProperty currentArea= new SimpleStringProperty(area.toString());
//    private StringProperty currentVolume = new SimpleStringProperty(volume.toString());
    private IntegerProperty aktuelleGesundheit = new SimpleIntegerProperty(10);

    public void setAktuelleGesundheit(int aktuelleGesundheit)
    {
        this.aktuelleGesundheit.set(aktuelleGesundheit);
    }

    public IntegerProperty aktuelleGesundheitProperty() {
        return this.aktuelleGesundheit;
    }

    public Polyeder() {
        this.triangleList = new ArrayList<>(0);
        this.area = 0.0;
        this.volume = 0.0;
    }

    public Polyeder(ArrayList<Triangle> triangleList) {
        this.triangleList = triangleList;
    }

    public double getSurfaceSerial () {
        for (Triangle triangle : triangleList) {
            triangle.calcArea();
        }
        sortTriangles();
        return this.area;

    }

    private double getSurfaceThreads() {
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
            int finalI = i;
            executor.submit(() -> {
                //TODO: lese den scheiß -> https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CountDownLatch.html
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

        sortTriangles();
        return this.area;
    }

    private void calcSurface () {
        this.area = 0.0;
        for (int i = 0; i < triangleList.size(); ++i) {
            this.area += triangleList.get(i).getArea();
        }
    }

    public double getSurface(boolean threading) {
        if (this.area == 0) {
            if (threading)
                getSurfaceThreads();
            else
                getSurfaceSerial();
            calcSurface();
        }
        return this.area;
    }

//    public void setCurrentSurfaceProperty(Double currentArea) {
//        this.currentArea.set(currentArea.toString());
//    }
//
//    public StringProperty getSurfaceProperty() {
//        System.out.println("Current Surface : " + this.currentArea.getValue());
//        return this.currentArea;
//    }

    private void calcVolume() {
        this.volume = 0.0;
        if (triangleList.size() > 1)
        for (Triangle triangle : triangleList) {
            this.volume += triangle.getVolume();
        }
    }

    public double getVolume() {
        if (this.volume == 0)
            calcVolume();
        return this.volume;
    }

//    public void setCurrentVolumeProperty(Double currentVolume) {
//        this.currentVolume.set(currentVolume.toString());
//    }
//
//    public StringProperty getVolumeProperty() {
//        System.out.println("Current Volume : " + this.currentVolume.getValue());
//        return this.currentVolume;
//    }

    private void sortTriangles () {
        Collections.sort(triangleList);
    }

    public TriangleMesh getMesh(){
        TriangleMesh mesh = new TriangleMesh();

        int faceCnt = 0;
        for(int x = 0; x < triangleList.size(); x++){
            for(int y = 0; y < 3; y++) {
                mesh.getTexCoords().addAll(((triangleList.get(x).getNormal().getX() + 1) / -2));
                mesh.getTexCoords().addAll(((triangleList.get(x).getNormal().getY() + 1) / -2));
                mesh.getTexCoords().addAll(((triangleList.get(x).getNormal().getZ() + 1) / -2));
            }

            for(int y = 0; y < 3; y++) {
                mesh.getPoints().addAll((triangleList.get(x).getVertex(y).getX()));
                mesh.getPoints().addAll((triangleList.get(x).getVertex(y).getY()));
                mesh.getPoints().addAll((triangleList.get(x).getVertex(y).getZ()));
            }

            mesh.getFaces().addAll(faceCnt, faceCnt, faceCnt + 1, faceCnt + 1, faceCnt + 2, faceCnt + 2);
            faceCnt += 3;
//            if (x %250000 == 0) {
//                System.gc();
//            }
        }
        return mesh;
    }

    public ArrayList<Triangle> getTriangleList()
    {
        return triangleList;
    }
}
