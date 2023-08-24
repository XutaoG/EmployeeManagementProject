package application;

import java.sql.Date;

public abstract class Employee
{	
	public static final String[] genders = {"Male", "Female", "Other"};
	public static final String[] employeeTypes = {"Salaried", "Waged"};
	public static final String[] status = {"Active", "Inactive"};
	public static final String[] positions = { "Cashier", "Food Preparation Worker", "Bagger", "Floral Assistant", "Stock Clerk",
			"Pharmacist", "Butcher", "Bakery Associate", "Customer Service Representative", "Store Manager" };
	
	public static final String MALE = "Male";
	public static final String FEMALE = "Female";
	public static final String ACTIVE = "Active";
	public static final String INACTIVE = "Inactive";
	public static final String SALARIED = "Salaried";
	public static final String WAGED = "Waged";
	
	private String employeeId;
	private String isActive;
	private String firstName;
	private String lastName;
	private String gender;
	private String phoneNumber;
	private String position;
	private Date date;
	
	public Employee(String employeeId, String isActive, String firstName, String lastName, String gender, String phoneNumber, String position, Date date)
	{
		this.setEmployeeId(employeeId);
		this.setIsActive(isActive);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setGender(gender);
		this.setPhoneNumber(phoneNumber);
		this.setPosition(position);
		this.setDate(date);
	}

	public String getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(String employeeId)
	{
		this.employeeId = employeeId;
	}
	
	public String getIsActive()
	{
		return isActive;
	}
	
	public void setIsActive(String isActive)
	{
		this.isActive = isActive;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public String getPosition()
	{
		return position;
	}

	public void setPosition(String position)
	{
		this.position = position;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}
}

class SalariedEmployee extends Employee
{
	private double salary;
	
	public SalariedEmployee(String employeeId, String isActive, String firstName, String lastName, String gender, String phoneNumber, String position, Date date, double salary)
	{
		super(employeeId, isActive, firstName, lastName, gender, phoneNumber, position, date);
		this.salary = salary;
	}
	
	public double getSalary()
	{
		return salary;
	}
	
	public void setSalary(double salary)
	{
		this.salary = salary;
	}
}

class WagedEmployee extends Employee
{
	private double wage;
	
	public WagedEmployee(String employeeId, String isActive, String firstName, String lastName, String gender, String phoneNumber, String position, Date date, double wage)
	{
		super(employeeId, isActive, firstName, lastName, gender, phoneNumber, position, date);
		this.wage = wage;
	}
	
	public double getWage()
	{
		return wage;
	}
	
	public void setWage(double wage)
	{
		this.wage = wage;
	}
}


