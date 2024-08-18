package it.maxmin.domain.hibernate.entity;

import java.io.Serial;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Address")
public class Address extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String description;
	private String city;
	private State state;
	private String region;
	private String postalCode;

	public static Address newInstance() {
		return new Address();
	}

	public Address withId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Address withDescription(String description) {
		this.description = description;
		return this;
	}

	@Column(name = "City")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Address withCity(String city) {
		this.city = city;
		return this;
	}

//	@Column(name = "StateId")
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Address withState(State state) {
		this.state = state;
		return this;
	}

	@Column(name = "Region")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Address withRegion(String region) {
		this.region = region;
		return this;
	}

	@Column(name = "PostalCode", unique = true, updatable = false)
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Address withPostalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(postalCode);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Address)) {
			return false;
		}
		Address that = (Address) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(postalCode, that.postalCode);
		return eb.isEquals();
	}
}
