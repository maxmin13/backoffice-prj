package it.maxmin.model.jdbc.dao.entity;

import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_VERSION_NOT_NULL_MSG;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.springframework.util.Assert.notNull;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

public abstract class AbstractEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 870969367705096128L;

	protected Long id;
	protected Integer version;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		notNull(id, ERROR_ID_NOT_NULL_MSG);
		this.id = id;
	}
	
	@Version
	@Column(name = "Version")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		notNull(version, ERROR_VERSION_NOT_NULL_MSG);
		this.version = version;
	}

}
