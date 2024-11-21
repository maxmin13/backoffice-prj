package it.maxmin.dao.jdbc.impl.operation.address;

public enum AddressQueryConstants {
	;

	public static final String SELECT_ADDRESSES_BY_USER_ID = ""
			+ "SELECT a.Id AS AddressId, a.Description, a.City, a.Region, a.PostalCode, "
			+ "         s.Id AS StateId, s.Name AS StateName, s.Code, " 
			+ "         u.Id AS UserId, u.AccountName, u.FirstName, u.LastName, u.BirthDate, u.CreatedAt, "
			+ "         d.Id AS DepartmentId, d.Name AS DepartmentName, "
			+ "         r.Id AS RoleId, r.RoleName "
			+ "FROM Address a "
			+ "INNER JOIN State s ON a.StateId = s.Id " 
			+ "INNER JOIN UserAddress ua ON a.Id = ua.AddressId " 
			+ "INNER JOIN User u ON ua.UserId = u.Id "
			+ "INNER JOIN Department d ON u.DepartmentId = d.Id "
			+ "LEFT JOIN UserRole ur ON u.Id = ur.UserId "
			+ "LEFT JOIN Role r ON ur.RoleId = r.Id "			
			+ "WHERE u.Id = :userId "
			+ "ORDER BY AddressId, UserId";
	
	public static final String SELECT_ADDRESS_BY_USER_POSTAL_CODE = ""
			+ "SELECT a.Id AS AddressId, a.Description, a.City, a.Region, a.PostalCode, "
			+ "         s.Id AS StateId, s.Name AS StateName, s.Code " 
			+ "FROM Address a "
			+ "INNER JOIN State s ON a.StateId = s.Id " 
			+ "WHERE a.PostalCode = :postalCode "
			+ "ORDER BY AddressId";	

	public static final String UPDATE_ADDRESS = "" 
			+ "UPDATE Address "
			+ "SET Description=:description, City=:city, StateId=:stateId, Region=:region, PostalCode=:postalCode "
			+ "WHERE Id=:addressId";

	public static final String INSERT_ADDRESS = "" 
			+ "INSERT INTO Address "
			+ "SET Description = :description, City = :city, StateId = :stateId, Region = :region, PostalCode = :postalCode";

}
