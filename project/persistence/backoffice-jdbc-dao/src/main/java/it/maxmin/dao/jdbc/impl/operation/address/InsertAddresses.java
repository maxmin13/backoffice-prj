package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESSES_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_STATE_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.INSERT_ADDRESS;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import it.maxmin.model.jdbc.dao.entity.Address;

public class InsertAddresses extends BatchSqlUpdate {

	private static final int BATCH_SIZE = 10;

	public InsertAddresses(DataSource dataSource) {
		super(dataSource, INSERT_ADDRESS);
		super.declareParameter(new SqlParameter("description", Types.VARCHAR));
		super.declareParameter(new SqlParameter("city", Types.VARCHAR));
		super.declareParameter(new SqlParameter("stateId", Types.INTEGER));
		super.declareParameter(new SqlParameter("region", Types.VARCHAR));
		super.declareParameter(new SqlParameter("postalCode", Types.VARCHAR));
		setBatchSize(BATCH_SIZE);
	}

	/**
	 * @return the number of rows affected by the update
	 */
	public Integer execute(List<Address> addresses) {
		notNull(addresses, ERROR_ADDRESSES_NOT_NULL_MSG);
		Integer rows = 0;
		for (Address address : addresses) {
			notNull(address.getState(), ERROR_STATE_NOT_NULL_MSG);
			notNull(address.getState().getId(), ERROR_ID_NOT_NULL_MSG);
			rows = rows + updateByNamedParam(Map.of("description", address.getDescription(), "city", address.getCity(),
					"stateId", address.getState().getId(), "region", address.getRegion(), "postalCode",
					address.getPostalCode()));
		}

		flush();
		return rows;
	}
}
