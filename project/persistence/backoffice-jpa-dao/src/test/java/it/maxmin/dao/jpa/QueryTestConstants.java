package it.maxmin.dao.jpa;

public enum QueryTestConstants {
	;

	public static final String SELECT_ROLES_BY_USER_ID = ""
			+ "SELECT ur.Id, ur.RoleName "
			+ "FROM UserRole ur "
			+ "LEFT JOIN UserUserRole uur ON ur.Id = uur.UserRoleId "
			+ "WHERE uur.UserId = :userId "
			+ "ORDER BY ur.Id";
	
	public static final String SELECT_ROLES_BY_USER_ACCOUNT_NAME = ""
			+ "SELECT ur.Id, ur.RoleName "
			+ "FROM UserRole ur "
			+ "LEFT JOIN UserUserRole uur ON ur.Id = uur.UserRoleId "
			+ "LEFT JOIN User u ON u.Id = uur.UserId "
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY ur.Id";	
	
	public static final String SELECT_ROLE_BY_NAME = ""
			+ "SELECT Id, RoleName "
			+ "FROM UserRole "
			+ "WHERE roleName = :roleName "
			+ "ORDER BY Id";

	public static final String SELECT_ADDRESSES_BY_USER_ID = ""
			+ "SELECT a.Id, a.Description, a.City, a.StateId, a.Region, a.PostalCode "
			+ "FROM Address a "
			+ "INNER JOIN UserAddress ua ON a.Id = ua.AddressId "
			+ "WHERE ua.UserId = :userId "
			+ "ORDER BY a.Id";
	
	public static final String SELECT_ADDRESS_BY_ADDRESS_ID = ""
			+ "SELECT Id, Description, City, StateId, Region, PostalCode "
			+ "FROM Address "
			+ "WHERE Id = :addressId "
			+ "ORDER BY Id";
	
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
			+ "SELECT Id, AccountName, FirstName, LastName, BirthDate, CreatedAt, DepartmentId "
			+ "FROM User WHERE Id = :userId "
			+ "ORDER BY Id";
	
	public static final String SELECT_USER_BY_ACCOUNT_NAME = ""
			+ "SELECT Id, AccountName, FirstName, LastName, BirthDate, CreatedAt, DepartmentId "
			+ "FROM User "
			+ "WHERE AccountName = :accountName "
			+ "ORDER BY Id";
    
	public static final String SELECT_STATE_BY_NAME = ""
			+ "SELECT Id, Name, Code "
			+ "FROM State "
			+ "WHERE Name = :name "
			+ "ORDER BY Id";
	
	public static final String SELECT_STATE_BY_ADDRESS_POSTAL_CODE = ""
			+ "SELECT s.Id, s.Name, s.Code "
			+ "FROM State s "
			+ "INNER JOIN Address a ON a.StateId = s.Id "
			+ "WHERE a.PostalCode = :postalCode "
			+ "ORDER BY s.Id";
	
	public static final String SELECT_DEPARTMENT_BY_NAME = ""
			+ "SELECT Id, Name "
			+ "FROM Department "
			+ "WHERE Name = :name "
			+ "ORDER BY Id";
	
	public static final String SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME = ""
			+ "SELECT d.Id, d.Name "
			+ "FROM Department d "
			+ "INNER JOIN User u ON u.DepartmentId = d.Id "
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY d.Id";
}