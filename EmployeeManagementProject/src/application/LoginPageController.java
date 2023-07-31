package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginPageController implements Initializable
{
	@FXML
	private AnchorPane mainForm;
	
    @FXML
    private Button closeButton;

    @FXML
    private Button loginButton;
    
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private ImageView employeeImageView;
    
    private double x;
	private double y;
    
    // Database connection variables
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void loginAdmin()
    {
    	// Blank username or password
      	if (usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty())
		{
			Alert alert = new Alert(AlertType.ERROR);
			
			alert.setTitle("Error");
			alert.setHeaderText("Empty username or password");
			alert.setContentText("Please fill in all blanks correctly");
			alert.show();
			return;
		}

    	connection = DatabaseUtility.connectToDatabase();
    	
    	try
		{
			preparedStatement = connection.prepareStatement("SELECT * From admin WHERE username = ? and password = ?");
			preparedStatement.setString(1, usernameTextField.getText());
			preparedStatement.setString(2, passwordField.getText());
			
			resultSet = preparedStatement.executeQuery();
	
			// Correct username and password
			if (resultSet.next())
			{
				// Update user data'
				UserData.username = usernameTextField.getText();
				
				// Hide login window and login user
				mainForm.getScene().getWindow().hide();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
				Parent root = loader.load();
				DashboardController dashBoardController = loader.getController();
				
				Stage stage = new Stage();
				Scene scene = new Scene(root);
				
				stage.setScene(scene);
				
				dashBoardController.setInitialization(stage);
				stage.show();
			
			}
			// Incorrect username and password
			else	
			{
				Alert alert = new Alert(AlertType.ERROR);
				
				alert.setTitle("Error");
				alert.setHeaderText("Incorrect username or password");
				alert.setContentText("Please check your username or password");
				alert.show();
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + LoginPageController.class.getName());
			sqle.printStackTrace();
		}
    	catch (IOException ioe)
    	{
    		System.out.println("Filename error in " + LoginPageController.class.getName());
    		ioe.printStackTrace();
    	}
    }
    
    public void close()
    {
    	System.exit(0);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
    	
    }
    
    public void setInitialization(Stage stage)
    {
    	stage.getScene().setOnMousePressed((MouseEvent event) ->
		{
			x = event.getSceneX();
			y = event.getSceneY();
		});
		
    	stage.getScene().setOnMouseDragged((MouseEvent event) ->
		{
			stage.setX(event.getScreenX() - x);
			stage.setY(event.getScreenY() - y);
			
			stage.setOpacity(0.8);
		});
		
    	stage.getScene().setOnMouseReleased((MouseEvent event) ->
		{
			stage.setOpacity(1);
		});
		
    	stage.initStyle(StageStyle.TRANSPARENT);
    }
}
