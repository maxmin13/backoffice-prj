package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.UPDATE_ADDRESS;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import it.maxmin.model.jdbc.domain.entity.Address;

public class UpdateAddress extends SqlUpdate {

	public UpdateAddress(DataSource dataSource) {
		super(dataSource, UPDATE_ADDRESS);
		super.declareParameter(new SqlParameter("description", Types.VARCHAR));
		super.declareParameter(new SqlParameter("city", Types.VARCHAR));
		super.declareParameter(new SqlParameter("stateId", Types.INTEGER));
		super.declareParameter(new SqlParameter("region", Types.VARCHAR));
		super.declareParameter(new SqlParameter("postalCode", Types.VARCHAR));
		super.declareParameter(new SqlParameter("addressId", Types.INTEGER));
	}

	public void execute(Address address) {
		notNull(address, "The address must not be null");
		notNull(address.getId(), "The address ID must not be null");
		notNull(address.getState(), "The state must not be null");
		notNull(address.getState().getId(), "The state ID must not be null");
		updateByNamedParam(Map.of("addressId", address.getId(), "description", address.getDescription(), "city",
				address.getCity(), "stateId", address.getState().getId(), "region", address.getRegion(), "postalCode",
				address.getPostalCode()));
	}
}
