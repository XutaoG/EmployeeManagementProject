package application;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyWindow
{
	double x;
	double y;
	
	public void setInitialization(Stage stage)
	{
		stage.getScene().setOnMousePressed((MouseEvent event) -> {
			x = event.getSceneX();
			y = event.getSceneY();
		});

		stage.getScene().setOnMouseDragged((MouseEvent event) -> {
			stage.setX(event.getScreenX() - x);
			stage.setY(event.getScreenY() - y);

			stage.setOpacity(0.8);
		});

		stage.getScene().setOnMouseReleased((MouseEvent event) -> {
			stage.setOpacity(1);
		});

		stage.initStyle(StageStyle.TRANSPARENT);
	}
}
