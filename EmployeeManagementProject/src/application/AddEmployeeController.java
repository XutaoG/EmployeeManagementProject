package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private Label blankFieldErrorLabel;
    
    @FXML
    private Label employeeIdErrorLabel;
    
    @FXML
    private Label phoneNumberErrorLabel;
    
    @FXML
    private Label employeeAddedLabel;
    
    private EmployeeInformationController employeeInformationController = null;
    
    private PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));;
    
    private String incorrectEmployeeIdMessage = "Employee ID must have 7 digits";
    private String incorrectPhoneNumberMessage = "Please enter a valid phone number";
    private String blankFieldsMessage = "Please fill in all appropriate fields";
    private String duplicateEmployeeIdMessage = "employee ID already exist";
    private String payDefaultText = "Salary/Wage:";
    private String paySalaryText = "Salary ($/Year):";
    private String payWageText = "Wage ($/Hour):";
    private String employeeAddedText = "Employee Added";
    
    private int employeeIdLength = 7;
    private int phoneNumberLength = 10;
    private int nameMaxLength = 16;

    // SQL connections
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		clearTextFields();		
		
		positionChoiceBox.setItems(FXCollections.observableArrayList(Employee.positions));
		genderChoiceBox.setItems(FXCollections.observableArrayList(Employee.genders));
		employeeTypeChoiceBox.setItems(FXCollections.observableArrayList(Employee.employeeTypes));
		
		employeeTypeChoiceBox.setOnAction(event -> onEmployeeTypeSelected());
	}
	
	public void setEmployeeInformationController(EmployeeInformationController controller)
	{
		employeeInformationController = controller;
	}
	
	public void close()
	{
		Stage stage = (Stage) closeButton.getScene().getWindow();
		
		stage.close();
	}
	
	public void addEmployee()
	{
		boolean hasError = false;
		
		// Check blank fields
		if (employeeIdTextField.getText().isEmpty() || firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
				genderChoiceBox.getSelectionModel().getSelectedItem().isEmpty() || phoneNumberTextField.getText().isEmpty() ||
				positionChoiceBox.getSelectionModel().getSelectedItem().isEmpty() || payTextField.getText().isEmpty())
		{
			blankFieldErrorLabel.setText(blankFieldsMessage);
			blankFieldErrorLabel.setVisible(true);
			
			pauseTransition.setDuration(Duration.seconds(5.0));
			pauseTransition.setOnFinished(event -> blankFieldErrorLabel.setVisible(false));
			pauseTransition.play();
			
			hasError = true;
		}
		
		// Check employee ID length
		if (employeeIdTextField.getText().length() != employeeIdLength)
		{
			employeeIdErrorLabel.setText(incorrectEmployeeIdMessage);
			employeeIdErrorLabel.setVisible(true);
			hasError = true;
		}
		
		// Check phone number Length
		if (phoneNumberTextField.getText().length() != phoneNumberLength)
		{
			phoneNumberErrorLabel.setText(incorrectPhoneNumberMessage);
			phoneNumberErrorLabel.setVisible(true);
			hasError = true;
		}
			
		if (hasError)
		{
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
				employeeIdErrorLabel.setText(duplicateEmployeeIdMessage);
				employeeIdErrorLabel.setVisible(true);
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
			preparedStatement.setDouble(10, Double.parseDouble(String.format("%.2f", Double.parseDouble(payTextField.getText()))));
			
			preparedStatement.executeUpdate();
			
			clearTextFields();
			
			employeeAddedLabel.setText(employeeAddedText);
			employeeAddedLabel.setVisible(true);
			
			pauseTransition.setDuration(Duration.seconds(5.0));
			pauseTransition.setOnFinished(event -> employeeAddedLabel.setVisible(false));
			pauseTransition.play();
			
			employeeInformationController.refreshTable();
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
			payText.setText(paySalaryText);
		}
		else if (employeeTypeChoiceBox.getSelectionModel().getSelectedItem().compareTo("Waged") == 0)
		{
			payText.setText(payWageText);
		}
	}
	
	private void clearTextFields()
	{
		blankFieldErrorLabel.setVisible(false);
		employeeIdErrorLabel.setVisible(false);
		phoneNumberErrorLabel.setVisible(false);
		employeeAddedLabel.setVisible(false);
		
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
	
	public void onKeyTypedOnlyDigits(KeyEvent event)
	{
		int maxLength = 0;
		String text;
		int caretPosition;
		
		if (event.getSource() == employeeIdTextField)
		{
			maxLength = employeeIdLength;
			text = ((TextField) event.getSource()).getText();
    		caretPosition = employeeIdTextField.getCaretPosition();
    		employeeIdErrorLabel.setVisible(false);
		}
		else if (event.getSource() == phoneNumberTextField)
		{
			maxLength = phoneNumberLength;
			text = ((TextField) event.getSource()).getText();
    		caretPosition = phoneNumberTextField.getCaretPosition();
    		phoneNumberErrorLabel.setVisible(false);
		}
		else
		{
			return;
		}
		
		TextField textField = (TextField) event.getSource();
		
		int length = text.length();
		
		if (caretPosition == 0)
		{
			return;
		}
		
		char lastChar = text.charAt(caretPosition - 1);
		
    	if (length > maxLength || !Character.isDigit(lastChar))
    	{    		
    		textField.setText(text.substring(0, caretPosition - 1) + text.substring(caretPosition, length));
    		textField.positionCaret(caretPosition - 1);
    	}
	}
	
	public void onKeyTypedOnlyLetters(KeyEvent event)
	{
		int maxLength = 0;
		String text;
		int caretPosition;
		
		if (event.getSource() == firstNameTextField)
		{
			maxLength = nameMaxLength;
			text = ((TextField) event.getSource()).getText();
    		caretPosition = firstNameTextField.getCaretPosition();
		}
		else if (event.getSource() == lastNameTextField)
		{
			maxLength = nameMaxLength;
			text = ((TextField) event.getSource()).getText();
    		caretPosition = lastNameTextField.getCaretPosition();
		}
		else
		{
			return;
		}
		
		TextField textField = (TextField) event.getSource();
		
		int length = text.length();
		
		if (caretPosition == 0)
		{
			return;
		}
		
		char lastChar = text.charAt(caretPosition - 1);
		
    	if (length > maxLength || !Character.isAlphabetic(lastChar))
    	{    		
    		textField.setText(text.substring(0, caretPosition - 1) + text.substring(caretPosition, length));
    		textField.positionCaret(caretPosition - 1);
    	}
	}
	
	public void onPayTyped()
	{	
    	String text = payTextField.getText();
    	int length = text.length();
    	
    	int caretPosition = payTextField.getCaretPosition();
    	
    	if (caretPosition == 0)
    	{
    		return;
    	}
    	
    	char lastChar = text.charAt(caretPosition - 1);
    	
    	if (lastChar == '.' && (text.substring(0, caretPosition - 1).contains(".") || text.substring(caretPosition, length).contains(".")))
    	{
    		payTextField.setText(text.substring(0, caretPosition - 1) + text.substring(caretPosition, length));
    		payTextField.positionCaret(caretPosition - 1);
    	}
    	else if (!Character.isDigit(lastChar) && lastChar != '.')
    	{    		
    		payTextField.setText(text.substring(0, caretPosition - 1) + text.substring(caretPosition, length));
    		payTextField.positionCaret(caretPosition - 1);
    	}
	}
}














