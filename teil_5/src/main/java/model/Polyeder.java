package model;

import javafx.scene.shape.TriangleMesh;
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

    private double getSurfaceThreads() {
//        triangleList.parallelStream().forEach(Triangle::calcArea);

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
        return this.surface;
    }

    public double getSurface(boolean threading) {
        if (this.surface == 0) {
            if (threading)
                getSurfaceThreads();
            else
                getSurfaceSerial();
            calcSurface();
        }
        return this.surface;
    }

    public double getVolume() {
        if (this.volume == 0)
            calcVolume();
        return this.volume;
    }

    public float[] getPoints(TriangleMesh mesh) {
        float[] points = new float[triangleList.size() * 3 * 3];

        for (int i = 0; i < triangleList.size(); ++i) {
            for (int n = 0; n < 3; ++n) {
                points[i * 9 + n * 3] = triangleList.get(i).getVertex(n).getX()/6;
                points[i * 9 + n * 3 + 1] = triangleList.get(i).getVertex(n).getY()/6;
                points[i * 9 + n * 3 + 2] = triangleList.get(i).getVertex(n).getZ()/6;
            }
        }

        return points;
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
        }
        return mesh;
    }

    public int[] getFaces() {
        int[] faces = new int[triangleList.size() * 3];

        for (int i = 0; i < triangleList.size(); ++i) {
            faces[i * 3] = (int) triangleList.get(i).getNormal().getX();
            faces[i * 3 + 1] = (int) triangleList.get(i).getNormal().getY();
            faces[i * 3 + 2] = (int) triangleList.get(i).getNormal().getZ();
        }

        return faces;
    }
}
