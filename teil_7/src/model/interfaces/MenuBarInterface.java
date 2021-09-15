package model.interfaces;
/**
 * MenuBarInterface Interface of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public interface MenuBarInterface {

    String MENU_FILE = "File";
    String MENU_FILE_OPEN = "Load file";

    String MENU_CONTROL = "Controls";
    String MENU_CONTROL_FACES = "Show faces";
    String MENU_CONTROL_AXIS = "Show Axis";

    String MENU_NETWORK = "Network";
    String MENU_NETWORK_CONNECT = "Connect";

    String MENU_HELP = "Help";
    String MENU_ABOUT = "About";

    String LABEL_LOCALE = "Lokale: ";
    String LABEL_SERVER = "Public: ";

    String ABOUT_MESSAGE = "Diese Applikation wurde von Sebastian Schramm, Joel Pitzler, Christoph Senft entwickelt.\nIm Sommersemester 2021 wurde sie fertiggestellt.";
    String ABOUT_HEADER = "STL-Viewer™";

    String MENU_HANDBOOK = "Guide";

}
