package controller;

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

    private static class ModelControllerHolder {
        private static final ModelController INSTANCE = new ModelController();
    }

}
