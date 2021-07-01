package controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import utilities.Network;

public class NetworkController {

    private Network network = null;;

    public void init() {
        network = new Network();
    }

    private NetworkController() {

    }

    public static NetworkController getInstance() {
        return NetworkControllerHolder.INSTANCE;
    }

    public Network getNetwork() {
        return this.network;
    }

    private static class NetworkControllerHolder {
        private static final NetworkController INSTANCE = new NetworkController();
    }

}
