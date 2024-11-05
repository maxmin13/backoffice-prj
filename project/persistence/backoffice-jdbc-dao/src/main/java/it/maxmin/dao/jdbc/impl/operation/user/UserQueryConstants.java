package it.maxmin.dao.jdbc.impl.operation.user;

public enum UserQueryConstants {
	;
	
	public static final String BASE_SELECT_USERS = ""
			+ "SELECT DISTINCT u.Id, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedAt, "
			+ "     a.Id AS AddressId, a.Description, a.City, a.Region, a.PostalCode, "
			+ "     d.Id AS DepartmentId, d.Name AS DepartmentName, "
			+ "     s.Id AS StateId, s.Name AS StateName, s.Code, "
			+ "     ur.Id AS RoleId, ur.RoleName "
			+ "FROM User u " 
			+ "LEFT JOIN UserAddress ua ON u.Id = ua.UserId "
			+ "LEFT JOIN Address a ON ua.AddressId = a.Id "
			+ "LEFT JOIN UserUserRole uur ON u.Id = uur.UserId "
			+ "LEFT JOIN UserRole ur ON uur.UserRoleId = ur.Id "
			+ "LEFT JOIN Department d ON u.DepartmentId = d.Id "
			+ "LEFT JOIN State s ON a.StateId  = s.Id ";

	public static final String SELECT_ALL_USERS = ""
			+ BASE_SELECT_USERS
			+ "ORDER BY u.Id, AddressId, StateId";

	public static final String SELECT_USER_BY_ACCOUNT_NAME = ""
			+ BASE_SELECT_USERS 
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY u.Id, AddressId, StateId";

	public static final String SELECT_USERS_BY_FIRST_NAME = ""
			+ BASE_SELECT_USERS
			+ "WHERE FirstName = :firstName "
			+ "ORDER BY u.Id, AddressId, StateId";

	public static final String UPDATE_USER = ""
			+ "UPDATE User "
			+ "SET AccountName=:accountName, FirstName=:firstName, LastName=:lastName, BirthDate=:birthData, DepartmentId=:departmentId "
			+ "WHERE Id = :userId";

	public static final String INSERT_USER = "" 
			+ "INSERT INTO User "
			+ "SET AccountName = :accountName, FirstName = :firstName, LastName = :lastName, DepartmentId = :departmentId, BirthDate = :birtDate";
	
	public static final String SELECT_USER_ROLE_BY_ROLE_NAME = ""
			+ "SELECT Id, RoleName "
			+ "FROM UserRole "
			+ "WHERE roleName = :roleName ";

	public static final String INSERT_USER_USER_ROLE = "" 
			+ "INSERT INTO UserUserRole "
			+ "SET UserId = :userId, UserRoleId = :roleId";
	
	public static final String INSERT_USER_ADDRESS = "" 
			+ "INSERT INTO UserAddress "
			+ "SET UserId = :userId, AddressId = :addressId";
}
