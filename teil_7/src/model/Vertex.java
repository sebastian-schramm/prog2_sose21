package model;

import java.io.Serializable;

/**
 * Vertex Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class Vertex implements Serializable {

    private final double x;
    private final double y;
    private final double z;

    /**
     * Der Vertex Konstruktor initialisiert alle Attribute der Klasse.
     *
     * @param x Setzt die X-Position des Vertex
     * @param y Setzt die Y-Position des Vertex
     * @param z Setzt die Z-Position des Vertex
     * */
    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Die Methode getX() gibt die X-Position des Vertex zurueck.
     *
     * @return x - Gibt das x Attribut zurueck.
     * */
    public double getX() {
        return x;
    }

    /**
     * Die Methode getY() gibt die Y-Position des Vertex zurueck.
     *
     * @return y - Gibt das y Attribut zurueck.
     * */
    public double getY() {
        return y;
    }

    /**
     * Die Methode getZ() gibt die Z-Position des Vertex zurueck.
     *
     * @return z - Gibt das z Attribut zurueck.
     * */
    public double getZ() {
        return z;
    }
}
