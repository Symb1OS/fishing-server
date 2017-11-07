package ru.namibios.fishing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.namibios.fishing.utils.Encoder;
import ru.namibios.fishing.utils.Status;

public class Service {

	@Autowired
	private JdbcTemplate jdbc;
	
	private static final String SQL_SELECT_CHECK_AUTHORIZATION = "SELECT COUNT(*) FROM fishing.AUTHORITY WHERE HASH = ?";
	
	private static final String SQL_CURRENT_PASSWORD = "select COUNT(*) from fishing.users where username = ? and password = ?";
	
	private static final String SQL_CHECK_VALID_LICENCE = "select enabled from fishing.users where licence_key = ?"; 
	
	private static final String SQL_UPDATE_PASSWORD = "update fishing.users SET password = ? where username = ?";
	
	public int checkHash(String hash){
		int status = jdbc.queryForObject(SQL_SELECT_CHECK_AUTHORIZATION, Integer.class, hash); 
		return status == 1 ? Status.AUTH_OK : Status.AUTH_BAD; 
	}
	
	public int checkValid(String licenceKey) {
		int status = jdbc.queryForObject(SQL_CHECK_VALID_LICENCE, Integer.class, licenceKey);
		return status == 1 ? Status.AUTH_OK : Status.AUTH_BAD;
	}
	
	public boolean checkOldPassword(String username, String oldPassword) {
		String hash = Encoder.encode(oldPassword);
		int status = jdbc.queryForObject(SQL_CURRENT_PASSWORD, Integer.class , username, hash);
		return status == 1 ? true : false;
	}
	
	public int changePassword(String username, String password) {
		String hash = Encoder.encode(password);
		return jdbc.update(SQL_UPDATE_PASSWORD, hash, username);
	}
}