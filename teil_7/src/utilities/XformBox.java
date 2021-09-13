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

    private final Point3D position = new Point3D(0.0, 0.0, 0.0);


    public XformBox() {
        super();
        getTransforms().addAll(new Affine(), t, rx, ry);
        angleX = 0.0;
        angleY = 0.0;
        rx.setAxis(Rotate.X_AXIS);
        ry.setAxis(Rotate.Y_AXIS);
    }

    public void addRotation(double angle, Point3D axis) {
        Rotate r = new Rotate(angle, axis);
        getTransforms().set(0, r.createConcatenation(getTransforms().get(0)));
    }

    public void addRotation(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        getTransforms().set(0, new Affine(mxx, mxy, mxz, tx, myx, myy, myz, ty, mzx, mzy, mzz, tz));
    }

    public void setRotation(Point3D axis, double angle) {
        Rotate r = new Rotate(angle, axis);
        getTransforms().set(0, r.createConcatenation(getTransforms().get(0)));
    }

    public void setTranslate(Point3D translation) {
        translation = sceneToLocal(translation);
        translation = t.transform(translation);
        t.setX(translation.getX());
        t.setY(translation.getY());
        t.setZ(translation.getZ());
    }

    public void translate(Point3D translation) {
        translation = sceneToLocal(translation);
        translation = t.transform(translation);
        this.t.setX(t.getX() + translation.getX());
        this.t.setY(t.getY() + translation.getY());
        this.t.setZ(t.getZ() + translation.getZ());
    }

    public void translate(double x, double y, double z) {
        this.t.setX(x);
        this.t.setY(y);
        this.t.setZ(z);
    }

    public Translate getT() {
        return t;
    }

    public void moveX(double value) {
        position.add(position.getX() + value, 0.0, 0.0);
        getTransforms().add(new Translate(position.getX(), position.getY(), position.getZ()));
    }

    public void reset() {
        getTransforms().set(0, new Affine());
    }
}
