package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HomeController implements Initializable
{
	@FXML
	private Label totalEmployeesLabel;
	
	@FXML
	private Label totalActiveLabel;

	@FXML
	private Label totalInactiveLabel;
	
	@FXML
	private Label totalMaleLabel;
	
	@FXML
	private Label totalFemaleLabel;
	
	@FXML
	private Label totalOtherLabel;

	@FXML
	private Label totalSalariedLabel;
	
	@FXML
	private Label totalWagedLabel;
	
	// SQL connection
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		setStatistics();
	}
	
	public void update()
	{
		setStatistics();
	}
	
	public void setStatistics()
	{
		int employeeCount = 0;
		int activeCount = 0;
		int inactiveCount = 0;
		int maleCount = 0;
		int femaleCount = 0;
		int otherCount = 0;
		int salariedCount = 0;
		int wagedCount = 0;
		
		try
		{
			connection = DatabaseUtility.connectToDatabase();
			preparedStatement = connection.prepareStatement("SELECT isActive, gender, employeeType FROM employees");
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next())
			{
				employeeCount++;
				
				if (resultSet.getString("isActive").compareTo("Active") == 0)
				{
					activeCount++;
				}
				
				if (resultSet.getString("gender").compareTo("Male") == 0)
				{
					maleCount++;
				}
				else if (resultSet.getString("gender").compareTo("Female") == 0)
				{
					femaleCount++;
				}
				
				if (resultSet.getString("employeeType").compareTo("Salaried") == 0)
				{
					salariedCount++;
				}
			}
			
			inactiveCount = employeeCount - activeCount;
			otherCount = employeeCount - maleCount - femaleCount;
			wagedCount = employeeCount - salariedCount;
			
			// Adjust Label
			totalEmployeesLabel.setText(String.valueOf(employeeCount));
			totalActiveLabel.setText(String.valueOf(activeCount));
			totalInactiveLabel.setText(String.valueOf(inactiveCount));
			
			totalMaleLabel.setText(String.valueOf(maleCount));
			totalFemaleLabel.setText(String.valueOf(femaleCount));
			totalOtherLabel.setText(String.valueOf(otherCount));
			
			totalSalariedLabel.setText(String.valueOf(salariedCount));
			totalWagedLabel.setText(String.valueOf(wagedCount));
		}
		catch (SQLException sqle)
		{
			System.out.println("Connection error in " + this.getClass().getName() + " setStatistics()");
			 sqle.printStackTrace();
		}
	}
}
