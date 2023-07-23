package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

    public void close()
    {
    	System.exit(0);
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
	}

}
