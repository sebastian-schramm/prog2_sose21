package controller;

import utilities.Server;

public class ServerController {
    private Server server = null;

    public void init() {
        server = new Server();
    }

    private ServerController() {

    }

    public static ServerController getInstance() {
        return ServerControllerHolder.INSTANCE;
    }

    public Server getServer() {
        return this.server;
    }

    private static class ServerControllerHolder {
        private static final ServerController INSTANCE = new ServerController();
    }
}
