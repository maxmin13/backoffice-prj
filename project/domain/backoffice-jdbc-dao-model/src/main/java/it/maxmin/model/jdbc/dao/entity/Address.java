package it.maxmin.model.jdbc.dao.entity;
import static it.maxmin.common.constant.MessageConstants.ERROR_CITY_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_DESCRIPTION_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_POSTAL_CODE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_REGION_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_STATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USERS_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_VERSION_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
		notNull(id, ERROR_ID_NOT_NULL_MSG);
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
	
	public Address withVersion(Integer version) {
		notNull(version, ERROR_VERSION_NOT_NULL_MSG);
		this.version = version;
		return this;
	}

	public Set<User> getUsers() {
		return users;
	}

	void setUsers(Set<User> users) {
		notNull(users, ERROR_USERS_NOT_NULL_MSG);
		this.users = users;
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
		return "Address [id=" + id + ", description=" + description + ", city=" + city + ", region=" + region
				+ ", postalCode=" + postalCode + ", version=" + version + ", state=" + state + ", users=" + users + "]";
	}

}
