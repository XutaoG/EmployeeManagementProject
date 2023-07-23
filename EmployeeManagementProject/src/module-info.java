module EmployeeManagementProject {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires fontawesomefx;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
