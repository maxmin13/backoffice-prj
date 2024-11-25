package it.maxmin.model.jdbc.service.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.maxmin.model.jdbc.dao.entity.State;

public final class StateDto implements Serializable {

	private static final long serialVersionUID = -8992873510355824345L;

	private String name;
	private String code;

	public static StateDto newInstance(String name, String code) {
		return new StateDto(name, code);
	}

	public static StateDto newInstance(State state) {
		return new StateDto(state.getName(), state.getCode());
	}

	StateDto(String name, String code) {
		super();
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
