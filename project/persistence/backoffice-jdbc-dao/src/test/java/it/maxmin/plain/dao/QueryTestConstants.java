package it.maxmin.plain.dao;

public enum QueryTestConstants {
    ;

	public static final String FIND_USER_BY_USER_ID = "select UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate from User where UserId = :userId";	
}
