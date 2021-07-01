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


    public Polyeder() {
        this.triangleList = new ArrayList<>(0);
        this.area = 0.0;
        this.volume = 0.0;
    }

    public Polyeder(ArrayList<Triangle> triangleList, boolean threading) {
        this.triangleList = triangleList;

        if (threading)
            getSurfaceThreads();
        else
            getSurfaceSerial();

        sortTriangles();
        calcSurface();
        calcVolume();
    }

    public void getSurfaceSerial () {
        for (Triangle triangle : triangleList) {
            triangle.calcArea();
        }
    }

    private void getSurfaceThreads() {
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

    private void calcSurface() {
        this.area = 0.0;
        for (int i = 0; i < triangleList.size(); ++i) {
            this.area += triangleList.get(i).getArea();
        }
    }

    public double getSurface() {
        return this.area;
    }

    private void calcVolume() {
        this.volume = 0.0;
        if (triangleList.size() > 1)
        for (Triangle triangle : triangleList) {
            this.volume += triangle.getVolume();
        }
    }

    public double getVolume() {
        return this.volume;
    }

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

    public int getTriangleListSize()
    {
        return triangleList.size();
    }
}
