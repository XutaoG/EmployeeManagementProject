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
	private TableColumn<Employee, String> salaryColumnEmployeeID;

	@FXML
	private TableColumn<Employee, String> salaryColumnFirstName;

	@FXML
	private TableColumn<Employee, String> salaryColumnLastName;

	@FXML
	private TableColumn<Employee, String> salaryColumnPosition;

	@FXML
	private TableColumn<Employee, String> salaryColumnSalary;

	@FXML
	private TableView<Employee> salaryTableView;

	@FXML
	private Button updateButton;
}
