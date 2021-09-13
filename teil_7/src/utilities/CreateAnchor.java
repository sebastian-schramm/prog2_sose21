package utilities;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
/**
 * CreateAnchor Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class CreateAnchor {
    public static void setConstraintsZero(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }
}
