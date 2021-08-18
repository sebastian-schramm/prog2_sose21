package utilities;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class XformBox extends Group {

    public XformBox() {
        super();
        getTransforms().add(new Affine());
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

    public void reset() {
        getTransforms().set(0, new Affine());
    }
}
