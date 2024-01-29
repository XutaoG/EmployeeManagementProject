package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EmployeeInformationController implements Initializable
{	
	@FXML
	private TextField searchTextField;
	
	@FXML
	private ChoiceBox<String> statusFilterChoiceBox;
	
	@FXML
	private ChoiceBox<String> genderFilterChoiceBox;
	
	@FXML
	private ChoiceBox<String> employeeTypeFilterChoiceBox;
	
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
        
    @FXML
    private Label editEmployeeErrorLabel;
    
    private ObservableList<Employee> employeeList = null;

    private PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
    
    private String editEmployeeErrorText = "Please select an employee to edit";
    
    // SQL connection
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    
    public void initialize(URL arg0, ResourceBundle arg1)
	{
    	editEmployeeErrorLabel.setVisible(false);
    	pauseTransition.setOnFinished(event -> editEmployeeErrorLabel.setVisible(false));
    	
    	initializeFilterChoiceBoxes();
    	showEmployeeList();
	}
    
    public void initializeFilterChoiceBoxes()
    {
    	String[] statusFilterArray = {"Active Status (No Filter)", "Active Only", "Inactive Only"};
    	ObservableList<String> statusFilterOptions = FXCollections.observableArrayList(statusFilterArray);
    	statusFilterChoiceBox.setItems(statusFilterOptions);
    	statusFilterChoiceBox.getSelectionModel().select(0);
    	
    	String[] genderFilterArray = {"Gender (No Filter)", "Male Only", "Female Only"};
    	ObservableList<String> genderFilterOptions = FXCollections.observableArrayList(genderFilterArray);
    	genderFilterChoiceBox.setItems(genderFilterOptions);
    	genderFilterChoiceBox.getSelectionModel().select(0);
    	
    	String[] employeeFilterArray = {"Employee Type (No Filter)", "Salaried Only", "Waged Only"};
    	ObservableList<String> employeeTypesFilterOptions = FXCollections.observableArrayList(employeeFilterArray);
    	employeeTypeFilterChoiceBox.setItems(employeeTypesFilterOptions);
    	employeeTypeFilterChoiceBox.getSelectionModel().select(0);
    	
    	statusFilterChoiceBox.setOnAction(actionEvent -> employeeSearch());
    	genderFilterChoiceBox.setOnAction(actionEvent -> employeeSearch());
    	employeeTypeFilterChoiceBox.setOnAction(actionEvent -> employeeSearch());
    }
    
    public void updateEmployeeInformation()
    {    	
    	showEmployeeList();
    	
    }
    
    private ObservableList<Employee> getEmployeeList()
    {
    	ObservableList<Employee> list = FXCollections.observableArrayList();
    	
    	connection = DatabaseUtility.connectToDatabase();
    	
    	try
    	{
    		preparedStatement = connection.prepareStatement("SELECT * FROM employees");
    		
    		resultSet = preparedStatement.executeQuery();
    		
    		Employee employee = null;
    		
    		while (resultSet.next())
    		{		
    			String employeeType = resultSet.getString("employeeType");
    			
    			if (employeeType.compareTo("Salaried") == 0)
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
    			else if (employeeType.compareTo("Waged") == 0)
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
			 System.out.println("Connection error in " + this.getClass().getName() + " getEmployeeList()");
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
    	int index = tableView.getSelectionModel().getSelectedIndex();
    	
    	if (index < 0)
    	{
    		return;
    	}
    	
    	Employee employee = tableView.getSelectionModel().getSelectedItem();
    	
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
    
    public void employeeSearch()
    {
    	ObservableList<Employee> list = FXCollections.observableArrayList(employeeList);
    	
    	list = filterStatus(list);
    	list = filterGender(list);
    	list = filterEmployeeType(list);
    	    	
    	FilteredList<Employee> filteredList = new FilteredList<>(list, e -> true);
    	
    	String newValue = searchTextField.getText();
    	
    	Predicate<Employee> predicate = (predicateEmployee) ->
    	{
    		if (newValue == null || newValue.isEmpty())
    		{
    			return true;
    		}
    		
    		String searchKey = newValue.toLowerCase();
    		
    		if (predicateEmployee.getEmployeeId().toLowerCase().contains(searchKey))
    		{
    			return true;
    		}
    		else if (predicateEmployee.getFirstName().toLowerCase().contains(searchKey))
    		{
    			return true;
    		}
    		else if (predicateEmployee.getLastName().toLowerCase().contains(searchKey))
    		{
    			return true;
    		}
    		else if (predicateEmployee.getPhoneNumber().toLowerCase().contains(searchKey))
    		{
    			return true;
    		}
    		else if (predicateEmployee.getPosition().toLowerCase().contains(searchKey))
    		{
    			return true;
    		}
    		return false;
    	};
    	
    	filteredList.setPredicate(predicate);
		
		SortedList<Employee> sortedList = new SortedList<>(filteredList);
		
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);
    }
    
    public ObservableList<Employee> filterStatus(ObservableList<Employee> list)
    {    	
    	if (statusFilterChoiceBox.getSelectionModel().getSelectedIndex() == 0)
    	{
    		return list;
    	}
    
    	FilteredList<Employee> filteredList = new FilteredList<>(list, e -> true);
    	Predicate<Employee> predicate = null;
    	
    	if (statusFilterChoiceBox.getSelectionModel().getSelectedIndex() == 1)
    	{
    		predicate = (predicateEmployee) ->
    		{
    			if (predicateEmployee.getIsActive().compareTo("Active") == 0)
    			{
    				return true;
    			}
    			return false;
    		};
    	}
    	else if (statusFilterChoiceBox.getSelectionModel().getSelectedIndex() == 2)
    	{
    		predicate = (predicateEmployee) ->
    		{
    			if (predicateEmployee.getIsActive().compareTo("Inactive") == 0)
    			{
    				return true;
    			}
    			return false;
    		};
    	}
    	
    	filteredList.setPredicate(predicate);
      	
    	return FXCollections.observableArrayList(filteredList);
    }
    
    public ObservableList<Employee> filterGender(ObservableList<Employee> list)
    {    	
    	if (genderFilterChoiceBox.getSelectionModel().getSelectedIndex() == 0)
    	{
    		return list;
    	}
    
    	FilteredList<Employee> filteredList = new FilteredList<>(list, e -> true);
    	Predicate<Employee> predicate = null;
    	
    	if (genderFilterChoiceBox.getSelectionModel().getSelectedIndex() == 1)
    	{
    		predicate = (predicateEmployee) ->
    		{
    			if (predicateEmployee.getGender().compareTo("Male") == 0)
    			{
    				return true;
    			}
    			return false;
    		};
    	}
    	else if (genderFilterChoiceBox.getSelectionModel().getSelectedIndex() == 2)
    	{
    		predicate = (predicateEmployee) ->
    		{
    			if (predicateEmployee.getGender().compareTo("Female") == 0)
    			{
    				return true;
    			}
    			return false;
    		};
    	}
    	else if (genderFilterChoiceBox.getSelectionModel().getSelectedIndex() == 3)
    	{
    		predicate = (predicateEmployee) ->
    		{
    			if (predicateEmployee.getGender().compareTo("Other") == 0)
    			{
    				return true;
    			}
    			return false;
    		};
    	}
    	
    	filteredList.setPredicate(predicate);
      	
    	return FXCollections.observableArrayList(filteredList);
    }
    
    public ObservableList<Employee> filterEmployeeType(ObservableList<Employee> list)
    {    	
    	if (employeeTypeFilterChoiceBox.getSelectionModel().getSelectedIndex() == 0)
    	{
    		return list;
    	}
    
    	FilteredList<Employee> filteredList = new FilteredList<>(list, e -> true);
    	Predicate<Employee> predicate = null;
    	
    	if (employeeTypeFilterChoiceBox.getSelectionModel().getSelectedIndex() == 1)
    	{
    		predicate = (predicateEmployee) ->
    		{
    			if (predicateEmployee instanceof SalariedEmployee)
    			{
    				return true;
    			}
    			return false;
    		};
    	}
    	else if (employeeTypeFilterChoiceBox.getSelectionModel().getSelectedIndex() == 2)
    	{
    		predicate = (predicateEmployee) ->
    		{
    			if (predicateEmployee instanceof WagedEmployee)
    			{
    				return true;
    			}
    			return false;
    		};
    	}
    	
    	filteredList.setPredicate(predicate);
      	
    	return FXCollections.observableArrayList(filteredList);
    }
    
    public void update()
    {
    	searchTextField.setText("");
    	statusFilterChoiceBox.getSelectionModel().select(0);
    	genderFilterChoiceBox.getSelectionModel().select(0);
    	employeeTypeFilterChoiceBox.getSelectionModel().select(0);
    	employeeSelect();
    	showEmployeeList();
    }
    
    public void launchAddEmployeePage()
    {
    	try
		{
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEmployeePage.fxml"));
			Parent root = loader.load();
			AddEmployeeController addEmployeeController = loader.getController();
			
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			
			addEmployeeController.setInitialization(stage);
			
			addEmployeeController.setEmployeeInformationController(this);
			
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			
		}
		catch (IOException e)
		{
			System.out.println("Connection error in " + this.getClass().getName() + " launchAddEmployeePage()");
			e.printStackTrace();
		}
    }
    
    public void launchEditEmployeePage()
    {
    	
    	int index = tableView.getSelectionModel().getSelectedIndex();
    	
    	if (index < 0)
    	{
    		editEmployeeErrorLabel.setText(editEmployeeErrorText);
    		editEmployeeErrorLabel.setVisible(true);
    		pauseTransition.setOnFinished(event -> editEmployeeErrorLabel.setVisible(false));
    		pauseTransition.setDuration(Duration.seconds(3));
    		pauseTransition.play();
    		
    		return;
    	}
    	
    	Employee employee = tableView.getSelectionModel().getSelectedItem();
    	
    	try
		{
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditEmployeePage.fxml"));
			Parent root = loader.load();
			EditEmployeeController editEmployeeController = loader.getController();
			
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			
			editEmployeeController.setInitialization(stage);
			editEmployeeController.displayEmployee(employee);
			editEmployeeController.setEmployeeInformationController(this);
			
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			
		}
		catch (IOException e)
		{
			System.out.println("Connection error in " + this.getClass().getName() + " launchEditEmployeePage()");
			e.printStackTrace();
		}
    }
}
