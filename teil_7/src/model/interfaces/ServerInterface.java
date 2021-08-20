package model.interfaces;

public interface ServerInterface {

    String SERVER_IP = "";
    String LOCALE_IP = "localhost";
    String PUBLIC_IP = "localhost";
    String PORT_NUMBER = "40404";

    String URL_TO_GET_PUBIC_IP = "http://checkip.amazonaws.com";

    String CHECK_IF_IP_IS_SET = "Prüfe ob eine IP für den Server gesetzt wurde...";
    String CHECK_NO_IP_FOUND = "Keine IP angegeben, Starte Server!";
    String CHECK_IP_FOUND = "Versuche eine Verbindung zum Server herzustellen!";

    String OFFLINE = "Nicht gestartet";
    String SERVER_START = "Server Startet";
    String SERVER_WAIT_FOR_CONNECTION = "Warte auf Verbindung";
    String SERVER_CONNECTION_DETECTED = "Verbindung aufgebaut";
    String SERVER_CLIENT_CONNECTED = "Client Verbunden";
    String SERVER_CLOSING = "Server wird beendet";
    String CONNECTED_WITH_SERVER = "Mit Server verbunden";

    //Error messages
    String WEBSITE_COULD_NOT_BE_FOUND = "Webseite für die Public IP Adresse konnte nicht aufgerufen werden!";
    String WEBSITE_IP_NOT_FOUND = "Die Public IP konnte nicht ermittelt werden!";

    String SERVER_INIT_ERROR = "Server Instanz kann nicht angelegt werden!";
    String SERVER_ACCEPT_ERROR = "Verbindungsaufbau zum Client fehlgeschlagen!";
    String SERVER_INPUT_ERROR = "Eingabe vom Client konnte nicht gelesen werden!";
    String SERVER_CLIENT_COULD_NOT_CLOSE = "Server client konnte nicht beendet werden!";
    String SERVER_COULD_NOT_CLOSE = "Server konnte nicht beendet werden!";

}
