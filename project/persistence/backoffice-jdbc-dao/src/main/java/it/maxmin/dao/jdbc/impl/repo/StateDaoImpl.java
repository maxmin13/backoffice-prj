package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.constant.MessageConstants.STATE_NAME_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jdbc.api.repo.StateDao;
import it.maxmin.dao.jdbc.impl.operation.state.SelectStateByStateName;
import it.maxmin.model.jdbc.dao.entity.State;

@Transactional
@Repository("stateDao")
public class StateDaoImpl implements StateDao {

	private SelectStateByStateName selectStateByStateName;

	public StateDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		super();
		this.selectStateByStateName = new SelectStateByStateName(jdbcTemplate);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<State> selectByStateName(String stateName) {
		notNull(stateName, STATE_NAME_NOT_NULL_MSG);
		State state = this.selectStateByStateName.execute(stateName);
		return state != null ? Optional.of(state) : Optional.empty();
	}

}
