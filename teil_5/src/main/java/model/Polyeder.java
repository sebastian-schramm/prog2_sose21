package model;

import model.interfaces.AllgemeineKonstanten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

public class Polyeder extends Thread {
    private ArrayList<Triangle> triangleList;

    private double surface = 0.0;
    private double volume = 0.0;

    public Polyeder(ArrayList<Triangle> triangleList) {
        this.triangleList = triangleList;
    }

    private void calcSurface () {
        this.surface = 0;
        for (Triangle triangle : triangleList) {
            this.surface += triangle.getArea();
        }
    }

    private void calcVolume() {
        this.volume = 0;
        for (Triangle triangle : triangleList) {
            this.volume = triangle.getVolume();
        }
    }

    private void sortTriangles () {
        Collections.sort(triangleList);
    }

    private void printAreas() {
        for (Triangle triangle : triangleList) {
            System.out.println(triangle.getArea());
        }
        System.out.println("--------------------------");
    }

    public double getSurfaceSerial () {
        for (Triangle triangle : triangleList) {
            triangle.calcArea();
        }
        sortTriangles();
        calcSurface();
        return this.surface;

    }
    public double getSurfaceThreads() {
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
                    triangleList.get(j).calcVolume();
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
        calcSurface();
        calcVolume();
        return this.surface;
    }

    public double getSurface() {
        return surface;
    }

    public double getVolume() {
        return volume;
    }
}
