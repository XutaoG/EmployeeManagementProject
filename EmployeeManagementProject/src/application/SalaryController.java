package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SalaryController
{
	@FXML
	private Button clearButton;

	@FXML
	private Label employeeId;

	@FXML
	private Label firstName;

	@FXML
	private Label lastName;

	@FXML
	private Label position;

	@FXML
	private TextField salary;

	@FXML
	private TableColumn<EmployeeData, String> salaryColumnEmployeeID;

	@FXML
	private TableColumn<EmployeeData, String> salaryColumnFirstName;

	@FXML
	private TableColumn<EmployeeData, String> salaryColumnLastName;

	@FXML
	private TableColumn<EmployeeData, String> salaryColumnPosition;

	@FXML
	private TableColumn<EmployeeData, String> salaryColumnSalary;

	@FXML
	private TableView<EmployeeData> salaryTableView;

	@FXML
	private Button updateButton;
}
