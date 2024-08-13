package it.maxmin.plain.dao.impl.operation.address;

public enum AddressQueryConstants {
	;

	public static final String SELECT_ADDRESSES_BY_USER_ID = ""
			+ "SELECT a.AddressId, a.Address, a.City, a.StateId, a.Region, a.PostalCode "
			+ "FROM Address a "
			+ "INNER JOIN UserAddress u ON a.AddressId = u.AddressId "
			+ "WHERE u.UserId = :userId "
			+ "ORDER BY a.addressId";
	
	public static final String UPDATE_ADDRESS = ""
			+ "UPDATE Address "
			+ "SET Address=:address, City=:city, StateId=:stateId, Region=:region, PostalCode=:postalCode "
			+ "WHERE AddressId=:addressId";

}
