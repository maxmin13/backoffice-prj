package it.maxmin.model.jdbc.service.dto;

import static it.maxmin.common.constant.MessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_FIRST_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_LAST_NAME_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.io.Serializable;

public final class CredentialsDto implements Serializable {

	private static final long serialVersionUID = -3956559320828966527L;

	private String accountName;
	private String firstName;
	private String lastName;

	public static CredentialsDto newInstance(String accountName, String firstName, String lastName) {
		return new CredentialsDto(accountName, firstName, lastName);
	}

	CredentialsDto(String accountName, String firstName, String lastName) {
		super();
		notNull(accountName, ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		notNull(firstName, ERROR_FIRST_NAME_NOT_NULL_MSG);
		notNull(lastName, ERROR_LAST_NAME_NOT_NULL_MSG);
		this.accountName = accountName;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
