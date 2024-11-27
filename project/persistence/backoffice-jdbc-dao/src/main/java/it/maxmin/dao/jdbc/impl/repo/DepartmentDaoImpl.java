package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.constant.MessageConstants.DEPARTMENT_NAME_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jdbc.api.repo.DepartmentDao;
import it.maxmin.dao.jdbc.impl.operation.department.SelectDepartmentByDepartmentName;
import it.maxmin.model.jdbc.dao.entity.Department;

@Transactional
@Repository("departmentDao")
public class DepartmentDaoImpl implements DepartmentDao {

	private SelectDepartmentByDepartmentName selectDepartmentByDepartmentName;

	public DepartmentDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		super();
		this.selectDepartmentByDepartmentName = new SelectDepartmentByDepartmentName(jdbcTemplate);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Department> selectByDepartmentName(String departmentName) {
		notNull(departmentName, DEPARTMENT_NAME_NOT_NULL_MSG);
		Department department = this.selectDepartmentByDepartmentName.execute(departmentName);
		return department != null ? Optional.of(department) : Optional.empty();
	}

}
