package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_STATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_VERSION_NOT_NULL_MSG;
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
		super.declareParameter(new SqlParameter("version", Types.INTEGER));
	}

	/**
	 * @return the number of rows affected by the update
	 */
	public Integer execute(Address address) {
		notNull(address, ERROR_ADDRESS_NOT_NULL_MSG);
		notNull(address.getId(), ERROR_ID_NOT_NULL_MSG);
		notNull(address.getState(), ERROR_STATE_NOT_NULL_MSG);
		notNull(address.getState().getId(), ERROR_ID_NOT_NULL_MSG);
		notNull(address.getVersion(), ERROR_VERSION_NOT_NULL_MSG);
		return updateByNamedParam(Map.of("addressId", address.getId(), "description", address.getDescription(), "city",
				address.getCity(), "stateId", address.getState().getId(), "region", address.getRegion(), "postalCode",
				address.getPostalCode(), "version", address.getVersion()));
	}
}
