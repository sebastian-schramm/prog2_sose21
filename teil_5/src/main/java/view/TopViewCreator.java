package view;

import controller.Main;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class TopViewCreator {

    private TopViewCreator () {

    }

    public static VBox createTopView (Stage stage, Main meinMain) {
        VBox verticalBox = new VBox();
        MenuBar menu = MenuCreator.createMenu(stage, meinMain);

        verticalBox.getChildren().addAll(menu);

        return verticalBox;
    }
}
