package it.maxmin.dao.jdbc.impl.operation.user;

public enum UserQueryConstants {
	;
	
	public static final String BASE_SELECT_USERS = ""
			+ "SELECT DISTINCT u.Id AS UserId, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedAt, "
			+ "     d.Id AS DepartmentId, d.Name AS DepartmentName, "
			+ "     r.Id AS RoleId, r.RoleName, "
			+ "     a.Id AS AddressId, a.Description, a.City, a.Region, a.PostalCode, "
			+ "     s.Id AS StateId, s.Name AS StateName, s.Code "
			+ "FROM User u " 
			+ "INNER JOIN Department d ON u.DepartmentId = d.Id "
			+ "LEFT JOIN UserRole ur ON u.Id = ur.UserId "
			+ "LEFT JOIN Role r ON ur.RoleId = r.Id "
			+ "LEFT JOIN UserAddress ua ON u.Id = ua.UserId "
			+ "LEFT JOIN Address a ON ua.AddressId = a.Id "
			+ "LEFT JOIN State s ON a.StateId  = s.Id ";

			
	public static final String SELECT_ALL_USERS = ""
			+ BASE_SELECT_USERS
			+ "ORDER BY UserId, AddressId, StateId";

	public static final String SELECT_USER_BY_ACCOUNT_NAME = ""
			+ BASE_SELECT_USERS 
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY UserId, AddressId, StateId";

	public static final String SELECT_USERS_BY_FIRST_NAME = ""
			+ BASE_SELECT_USERS
			+ "WHERE FirstName = :firstName "
			+ "ORDER BY UserId, AddressId, StateId";

	public static final String UPDATE_USER = ""
			+ "UPDATE User "
			+ "SET AccountName=:accountName, FirstName=:firstName, LastName=:lastName, BirthDate=:birthData, DepartmentId=:departmentId "
			+ "WHERE Id = :userId";

	public static final String INSERT_USER = "" 
			+ "INSERT INTO User "
			+ "SET AccountName = :accountName, FirstName = :firstName, LastName = :lastName, DepartmentId = :departmentId, BirthDate = :birtDate";
	
	public static final String SELECT_ROLE_BY_ROLE_NAME = ""
			+ "SELECT Id, RoleName "
			+ "FROM Role "
			+ "WHERE RoleName = :roleName ";

	public static final String INSERT_USER_ROLE = "" 
			+ "INSERT INTO UserRole "
			+ "SET UserId = :userId, RoleId = :roleId";
	
	public static final String INSERT_USER_ADDRESS = "" 
			+ "INSERT INTO UserAddress "
			+ "SET UserId = :userId, AddressId = :addressId";
	
	public static final String SELECT_STATE_BY_STATE_NAME = ""
			+ "SELECT Id, Name "
			+ "FROM State "
			+ "WHERE Name = :name ";
	
	public static final String SELECT_DEPARTMENT_BY_DEPARTMENT_NAME = ""
			+ "SELECT Id, Name "
			+ "FROM Department "
			+ "WHERE Name = :name ";
}
