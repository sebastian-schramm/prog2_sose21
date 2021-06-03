package controller;

import model.Triangle;
import model.Vertex;
import model.interfaces.AllgemeineKonstanten;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriangleController {
    private Triangle triangle = null;
    private ArrayList<Triangle> triangles = new ArrayList<>();

    public void init(String dateiName) {

    }

    private TriangleController() {

    }

    public static TriangleController getInstance() {
        return TriangleControllerHolder.INSTANCE;
    }

    private static class TriangleControllerHolder {
        private static final TriangleController INSTANCE = new TriangleController();
    }

    //Interpretiere Daten
    public void constructTriangle(String[] relevantData) {
        Vertex[] vertices = new Vertex[4];
        for (int i = 0; i < relevantData.length; ++i) {
            String coord[] = relevantData[i].replaceAll(AllgemeineKonstanten.FACET_NORMAL, "").replaceAll(AllgemeineKonstanten.VERTEX, "").stripLeading().split(" ");
            vertices[i] = new Vertex(Float.parseFloat(coord[0]), Float.parseFloat(coord[1]), Float.parseFloat(coord[2]));
        }

        triangles.add(new Triangle(vertices[0], vertices[1], vertices[2], vertices[3]));
    }

    public ArrayList<Triangle> getTriangleList() {
        return this.triangles;
    }

}
