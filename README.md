# javafx-supermarket-employee-management-system

### Last updated: 9/23

Graphical user interface project implemented with JavaFX.

Includes local SQL database integration with standard CRUD operations

Include interface to track employee information and salaries and sorting functions

## To run:
Set up local SQL schema with 3 tables "admin" and "employees" (I don't know how to write SQL scripts, sorry)

"admin" table columns: 
1. id (INT)
2. username (VARCHAR(45))
3. password (VARCHAR(45))

Manually insert credentials into table to log in.

"employee" table columns: 
1. id (INT)
2. employee_id (VARCHAR(45))
3. is_active (VARCHAR(45))
4. firstName (VARCHAR(45))
5. lastName (VARCHAR(45))
6. gender (VARCHAR(45))
7. phoneNumber (VARCHAR(45))
8. position (VARCHAR(45))
9. date (SQL_DATE)
10. employeeType (VARCHAR(45))
11. pay (DOUBLE)
    
GUI are used to create, read, edit, delete employees.
