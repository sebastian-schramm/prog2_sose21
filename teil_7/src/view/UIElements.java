package view;

import controller.Main;
import controller.MouseController;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utilities.CreateAnchor;
/**
 * UIElements Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public final class UIElements {
    /**
     * Beherbergt alle visuellen Objekte des Programms wie z.B. Top und BottomView.
     *
     * @param stage Aktuelle Stage des Programms
     * @param main Instanz der Main Klasse
     * @return stackPane - Fertig aufgebautes Stackpane mit allen visuellen Objekten.
     */
    public static StackPane getStackPane(Stage stage, Main main) {
        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();

        CreateAnchor.setConstraintsZero(stackPane);

        borderPane.setTop(TopViewCreator.createTopView(stage, main));
        borderPane.setBottom(BottomViewCreator.createBottomView());

        MouseController.getInstance().handleMouseEvents(stackPane);

        stackPane.getChildren().add(borderPane);
        return stackPane;
    }
}
