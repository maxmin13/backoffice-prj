package it.maxmin.dao.jpa.impl.repo;

import static it.maxmin.common.constant.MessageConstants.ERROR_FIELD_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.common.service.impl.MessageServiceImpl;
import it.maxmin.dao.jpa.api.repo.DepartmentDao;
import it.maxmin.model.jpa.dao.entity.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Transactional
@Repository("departmentDao")
public class DepartmentDaoImpl implements DepartmentDao {

	@PersistenceContext
	private EntityManager em;
	private MessageServiceImpl messageService;

	@Autowired
	public DepartmentDaoImpl(MessageServiceImpl messageService) {
		this.messageService = messageService;
	}
	
	@Override
	public Optional<Department> find(Long id) {
		return Optional.ofNullable(em.find(Department.class, id));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Department> findByDepartmentName(String departmentName) {
		notNull(departmentName, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "department name"));
		try {
			return Optional.of(em.createNamedQuery("Department.findByDepartmentName", Department.class)
					.setParameter("departmentName", departmentName).getSingleResult());
		}
		catch (NoResultException e) {
			return Optional.empty();
		}
	}

}
