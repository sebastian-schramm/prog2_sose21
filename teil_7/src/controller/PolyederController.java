package controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.Polyeder;

public class PolyederController {

    private final DoubleProperty surfaceProperty = new SimpleDoubleProperty();
    private final DoubleProperty volumeProperty = new SimpleDoubleProperty();
    private final IntegerProperty triangleAmountProperty = new SimpleIntegerProperty();

    private Polyeder polyeder;

    private PolyederController() {
        polyeder = new Polyeder();
        this.surfaceProperty.set(0.0);
        this.volumeProperty.set(0.0);
        this.triangleAmountProperty.set(0);
    }

    public Polyeder getPolyeder() {
        return polyeder;
    }

    public DoubleProperty getSurfaceProperty() {
        return this.surfaceProperty;
    }

    public DoubleProperty getVolumeProperty() {
        return this.volumeProperty;
    }

    public IntegerProperty getTriangleAmountProperty() {
        return this.triangleAmountProperty;
    }

    public void setTriangleAmount(int triangleAmount) {
        this.triangleAmountProperty.setValue(triangleAmount);
    }

    public int getTriangleAmount() {
        return this.triangleAmountProperty.intValue();
    }

    public static PolyederController getInstance() {
        return PolyederControllerHolder.INSTANCE;
    }

    private static class PolyederControllerHolder {
        private static final PolyederController INSTANCE = new PolyederController();
    }

}