package it.maxmin.dao.jdbc.api.repo;

import java.util.Optional;

import it.maxmin.model.jdbc.dao.entity.Department;

public interface DepartmentDao {

	Optional<Department> selectByDepartmentName(String departmentName);
}
