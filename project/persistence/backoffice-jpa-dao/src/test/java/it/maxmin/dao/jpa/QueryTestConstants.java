package it.maxmin.dao.jpa;

public enum QueryTestConstants {
	;

	public static final String SELECT_ROLES_BY_USER_USER_ID = ""
			+ "SELECT ur.Id, ur.RoleName "
			+ "FROM UserRole ur "
			+ "INNER JOIN UserUserRole uur ON ur.Id = uur.UserRoleId "
			+ "WHERE uur.UserId = :userId "
			+ "ORDER BY ur.Id";
	
	public static final String SELECT_ROLES_BY_USER_ACCOUNT_NAME = ""
			+ "SELECT ur.Id, ur.RoleName "
			+ "FROM UserRole ur "
			+ "INNER JOIN UserUserRole uur ON ur.Id = uur.UserRoleId "
			+ "INNER JOIN User u ON u.Id = uur.UserId "
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY ur.Id";	
	
	public static final String SELECT_ROLE_BY_NAME = ""
			+ "SELECT Id, RoleName "
			+ "FROM UserRole "
			+ "WHERE roleName = :roleName ";
	
	public static final String SELECT_ADDRESSES_BY_USER_USER_ID = ""
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
	
	public static final String SELECT_ADDRESS_BY_ADDRESS_ID = ""
			+ "SELECT Id, Description, City, StateId, Region, PostalCode "
			+ "FROM Address "
			+ "WHERE Id = :addressId ";
	
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
	
	public static final String SELECT_USER_BY_ADDRESS_POSTAL_CODE = ""
			+ "SELECT u.Id, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedAt, u.DepartmentId, u.Version "
			+ "FROM User u "
			+ "INNER JOIN UserAddress ua ON u.Id = ua.UserId "
			+ "INNER JOIN Address a ON ua.AddressId = a.Id "
			+ "WHERE a.PostalCode = :postalCode "
			+ "ORDER BY u.Id";
    
	public static final String SELECT_STATE_BY_NAME = ""
			+ "SELECT Id, Name, Code "
			+ "FROM State "
			+ "WHERE Name = :name ";
	
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
	
	public static final String SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME = ""
			+ "SELECT d.Id, d.Name "
			+ "FROM Department d "
			+ "INNER JOIN User u ON u.DepartmentId = d.Id "
			+ "WHERE u.AccountName = :accountName ";
}
