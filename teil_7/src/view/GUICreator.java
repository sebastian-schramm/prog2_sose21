package view;

import controller.Main;
import controller.ModelController;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.interfaces.AllgemeineKonstanten;
import model.interfaces.GUIKonstanten;

public class GUICreator {

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
