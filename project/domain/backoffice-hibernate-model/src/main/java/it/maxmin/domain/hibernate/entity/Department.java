package it.maxmin.domain.hibernate.entity;

import java.io.Serial;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Department")
public class Department extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String name;
	
	public static Department newInstance() {
		return new Department();
	}
	
	
	public Department withId(Long id) {
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

	public Department withName(String name) {
		this.name = name;
		return this;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(name);
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
		Department that = (Department) obj;
		return name.equals(that.name);
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
}
