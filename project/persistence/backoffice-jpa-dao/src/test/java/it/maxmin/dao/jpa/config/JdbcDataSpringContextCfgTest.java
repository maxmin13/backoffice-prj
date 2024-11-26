package it.maxmin.dao.jpa.config;

import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.USE_SQL_COMMENTS;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.persistence.EntityManagerFactory;

/**
 * Verifies that by loading {@link JpaDataContextSpringCfg}, in the Spring context there is
 * a EntityManagerFactory.
 */
class JdbcDataSpringContextCfgTest {

	@Test
	void testEntityManagerFactory() throws IllegalStateException {

		var springCtx = new AnnotationConfigApplicationContext(JpaDataContextSpringCfg.class);
		var entityManagerFactory = springCtx.getBean("entityManagerFactory", EntityManagerFactory.class);

		assertNotNull(entityManagerFactory);

		Map<String, Object> properties = entityManagerFactory.getProperties();

		assertEquals("none", properties.get(HBM2DDL_AUTO));
		assertEquals(false, properties.get(FORMAT_SQL));
		assertEquals(false, properties.get(USE_SQL_COMMENTS));
		assertEquals(false, properties.get(HIGHLIGHT_SQL));
		assertEquals(false, properties.get(SHOW_SQL));

		springCtx.close();
	}

}
