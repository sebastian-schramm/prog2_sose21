package view;

import javafx.scene.control.Alert;
import model.interfaces.DialogKonstanten;

public class AlertMessage {

    private static Alert alert;

    public static void aboutMessage(String message, String header, String title) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void errorMessage(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void handbookMessage() {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(DialogKonstanten.HANDBOOK_TITLE);
    }

    public static void showMessage(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setHeaderText(MenuBarInterface.MENU_ABOUT);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
