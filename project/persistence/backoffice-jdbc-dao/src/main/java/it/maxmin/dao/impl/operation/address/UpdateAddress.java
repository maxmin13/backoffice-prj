package it.maxmin.dao.impl.operation.address;

import static it.maxmin.dao.impl.operation.address.AddressQueryConstants.UPDATE_ADDRESS;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import it.maxmin.model.plain.pojos.Address;

public class UpdateAddress extends SqlUpdate {

	public UpdateAddress(DataSource dataSource) {
		super(dataSource, UPDATE_ADDRESS);
		super.declareParameter(new SqlParameter("address", Types.VARCHAR));
		super.declareParameter(new SqlParameter("city", Types.VARCHAR));
		super.declareParameter(new SqlParameter("stateId", Types.INTEGER));
		super.declareParameter(new SqlParameter("region", Types.VARCHAR));
		super.declareParameter(new SqlParameter("postalCode", Types.VARCHAR));
		super.declareParameter(new SqlParameter("addressId", Types.INTEGER));
	}

	public void execute(Address address) {
		notNull(address, "The address must not be null");
		updateByNamedParam(Map.of("addressId", address.getAddressId(), "address", address.getAddress(), "city",
				address.getCity(), "stateId", address.getStateId(), "region", address.getRegion(), "postalCode",
				address.getPostalCode()));
	}
}
