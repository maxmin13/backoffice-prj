package it.maxmin.plain.dao.impl.operation.address;

public enum AddressQueryConstants {
    ;

    public static final String UPDATE_ADDRESS = "update Address set Address=:address, City=:city, StateId=:stateId, Region=:region, PostalCode=:postalCode where AddressId=:addressId";
    public static final String SELECT_ADDRESSES_BY_USER_ID = "select a.AddressId, a.Address, a.City, a.StateId, a.Region, a.PostalCode from Address a left join UserAddress u on a.AddressId = u.AddressId where u.UserId = :userId";

}
