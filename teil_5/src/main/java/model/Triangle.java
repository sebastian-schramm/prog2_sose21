package model;

public class Triangle implements Comparable<Triangle> {
    private final Vertex normal;
    private final Vertex[] vertices;
    private double area;
    private double volume;

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
    public void calcVolume() {
        var v321 = vertices[2].getX()*vertices[1].getY()*vertices[0].getZ();
        var v231 = vertices[1].getX()*vertices[2].getY()*vertices[0].getZ();
        var v312 = vertices[2].getX()*vertices[0].getY()*vertices[1].getZ();
        var v132 = vertices[0].getX()*vertices[2].getY()*vertices[1].getZ();
        var v213 = vertices[1].getX()*vertices[0].getY()*vertices[2].getZ();
        var v123 = vertices[0].getX()*vertices[1].getY()*vertices[2].getZ();
        this.volume = (1.0f/6.0f)*(-v321 + v231 + v312 - v132 - v213 + v123);
    }

    public float getArea() {
        return (float) this.area;
    }

    public float getVolume() {
        return (float) this.volume;
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
