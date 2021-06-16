package model;

public class Triangle implements Comparable<Triangle> {
    private final Vertex normal;
    private final Vertex[] vertices;
    private double area;

    public Triangle(Vertex normal, Vertex v1, Vertex v2, Vertex v3) {
        this.normal = normal;
        vertices = new Vertex[3];
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
    }

    private float getSide(Vertex v1, Vertex v2) {
        return (float) Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2) + Math.pow(v1.getZ() - v2.getZ(), 2));
    }

    //TODO Eventuell nach einer besseren berechnung suchen!
    public void calcArea() {
        float a = getSide(vertices[1], vertices[2]);
        float b = getSide(vertices[0], vertices[2]);
        float c = getSide(vertices[0], vertices[1]);
        float p = a + b + c;
        float s = p / 2;
        this.area = (float) Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public float getArea() {
        return (float) this.area;
    }

    public Vertex getNormal() {
        return normal;
    }

    public Vertex getVertex(int position) {
        return vertices[position];
    }

    @Override
    public int compareTo(Triangle triangle) {
        return (this.getArea() < (triangle.getArea()) ? -1 : (this.getArea() == triangle.getArea() ? 0:1));
    }


}
