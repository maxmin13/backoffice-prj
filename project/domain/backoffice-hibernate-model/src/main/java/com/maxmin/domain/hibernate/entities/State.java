package com.maxmin.domain.hibernate.entities;

import java.io.Serial;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;

public class State extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@Column(unique = true, updatable = false)
	private String name;
	
	@Column(unique = true, updatable = false)
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
		if (!(obj instanceof State)) {
			return false;
		}
		State that = (State) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(code, that.code);
		return eb.isEquals();
	}
}
