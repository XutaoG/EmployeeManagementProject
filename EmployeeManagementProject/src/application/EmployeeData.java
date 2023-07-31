package application;

import java.sql.Date;

public class EmployeeData
{
	private int employeeId;
	private String firstName;
	private String lastName;
	private String gender;
	private String phoneNumber;
	private String position;
	private String image;
	private Date date;
	
	public EmployeeData(int employeeId, String firstName, String lastName, String gender, String phoneNumber, String position, String image, Date date)
	{
		this.setEmployeeId(employeeId);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setGender(gender);
		this.setPhoneNumber(phoneNumber);
		this.setPosition(position);
		this.setImage(image);
		this.setDate(date);
	}

	public int getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(int employeeId)
	{
		this.employeeId = employeeId;
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

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
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
