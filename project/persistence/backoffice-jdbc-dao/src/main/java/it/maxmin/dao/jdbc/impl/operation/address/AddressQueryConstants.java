package it.maxmin.dao.jdbc.impl.operation.address;

public enum AddressQueryConstants {
	;

	public static final String SELECT_ADDRESSES_BY_USER_ID = ""
			+ "SELECT a.Id, a.Description, a.City, a.Region, a.PostalCode, "
			+ "         s.Id AS StateId, s.Name, s.Code, " 
			+ "         ua.UserId "
			+ "FROM Address a "
			+ "INNER JOIN UserAddress ua ON a.Id = ua.AddressId " 
			+ "INNER JOIN State s ON a.StateId = s.Id " 
			+ "WHERE ua.UserId = :userId "
			+ "ORDER BY a.Id, ua.UserId";
	
	public static final String SELECT_ADDRESS_BY_USER_POSTAL_CODE = ""
			+ "SELECT a.Id, a.Description, a.City, a.Region, a.PostalCode, "
			+ "         s.Id AS StateId, s.Name, s.Code " 
			+ "FROM Address a "
			+ "INNER JOIN State s ON a.StateId = s.Id " 
			+ "WHERE a.PostalCode = :postalCode "
			+ "ORDER BY a.Id";	

	public static final String UPDATE_ADDRESS = "" 
			+ "UPDATE Address "
			+ "SET Description=:description, City=:city, StateId=:stateId, Region=:region, PostalCode=:postalCode "
			+ "WHERE Id=:addressId";

	public static final String INSERT_ADDRESS = "" 
			+ "INSERT INTO Address "
			+ "SET Description = :description, City = :city, StateId = :stateId, Region = :region, PostalCode = :postalCode";

}
