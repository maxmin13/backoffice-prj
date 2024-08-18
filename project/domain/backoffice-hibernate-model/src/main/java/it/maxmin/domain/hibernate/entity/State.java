package it.maxmin.domain.hibernate.entity;

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

	@Column(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "Code")
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
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		State that = (State) obj;
		if (that.getId() != null && this.getId() != null) {
			return super.equals(obj);
		}
		return code.equals(that.code);
	}

	@Override
	public String toString() {
		return  "State [id=" + id + ", name=" + name + ", code=" + code + "]";
	}
	
}
