package view;

import controller.Main;
import controller.ModelController;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.interfaces.AllgemeineKonstanten;
import model.interfaces.GUIKonstanten;
/**
 * GUICreator Class of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public class GUICreator {
    /**
     * Erzeugt das gesamte Fenster in dem alle Elemente des Programms eingepflegt sind.
     *
     * @param stage Aktuelle Stage des Programms
     * @param main Instanz der Main Klasse
     * @return root - Erstelltes AnchorPane
     */
    public static AnchorPane createMainFrame(Stage stage, Main main) {
        stage.setTitle(GUIKonstanten.MY_TITLE);
        stage.setWidth(GUIKonstanten.WINDOW_SIZE_X);
        stage.setHeight(GUIKonstanten.WINDOW_SIZE_Y);
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());

        stage.getIcons().add(new Image(AllgemeineKonstanten.DEFAULT_APPLICATION_ICON));

        AnchorPane root = new AnchorPane();
        root.setStyle(GUIKonstanten.ANCHORPANE_COLOR);

        root.getChildren().addAll(
                ModelController.getInstance().getModelStackPane(),
                UIElements.getStackPane(stage, main)
        );

        return root;
    }

}
