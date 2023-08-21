package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEmployeeController extends MyWindow implements Initializable
{
	@FXML
	private Button closeButton;
	
	@FXML
	private Button cancelButton;
	
	@FXML
    private Button addEmployeeButton;

    @FXML
    private TextField employeeIdTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;
    
    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private ChoiceBox<String> employeeTypeChoiceBox;
    
    @FXML
    private ChoiceBox<String> positionChoiceBox;
    
    @FXML
    private TextField payTextField;

    @FXML
    private Label payText;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Label errorLabel;
    
    private String incorrectEmployeeIdMessage = "Employee ID must have 7 characters";
    private String blankFieldsMessage = "Please fill in all blanks please";
    private String duplicateEmployeeIdMessage = "This employee ID already exist";
    private String incorrectPayFormatMessage = "Please enter salary/wage correctly";
    private String payDefaultText = "Salary/Wage:";

    // SQL connections
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		errorLabel.setVisible(false);
		positionChoiceBox.setItems(FXCollections.observableArrayList(Employee.positions));
		genderChoiceBox.setItems(FXCollections.observableArrayList(Employee.genders));
		employeeTypeChoiceBox.setItems(FXCollections.observableArrayList(Employee.employeeTypes));
		
		employeeTypeChoiceBox.setOnAction(event -> onEmployeeTypeSelected());
	}
	
	public void close()
	{
		Stage stage = (Stage) closeButton.getScene().getWindow();
		
		stage.close();
	}
	
	public void addEmployee()
	{
		// Blank fields found
		if (employeeIdTextField.getText().isEmpty() || firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
				genderChoiceBox.getSelectionModel().getSelectedItem().isEmpty() || phoneNumberTextField.getText().isEmpty() ||
				positionChoiceBox.getSelectionModel().getSelectedItem().isEmpty() || payTextField.getText().isEmpty())
		{
			errorLabel.setText(blankFieldsMessage);
			errorLabel.setVisible(true);
			return;
		}
		// Employee ID is the wrong length
		else if (employeeIdTextField.getText().length() != 7)
		{
			errorLabel.setText(incorrectEmployeeIdMessage);
			errorLabel.setVisible(true);
			return;
		}
		
		try
		{
			double pay = Double.parseDouble(payTextField.getText());
		}
		// Pay is not entered correctly
		catch (NumberFormatException nfe)
		{
			errorLabel.setText(incorrectPayFormatMessage);
			errorLabel.setVisible(true);
			return;
		}
		
		try
		{
			connection = DatabaseUtility.connectToDatabase();
						
			preparedStatement = connection.prepareStatement(String.format("SELECT * FROM employees WHERE employeeId = '%s'", employeeIdTextField.getText()));
			resultSet = preparedStatement.executeQuery();
			
			// Duplicate employee ID found
			if (resultSet.next())
			{
				errorLabel.setText(duplicateEmployeeIdMessage);
				errorLabel.setVisible(true);
				return;
			}
			
			preparedStatement = connection.prepareStatement("INSERT INTO employees "
					+ "(employeeId, isActive, firstName, lastName, gender, phoneNumber, position, date, employeeType, pay) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setString(1, employeeIdTextField.getText());
			preparedStatement.setString(2, "Active");
			preparedStatement.setString(3, firstNameTextField.getText());
			preparedStatement.setString(4, lastNameTextField.getText());
			preparedStatement.setString(5, genderChoiceBox.getSelectionModel().getSelectedItem());
			preparedStatement.setString(6, phoneNumberTextField.getText());
			preparedStatement.setString(7, positionChoiceBox.getSelectionModel().getSelectedItem());
			preparedStatement.setDate(8, new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(9, employeeTypeChoiceBox.getSelectionModel().getSelectedItem());
			preparedStatement.setDouble(10, Double.parseDouble(payTextField.getText()));
			
			preparedStatement.executeUpdate();
			
			clearTextFields();
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + this.getClass().getName() + " addEmployee()");
			sqle.printStackTrace();
		}
	}
	
	public void onEmployeeTypeSelected()
	{
		if (employeeTypeChoiceBox.getSelectionModel().getSelectedItem() == null)
		{
			return;
		}
		
		if (employeeTypeChoiceBox.getSelectionModel().getSelectedItem().compareTo("Salaried") == 0)
		{
			payText.setText("Salary ($/Year):");
		}
		else if (employeeTypeChoiceBox.getSelectionModel().getSelectedItem().compareTo("Waged") == 0)
		{
			payText.setText("Wage ($/Hour):");
		}
	}
	
	private void clearTextFields()
	{
		employeeIdTextField.setText("");
		firstNameTextField.setText("");
		lastNameTextField.setText("");
		genderChoiceBox.getSelectionModel().clearSelection();
		phoneNumberTextField.setText("");
		positionChoiceBox.getSelectionModel().clearSelection();
		employeeTypeChoiceBox.getSelectionModel().clearSelection();
		payTextField.setText("");
		payText.setText(payDefaultText);
	}
}
