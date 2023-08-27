package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EditEmployeeController extends MyWindow implements Initializable
{
	@FXML
	private Button closeButton;
	
	@FXML
	private Button cancelButton;
	
	@FXML
    private Button addEmployeeButton;

	@FXML
	private Button deleteEmployeeButton;
	
	@FXML
	private Label employeeIdLabel;
	
    @FXML
    private Label blankFieldErrorLabel;

    @FXML
    private TextField firstNameTextField;
    
    @FXML
    private TextField lastNameTextField;
    
    @FXML
    private ChoiceBox<String> employeeTypeChoiceBox;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private ChoiceBox<String> positionChoiceBox;
    
    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private Label payText;

    @FXML
    private TextField payTextField;

    @FXML
    private Label phoneNumberErrorLabel;

    @FXML
    private TextField phoneNumberTextField;
    
    @FXML
    private Label employeeUpdatedLabel;
    
    private EmployeeInformationController employeeInformationController = null;
    
    private PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));;
    
    private String incorrectPhoneNumberMessage = "Please enter a valid phone number";
    private String blankFieldsMessage = "Please fill in all appropriate fields";
    private String paySalaryText = "Salary ($/Year):";
    private String payWageText = "Wage ($/Hour):";
    private String employeeUpdatedText = "Employee Updated";
    
    private int phoneNumberLength = 10;
    private int nameMaxLength = 16;
    
    // SQL connections
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		phoneNumberErrorLabel.setVisible(false);
		blankFieldErrorLabel.setVisible(false);
		employeeUpdatedLabel.setVisible(false);
		
		employeeUpdatedLabel.setText(employeeUpdatedText);
		
		positionChoiceBox.setItems(FXCollections.observableArrayList(Employee.positions));
		genderChoiceBox.setItems(FXCollections.observableArrayList(Employee.genders));
		employeeTypeChoiceBox.setItems(FXCollections.observableArrayList(Employee.employeeTypes));
		statusChoiceBox.setItems(FXCollections.observableArrayList(Employee.status));
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
	
	public void displayEmployee(Employee employee)
	{
		employeeIdLabel.setText(employee.getEmployeeId());
		firstNameTextField.setText(employee.getFirstName());
		lastNameTextField.setText(employee.getLastName());
		genderChoiceBox.getSelectionModel().select(employee.getGender());
		statusChoiceBox.getSelectionModel().select(employee.getIsActive());
		phoneNumberTextField.setText(employee.getPhoneNumber());
		positionChoiceBox.getSelectionModel().select(employee.getPosition());
		
		if (employee instanceof SalariedEmployee)
		{
			employeeTypeChoiceBox.getSelectionModel().select(Employee.SALARIED);
			payText.setText(paySalaryText);
			payTextField.setText(String.valueOf(((SalariedEmployee) employee).getSalary()));
		}
		else if (employee instanceof WagedEmployee)
		{
			employeeTypeChoiceBox.getSelectionModel().select(Employee.WAGED);
			payText.setText(payWageText);
			payTextField.setText(String.valueOf(((WagedEmployee) employee).getWage()));
		}
	}
	
	public void deleteEmployee()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		
		alert.setTitle("Confirm Action");
		alert.setHeaderText("Are you sure you want to delete this employee?");
		alert.setContentText("This action cannot be undone");
		Optional<ButtonType> option = alert.showAndWait();
		
		if (option.get().equals(ButtonType.OK))
		{
			try
			{
				connection = DatabaseUtility.connectToDatabase();
				
				preparedStatement = connection.prepareStatement("DELETE FROM employees WHERE employeeId = ?");
				preparedStatement.setString(1, employeeIdLabel.getText());
				preparedStatement.execute();
				
				employeeInformationController.refreshTable();
				
				close();
			}
			catch (SQLException sqle)
			{
				System.out.println("Connection error in " + this.getClass().getName() + " deleteEmployee()");
				sqle.printStackTrace();
			}
		}
	}
	
	public void editEmployee()
	{
		boolean hasError = false;
		
		// Check blank fields
		if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() || statusChoiceBox.getSelectionModel().getSelectedItem().isEmpty() ||
				genderChoiceBox.getSelectionModel().getSelectedItem().isEmpty() || phoneNumberTextField.getText().isEmpty() ||
				positionChoiceBox.getSelectionModel().getSelectedItem().isEmpty() || payTextField.getText().isEmpty())
		{
			blankFieldErrorLabel.setText(blankFieldsMessage);
			blankFieldErrorLabel.setVisible(true);
			hasError = true;
			pauseTransition.setDuration(Duration.seconds(3.0));
			pauseTransition.setOnFinished(event -> blankFieldErrorLabel.setVisible(false));
			pauseTransition.play();
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

			
			preparedStatement = connection.prepareStatement("UPDATE employees SET isActive = ?, firstName = ?, lastName = ?, gender = ?, "
					+ "phoneNumber = ?, position = ?, employeeType = ?, pay = ? WHERE employeeId = ?");
		
			preparedStatement.setString(1, statusChoiceBox.getSelectionModel().getSelectedItem());
			preparedStatement.setString(2, firstNameTextField.getText());
			preparedStatement.setString(3, lastNameTextField.getText());
			preparedStatement.setString(4, genderChoiceBox.getSelectionModel().getSelectedItem());
			preparedStatement.setString(5, phoneNumberTextField.getText());
			preparedStatement.setString(6, positionChoiceBox.getSelectionModel().getSelectedItem());
			preparedStatement.setString(7, employeeTypeChoiceBox.getSelectionModel().getSelectedItem());
			preparedStatement.setDouble(8, Double.parseDouble(String.format("%.2f", Double.parseDouble(payTextField.getText()))));
			preparedStatement.setString(9, employeeIdLabel.getText());
			
			preparedStatement.executeUpdate();
			
			employeeUpdatedLabel.setVisible(true);
			
			pauseTransition.setOnFinished(event -> employeeUpdatedLabel.setVisible(false));
			pauseTransition.setDuration(Duration.seconds(5.0));
			pauseTransition.play();
			
			employeeInformationController.refreshTable();
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + this.getClass().getName() + " editEmployee()");
			sqle.printStackTrace();
		}
	}
	
	public void onKeyTypedOnlyDigits()
	{
		int maxLength = 0;
		int caretPosition;
		
		String text = phoneNumberTextField.getText();
	
		maxLength = phoneNumberLength;
		caretPosition = phoneNumberTextField.getCaretPosition();
		phoneNumberErrorLabel.setVisible(false);
			
		int length = text.length();
		
		if (caretPosition == 0)
		{
			return;
		}
		
		char lastChar = text.charAt(caretPosition - 1);
		
    	if (length > maxLength || !Character.isDigit(lastChar))
    	{    		
    		phoneNumberTextField.setText(text.substring(0, caretPosition - 1) + text.substring(caretPosition, length));
    		phoneNumberTextField.positionCaret(caretPosition - 1);
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
