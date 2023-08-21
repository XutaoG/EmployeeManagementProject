package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmployeeInformationController
{
	@FXML
	private TextField search;
	
	@FXML
	private TableView<Employee> tableView;

    @FXML
    private TableColumn<Employee, String> employeeIdColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn;

    @FXML
    private TableColumn<Employee, String> lastNameColumn;

    @FXML
    private TableColumn<Employee, String> isActiveColumn;

    @FXML
    private TableColumn<Employee, String> genderColumn;

    @FXML
    private TableColumn<Employee, String> phoneNumberColumn;

    @FXML
    private TableColumn<Employee, String> positionColumn;
    
    @FXML
    private TableColumn<Employee, String> dateColumn;

    @FXML
    private TableColumn<Employee, String> payColumn;
    
    @FXML
    private Label employeeIdLabel;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;
    
    @FXML
    private Label genderLabel;
    
    @FXML
    private Label positionLabel;

    @FXML
    private Label phoneNumberLabel;
    
    @FXML
    private Label isActiveLabel;
    
    @FXML
    private Label employeeTypeLabel;
    
    @FXML
    private Label payLabel;
    
    @FXML
    private Label payText;

    @FXML
    private Button editButton;
    
    @FXML
    private Button addButton;
    
    private ObservableList<Employee> employeeList = null;

    // SQL connection
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    
    public void updateEmployeeInformation()
    {    	
    	showEmployeeList();
    	
    }
    
    private ObservableList<Employee> getEmployeeList()
    {
    	ObservableList<Employee> list = FXCollections.observableArrayList();
    	System.out.println(list.size());
    	
    	connection = DatabaseUtility.connectToDatabase();
    	
    	try
    	{
    		preparedStatement = connection.prepareStatement("SELECT * FROM employees");
    		
    		resultSet = preparedStatement.executeQuery();
    		
    		Employee employee = null;
    		
    		while (resultSet.next())
    		{		
    			String employeeType = resultSet.getString("employeeType");
    			
    			if (employeeType.compareTo("salaried") == 0)
    			{
    				employee = new SalariedEmployee(resultSet.getString("employeeId"),
    						resultSet.getString("isActive"),
    						resultSet.getString("firstName"),
    						resultSet.getString("lastName"),
    						resultSet.getString("gender"),
    						resultSet.getString("phoneNumber"),
    						resultSet.getString("position"),
    						resultSet.getDate("date"),
    						resultSet.getDouble("pay"));		
    			}
    			else if (employeeType.compareTo("waged") == 0)
    			{
    				employee = new WagedEmployee(resultSet.getString("employeeId"),
    						resultSet.getString("isActive"),
    						resultSet.getString("firstName"),
    						resultSet.getString("lastName"),
    						resultSet.getString("gender"),
    						resultSet.getString("phoneNumber"),
    						resultSet.getString("position"),
    						resultSet.getDate("date"),
    						resultSet.getDouble("pay"));		
    			}

    			list.add(employee);
    		}
    	}
		catch (SQLException sqle)
		{
			 System.out.println("Connection error in " + this.getClass().getName() + " getEmployeeList");
			 sqle.printStackTrace();
		}
		
		return list;
    }
    
    private void showEmployeeList()
    {
		employeeList = getEmployeeList();
    			
		employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
		phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		payColumn.setCellValueFactory(parem -> 
		{
			if (parem != null)
			{
				Employee employee = parem.getValue();
				if (employee instanceof WagedEmployee)
		    	{
					return new SimpleStringProperty(String.valueOf(((WagedEmployee) employee).getWage()) + "/Hour");
		    	}
		    	else if (employee instanceof SalariedEmployee)
		    	{
		    		return new SimpleStringProperty(String.valueOf(((SalariedEmployee) employee).getSalary()) + "/Year");
		    	}
			}
			return null;
		});
		
		tableView.setItems(employeeList);
    }
    
    public void employeeSelect()
    {
    	Employee employee = tableView.getSelectionModel().getSelectedItem();
    	
    	int index = tableView.getSelectionModel().getSelectedIndex();
    	
    	if (index < 0)
    	{
    		return;
    	}
    	
    	employeeIdLabel.setText(employee.getEmployeeId());
    	isActiveLabel.setText(employee.getIsActive());
    	firstNameLabel.setText(employee.getFirstName());
    	lastNameLabel.setText(employee.getLastName());
    	genderLabel.setText(employee.getGender());
    	positionLabel.setText(employee.getPosition());
    	phoneNumberLabel.setText(employee.getPhoneNumber());
    	
    	if (employee instanceof SalariedEmployee)
    	{
    		employeeTypeLabel.setText("Salaried");
    		payText.setText("Wage ($/Hour):");
    		payLabel.setText(String.valueOf(((SalariedEmployee) employee).getSalary()));
    	}
    	else if (employee instanceof WagedEmployee)
    	{
    		employeeTypeLabel.setText("Waged");
    		payText.setText("Salary ($/Year):");
    		payLabel.setText(String.valueOf(((WagedEmployee) employee).getWage()));
    	}
    }
}
