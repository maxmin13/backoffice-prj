package it.maxmin.model.jdbc.service.dto;

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
