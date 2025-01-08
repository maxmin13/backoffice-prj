package it.maxmin.dao.jpa.api.repo;

import java.util.Optional;

import it.maxmin.model.jpa.dao.entity.Department;

public interface DepartmentDao {

	Optional<Department> selectByDepartmentName(String departmentName);
}
