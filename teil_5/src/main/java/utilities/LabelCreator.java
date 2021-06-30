package utilities;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class LabelCreator {

    public static Label createLabel (ObservableValue<String> property, int fontSize)
    {
        Label label = new Label();
        label.setFont(Font.font(java.awt.Font.MONOSPACED, fontSize));
        StringProperty labelProperty = label.textProperty();
        labelProperty.bind(property);

        return label;
    }
}
