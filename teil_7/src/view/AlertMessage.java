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

    public static void aboutMessage(String message, String header, String title) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void errorMessage(String message, String header) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void handbookMessage() {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(DialogKonstanten.HANDBOOK_TITLE);
        alert.setContentText(DialogKonstanten.HANDBOOK_TEXT);
        alert.setHeaderText(DialogKonstanten.HANDBOOK_HEADER);
        alert.showAndWait();
    }

    public static void showMessage(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setHeaderText(MenuBarInterface.MENU_ABOUT);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
