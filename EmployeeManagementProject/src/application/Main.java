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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
			Parent root = loader.load();
			LoginPageController loginPageController = loader.getController();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			
			loginPageController.setInitialization(primaryStage);
			
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
