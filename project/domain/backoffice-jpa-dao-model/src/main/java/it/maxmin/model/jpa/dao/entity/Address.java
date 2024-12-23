package it.maxmin.model.jpa.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_CITY_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_DESCRIPTION_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_POSTAL_CODE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_REGION_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_STATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USERS_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "Address")
@NamedQuery(name = "Address.findById", query = """
		       select distinct a
		            from Address a
		            left join fetch a.users
		            inner join fetch a.state
		            where a.id = :id
		""")
public class Address implements Serializable {

	@Serial
	private static final long serialVersionUID = 7632536256395423354L;

	@SuppressWarnings("deprecation")
	@Id
	@GeneratedValue(generator = "AddressSeq")
	@GenericGenerator(name = "AddressSeq", 
		strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", 
		parameters = {
			@Parameter(name = "AddressSeq", value = "AddressSeq"), @Parameter(name = "initial_value", value = "100"),
			@Parameter(name = "increment_size", value = "1") })
	@Column(name = "Id")
	private Long id;

	@Version
	@Column(name = "Version")
	private Integer version;

	@NotNull
	@Size(min = 2, max = 120, message = "description is required, maximum 60 characters.")
	@Column(name = "Description")
	private String description;

	@NotNull
	@Size(min = 2, max = 100, message = "city is required, maximum 100 characters.")
	@Column(name = "City")
	private String city;

	@NotNull
	@Size(min = 2, max = 100, message = "region is required, maximum 100 characters.")
	@Column(name = "Region")
	private String region;

	@NotNull
	@Size(min = 2, max = 16, message = "postalCode is required, maximum 16 characters.")
	@Column(name = "PostalCode")
	private String postalCode;

	@NotNull
	@OneToOne
	@JoinColumn(name = "StateId", referencedColumnName = "Id")
	private State state;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "UserAddress", joinColumns = @JoinColumn(name = "AddressId"), inverseJoinColumns = @JoinColumn(name = "UserId"))
	private Set<User> users = new HashSet<>();

	public static Address newInstance() {
		return new Address();
	}

	public Long getId() {
		return id;
	}

	Address withId(Long id) {
		this.id = id;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		notNull(description, ERROR_DESCRIPTION_NOT_NULL_MSG);
		this.description = description;
	}

	public Address withDescription(String description) {
		notNull(description, ERROR_DESCRIPTION_NOT_NULL_MSG);
		this.description = description;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		notNull(city, ERROR_CITY_NOT_NULL_MSG);
		this.city = city;
	}

	public Address withCity(String city) {
		notNull(city, ERROR_CITY_NOT_NULL_MSG);
		this.city = city;
		return this;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		notNull(region, ERROR_REGION_NOT_NULL_MSG);
		this.region = region;
	}

	public Address withRegion(String region) {
		notNull(region, ERROR_REGION_NOT_NULL_MSG);
		this.region = region;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		notNull(postalCode, ERROR_POSTAL_CODE_NOT_NULL_MSG);
		this.postalCode = postalCode;
	}

	public Address withPostalCode(String postalCode) {
		notNull(postalCode, ERROR_POSTAL_CODE_NOT_NULL_MSG);
		this.postalCode = postalCode;
		return this;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		notNull(state, ERROR_STATE_NOT_NULL_MSG);
		this.state = state;
	}

	public Address withState(State state) {
		notNull(state, ERROR_STATE_NOT_NULL_MSG);
		this.state = state;
		return this;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		notNull(users, ERROR_USERS_NOT_NULL_MSG);
		this.users = users;
	}

	public Address withUsers(Set<User> users) {
		notNull(users, ERROR_USERS_NOT_NULL_MSG);
		this.users = users;
		return this;
	}

	public boolean addUser(User user) {
		if (user == null || users.contains(user)) {
			return false;
		}
		else {
			users.add(user);
			return true;
		}
	}

	public Optional<User> getUser(String accountName) {
		if (accountName == null) {
			return Optional.empty();
		}
		return users.stream().filter(each -> each.getAccountName().equals(accountName)).findFirst();
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
