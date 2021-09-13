package utilities;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import model.interfaces.GUIKonstanten;

/**
 * LabelCreator Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class LabelCreator {

    public static Label createLabel(ObservableValue<String> property, int fontSize, String message) {
        Label label = new Label();
        label.setFont(Font.font(java.awt.Font.MONOSPACED, fontSize));
        label.setPadding(new Insets(GUIKonstanten.LABEL_PADDING, GUIKonstanten.LABEL_PADDING, GUIKonstanten.LABEL_PADDING, GUIKonstanten.LABEL_PADDING));
        StringProperty labelProperty = label.textProperty();
        labelProperty.bind(Bindings.concat(message, property));
        return label;
    }
}
