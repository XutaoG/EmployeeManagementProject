package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashboardController extends MyWindow implements Initializable
{
	@FXML
	private BorderPane mainForm;

	// Title bar
	// ==================

	@FXML
	private Button closeButton;

	@FXML
	private Button minimizeButton;

	// Navigation
	// ==================

	@FXML
	private Label usernameLabel;

	@FXML
	private Button employeeInformationButton;

	@FXML
	private Button homeButton;

	@FXML
	private Button logoutButton;
	
	@FXML
	private Button confirmLogoutButton;
	
	@FXML
	private Button cancelLogoutButton;

	// Home Form
	@FXML
	private AnchorPane homeForm;

	@FXML
	private AnchorPane employeeInformationForm;
	
	@FXML
	private AnchorPane homeFxml;
	
	// Employee Information Form	
	@FXML
	private HomeController homeFxmlController;
	
	@FXML
	private AnchorPane employeeInformationFxml;
	
	@FXML
	private EmployeeInformationController employeeInformationFxmlController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{		
		displayUsername();

		openDefaultForm();
		
		logoutButton.setVisible(true);
		
		confirmLogoutButton.visibleProperty().bind(logoutButton.visibleProperty().not());
		cancelLogoutButton.visibleProperty().bind(logoutButton.visibleProperty().not());
	}

	public void openDefaultForm()
	{
		homeButton.setStyle("-fx-background-color: transparent");
		employeeInformationButton.setStyle("-fx-background-color: transparent");

		employeeInformationForm.setVisible(false);

		homeForm.setVisible(true);
		homeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
	}

	public void switchForm(ActionEvent event)
	{
		homeButton.setStyle("-fx-background-color: transparent");
		employeeInformationButton.setStyle("-fx-background-color: transparent");

		homeForm.setVisible(false);
		employeeInformationForm.setVisible(false);

		if (event.getSource() == homeButton)
		{
			homeForm.setVisible(true);
			homeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
			
			homeFxmlController.update();
		}
		else if (event.getSource() == employeeInformationButton)
		{
			employeeInformationForm.setVisible(true);
			employeeInformationButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
			
			employeeInformationFxmlController.update();
		}
	}

	public void displayUsername()
	{
		usernameLabel.setText(UserData.username);
	}

	public void close()
	{
		System.exit(0);
	}

	public void minimize()
	{
		Stage stage = (Stage) mainForm.getScene().getWindow();
		stage.setIconified(true);
	}

	public void logout()
	{
		logoutButton.setVisible(false);
	}
	
	public void confirmLogout()
	{
		try
		{
			mainForm.getScene().getWindow().hide();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
			Parent root = loader.load();
			LoginPageController loginPageController = loader.getController();

			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);

			loginPageController.setInitialization(stage);

			stage.show();
		}
		catch (IOException ioe)
		{
			System.out.println("Filename error in " + this.getClass().getName() + " logout()");
			ioe.printStackTrace();
		}
	}
	
	public void cancelLogout()
	{
		logoutButton.setVisible(true);
	}
	
	@Override
	public void setInitialization(Stage stage)
	{
		super.setInitialization(stage);
		
		displayUsername();
	}
}
