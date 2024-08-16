package it.maxmin.dao.jdbc.impl.operation.user;

public enum UserQueryConstants {
	;
	
	public static final String BASE_SELECT_USERS = ""
			+ "SELECT DISTINCT u.Id, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedDate, a.Id AS AddressId, a.Description, a.City, a.StateId, a.Region, a.PostalCode "
			+ "FROM User u " 
			+ "LEFT JOIN UserAddress ua ON u.Id = ua.UserId "
			+ "LEFT JOIN Address a ON ua.AddressId = a.Id " ;

	public static final String SELECT_ALL_USERS = ""
			+ BASE_SELECT_USERS
			+ "ORDER BY u.Id, a.id";

	public static final String SELECT_USER_BY_ACCOUNT_NAME = ""
			+ BASE_SELECT_USERS 
			+ "WHERE u.AccountName = :accountName "
			+ "ORDER BY u.Id, a.id";

	public static final String SELECT_USERS_BY_FIRST_NAME = ""
			+ BASE_SELECT_USERS
			+ "WHERE FirstName = :firstName "
			+ "ORDER BY u.Id, a.id";

	public static final String UPDATE_USER = ""
			+ "UPDATE User "
			+ "SET AccountName=:accountName, FirstName=:firstName, LastName=:lastName, BirthDate=:birthData "
			+ "WHERE Id=:userId";

}
