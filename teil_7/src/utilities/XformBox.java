package utilities;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.chart.Axis;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * XformBox Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class XformBox extends Group {

    private final Translate t = new Translate();
    private final Rotate r = new Rotate();
    private final Rotate rx = new Rotate();
    private final Rotate ry = new Rotate();
    private double angleX, angleY;
    private double tx, ty, tz;

    private final Point3D position = new Point3D(0.0, 0.0, 0.0);

    /**
     * Der XformBox Konstruktor initialisiert alle Attribute der Klasse.
     **/
    public XformBox() {
        super();
        getTransforms().addAll(new Affine(), t, rx, ry);
        angleX = 0.0;
        angleY = 0.0;
        rx.setAxis(Rotate.X_AXIS);
        ry.setAxis(Rotate.Y_AXIS);
    }

    /**
     * Die ueberladene addRotation() Methode ermoeglicht eine Rotatition um eine Achse mit einem spezifischen Winkel.
     *
     * @param axis Die ausgewaehlte Achse um die rotiert werden soll.
     * @param angle Der angegebene Winkel fuer die Rotation.
     **/
    public void addRotation(Point3D axis, double angle) {
        Rotate r = new Rotate(angle, axis);
        getTransforms().set(0, r.createConcatenation(getTransforms().get(0)));
    }

    /**
     * Die ueberladene addRotation() Methode ermoeglicht eine Rotatition mittels der Werte aus einer Affine 3x4 Matrix.
     * Diese Methode wird verwendet um eine Rotation zu interpretieren, die von einen anderen Client gesendet wurde.
     *
     * @param mxx Bestimmt das Skalierungselement der X-Koordinate der 3x4-Matrix.
     * @param mxy Bestimmt das Skalierungselement der XY-Koordinate der 3x4-Matrix.
     * @param mxz Bestimmt das Skalierungselement der XZ-Koordinate der 3x4-Matrix.
     * @param tx Bestimmt die X-Koordinatenverschiebung der 3x4-Matrix.
     * @param myx Bestimmt das Skalierungselement der YX-Koordinate der 3x4-Matrix
     * @param myy Bestimmt das Skalierungselement der Y-Koordinate der 3x4-Matrix
     * @param myz Bestimmt das Skalierungselement der YZ-Koordinate der 3x4-Matrix
     * @param ty Bestimmt die Y-Koordinatenverschiebung der 3x4-Matrix.
     * @param mzx Bestimmt das Skalierungselement der ZX-Koordinate der 3x4-Matrix
     * @param mzy Bestimmt das Skalierungselement der ZY-Koordinate der 3x4-Matrix.
     * @param mzz Bestimmt das Skalierungselement der Z-Koordinate der 3x4-Matrix.
     * @param tz Bestimmt die Z-Koordinatenverschiebung der 3x4-Matrix.
     **/
    public void addRotation(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        getTransforms().set(0, new Affine(mxx, mxy, mxz, tx, myx, myy, myz, ty, mzx, mzy, mzz, tz));
    }

    /**
     * Die ueberladene translate() Methode ermoeglicht eine Verschiebung eines Objekts im 3D-Raum.
     *
     * @param translation Die erzeugte Translation aus dem ModelController.
     **/
    public void translate(Point3D translation) {
        translation = sceneToLocal(translation);
        translation = t.transform(translation);
        this.t.setX(t.getX() + translation.getX());
        this.t.setY(t.getY() + translation.getY());
        this.t.setZ(t.getZ() + translation.getZ());
    }

    /**
     * Die ueberladene translate() Methode ermoeglicht eine Verschiebung nach x, y und z Koordinaten.
     * Diese Methode wird verwendet um eine Bewegung zu interpretieren, die von einen anderen Client gesendet wurde.
     *
     * @param x Die x verschiebung im Raum.
     * @param y Die y verschiebung im Raum.
     * @param z Die z verschiebung im Raum.
     **/
    public void translate(double x, double y, double z) {
        this.t.setX(x);
        this.t.setY(y);
        this.t.setZ(z);
    }

    /**
     * Die getT() Methode gibt ein Translate Objekt zurueck.
     *
     * @return Translate Objekt wird zurueckgegeben.
     **/
    public Translate getT() {
        return t;
    }
}
