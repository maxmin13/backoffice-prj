package it.maxmin.dao.jdbc.api.repo;

import java.util.Optional;

import it.maxmin.model.jdbc.dao.entity.State;

public interface StateDao {

	Optional<State> selectByStateName(String stateName);
}
