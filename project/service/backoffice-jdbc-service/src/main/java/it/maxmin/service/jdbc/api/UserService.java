package it.maxmin.service.jdbc.api;

import it.maxmin.model.jdbc.service.dto.UserDto;

public interface UserService {

	void createUser(UserDto user);
}
