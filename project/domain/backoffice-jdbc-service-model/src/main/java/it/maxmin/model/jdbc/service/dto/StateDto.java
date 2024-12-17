package it.maxmin.model.jdbc.service.dto;

import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_STATE_CODE_NOT_NULL_MSG;
import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_STATE_NAME_NOT_NULL_MSG;
import static it.maxmin.model.jdbc.service.constant.JdbcModelMessageConstants.ERROR_STATE_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.State;

public final class StateDto implements Serializable {

	private static final long serialVersionUID = -8992873510355824345L;

	private String name;
	private String code;

	public static StateDto newInstance(State state) {
		notNull(state, ERROR_STATE_NOT_NULL_MSG);
		return newInstance(state.getName(), state.getCode());
	}

	public static StateDto newInstance(String name, String code) {
		return new StateDto(name, code);
	}

	StateDto(String name, String code) {
		super();
		notNull(name, ERROR_STATE_NAME_NOT_NULL_MSG);
		notNull(code, ERROR_STATE_CODE_NOT_NULL_MSG);
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(code);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		StateDto that = (StateDto) obj;
		return code.equals(that.code);
	}

	@Override
	public String toString() {
		return "State [name=" + name + ", code=" + code + "]";
	}
}
