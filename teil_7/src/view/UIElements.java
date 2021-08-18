package view;

import controller.Main;
//import controller.MouseController;
import controller.MouseController;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utilities.CreateAnchor;

public final class UIElements {

    public static StackPane getStackPane(Stage stage, Main main) {
        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();

        CreateAnchor.setConstraintsZero(stackPane);

        borderPane.setTop(TopViewCreator.createTopView(stage, main));
        borderPane.setBottom(BottomViewCreator.createBottomView(stage, main));

        MouseController.getInstance().handleMouseEvents(stackPane);

        stackPane.getChildren().add(borderPane);
        return stackPane;
    }
}
