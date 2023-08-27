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
	private Button employeeInformationButton;

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

	private ObservableList<Employee> addEmployeeDataList = null;
	private ObservableList<Employee> salaryDataList = null;

	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

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
		employeeInformationButton.setStyle("-fx-background-color: transparent");
		salaryButton.setStyle("-fx-background-color: transparent");

		addEmployeeForm.setVisible(false);
		salaryForm.setVisible(false);

		homeForm.setVisible(true);
		homeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
	}

	public void switchForm(ActionEvent event)
	{
		homeButton.setStyle("-fx-background-color: transparent");
		employeeInformationButton.setStyle("-fx-background-color: transparent");
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
		else if (event.getSource() == employeeInformationButton)
		{
			addEmployeeForm.setVisible(true);
			employeeInformationButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #083fb5, #f124f8)");
			
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
