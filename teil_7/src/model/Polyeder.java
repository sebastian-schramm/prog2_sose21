package model;

import controller.ModelController;
import controller.PolyederController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import model.interfaces.AllgemeineKonstanten;
import model.interfaces.GUIKonstanten;
import utilities.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Polyeder extends Thread {

    private ArrayList<Triangle> triangleList;

    private Double area;
    private Double volume;

    public Polyeder() {
        this.triangleList = new ArrayList<>(0);
        this.area = 0.0;
        this.volume = 0.0;
    }

    public void loadFile(File file, Stage stage) {
        boolean threading = true;
        if (file != null) {
//            Thread loadingThread = new Thread() {
//                @Override
//                public void run() {
//                    System.out.println("Thread start!");
                    Parser.ladeStlAusDatei(file);


                    if (threading)
                        getSurfaceThreads();
                    else
                        getSurfaceSerial();

                    sortTriangles();
                    calcSurface();
                    calcVolume();


//                    Platform.runLater(() -> {
                        stage.setTitle(GUIKonstanten.MY_TITLE + file.getName());
                        ModelController.getInstance().buildModel();
//                    });
//                }
//            };
//            loadingThread.start();
        }
    }

    public void initTriangleList(int triangleNumber) {
        triangleList = new ArrayList<>(triangleNumber);
    }

    public void constructTriangle(Vertex[] vertices) {
        triangleList.add(new Triangle(vertices[0], vertices[1], vertices[2], vertices[3]));
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
        for (Triangle triangle : triangleList) {
            this.area += triangle.getArea();
        }
        PolyederController.getInstance().getSurfaceProperty().set(Math.round(area * AllgemeineKonstanten.ROUND_KOMMASTELLE) / AllgemeineKonstanten.ROUND_KOMMASTELLE);
    }

    private void calcVolume() {
        this.volume = 0.0;
        if (triangleList.size() > 1)
        for (Triangle triangle : triangleList) {
            this.volume += triangle.getVolume();
        }
        PolyederController.getInstance().getVolumeProperty().set(Math.round(area * AllgemeineKonstanten.ROUND_KOMMASTELLE) / AllgemeineKonstanten.ROUND_KOMMASTELLE);
    }

    private void sortTriangles () {
        Collections.sort(triangleList);
    }

    public TriangleMesh getMesh(){
        TriangleMesh mesh = new TriangleMesh();

        int faceCnt = 0;
        for(int x = 0; x < triangleList.size(); x++){
            for(int y = 0; y < 3; y++) {
                mesh.getTexCoords().addAll((float) ((triangleList.get(x).getNormal().getX() + 1) / -2));
                mesh.getTexCoords().addAll((float) ((triangleList.get(x).getNormal().getY() + 1) / -2));
                mesh.getTexCoords().addAll((float) ((triangleList.get(x).getNormal().getZ() + 1) / -2));
            }

            for(int y = 0; y < 3; y++) {
                mesh.getPoints().addAll((float) triangleList.get(x).getVertex(y).getX());
                mesh.getPoints().addAll((float) triangleList.get(x).getVertex(y).getY());
                mesh.getPoints().addAll((float) triangleList.get(x).getVertex(y).getZ());
            }

            mesh.getFaces().addAll(faceCnt, faceCnt, faceCnt + 1, faceCnt + 1, faceCnt + 2, faceCnt + 2);
            faceCnt += 3;
            if (x %250000 == 0) {
                System.gc();
            }
        }
        return mesh;
    }

    public ArrayList<Triangle> getTriangleList() {
        return this.triangleList;
    }
}
