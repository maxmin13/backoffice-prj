package it.maxmin.plain.dao;

public enum QueryTestConstants {
	;

	public static final String SELECT_ADDRESSES_BY_USER_ID = "SELECT a.AddressId, a.Address, a.City, a.StateId, a.Region, a.PostalCode FROM Address a LEFT JOIN UserAddress u ON a.AddressId = u.AddressId WHERE u.UserId = :userId ORDER BY a.AddressId";
	public static final String SELECT_ADDRESS_BY_ADDRESS_ID = "SELECT AddressId, Address, City, StateId, Region, PostalCode FROM Address WHERE AddressId = :addressId ORDER BY AddressId";
	public static final String SELECT_ALL_ADDRESSES = "SELECT AddressId, Address, City, StateId, Region, PostalCode FROM Address ORDER BY AddressId";
	public static final String SELECT_USER_BY_USER_ID = "SELECT UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate FROM User WHERE UserId = :userId ORDER BY userId";
	public static final String SELECT_USER_BY_ACCOUNT_NAME = "SELECT UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate FROM User WHERE AccountName = :accountName ORDER BY UserId";
    public static final String SELECT_STATE_BY_NAME = "SELECT StateId, Name, Code FROM State WHERE Name = :name ORDER BY StateId";
}
