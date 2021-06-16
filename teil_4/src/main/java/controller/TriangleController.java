package controller;

import model.Triangle;
import model.Vertex;
import model.interfaces.AllgemeineKonstanten;

import java.util.ArrayList;

public class TriangleController {
    private Triangle triangle = null;
    private ArrayList<Triangle> triangles = null;
    private Vertex[] vertices;

    public void init() {
        triangles = new ArrayList<>();
        vertices = new Vertex[AllgemeineKonstanten.TRIANGLE_VERTICES];
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
    public void constructTriangle(Vertex[] vertices) {
        triangles.add(new Triangle(vertices[0], vertices[1], vertices[2], vertices[3]));
    }

    public ArrayList<Triangle> getTriangleList() {
        return this.triangles;
    }

    public int getTriangleCount ()
    {
        return  triangles.size();
    }

}
