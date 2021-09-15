package view;

import javafx.scene.control.Alert;
import model.interfaces.DialogKonstanten;

/**
 * AlertMessage Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class AlertMessage {

    private static Alert alert;

    /**
     * Informiert den Benutzer ueber die Ersteller des Programms
     * und den Zweck der Erstellung.
     */
    public static void aboutMessage(String message, String header, String title) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Informiert den Benutzer ueber aufgetretene Fehler des Programms,
     * die waerden der Laufzeit aufgetreten sind.
     */
    public static void errorMessage(String message, String header) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Ermöglicht dem Benutzer auch ohne das aufmerksame lesen der Dokumentation das Programm zu verwenden.
     */
    public static void handbookMessage() {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(DialogKonstanten.HANDBOOK_TITLE);
        alert.setContentText(DialogKonstanten.HANDBOOK_TEXT);
        alert.setHeaderText(DialogKonstanten.HANDBOOK_HEADER);
        alert.showAndWait();
    }

    /**
     * Diese Nachricht informiert den Benutzer darueber ob das Laden einer Datei erfolgreich war
     * oder fehlgeschlagen ist.
     */
    public static void showMessage(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
