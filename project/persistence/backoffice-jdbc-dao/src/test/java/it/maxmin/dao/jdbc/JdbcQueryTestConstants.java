package it.maxmin.dao.jdbc;

public enum JdbcQueryTestConstants {
	;

	public static final String SELECT_ROLES_BY_USER_ID = ""
			+ "SELECT r.Id, r.Name "
			+ "FROM Role r "
			+ "INNER JOIN UserRole ur ON r.Id = ur.RoleId "
			+ "WHERE ur.UserId = :userId "
			+ "ORDER BY r.Id";
	
	public static final String SELECT_ROLES_BY_USER_ACCOUNT_NAME = ""
			+ "SELECT r.Id, r.Name "
			+ "FROM Role r "
			+ "INNER JOIN UserRole ur ON r.Id = ur.RoleId "
			+ "INNER JOIN User u ON u.Id = ur.UserId "
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY r.Id";	
	
	public static final String SELECT_ROLE_BY_NAME = ""
			+ "SELECT Id, Name "
			+ "FROM Role "
			+ "WHERE name = :name ";
	
	public static final String SELECT_ADDRESSES_BY_USER_ID = ""
			+ "SELECT a.Id, a.Description, a.City, a.StateId, a.Region, a.PostalCode "
			+ "FROM Address a "
			+ "INNER JOIN UserAddress ua ON a.Id = ua.AddressId "
			+ "INNER JOIN User u ON u.Id = ua.UserId "
			+ "WHERE u.Id = :userId "
			+ "ORDER BY a.Id";
		
	public static final String SELECT_ADDRESSES_BY_USER_ACCOUNT_NAME = ""
			+ "SELECT a.Id, a.Description, a.City, a.StateId, a.Region, a.PostalCode "
			+ "FROM Address a "
			+ "INNER JOIN UserAddress ua ON a.Id = ua.AddressId "
			+ "INNER JOIN User u ON u.Id = ua.UserId "
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY a.Id";
	
	public static final String SELECT_ADDRESS_BY_ID = ""
			+ "SELECT Id, Description, City, StateId, Region, PostalCode "
			+ "FROM Address "
			+ "WHERE Id = :id ";
	
	public static final String SELECT_ADDRESS_BY_POSTAL_CODE = ""
			+ "SELECT Id, Description, City, StateId, Region, PostalCode "
			+ "FROM Address "
			+ "WHERE PostalCode = :postalCode "
			+ "ORDER BY Id";
	
	public static final String SELECT_ALL_ADDRESSES = ""
			+ "SELECT Id, Description, City, StateId, Region, PostalCode "
			+ "FROM Address "
			+ "ORDER BY Id";
	
	public static final String SELECT_USER_BY_USER_ID = ""
			+ "SELECT Id, AccountName, FirstName, LastName, BirthDate, CreatedAt, DepartmentId, Version "
			+ "FROM User "
			+ "WHERE Id = :userId ";
	
	public static final String SELECT_USER_BY_ACCOUNT_NAME = ""
			+ "SELECT Id, AccountName, FirstName, LastName, BirthDate, CreatedAt, DepartmentId, Version "
			+ "FROM User "
			+ "WHERE AccountName = :accountName ";
	
	public static final String SELECT_USERS_BY_ADDRESS_ID = ""
			+ "SELECT u.Id, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedAt, u.DepartmentId, u.Version "
			+ "FROM User u "
			+ "INNER JOIN UserAddress ua ON u.Id = ua.UserId "
			+ "INNER JOIN Address a ON ua.AddressId = a.Id "
			+ "WHERE a.Id = :id "
			+ "ORDER BY u.Id";
	
	public static final String SELECT_USERS_BY_ADDRESS_POSTAL_CODE = ""
			+ "SELECT u.Id, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedAt, u.DepartmentId, u.Version "
			+ "FROM User u "
			+ "INNER JOIN UserAddress ua ON u.Id = ua.UserId "
			+ "INNER JOIN Address a ON ua.AddressId = a.Id "
			+ "WHERE a.PostalCode = :postalCode "
			+ "ORDER BY u.Id";
	
	public static final String SELECT_USERS_BY_ROLE_NAME = ""
			+ "SELECT u.Id, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedAt, u.DepartmentId "
			+ "FROM User u "
			+ "INNER JOIN UserRole ur ON u.Id = ur.UserId "
			+ "INNER JOIN Role r ON ur.RoleId = r.Id "
			+ "WHERE r.Name = :name "
			+ "ORDER BY u.Id";
    
	public static final String SELECT_STATE_BY_NAME = ""
			+ "SELECT Id, Name, Code "
			+ "FROM State "
			+ "WHERE Name = :name ";

	public static final String SELECT_STATE_BY_ADDRESS_ID = ""
			+ "SELECT s.Id, s.Name, s.Code "
			+ "FROM State s "
			+ "INNER JOIN Address a ON a.StateId = s.Id "
			+ "WHERE a.Id = :addressId ";
	
	public static final String SELECT_STATE_BY_ADDRESS_POSTAL_CODE = ""
			+ "SELECT s.Id, s.Name, s.Code "
			+ "FROM State s "
			+ "INNER JOIN Address a ON a.StateId = s.Id "
			+ "WHERE a.PostalCode = :postalCode ";

	public static final String SELECT_DEPARTMENT_BY_ID = ""
			+ "SELECT Id, Name "
			+ "FROM Department "
			+ "WHERE Id = :id ";
	
	public static final String SELECT_DEPARTMENT_BY_NAME = ""
			+ "SELECT Id, Name "
			+ "FROM Department "
			+ "WHERE Name = :name ";
	
	public static final String SELECT_DEPARTMENT_BY_USER_ID = ""
			+ "SELECT d.Id, d.Name "
			+ "FROM Department d "
			+ "INNER JOIN User u ON u.DepartmentId = d.Id "
			+ "WHERE u.Id = :userId ";	
	
	public static final String SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME = ""
			+ "SELECT d.Id, d.Name "
			+ "FROM Department d "
			+ "INNER JOIN User u ON u.DepartmentId = d.Id "
			+ "WHERE u.AccountName = :accountName ";
}
