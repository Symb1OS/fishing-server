package ru.namibios.fishing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.namibios.fishing.utils.Status;

public class Service {

	@Autowired
	private JdbcTemplate jdbc;
	
	private static final String SELECT_CHECK_AUTHORIZATION = "SELECT COUNT(*) FROM fishing.AUTHORITY WHERE HASH = ?";
	
	public int checkHash(String hash){
		int status = jdbc.queryForObject(SELECT_CHECK_AUTHORIZATION, Integer.class, hash); 
		return status == 1 ? Status.AUTH_OK : Status.AUTH_BAD; 
	}

}
