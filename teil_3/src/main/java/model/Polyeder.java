package model;

import model.interfaces.AllgemeineKonstanten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Polyeder extends Thread {
    private ArrayList<Triangle> triangleList;
    private float surface = 0.0f;

    public Polyeder(ArrayList<Triangle> triangleList) {
        this.triangleList = triangleList;
    }

    private void calcSurface ()
    {
        this.surface = 0;
        for (Triangle triangle : triangleList)
        {
            float tmp = triangle.getArea();
            this.surface += tmp;
        }
    }

    private void sortTriangles ()
    {
        Collections.sort(triangleList);
    }

    private void printAreas()
    {
        for (Triangle triangle : triangleList)
        {
            System.out.println(triangle.getArea());
        }
        System.out.println("--------------------------");
    }

    public float getSurfaceSerial ()
    {
        for (Triangle triangle : triangleList)
        {
            triangle.calcArea();
        }
//        sortTriangles();
        calcSurface();
        return this.surface;

    }
    public float getSurfaceThreads() {
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
                //TODO: lese den scheiß -> https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CountDownLatch.html
                for (int j = start; j < end; j++) {
                    triangleList.get(j).calcArea();
                }
                return null;
            });
        }
        executor.shutdown();

//        sortTriangles();
        calcSurface();
        return this.surface;
    }


}
