package it.maxmin.dao.jpa.api.repo;

import java.util.Optional;

import it.maxmin.model.jpa.dao.entity.Department;

public interface DepartmentDao {
	
	Optional<Department> find(Long id);

	Optional<Department> findByDepartmentName(String departmentName);
}
