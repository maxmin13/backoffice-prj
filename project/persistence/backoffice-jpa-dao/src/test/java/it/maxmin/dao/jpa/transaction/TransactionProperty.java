package it.maxmin.dao.jpa.transaction;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionProperty {

	private Integer timeout;

	public Integer getTimeout() {
		return timeout;
	}

	@JsonProperty("database")
	public void setTimeout(Map<String, Object> database) {
		@SuppressWarnings("unchecked")
		Map<String, Object> transaction = (Map<String, Object>) database.get("transaction");
		this.timeout = (Integer) transaction.get("timeout");
	}

}
