package it.maxmin.domain.hibernate.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

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
	protected int version;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public AbstractEntity withId(Long id) {
		this.id = id;
		return this;
	}

	@Version
	@Column(name = "Version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		AbstractEntity that = (AbstractEntity) obj;
		return Objects.equals(id, that.id);
	}

}
