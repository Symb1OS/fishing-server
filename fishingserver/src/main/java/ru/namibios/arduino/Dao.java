package ru.namibios.arduino;

import org.springframework.jdbc.core.JdbcTemplate;

public class Dao {
	
	private JdbcTemplate jdbc;
	
	private static final String SELECT_CHECK_AUTHORIZATION = "SELECT COUNT FROM PROPERTIES.AUTHORITY WHERE HASH = ?";
	
	public boolean checkAuth(){
		return jdbc.queryForObject(SELECT_CHECK_AUTHORIZATION, Boolean.class);
	}

}
