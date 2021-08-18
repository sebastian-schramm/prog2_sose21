package view;

import javafx.scene.control.Alert;

public class AlertMessage {

    private static Alert alert;

    public static void showMessage(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
//        alert.setHeaderText();
        alert.setContentText(message);
    }
}
