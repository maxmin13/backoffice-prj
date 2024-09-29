package it.maxmin.domain.hibernate.entity;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Address")
@NamedQuery(name = "Address.findById", query = """
		       select distinct a 
		            from Address a
		            left join fetch a.users
		            inner join fetch a.state
		            where a.id = :id
		""")
public class Address extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	private String description;
	private String city;
	private String region;
	private String postalCode;
	private State state;
	private Set<User> users = new HashSet<>();

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

	@Column(name = "PostalCode")
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

	@OneToOne
	@JoinColumn(name = "StateId")
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "UserAddress", joinColumns = @JoinColumn(name = "AddressId"), inverseJoinColumns = @JoinColumn(name = "UserId"))
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Address withUsers(Set<User> users) {
		this.users = users;
		return this;
	}

	// TODO test it in the model
	public boolean addUser(User user) {
		if (user == null || users.contains(user)) {
			return false;
		} else {
			users.add(user);
			return true;
		}
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
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Address that = (Address) obj;
		return postalCode.equals(that.postalCode);
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", description=" + description + ", city=" + city + ", state=" + state
				+ ", region=" + region + ", postalCode=" + postalCode + "]";
	}
}
