package it.maxmin.domain.jpa.entity;

import java.io.Serial;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "State")
public class State extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String name;
	private String code;

	public static State newInstance() {
		return new State();
	}

	public State withId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State withName(String name) {
		this.name = name;
		return this;
	}

	@Column(name = "Code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public State withCode(String code) {
		this.code = code;
		return this;
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
		State that = (State) obj;
		return code.equals(that.code);
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + ", code=" + code + "]";
	}
}
