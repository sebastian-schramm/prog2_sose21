package utilities;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import model.interfaces.GUIKonstanten;


public class LabelCreator {

    public static Label createLabel (ObservableValue<String> property, int fontSize, String message) {
        Label label = new Label();
        label.setFont(Font.font(java.awt.Font.MONOSPACED, GUIKonstanten.FONT_SIZE));
        label.setPadding(new Insets(8,8,8,8));
        StringProperty labelProperty = label.textProperty();
        labelProperty.bind(Bindings.concat(message,property));
        return label;
    }
}
