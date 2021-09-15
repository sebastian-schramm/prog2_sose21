package model;

import java.io.Serializable;

/**
 * Triangle Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class Triangle implements Comparable<Triangle>, Serializable {

    private final Vertex normal;
    private final Vertex[] vertices;

    private double area;
    private double volume;

    /**
     * Der Polyeder Konstruktor initialisiert alle Attribute der Klasse.
     *
     * @param normal Setzt den Normalen Vektor des Dreiecks.
     * @param v1 Setzt den ersten Vertex des Dreiecks.
     * @param v2 Setzt den zweiten Vertex des Dreiecks.
     * @param v3 Setzt den ersten Vertex des Dreiecks.
     *
     * */
    public Triangle(Vertex normal, Vertex v1, Vertex v2, Vertex v3) {
        this.normal = normal;
        vertices = new Vertex[3];
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
    }

    /**
     * Die Methode calcArea() berechnet den Flaecheninhalt des Dreiecks.
     * */
    public void calcArea() {
        double a = calcSide(vertices[1], vertices[2]);
        double b = calcSide(vertices[0], vertices[2]);
        double c = calcSide(vertices[0], vertices[1]);
        double s = (a + b + c) / 2;
        this.area = Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    /**
     * Die Methode calcSide() berechnet eine Seite zwischen Vertices.
     *
     * @param v1 Der erste Vertex einer Seite im Dreieck.
     * @param v2 Der zweite Vertex einer Seite im Dreieck.
     * */
    private double calcSide(Vertex v1, Vertex v2) {
        return Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2) + Math.pow(v1.getZ() - v2.getZ(), 2));
    }

    /**
     * Die Methode calcVolume() berechnet uebergibt einer Ueberladenen Methode calcVolume die drei Vertices des Dreiecks
     * zur berchnung des Dreiecks.
     */
    private void calcVolume() {
        calcVolume(vertices[0], vertices[1], vertices[2]);
    }

    /**
     * Die ueberlagerte Methode calcVolume() berechnet aus den drei Vertices des Dreiecks ein Tetraeder und daraus das Volumen.
     */
    private void calcVolume(Vertex v1, Vertex v2, Vertex v3) {
        double v321 = v3.getX() * v2.getY() * v1.getZ();
        double v231 = v2.getX() * v3.getY() * v1.getZ();
        double v312 = v3.getX() * v1.getY() * v2.getZ();
        double v132 = v1.getX() * v3.getY() * v2.getZ();
        double v213 = v2.getX() * v1.getY() * v3.getZ();
        double v123 = v1.getX() * v2.getY() * v3.getZ();
        this.volume = (1.0 / 6.0) * (-v321 + v231 + v312 - v132 - v213 + v123);
    }

    /**
     * Die Methode getArea() gibt den Flaecheninhalt des Dreiecks zurueck.
     *
     * @return double this.area - Das Attribut welches den Flaecheninhalt des Dreiecks enthaellt.
     */
    public double getArea() {
        if (this.area == 0)
            calcArea();

        return this.area;
    }

    /**
     * Die Methode getVolume() gibt das Volumen des Tetraeder aus dem Dreieck zurueck.
     *
     * @return double this.volume - Das Attribut welches das Volumen des Dreiecks enthaellt.
     */
    public double getVolume() {
        if (this.volume == 0)
            calcVolume();

        return this.volume;
    }

    /**
     * Die Methode getNormal() gibt den Normalen Vektor des Dreiecks zurueck.
     *
     * @return Vertex normal - Das Attribut welches den Normalen Vektor des Dreiecks enthaellt.
     */
    public Vertex getNormal() {
        return normal;
    }

    /**
     * Die Methode getVertex() gibt den Vertex an einer bestimmten Position aus dem vertices Array zurueck.
     *
     * @param position Die Position des Vertex im vertices Array.
     * @return Vertex vertices[position] - Gibt den Vertex an der gegebenen Position im vertices Array zurueck.
     */
    public Vertex getVertex(int position) {
        return vertices[position];
    }

    /**
     * Die Methode compareTo() wurde aus dem Interface Comparable implementier und ausgepraegt um Dreiecke miteinander
     * zu vergleichen. Das Vergleichskriterium ist der Flaecheninhalt der Dreiecke.
     *
     * @param triangle Das Dreieck Objekt welches mit dem Objekt dieser Methode verglichen werden soll.
     * @return Gibt einen Wert von -1 0 oder 1 zurück. Die Werte repraesentieren ob der verglichene Flaecheninhalt kleiner gleich oder groesser war.
     */
    @Override
    public int compareTo(Triangle triangle) {
        return (Double.compare(this.getArea(), triangle.getArea()));
    }
}
