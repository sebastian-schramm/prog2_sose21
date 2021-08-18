package utilities;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.TextField;
import model.interfaces.GUIKonstanten;

public class MenuTextFieldCreator {

    public static CustomMenuItem customMenuItem(ObservableValue<String> property) {
        CustomMenuItem menuItem = new CustomMenuItem();
        TextField menuTextField = new TextField();
        menuTextField.setPrefWidth(GUIKonstanten.TEXTFELD_WIDTH);
//        menuTextField.textProperty().bindBidirectional(property);
        menuItem.setContent(menuTextField);
        menuItem.setHideOnClick(false);

        return menuItem;
    }
}
