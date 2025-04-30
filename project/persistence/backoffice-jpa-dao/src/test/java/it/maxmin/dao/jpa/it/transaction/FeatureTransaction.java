package it.maxmin.dao.jpa.it.transaction;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FeatureTransaction {

	private String name;
	private String isolation;
	private String propagation;

	public static FeatureTransaction newInstance() {
		return new FeatureTransaction();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		assertNotNull(name);
		this.name = name;
	}

	public FeatureTransaction withName(String name) {
		assertNotNull(name);
		this.name = name;
		return this;
	}

	public String getIsolation() {
		return isolation;
	}

	public void setIsolation(String isolation) {
		assertNotNull(isolation);
		this.isolation = isolation;
	}

	public FeatureTransaction withIsolation(String isolation) {
		assertNotNull(isolation);
		this.isolation = isolation;
		return this;
	}

	public String getPropagation() {
		return propagation;
	}

	public void setPropagation(String propagation) {
		assertNotNull(propagation);
		this.propagation = propagation;
	}

	public FeatureTransaction withPropagation(String propagation) {
		assertNotNull(propagation);
		this.propagation = propagation;
		return this;
	}

}
