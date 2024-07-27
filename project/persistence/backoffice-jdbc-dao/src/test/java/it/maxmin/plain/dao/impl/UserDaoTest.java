package it.maxmin.plain.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.maxmin.model.plain.pojos.User;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

	private static Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	@Mock
	private DataSource dataSource;

	@Mock
	private Connection connection;

	@Mock
	private PreparedStatement statement;

    @Mock
	private ResultSet rs;

	private UserDaoImpl userDao;

	@BeforeEach
	public void setup() throws Exception {
		assertNotNull(dataSource);
		userDao = new UserDaoImpl();
		userDao.setDataSource(dataSource);
	}

	@Test
	public void nullCreateThrowsException() {

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(null);
		});
		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	public void testCreate() throws SQLException {
		
		when(dataSource.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
		when(statement.getGeneratedKeys()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getLong(1)).thenReturn(2l);
		
		User user = new User();
		user.setAccountName("maxmin");
		user.setBirthDate(LocalDate.of(1988, 1, 13));
		user.setCreateDate(LocalDateTime.of(2024, 1, 1, 1, 1));
		user.setFirstName("MaxMin");
		user.setLastName("Min");

		User created = userDao.create(user);
		
		assertEquals(2l, created.getUserId());
		assertEquals("maxmin", created.getAccountName());
		assertEquals(LocalDate.of(1988, 1, 13), created.getBirthDate());
		assertEquals(LocalDateTime.of(2024, 1, 1, 1, 1), created.getCreateDate());
		assertEquals("MaxMin", created.getFirstName());
		assertEquals("Min", created.getLastName());
	}

}
