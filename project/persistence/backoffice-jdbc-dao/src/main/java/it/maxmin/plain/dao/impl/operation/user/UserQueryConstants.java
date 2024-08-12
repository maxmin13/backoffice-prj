package it.maxmin.plain.dao.impl.operation.user;

public enum UserQueryConstants {
    ;

	public static final String SELECT_ALL_USERS = "SELECT UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate FROM User ORDER BY UserId";
	public static final String SELECT_USER_BY_ACCOUNT_NAME = "SELECT UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate FROM User WHERE AccountName = :accountName ORDER BY UserId";
    public static final String SELECT_USER_BY_FIRST_NAME = "SELECT UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate FROM User WHERE FirstName = :firstName ORDER BY UserId";
    public static final String UPDATE_USER = "UPDATE User SET AccountName=:accountName, FirstName=:firstName, LastName=:lastName, BirthDate=:birthData WHERE UserId=:userId";

}
