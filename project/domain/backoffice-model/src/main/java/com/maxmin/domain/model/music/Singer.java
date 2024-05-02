package com.maxmin.domain.model.music;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

public class Singer implements Serializable {

	private static final long serialVersionUID = 4478785965653808821L;
	private Long id;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private Set<Album> albums;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Set<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(Set<Album> albums) {
		this.albums = albums;
	}

}
