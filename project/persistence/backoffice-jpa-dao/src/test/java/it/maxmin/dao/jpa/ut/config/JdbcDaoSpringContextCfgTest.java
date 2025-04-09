package it.maxmin.dao.jpa.ut.config;

import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.maxmin.dao.jpa.config.JpaDaoSpringContextCfg;
import jakarta.persistence.EntityManagerFactory;

/**
 * Verifies that by loading {@link JpaDaoSpringContextCfg}, in the Spring context there is
 * a EntityManagerFactory.
 */
class JdbcDaoSpringContextCfgTest {

	@Test
	void testEntityManagerFactory() throws IllegalStateException {

		var springCtx = new AnnotationConfigApplicationContext(JpaDaoSpringContextCfg.class);
		var entityManagerFactory = springCtx.getBean("entityManagerFactory", EntityManagerFactory.class);

		assertNotNull(entityManagerFactory);

		Map<String, Object> properties = entityManagerFactory.getProperties();

		assertEquals("none", properties.get(HBM2DDL_AUTO));
		assertEquals(true, properties.get("hibernate.jpa.compliance.proxy"));
		assertEquals(true, properties.get("hibernate.jpa.compliance.closed"));
		assertEquals(false, properties.get("hibernate.jpa.compliance.query"));
		assertEquals(true, properties.get("hibernate.jpa.compliance.transaction"));
		
		springCtx.close();
	}

}
