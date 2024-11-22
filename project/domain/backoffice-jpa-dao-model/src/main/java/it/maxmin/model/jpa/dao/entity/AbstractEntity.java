package it.maxmin.model.jpa.dao.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 870969367705096128L;

	protected Long id;
	protected int version = 0;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Version
	@Column(name = "Version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
