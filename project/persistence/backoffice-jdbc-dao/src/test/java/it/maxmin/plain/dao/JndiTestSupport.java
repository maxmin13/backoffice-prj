package it.maxmin.plain.dao;

import javax.naming.InitialContext;

import org.junit.jupiter.api.BeforeAll;

public abstract class JndiTestSupport {
	
	@BeforeAll
	public static void setup() throws Exception {
		// creates a (test) directory service by:
		// loading jndi.properties file the test/resource/jndi.properties and creating a
		// SimpleContextFactory JNDI context
		// populated with the objects (a data source) described in the properties files in
		// test/simple-jndi folder
		new InitialContext();
	}
	
}
