package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_STATE_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.INSERT_ADDRESS;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import it.maxmin.model.jdbc.dao.entity.Address;

public class InsertAddress extends SqlUpdate {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InsertAddress.class);

	public InsertAddress(DataSource dataSource) {
		super(dataSource, INSERT_ADDRESS);
		super.declareParameter(new SqlParameter("description", Types.VARCHAR));
		super.declareParameter(new SqlParameter("city", Types.VARCHAR));
		super.declareParameter(new SqlParameter("stateId", Types.INTEGER));
		super.declareParameter(new SqlParameter("region", Types.VARCHAR));
		super.declareParameter(new SqlParameter("postalCode", Types.VARCHAR));
		super.setGeneratedKeysColumnNames("id");
		super.setReturnGeneratedKeys(true);
	}

	public Address execute(Address address) {
		notNull(address, ERROR_ADDRESS_NOT_NULL_MSG);
		notNull(address.getState(), ERROR_STATE_NOT_NULL_MSG);
		notNull(address.getState().getId(), ERROR_ID_NOT_NULL_MSG);

		var keyHolder = new GeneratedKeyHolder();
		Integer rows = updateByNamedParam(Map.of("description", address.getDescription(), "city",
				address.getCity(), "stateId", address.getState().getId(), "region", address.getRegion(), "postalCode",
				address.getPostalCode()), keyHolder);

		var addressId = Objects.requireNonNull(keyHolder.getKey()).longValue();
		address.setId(addressId);

		return address;
	}

}
