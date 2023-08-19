package application;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;

public class HomeController
{
	@FXML
	private BarChart<String, Integer> employeeDataChart;

	@FXML
	private Label totalEmployees;

	@FXML
	private Label totalInactive;

	@FXML
	private Label totalPresent;
}
