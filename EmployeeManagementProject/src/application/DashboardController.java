package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
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
    private TableView<EmployeeData> addEmployeeTableView;

    @FXML
    private TableColumn<EmployeeData, String> addEmployeeColumnDate;

    @FXML
    private TableColumn<EmployeeData, String> addEmployeeColumnEmployeeID;

    @FXML
    private TableColumn<EmployeeData, String> addEmployeeColumnFirstName;

    @FXML
    private TableColumn<EmployeeData, String> addEmployeeColumnGender;

    @FXML
    private TableColumn<EmployeeData, String> addEmployeeColumnLastName;

    @FXML
    private TableColumn<EmployeeData, String> addEmployeeColumnPhoneNumber;

    @FXML
    private TableColumn<EmployeeData, String> addEmployeeColumnPosition;

    @FXML
    private TextField addEmployeeEmployeeID;

    @FXML
    private TextField addEmployeeFirstName;

    @FXML
    private ComboBox<?> addEmployeeGender;

    @FXML
    private ComboBox<?> addEmployeePosition;

    @FXML
    private TextField addEmployeeLastName;

    @FXML
    private TextField addEmployeePhoneNumber;

    @FXML
    private TextField addEmployeeSearch;
    
    @FXML
    private ImageView addEmployeeImageView;

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
    
    private ObservableList<EmployeeData> addEmployeeDataList = null;
    
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    private String[] positionList = {"Cashier", "Food Preparation Worker", "Bagger", 
    		"Floral Assistant", "Stock Clerk", "Pharmacist", "Butcher", 
    		"Bakery Associate", "Customer Service Representative", "Store Manager"};
    
    private String[] genderList = {"Male", "Female", "Other"};
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		addEmployeeShowListData();
		addEmployeePositionList();
		addEmployeeGenderList();
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
			
			addEmployeePositionList();
			addEmployeeGenderList();
			addEmployeeSearch();
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


	public ObservableList<EmployeeData> addEmployeeGetListData()
	{
		ObservableList<EmployeeData> observableList = FXCollections.observableArrayList();
		
		connection = DatabaseUtility.connectToDatabase();
		
		try
		{
			preparedStatement = connection.prepareStatement("SELECT * FROM employees");
			resultSet = preparedStatement.executeQuery();
			EmployeeData employeeData;
			
			while (resultSet.next())
			{
				employeeData = new EmployeeData(resultSet.getInt("employeeId"),
						resultSet.getString("firstName"),
						resultSet.getString("lastName"),
						resultSet.getString("gender"),
						resultSet.getString("phoneNumber"),
						resultSet.getString("position"),
						resultSet.getString("image"),
						resultSet.getDate("date"));
				
				observableList.add(employeeData);
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + this.getClass().getName());
			sqle.printStackTrace();
		}
		
		return observableList;
	}
	
	public void addEmployeeShowListData()
	{
		addEmployeeDataList = addEmployeeGetListData();
		
		addEmployeeColumnEmployeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
		addEmployeeColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		addEmployeeColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		addEmployeeColumnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		addEmployeeColumnPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
		addEmployeeColumnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		addEmployeeColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		addEmployeeTableView.setItems(addEmployeeDataList);
		
	}
	
	public void addEmployeeSelect()
	{
		EmployeeData employeeData = addEmployeeTableView.getSelectionModel().getSelectedItem();
		
		int index = addEmployeeTableView.getSelectionModel().getSelectedIndex();
		
		if (index < 0)
		{
			return;
		}
		
		addEmployeeEmployeeID.setText(String.valueOf(employeeData.getEmployeeId()));
		addEmployeeFirstName.setText(employeeData.getFirstName());
		addEmployeeLastName.setText(employeeData.getLastName());
		addEmployeePhoneNumber.setText(employeeData.getPhoneNumber());
		
		UserData.path = employeeData.getImage();
		
		String uri = "file:" + employeeData.getImage();
		Image employeeImage = new Image(uri, addEmployeeImageView.getFitWidth(), addEmployeeImageView.getFitHeight(), false, true);
		
		addEmployeeImageView.setImage(employeeImage);
	}
	
	public void addEmployeeInsertImage()
	{
		FileChooser open = new FileChooser();
		File file = open.showOpenDialog(mainForm.getScene().getWindow());
		
		if (file != null)
		{
			UserData.path = file.getAbsolutePath();
			
			Image image = new Image(file.toURI().toString(), addEmployeeImageView.getFitWidth(), addEmployeeImageView.getFitHeight(), false, true);
			addEmployeeImageView.setImage(image);
		}
	}
	
	public void addEmployeeAdd()
	{
		System.out.println("ADD");
		
		connection = DatabaseUtility.connectToDatabase();
		
		Date date = new Date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		try
		{
			Alert alert;
			
			if (addEmployeeEmployeeID.getText().isEmpty() ||
					addEmployeeFirstName.getText().isEmpty() ||
					addEmployeeLastName.getText().isEmpty() ||
					addEmployeeGender.getSelectionModel().getSelectedItem() == null ||
					addEmployeePhoneNumber.getText().isEmpty() ||
					addEmployeePosition.getSelectionModel().getSelectedItem() == null ||
					UserData.path == null || UserData.path.isEmpty())
			{
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid textfield(s)");
				alert.setContentText("Please fill in all blank fields");
				alert.showAndWait();
				return;
			}
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT employeeId from employees WHERE employeeId = '" + addEmployeeEmployeeID.getText() + "'");
			
			if (resultSet.next())
			{
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Employee ID: " + addEmployeeEmployeeID.getText() + " already exist.");
				alert.showAndWait();
				return;
			}
			
			preparedStatement = connection.prepareStatement("INSERT INTO employees " + 
					"(employeeId, firstName, lastName, gender, phoneNumber, position, image, date) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			
			preparedStatement.setString(1, addEmployeeEmployeeID.getText());
			preparedStatement.setString(2, addEmployeeFirstName.getText());
			preparedStatement.setString(3, addEmployeeLastName.getText());
			preparedStatement.setString(4, (String) addEmployeeGender.getSelectionModel().getSelectedItem());
			preparedStatement.setString(5, addEmployeePhoneNumber.getText());
			preparedStatement.setString(6, (String) addEmployeePosition.getSelectionModel().getSelectedItem());
			
			String uri = UserData.path.replace("\\", "\\\\");
			
			preparedStatement.setString(7, uri);
			preparedStatement.setDate(8, sqlDate);
			
			preparedStatement.executeUpdate();
			
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Employee Added");
			alert.setContentText("Employee successfully added");
			alert.showAndWait();
			
			addEmployeeReset();
			addEmployeeShowListData();
			
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + this.getClass().getName());
			sqle.printStackTrace();
		}
	}
	
	public void addEmployeeReset()
	{
		addEmployeeEmployeeID.setText("");
		addEmployeeFirstName.setText("");
		addEmployeeLastName.setText("");
		addEmployeeGender.getSelectionModel().clearSelection();
		addEmployeePhoneNumber.setText("");
		addEmployeePosition.getSelectionModel().clearSelection();
		addEmployeeImageView.setImage(null);
		
		UserData.path = null;
	}
	
	public void addEmployeePositionList()
	{
		ArrayList<String> positionArrayList = new ArrayList<>();
		
		for (int i = 0; i < positionList.length; i++)
		{
			positionArrayList.add(positionList[i]);
		}
		
		ObservableList positionObservableList = FXCollections.observableArrayList(positionArrayList);
		addEmployeePosition.setItems(positionObservableList);
	}
	
	public void addEmployeeGenderList()
	{
		ArrayList<String> genderArrayList = new ArrayList<>();
		
		for (int i = 0; i < genderList.length; i++)
		{
			genderArrayList.add(genderList[i]);
		}
		
		ObservableList genderObservableList = FXCollections.observableArrayList(genderArrayList);
		addEmployeeGender.setItems(genderObservableList);
	}
	
	public void addEmployeeUpdate()
	{
		try
		{
			Alert alert;
			
			if (addEmployeeEmployeeID.getText().isEmpty() ||
					addEmployeeFirstName.getText().isEmpty() ||
					addEmployeeLastName.getText().isEmpty() ||
					addEmployeeGender.getSelectionModel().getSelectedItem() == null ||
					addEmployeePhoneNumber.getText().isEmpty() ||
					addEmployeePosition.getSelectionModel().getSelectedItem() == null ||
					UserData.path == null || UserData.path.isEmpty())
			{
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid textfield(s)");
				alert.setContentText("Please fill in all blank fields");
				alert.showAndWait();
				return;
			}
			
			alert = new Alert(AlertType.CONFIRMATION);
			
			alert.setTitle("Confirm Action");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to update employee ID: " + addEmployeeEmployeeID.getText() + "?");
			Optional<ButtonType> option = alert.showAndWait();
			
			if (option.get().equals(ButtonType.OK))
			{
				connection = DatabaseUtility.connectToDatabase();
				statement = connection.createStatement();
				statement.execute(String.format("UPDATE employees SET firstName = '%s', lastName = '%s', gender = '%s', position = '%s', phoneNumber = '%s', image = '%s', date = '%s' WHERE employeeId = '%s'",
						addEmployeeFirstName.getText(), addEmployeeLastName.getText(), addEmployeeGender.getSelectionModel().getSelectedItem(), addEmployeePosition.getSelectionModel().getSelectedItem(),
						addEmployeePhoneNumber.getText(), UserData.path.replace("\\","\\\\"), new java.sql.Date(new Date().getTime()), addEmployeeEmployeeID.getText()));
				
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText(null);
				alert.setContentText("Employee ID: " + addEmployeeEmployeeID.getText() + " is updated.");
				alert.showAndWait();
				
				addEmployeeShowListData();
				addEmployeeReset();
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + this.getClass().getName());
			sqle.printStackTrace();
		}
	}
	
	public void addEmployeeDelete()
	{
		try
		{
			Alert alert;
			
			if (addEmployeeEmployeeID.getText().isEmpty() ||
					addEmployeeFirstName.getText().isEmpty() ||
					addEmployeeLastName.getText().isEmpty() ||
					addEmployeeGender.getSelectionModel().getSelectedItem() == null ||
					addEmployeePhoneNumber.getText().isEmpty() ||
					addEmployeePosition.getSelectionModel().getSelectedItem() == null ||
					UserData.path == null || UserData.path.isEmpty())
			{
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid textfield(s)");
				alert.setContentText("Please fill in all blank fields");
				alert.showAndWait();
				return;
			}
			
			alert = new Alert(AlertType.CONFIRMATION);
			
			alert.setTitle("Confirm Action");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to delete employee ID: " + addEmployeeEmployeeID.getText() + "?");
			Optional<ButtonType> option = alert.showAndWait();
			
			if (option.get().equals(ButtonType.OK))
			{
				connection = DatabaseUtility.connectToDatabase();
				statement = connection.createStatement();
				statement.execute("DELETE FROM employees WHERE employeeId = '" + addEmployeeEmployeeID.getText() + "'");
				
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText(null);
				alert.setContentText("Employee ID: " + addEmployeeEmployeeID.getText() + " is deleted.");
				alert.showAndWait();
				
				addEmployeeShowListData();
				addEmployeeReset();
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + this.getClass().getName());
			sqle.printStackTrace();
		}
	}
	
	public void addEmployeeSearch()
	{
		FilteredList<EmployeeData> filteredList = new FilteredList<>(addEmployeeDataList, e -> true);
		
		String newValue = addEmployeeSearch.getText();

		filteredList.setPredicate(predicateEmployeeData -> 
		{
			if (newValue == null || newValue.isEmpty()) 
			{
				return true;
			}

	        String searchKey = newValue.toLowerCase();
	
	        if (String.valueOf(predicateEmployeeData.getEmployeeId()).contains(searchKey)) 
	        {
	            return true;
	        } 
	        else if (predicateEmployeeData.getFirstName().toLowerCase().contains(searchKey)) 
	        {
	            return true;
	        } 
	        else if (predicateEmployeeData.getLastName().toLowerCase().contains(searchKey)) 
	        {
	            return true;
	        } 
	        else if (predicateEmployeeData.getGender().toLowerCase().contains(searchKey)) 
	        {
	            return true;
	        } 
	        else if (predicateEmployeeData.getPhoneNumber().toLowerCase().contains(searchKey)) 
	        {
	            return true;
	        } 
	        else if (predicateEmployeeData.getPosition().toLowerCase().contains(searchKey)) 
	        {
	            return true;
	        } 
	        else if (predicateEmployeeData.getDate().toString().contains(searchKey)) 
	        {
	            return true;
	        } 
	        else 
	        {
	            return false;
	        }
	    });
	
		SortedList<EmployeeData> sortedList = new SortedList<>(filteredList);
		
        sortedList.comparatorProperty().bind(addEmployeeTableView.comparatorProperty());
        addEmployeeTableView.setItems(sortedList);
	}
}













