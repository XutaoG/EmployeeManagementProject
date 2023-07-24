package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Main extends Application
{
	
	private double x;
	private double y;
	
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
			Scene scene = new Scene(root);
			
			root.setOnMousePressed((MouseEvent event) ->
			{
				x = event.getSceneX();
				y = event.getSceneY();
			});
			
			root.setOnMouseDragged((MouseEvent event) ->
			{
				primaryStage.setX(event.getScreenX() - x);
				primaryStage.setY(event.getScreenY() - y);
				
				primaryStage.setOpacity(0.8);
			});
			
			root.setOnMouseReleased((MouseEvent event) ->
			{
				primaryStage.setOpacity(1);
			});
			
			
			primaryStage.initStyle(StageStyle.TRANSPARENT);

			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
