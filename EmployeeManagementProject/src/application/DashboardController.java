package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable
{
	@FXML
	private AnchorPane mainForm;
	
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
	private Button addEmployeeButton;
	
	@FXML
	private Button homeButton;
	
	@FXML
	private Button salaryButton;
	
	@FXML
	private Button logoutButton;
	
	// Home form
	@FXML
	private AnchorPane homeForm;
	
    @FXML
    private BarChart<?, ?> homeEmployeesDataChart;

    @FXML
    private Label homeTotalEmployees;

    @FXML
    private Label homeTotalInactiveEmployees;

    @FXML
    private Label homeTotalPresent;
    
    // Add employee form
    // ==================
    
    @FXML
    private AnchorPane addEmployeeForm;
    
    @FXML
    private Button addEmployeeImportButton;
    
    @FXML
    private Button addEmployeeUpdateButton;
    
    @FXML
    private Button addEmployeeDeleteButton;
    
	@FXML
	private Button addEmployeeAddButton;

    @FXML
    private Button addEmployeeClearButton;
    
    @FXML
    private TableView<?> addEmployeeTableView;

    @FXML
    private TableColumn<?, ?> addEmployeeColumnDate;

    @FXML
    private TableColumn<?, ?> addEmployeeColumnEmployeeID;

    @FXML
    private TableColumn<?, ?> addEmployeeColumnFirstName;

    @FXML
    private TableColumn<?, ?> addEmployeeColumnGender;

    @FXML
    private TableColumn<?, ?> addEmployeeColumnLastName;

    @FXML
    private TableColumn<?, ?> addEmployeeColumnPhoneNumber;

    @FXML
    private TableColumn<?, ?> addEmployeeColumnPosition;

    @FXML
    private TextField addEmployeeEmployeeID;

    @FXML
    private TextField addEmployeeFirstName;

    @FXML
    private ComboBox<?> addEmployeeGender;

    @FXML
    private ComboBox<?> addEmployeeGennder;

    @FXML
    private TextField addEmployeeLastName;

    @FXML
    private TextField addEmployeePhoneNumber;

    @FXML
    private TextField addEmployeeSearch;

    // Salary form
    // ==================
    
    @FXML
    private AnchorPane salaryForm;
    
    @FXML
    private Button salaryClearButton;
    
    @FXML
    private Button salaryUpdateButton;

    @FXML
    private TableView<?> salaryTableView;
    
    @FXML
    private TableColumn<?, ?> salaryColumnEmployeeID;

    @FXML
    private TableColumn<?, ?> salaryColumnFirstName;

    @FXML
    private TableColumn<?, ?> salaryColumnLastName;

    @FXML
    private TableColumn<?, ?> salaryColumnPosition;

    @FXML
    private TableColumn<?, ?> salaryColumnSalary;

    @FXML
    private TextField salaryEmployeeID;

    @FXML
    private Label salaryFirstName;

    @FXML
    private Label salaryLastName;

    @FXML
    private Label salaryPosition;

    @FXML
    private TextField salarySalary;
    
    private double x;
    private double y;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
	}
	
	public void switchForm(ActionEvent event)
	{
		homeButton.setStyle("-fx-background-color: transparent");
		addEmployeeButton.setStyle("-fx-background-color: transparent");
		salaryButton.setStyle("-fx-background-color: transparent");
		
		homeForm.setVisible(false);
		addEmployeeForm.setVisible(false);
		salaryForm.setVisible(false);
		
		if (event.getSource() == homeButton)
		{
			homeForm.setVisible(true);
			homeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
		}
		else if (event.getSource() == addEmployeeButton) 
		{
			addEmployeeForm.setVisible(true);
			addEmployeeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
		}
		else if (event.getSource() == salaryButton) 
		{
			salaryForm.setVisible(true);
			salaryButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
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
		Alert alert = new Alert(AlertType.CONFIRMATION);
		
		alert.setTitle("Logout confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");
		
		Optional<ButtonType> options = alert.showAndWait();
		
		alert.show();
		
		if (options.get().equals(ButtonType.OK))
		{
			try
			{
				mainForm.getScene().getWindow().hide();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
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
				System.out.println("Filename error in " + this.getClass().getName());
				ioe.printStackTrace();
			}
		}
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
    	
    	displayUsername();
    }
}


























