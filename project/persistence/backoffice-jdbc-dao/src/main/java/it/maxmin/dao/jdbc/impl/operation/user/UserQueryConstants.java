package it.maxmin.dao.jdbc.impl.operation.user;

public enum UserQueryConstants {
	;
	
	public static final String BASE_SELECT_USERS = ""
			+ "SELECT DISTINCT u.UserId, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedDate, a.AddressId, a.Address, a.City, a.StateId, a.Region, a.PostalCode "
			+ "FROM User u " 
			+ "LEFT JOIN UserAddress ua ON u.UserId = ua.UserId "
			+ "LEFT JOIN Address a ON ua.AddressId = a.AddressId " ;

	public static final String SELECT_ALL_USERS = ""
			+ BASE_SELECT_USERS
			+ "ORDER BY u.UserId";

	public static final String SELECT_USER_BY_ACCOUNT_NAME = ""
			+ BASE_SELECT_USERS 
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY u.UserId";

	public static final String SELECT_USERS_BY_FIRST_NAME = ""
			+ BASE_SELECT_USERS
			+ "WHERE FirstName = :firstName "
			+ "ORDER BY u.UserId";

	public static final String UPDATE_USER = ""
			+ "UPDATE User "
			+ "SET AccountName=:accountName, FirstName=:firstName, LastName=:lastName, BirthDate=:birthData "
			+ "WHERE UserId=:userId";

}
