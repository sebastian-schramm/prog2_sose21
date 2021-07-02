package controller;

import model.interfaces.GUIKonstanten;
import view.ModelCreator;

public class ModelController {
    ModelCreator model;

    public void init() {
        model = new ModelCreator();
    }

    private ModelController() {

    }

    public static ModelController getInstance() {
        return ModelControllerHolder.INSTANCE;
    }

    public ModelCreator getModel() {
        return model;
    }

    public void setSubSceneHeight(double height) {
        model.subScene.setHeight(height - GUIKonstanten.MENUBAR_HEIGHT - GUIKonstanten.BOTTOMBAR_HEIGHT*2);
    }

    public void setSubSceneWidth(double width) {
        model.subScene.setWidth(width);
    }

    private static class ModelControllerHolder {
        private static final ModelController INSTANCE = new ModelController();
    }
}
