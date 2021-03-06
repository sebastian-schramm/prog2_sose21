package model.interfaces;
/**
 * ServerInterface Interface of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public interface ServerInterface {

    String SERVER_IP = "";
    String LOCALE_IP = "localhost";
    String PUBLIC_IP = "localhost";
    String PORT_NUMBER = "40404";

    String URL_TO_GET_PUBIC_IP = "http://checkip.amazonaws.com";

    String OFFLINE = "Nicht gestartet";
    String SERVER_START = "Server Startet";
    String SERVER_WAIT_FOR_CONNECTION = "Warte auf Verbindung";
    String SERVER_CONNECTION_DETECTED = "Verbindung aufgebaut";
    String SERVER_CONNECTION_FAILED = "Verbindung fehlgeschlagen!";
    String SERVER_CLOSING = "Server wird beendet";
    String CONNECTED_WITH_SERVER = "Mit Server verbunden";
    String SERVER_PORT_ALREADY_USED = "Port schon vergeben!";

    //Error messages
    String WEBSITE_COULD_NOT_BE_FOUND = "Webseite für die Public IP Adresse konnte nicht aufgerufen werden!";
    String WEBSITE_IP_NOT_FOUND = "Die Public IP konnte nicht ermittelt werden!";

    String SERVER_ERROR = "";

    String SERVER_INIT_ERROR = "Server Instanz kann nicht angelegt werden!";
    String SERVER_ACCEPT_ERROR = "Verbindungsaufbau zum Client fehlgeschlagen!";
    String SERVER_CLIENT_COULD_NOT_CLOSE = "Server client konnte nicht beendet werden!";
    String SERVER_COULD_NOT_CLOSE = "Server konnte nicht beendet werden!";
    String SERVER_COULD_NOT_SEND_TRIANGLES = "Ein Fehler beim senden der Datei ist aufgetreten!";

    String CONNECTION_CLOSED = "Verbindung wurde abgebrochen";
    String CONNECTION_CLOSED_BY_CLIENT = "Client disconnect";

    String MESSAGE_TRENNUNG = ";";
    String MESSAGE_EXIT = "exit";
    String MESSAGE_START_CLIENT = "startClient";
    String MESSAGE_SET_ON_MOUSE_DRAGGED = "setOnMouseDragged";
    String MESSAGE_UPDATE_GUI_ELEMENTS = "updateGUIElements";
    String MESSAGE_TRANSLATE_Y_AXIS = "translateYAxis";
    String MESSAGE_TRANSLATE_X_AXIS = "translateXAxis";

    int MESSAGE_MILLIS_WAIT = 50;
    int MESSAGE_MILLIS_WAIT_TRANSLATE = 20;
}
