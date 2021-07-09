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

    //TODO Eventuell nach einer besseren berechnung suchen!
    public void calcArea() {
        double a = calcSide(vertices[1], vertices[2]);
        double b = calcSide(vertices[0], vertices[2]);
        double c = calcSide(vertices[0], vertices[1]);
        double s = (a + b + c) / 2;
        this.area = Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    private double calcSide(Vertex v1, Vertex v2) {
        return Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2) + Math.pow(v1.getZ() - v2.getZ(), 2));
    }

    //TODO Eventuell nach einer besseren berechnung suchen!
    private void calcVolume() {
        calcVolume(vertices[0], vertices[1], vertices[2]);
    }

    private void calcVolume(Vertex v1, Vertex v2, Vertex v3) {
        double v321 = v3.getX()*v2.getY()*v1.getZ();
        double v231 = v2.getX()*v3.getY()*v1.getZ();
        double v312 = v3.getX()*v1.getY()*v2.getZ();
        double v132 = v1.getX()*v3.getY()*v2.getZ();
        double v213 = v2.getX()*v1.getY()*v3.getZ();
        double v123 = v1.getX()*v2.getY()*v3.getZ();
        this.volume = (1.0/6.0)*(-v321 + v231 + v312 - v132 - v213 + v123);
    }

    public double getArea() {
        if (this.area == 0)
            calcArea();

        return this.area;
    }

    public double getVolume() {
        if (this.volume == 0)
            calcVolume();

        return this.volume;
    }

    public Vertex getNormal() {
        return normal;
    }

    public Vertex getVertex(int position) {
        return vertices[position];
    }

    @Override
    public int compareTo(Triangle triangle) {
        return (Double.compare(this.getArea(), triangle.getArea()));
        //TODO Prüfen ob beides das selbe ergibt!
//        return (this.getArea() < (triangle.getArea()) ? -1 : (this.getArea() == triangle.getArea() ? 0:1));
    }
}
