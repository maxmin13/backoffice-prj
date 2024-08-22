package it.maxmin.dao.jdbc;

public enum JdbcQueryTestConstants {
	;

	public static final String SELECT_ROLES_BY_USER_ID = ""
			+ "SELECT ur.Id, ur.RoleName "
			+ "FROM UserRole ur "
			+ "INNER JOIN UserUserRole uur ON ur.Id = uur.UserRoleId "
			+ "WHERE uur.UserId = :userId "
			+ "ORDER BY ur.Id";

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
	
	public static final String SELECT_DEPARTMENT_BY_NAME = ""
			+ "SELECT Id, Name "
			+ "FROM Department "
			+ "WHERE Name = :name "
			+ "ORDER BY Id";
}
