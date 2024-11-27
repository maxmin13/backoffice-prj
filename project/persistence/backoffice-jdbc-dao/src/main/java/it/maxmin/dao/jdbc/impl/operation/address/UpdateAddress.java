package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.constant.MessageConstants.ADDRESS_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.MessageConstants.ADDRESS_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.MessageConstants.STATE_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.MessageConstants.STATE_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.UPDATE_ADDRESS;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import it.maxmin.model.jdbc.dao.entity.Address;

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
		notNull(address, ADDRESS_NOT_NULL_MSG);
		notNull(address.getId(), ADDRESS_ID_NOT_NULL_MSG);
		notNull(address.getState(), STATE_NOT_NULL_MSG);
		notNull(address.getState().getId(), STATE_ID_NOT_NULL_MSG);
		updateByNamedParam(Map.of("addressId", address.getId(), "description", address.getDescription(), "city",
				address.getCity(), "stateId", address.getState().getId(), "region", address.getRegion(), "postalCode",
				address.getPostalCode()));
	}
}
