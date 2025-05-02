package it.maxmin.dao.jpa.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class JpaCacheManager {

	private LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@Autowired
	public JpaCacheManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public void flush() {
//		((EntityManagerFactory) entityManagerFactory).unwrap(SessionFactory.class).getCurrentSession().flush();
		// entityManagerFactory.getNativeEntityManagerFactory().unwrap(Session.class).flush();
	}
}
