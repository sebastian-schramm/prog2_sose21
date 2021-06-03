package model;

import model.interfaces.AllgemeineKonstanten;

import java.util.ArrayList;
import java.util.Collections;

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
        //sortTriangles();
        calcSurface();
        return this.surface;

    }



    public float getSurfaceThreads()
    {
        int threadAmount = AllgemeineKonstanten.THREAD_AMOUNT;
        int[] range = new int[threadAmount + 1];
        for (int i = 1; i < range.length - 1; ++i) {
            range[i] = range[i - 1] + triangleList.size() / threadAmount;
        }
        range[range.length - 1] = triangleList.size();


        Thread[] threads = new Thread[threadAmount];;

        for (int i = 0; i < threads.length; i++) {
            int start = range[i];
            int end = range[i + 1];
            int tmp = i;
            threads[i] = new Thread()
            {
                public void run()
                {
                    for (int j = start; j < end; j++) {
                        triangleList.get(j).calcArea();
                    }
                    System.out.println("Thread Nummer: " + tmp + " beendet");
                    //TODO: lese den scheiß -> https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CountDownLatch.html
                }
            };
            threads[i].start();
        }

        /*for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        //sortTriangles();
        calcSurface();
        return this.surface;
    }


}
