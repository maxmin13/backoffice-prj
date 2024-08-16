package it.maxmin.dao.jdbc.impl.operation.address;

public enum AddressQueryConstants {
	;

	public static final String SELECT_ADDRESSES_BY_USER_ID = ""
			+ "SELECT a.Id, a.Description, a.City, a.StateId, a.Region, a.PostalCode, ua.UserId "
			+ "FROM Address a "
			+ "INNER JOIN UserAddress ua ON a.Id = ua.AddressId "
			+ "WHERE ua.UserId = :userId "
			+ "ORDER BY a.Id, ua.UserId";
	
	public static final String UPDATE_ADDRESS = ""
			+ "UPDATE Address "
			+ "SET Description=:description, City=:city, StateId=:stateId, Region=:region, PostalCode=:postalCode "
			+ "WHERE Id=:addressId";

}
