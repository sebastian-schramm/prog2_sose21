package utilities;

import javafx.beans.property.StringProperty;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.TextField;
import model.interfaces.GUIKonstanten;

/**
 * MenuTextFieldCreator Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class MenuTextFieldCreator {

    /**
     * Die Methode customMenuItem() baut ein eigenes Menu Item auf.
     *
     * @param property Der Wert, der im Textfield dargestellt wird.
     * @return menuItem - Das selbst erstellte MenuItem.
     **/
    public static CustomMenuItem customMenuItem(StringProperty property) {
        CustomMenuItem menuItem = new CustomMenuItem();
        TextField menuTextField = new TextField();
        menuTextField.setPrefWidth(GUIKonstanten.TEXTFELD_WIDTH);
        menuTextField.textProperty().bindBidirectional(property);
        menuItem.setContent(menuTextField);
        menuItem.setHideOnClick(false);

        return menuItem;
    }
}
