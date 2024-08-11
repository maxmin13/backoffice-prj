package it.maxmin.plain.dao;

public enum QueryTestConstants {
    ;
	
	public static final String FIND_ADDRESS_BY_ADDRESS_ID = "select AddressId, Address, City, StateId, Region, PostalCode from Address where AddressId = :addressId";	
	public static final String FIND_USER_BY_USER_ID = "select UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate from User where UserId = :userId";	
	public static final String FIND_STATE_BY_NAME = "select StateId, Name, Code from State where Name = :name";
}
