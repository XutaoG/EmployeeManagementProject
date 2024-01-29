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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginPageController extends MyWindow implements Initializable
{
	@FXML
	private AnchorPane mainForm;

    @FXML
    private Button loginButton;
    
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextField passwordTextField;
    
    @FXML
    private CheckBox showPasswordCheckBox;
    
    @FXML
    private Label wrongInformationMessage;
    
    private String incorrectInformationMessage = "Your username or password is incorrect.";
    private String emptyUsernameMessage = "Please fill in the username or password correctly";
	
	private int usernameMaxLength = 12;
	private int passwordMaxLength = 20;
    
    // Database connection variables
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void login()
    {
    	// Blank username or password
      	if (usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty())
		{	
      		wrongInformationMessage.setVisible(true);
      		wrongInformationMessage.setText(emptyUsernameMessage);
      		return;
		}
    	
    	try
		{
    		connection = DatabaseUtility.connectToDatabase();
    		
			preparedStatement = connection.prepareStatement("SELECT * From admin WHERE username = ? and password = ?");
			preparedStatement.setString(1, usernameTextField.getText());
			preparedStatement.setString(2, passwordField.getText());
			
			resultSet = preparedStatement.executeQuery();
	
			// Correct username and password
			if (resultSet.next())
			{
				// Update user data
				UserData.username = usernameTextField.getText();
				
				// Hide login window and login user
				mainForm.getScene().getWindow().hide();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
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
				wrongInformationMessage.setVisible(true);
	      		wrongInformationMessage.setText(incorrectInformationMessage);
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + LoginPageController.class.getName() + " Login()");
			sqle.printStackTrace();
		}
    	catch (IOException ioe)
    	{
    		System.out.println("Filename error in " + LoginPageController.class.getName() + " Login()");
    		ioe.printStackTrace();
    	}
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
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
    	wrongInformationMessage.setVisible(false);
    	
    	passwordTextField.setVisible(false);
    	passwordField.setVisible(true);
    	showPasswordCheckBox.setSelected(false);
    	
    	passwordTextField.setManaged(false);
    	
    	passwordTextField.managedProperty().bind(showPasswordCheckBox.selectedProperty());
    	passwordTextField.visibleProperty().bind(showPasswordCheckBox.selectedProperty());

        passwordField.managedProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordField.visibleProperty().bind(showPasswordCheckBox.selectedProperty().not());

        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        
        passwordField.setOnKeyPressed(event ->
        {
        	if (event.getCode().equals(KeyCode.ENTER))
        	{
        		loginButton.requestFocus();
        		login();
        	}
        });
        
    }
    
    public void onUsernameTyped()
    {
    	// Hide any error message
    	wrongInformationMessage.setVisible(false);
    	
    	String text = usernameTextField.getText();
    	int length = text.length();
    	
    	int caretPosition = usernameTextField.getCaretPosition();
    	
    	if (caretPosition == 0)
    	{
    		return;
    	}
    	
    	char lastChar = text.charAt(caretPosition - 1);
    		
    	if (!Character.isAlphabetic(lastChar) && !Character.isDigit(lastChar) || length > usernameMaxLength)
    	{    		
    		usernameTextField.setText(text.substring(0, caretPosition - 1) + text.substring(caretPosition, length));
    		usernameTextField.positionCaret(caretPosition - 1);
    	}
    }
    
    public void onPasswordTyped(KeyEvent event)
    {    	
    	// Hide any error message
    	wrongInformationMessage.setVisible(false);
    	
    	String text;
    	int caretPosition;
    	
    	if (event.getSource() == passwordTextField)
    	{
    		text = ((TextField) event.getSource()).getText();
    		caretPosition = passwordTextField.getCaretPosition();
    	}
    	else if (event.getSource() == passwordField)
    	{
    		text = ((PasswordField) event.getSource()).getText();
    		caretPosition = passwordField.getCaretPosition();
    	}
    	else
    	{
    		return;
    	}
   	
    	int length = text.length();
    	
    	if (caretPosition == 0)
    	{
    		return;
    	}
    	
    	char lastChar = text.charAt(caretPosition - 1);
    		
    	if (lastChar == ' ' || length > passwordMaxLength)
    	{    		
    		passwordTextField.setText(text.substring(0, caretPosition - 1) + text.substring(caretPosition, length));
    		passwordField.setText(passwordTextField.getText());
    		
    		passwordTextField.positionCaret(caretPosition - 1);
    		passwordField.positionCaret(caretPosition - 1);
    	}
    }
}
