package utilities;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class XformBox extends Group {

    private Translate t = new Translate();

    public XformBox() {
        super();
        getTransforms().addAll(new Affine(), t);
    }

    /**
     * Accumulate rotation about specified axis
     *
     * @param angle
     * @param axis
     */
    public void addRotation(double angle, Point3D axis) {
        Rotate r = new Rotate(angle, axis);
        /**
         * This is the important bit and thanks to bronkowitz in this post
         * https://stackoverflow.com/questions/31382634/javafx-3d-rotations for
         * getting me to the solution that the rotations need accumulated in
         * this way
         */
        getTransforms().set(0, r.createConcatenation(getTransforms().get(0)));
    }

    public void addRotation(double mxx, double mxy, double mxz, double tx, double myx, double myy, double myz, double ty, double mzx, double mzy, double mzz, double tz) {
        getTransforms().set(0, new Affine(mxx, mxy, mxz, tx, myx, myy, myz, ty, mzx, mzy, mzz, tz));
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
        t.setX(t.getX() + translation.getX());
        t.setY(t.getY() + translation.getY());
        t.setZ(t.getZ() + translation.getZ());
    }

    public void reset() {
        getTransforms().set(0, new Affine());
    }
}
