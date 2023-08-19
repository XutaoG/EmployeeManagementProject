package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

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
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
	private Button addEmployeeButton;

	@FXML
	private Button homeButton;

	@FXML
	private Button salaryButton;

	@FXML
	private Button logoutButton;
	
	@FXML
	private Button confirmLogoutButton;
	
	@FXML
	private Button cancelLogoutButton;

	// Home form
	@FXML
	private AnchorPane homeForm;

	@FXML
	private AnchorPane addEmployeeForm;

	@FXML
	private AnchorPane salaryForm;

	private ObservableList<EmployeeData> addEmployeeDataList = null;
	private ObservableList<EmployeeData> salaryDataList = null;

	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	private String[] positionList = { "Cashier", "Food Preparation Worker", "Bagger", "Floral Assistant", "Stock Clerk",
			"Pharmacist", "Butcher", "Bakery Associate", "Customer Service Representative", "Store Manager" };

	private String[] genderList = { "Male", "Female", "Other" };

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		displayUsername();

		openDefaultForm();
		
		logoutButton.setVisible(true);
		
		confirmLogoutButton.visibleProperty().bind(logoutButton.visibleProperty().not());
		cancelLogoutButton.visibleProperty().bind(logoutButton.visibleProperty().not());

// homeSetTotalEmployees();
// homeSetTotalPresent();
// homeSetTotalInactive();
// homeSetChart();
//
// addEmployeePositionList();
// addEmployeeGenderList();
// addEmployeeShowListData();
//
// salaryShowListData();
	}

	public void openDefaultForm()
	{
		homeButton.setStyle("-fx-background-color: transparent");
		addEmployeeButton.setStyle("-fx-background-color: transparent");
		salaryButton.setStyle("-fx-background-color: transparent");

		addEmployeeForm.setVisible(false);
		salaryForm.setVisible(false);

		homeForm.setVisible(true);
		homeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
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

// homeSetTotalEmployees();
// homeSetTotalPresent();
// homeSetTotalInactive();
// homeSetChart();
		}
		else if (event.getSource() == addEmployeeButton)
		{
			addEmployeeForm.setVisible(true);
			addEmployeeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");

// addEmployeePositionList();
// addEmployeeGenderList();
// addEmployeeSearch();
		}
		else if (event.getSource() == salaryButton)
		{
			salaryForm.setVisible(true);
			salaryButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");

// salaryShowListData();
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

// public ObservableList<EmployeeData> addEmployeeGetListData()
// {
// ObservableList<EmployeeData> observableList =
// FXCollections.observableArrayList();
//
// connection = DatabaseUtility.connectToDatabase();
//
// try
// {
// preparedStatement = connection.prepareStatement("SELECT * FROM employees");
// resultSet = preparedStatement.executeQuery();
// EmployeeData employeeData;
//
// while (resultSet.next())
// {
// employeeData = new EmployeeData(resultSet.getString("employeeId"),
// resultSet.getString("firstName"),
// resultSet.getString("lastName"),
// resultSet.getString("gender"),
// resultSet.getString("phoneNumber"),
// resultSet.getString("position"),
// resultSet.getString("image"),
// resultSet.getDate("date"));
//
// observableList.add(employeeData);
// }
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
//
// return observableList;
// }
//
// public void addEmployeeShowListData()
// {
// addEmployeeDataList = addEmployeeGetListData();
//
// addEmployeeColumnEmployeeId.setCellValueFactory(new
// PropertyValueFactory<>("employeeId"));
// addEmployeeColumnFirstName.setCellValueFactory(new
// PropertyValueFactory<>("firstName"));
// addEmployeeColumnLastName.setCellValueFactory(new
// PropertyValueFactory<>("lastName"));
// addEmployeeColumnGender.setCellValueFactory(new
// PropertyValueFactory<>("gender"));
// addEmployeeColumnPosition.setCellValueFactory(new
// PropertyValueFactory<>("position"));
// addEmployeeColumnPhoneNumber.setCellValueFactory(new
// PropertyValueFactory<>("phoneNumber"));
// addEmployeeColumnDate.setCellValueFactory(new
// PropertyValueFactory<>("date"));
//
// addEmployeeTableView.setItems(addEmployeeDataList);
//
// }
//
// public void addEmployeeSelect()
// {
// EmployeeData employeeData =
// addEmployeeTableView.getSelectionModel().getSelectedItem();
//
// int index = addEmployeeTableView.getSelectionModel().getSelectedIndex();
//
// if (index < 0)
// {
// return;
// }
//
// addEmployeeEmployeeID.setText(String.valueOf(employeeData.getEmployeeId()));
// addEmployeeFirstName.setText(employeeData.getFirstName());
// addEmployeeLastName.setText(employeeData.getLastName());
// addEmployeePhoneNumber.setText(employeeData.getPhoneNumber());
//
// UserData.path = employeeData.getImage();
//
// String uri = "file:" + employeeData.getImage();
// Image employeeImage = new Image(uri, addEmployeeImageView.getFitWidth(),
// addEmployeeImageView.getFitHeight(), false, true);
//
// addEmployeeImageView.setImage(employeeImage);
// }
//
// public void addEmployeeInsertImage()
// {
// FileChooser open = new FileChooser();
// File file = open.showOpenDialog(mainForm.getScene().getWindow());
//
// if (file != null)
// {
// UserData.path = file.getAbsolutePath();
//
// Image image = new Image(file.toURI().toString(),
// addEmployeeImageView.getFitWidth(), addEmployeeImageView.getFitHeight(),
// false, true);
// addEmployeeImageView.setImage(image);
// }
// }
//
// public void addEmployeeAdd()
// {
// connection = DatabaseUtility.connectToDatabase();
//
// Date date = new Date();
// java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//
// try
// {
// Alert alert;
//
// if (addEmployeeEmployeeID.getText().isEmpty() ||
// addEmployeeFirstName.getText().isEmpty() ||
// addEmployeeLastName.getText().isEmpty() ||
// addEmployeeGender.getSelectionModel().getSelectedItem() == null ||
// addEmployeePhoneNumber.getText().isEmpty() ||
// addEmployeePosition.getSelectionModel().getSelectedItem() == null ||
// UserData.path == null || UserData.path.isEmpty())
// {
// alert = new Alert(AlertType.ERROR);
// alert.setTitle("Error");
// alert.setHeaderText("Invalid textfield(s)");
// alert.setContentText("Please fill in all blank fields");
// alert.showAndWait();
// return;
// }
//
// statement = connection.createStatement();
// resultSet = statement.executeQuery("SELECT employeeId from employees WHERE
// employeeId = '" + addEmployeeEmployeeID.getText() + "'");
//
// if (resultSet.next())
// {
// alert = new Alert(AlertType.ERROR);
// alert.setTitle("Error");
// alert.setHeaderText(null);
// alert.setContentText("Employee ID: " + addEmployeeEmployeeID.getText() + "
// already exist.");
// alert.showAndWait();
// return;
// }
//
// preparedStatement = connection.prepareStatement("INSERT INTO employees
// (employeeId, firstName, lastName, gender, phoneNumber, position, image, date)
// VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//
// preparedStatement.setString(1, addEmployeeEmployeeID.getText());
// preparedStatement.setString(2, addEmployeeFirstName.getText());
// preparedStatement.setString(3, addEmployeeLastName.getText());
// preparedStatement.setString(4, (String)
// addEmployeeGender.getSelectionModel().getSelectedItem());
// preparedStatement.setString(5, addEmployeePhoneNumber.getText());
// preparedStatement.setString(6, (String)
// addEmployeePosition.getSelectionModel().getSelectedItem());
//
// String uri = UserData.path.replace("\\", "\\\\");
//
// preparedStatement.setString(7, uri);
// preparedStatement.setDate(8, sqlDate);
//
// preparedStatement.executeUpdate();
//
// preparedStatement = connection.prepareStatement("INSERT INTO employee_salary
// (employeeId, firstName, lastName, position, salary) VALUES (?, ?, ?, ?, ?)");
//
// preparedStatement.setString(1, addEmployeeEmployeeID.getText());
// preparedStatement.setString(2, addEmployeeFirstName.getText());
// preparedStatement.setString(3, addEmployeeLastName.getText());
// preparedStatement.setString(4, (String)
// addEmployeePosition.getSelectionModel().getSelectedItem());
// preparedStatement.setDouble(5, 0.0);
//
// preparedStatement.executeUpdate();
//
//
// alert = new Alert(AlertType.INFORMATION);
// alert.setTitle("Information");
// alert.setHeaderText("Employee Added");
// alert.setContentText("Employee successfully added");
// alert.showAndWait();
//
// addEmployeeReset();
// addEmployeeShowListData();
//
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
// }
//
// public void addEmployeeReset()
// {
// addEmployeeEmployeeID.setText("");
// addEmployeeFirstName.setText("");
// addEmployeeLastName.setText("");
//// addEmployeeGender.getSelectionModel().clearSelection();
// addEmployeeGender.setValue("");
// addEmployeePhoneNumber.setText("");
// addEmployeePosition.getSelectionModel().clearSelection();
// addEmployeePosition.setValue("");
// addEmployeeImageView.setImage(null);
//
// UserData.path = null;
// }
//
// public void addEmployeePositionList()
// {
// ObservableList<String> positionObservableList =
// FXCollections.observableArrayList();
//
// for (int i = 0; i < positionList.length; i++)
// {
// positionObservableList.add(positionList[i]);
// }
//
// addEmployeePosition.setItems(positionObservableList);
// }
//
// public void addEmployeeGenderList()
// {
// ObservableList<String> genderObservableList =
// FXCollections.observableArrayList();
//
// for (int i = 0; i < genderList.length; i++)
// {
// genderObservableList.add(genderList[i]);
// }
//
// addEmployeeGender.setItems(genderObservableList);
// }
//
// public void addEmployeeUpdate()
// {
// try
// {
// Alert alert;
//
// if (addEmployeeEmployeeID.getText().isEmpty() ||
// addEmployeeFirstName.getText().isEmpty() ||
// addEmployeeLastName.getText().isEmpty() ||
// addEmployeeGender.getSelectionModel().getSelectedItem() == null ||
// addEmployeePhoneNumber.getText().isEmpty() ||
// addEmployeePosition.getSelectionModel().getSelectedItem() == null ||
// UserData.path == null || UserData.path.isEmpty())
// {
// alert = new Alert(AlertType.ERROR);
// alert.setTitle("Error");
// alert.setHeaderText("Invalid textfield(s)");
// alert.setContentText("Please fill in all blank fields");
// alert.showAndWait();
// return;
// }
//
// alert = new Alert(AlertType.CONFIRMATION);
//
// alert.setTitle("Confirm Action");
// alert.setHeaderText(null);
// alert.setContentText("Are you sure you want to update employee ID: " +
// addEmployeeEmployeeID.getText() + "?");
// Optional<ButtonType> option = alert.showAndWait();
//
// if (option.get().equals(ButtonType.OK))
// {
// connection = DatabaseUtility.connectToDatabase();
// statement = connection.createStatement();
// statement.execute(String.format("UPDATE employees SET firstName = '%s',
// lastName = '%s', gender = '%s', position = '%s', phoneNumber = '%s', image =
// '%s', date = '%s' WHERE employeeId = '%s'",
// addEmployeeFirstName.getText(), addEmployeeLastName.getText(),
// addEmployeeGender.getSelectionModel().getSelectedItem(),
// addEmployeePosition.getSelectionModel().getSelectedItem(),
// addEmployeePhoneNumber.getText(), UserData.path.replace("\\","\\\\"), new
// java.sql.Date(new Date().getTime()), addEmployeeEmployeeID.getText()));
//
// statement.execute(String.format("UPDATE employee_salary SET firstName = '%s',
// lastName = '%s', position = '%s' WHERE employeeId = '%s'",
// addEmployeeFirstName.getText(), addEmployeeLastName.getText(),
// addEmployeePosition.getSelectionModel().getSelectedItem(),
// addEmployeeEmployeeID.getText()));
//
// alert = new Alert(AlertType.INFORMATION);
// alert.setTitle("Information");
// alert.setHeaderText(null);
// alert.setContentText("Employee ID: " + addEmployeeEmployeeID.getText() + " is
// updated.");
// alert.showAndWait();
//
// addEmployeeShowListData();
// addEmployeeReset();
// }
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
// }
//
// public void addEmployeeDelete()
// {
// try
// {
// Alert alert;
//
// if (addEmployeeEmployeeID.getText().isEmpty() ||
// addEmployeeFirstName.getText().isEmpty() ||
// addEmployeeLastName.getText().isEmpty() ||
// addEmployeeGender.getSelectionModel().getSelectedItem() == null ||
// addEmployeePhoneNumber.getText().isEmpty() ||
// addEmployeePosition.getSelectionModel().getSelectedItem() == null ||
// UserData.path == null || UserData.path.isEmpty())
// {
// alert = new Alert(AlertType.ERROR);
// alert.setTitle("Error");
// alert.setHeaderText("Invalid textfield(s)");
// alert.setContentText("Please fill in all blank fields");
// alert.showAndWait();
// return;
// }
//
// alert = new Alert(AlertType.CONFIRMATION);
//
// alert.setTitle("Confirm Action");
// alert.setHeaderText(null);
// alert.setContentText("Are you sure you want to delete employee ID: " +
// addEmployeeEmployeeID.getText() + "?");
// Optional<ButtonType> option = alert.showAndWait();
//
// if (option.get().equals(ButtonType.OK))
// {
// connection = DatabaseUtility.connectToDatabase();
// statement = connection.createStatement();
// statement.execute("DELETE FROM employees WHERE employeeId = '" +
// addEmployeeEmployeeID.getText() + "'");
//
// statement.execute("DELETE FROM employee_salary WHERE employeeId = '" +
// addEmployeeEmployeeID.getText() + "'");
//
// alert = new Alert(AlertType.INFORMATION);
// alert.setTitle("Information");
// alert.setHeaderText(null);
// alert.setContentText("Employee ID: " + addEmployeeEmployeeID.getText() + " is
// deleted.");
// alert.showAndWait();
//
// addEmployeeShowListData();
// addEmployeeReset();
// }
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
// }
//
// public void addEmployeeSearch()
// {
// FilteredList<EmployeeData> filteredList = new
// FilteredList<>(addEmployeeDataList, e -> true);
//
// String newValue = addEmployeeSearch.getText();
//
// Predicate<EmployeeData> predicate = (predicateEmployeeData) ->
// {
// if (newValue == null || newValue.isEmpty())
// {
// return true;
// }
//
// String searchKey = newValue.toLowerCase();
//
// if (predicateEmployeeData.getEmployeeId().toLowerCase().contains(searchKey))
// {
// return true;
// }
// else if
// (predicateEmployeeData.getFirstName().toLowerCase().contains(searchKey))
// {
// return true;
// }
// else if
// (predicateEmployeeData.getLastName().toLowerCase().contains(searchKey))
// {
// return true;
// }
// else if (predicateEmployeeData.getGender().toLowerCase().contains(searchKey))
// {
// return true;
// }
// else if
// (predicateEmployeeData.getPhoneNumber().toLowerCase().contains(searchKey))
// {
// return true;
// }
// else if
// (predicateEmployeeData.getPosition().toLowerCase().contains(searchKey))
// {
// return true;
// }
// else if (predicateEmployeeData.getDate().toString().contains(searchKey))
// {
// return true;
// }
// else
// {
// return false;
// }
// };
//
// filteredList.setPredicate(predicate);
//
// SortedList<EmployeeData> sortedList = new SortedList<>(filteredList);
//
// sortedList.comparatorProperty().bind(addEmployeeTableView.comparatorProperty());
// addEmployeeTableView.setItems(sortedList);
// }
//
//
// public ObservableList<EmployeeData> salaryListData()
// {
// ObservableList<EmployeeData> listData = FXCollections.observableArrayList();
//
// connection = DatabaseUtility.connectToDatabase();
//
// try
// {
// preparedStatement = connection.prepareStatement("SELECT * from
// employee_salary");
// resultSet = preparedStatement.executeQuery();
//
// EmployeeData employeeData;
//
// while (resultSet.next())
// {
// employeeData = new EmployeeData(resultSet.getString("employeeId"),
// resultSet.getString("firstName"),
// resultSet.getString("lastName"),
// resultSet.getString("position"),
// resultSet.getDouble("salary"));
//
// listData.add(employeeData);
// }
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
//
// return listData;
// }
//
// public void salaryShowListData()
// {
// salaryDataList = salaryListData();
//
// salaryColumnEmployeeID.setCellValueFactory(new
// PropertyValueFactory<>("employeeId"));
// salaryColumnFirstName.setCellValueFactory(new
// PropertyValueFactory<>("firstName"));
// salaryColumnLastName.setCellValueFactory(new
// PropertyValueFactory<>("lastName"));
// salaryColumnPosition.setCellValueFactory(new
// PropertyValueFactory<>("position"));
// salaryColumnSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
//
// salaryTableView.setItems(salaryDataList);
// }
//
// public void salaryUpdate()
// {
// try
// {
// Alert alert;
//
// if (salaryEmployeeId.getText().isEmpty() ||
// salaryFirstName.getText().isEmpty() ||
// salaryLastName.getText().isEmpty() ||
// salaryPosition.getText().isEmpty())
// {
// alert = new Alert(AlertType.ERROR);
// alert.setTitle("Employee not selected");
// alert.setHeaderText(null);
// alert.setContentText("Please select an employee");
// alert.showAndWait();
// return;
// }
//
// connection = DatabaseUtility.connectToDatabase();
// statement = connection.createStatement();
// statement.executeUpdate(String.format("UPDATE employee_salary SET salary =
// '%s' WHERE employeeId = '%s'", salarySalary.getText(),
// salaryEmployeeId.getText()));
//
// alert = new Alert(AlertType.INFORMATION);
// alert.setTitle("Salary updated");
// alert.setHeaderText(null);
// alert.setContentText("Successfully updated!");
// alert.showAndWait();
//
// salaryShowListData();
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
// }
//
// public void salaryReset()
// {
// salaryEmployeeId.setText("");
// salaryFirstName.setText("");
// salaryLastName.setText("");
// salaryPosition.setText("");
// salarySalary.setText("");
// }
//
// public void salarySelect()
// {
// EmployeeData employeeData =
// salaryTableView.getSelectionModel().getSelectedItem();
// int index = salaryTableView.getSelectionModel().getSelectedIndex();
//
// if (index < 0)
// {
// return;
// }
//
// salaryEmployeeId.setText(employeeData.getEmployeeId());
// salaryFirstName.setText(employeeData.getFirstName());
// salaryLastName.setText(employeeData.getLastName());
// salaryPosition.setText(employeeData.getPosition());
// salarySalary.setText(String.valueOf(employeeData.getSalary()));
// }
//
// public void homeSetTotalEmployees()
// {
// int employeeCount = 0;
//
// try
// {
// connection = DatabaseUtility.connectToDatabase();
// statement = connection.createStatement();
//
// resultSet = statement.executeQuery("SELECT COUNT(id) FROM employees");
//
// while (resultSet.next())
// {
// employeeCount = resultSet.getInt("COUNT(id)");
// }
//
// homeTotalEmployees.setText(String.valueOf(employeeCount));
//
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
// }
//
// public void homeSetTotalPresent()
// {
// int totalPresent = 0;
//
// try
// {
// connection = DatabaseUtility.connectToDatabase();
// statement = connection.createStatement();
//
// resultSet = statement.executeQuery("SELECT COUNT(id) FROM employee_salary
// WHERE salary != '0.0'");
//
// while (resultSet.next())
// {
// totalPresent = resultSet.getInt("COUNT(id)");
// }
//
// homeTotalPresent.setText(String.valueOf(totalPresent));
//
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
// }
//
// public void homeSetTotalInactive()
// {
// int totalInactive = 0;
//
// try
// {
// connection = DatabaseUtility.connectToDatabase();
// statement = connection.createStatement();
//
// resultSet = statement.executeQuery("SELECT COUNT(id) FROM employee_salary
// WHERE salary = '0.0'");
//
// while (resultSet.next())
// {
// totalInactive = resultSet.getInt("COUNT(id)");
// }
//
// homeTotalInactive.setText(String.valueOf(totalInactive));
//
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
// }
//
// public void homeSetChart()
// {
// homeEmployeesDataChart.getData().clear();
//
// try
// {
// Series<String, Integer> chart = new Series<>();
//
// connection = DatabaseUtility.connectToDatabase();
// statement = connection.createStatement();
//
// resultSet = statement.executeQuery("SELECT date, COUNT(id) FROM employees
// GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 7");
//
// while (resultSet.next())
// {
// chart.getData().add(new Data<String, Integer>(resultSet.getString(1),
// resultSet.getInt(2)));
// }
//
// homeEmployeesDataChart.getData().add(chart);
// }
// catch (SQLException sqle)
// {
// System.out.println("Connection error in " + this.getClass().getName());
// sqle.printStackTrace();
// }
// }
}
