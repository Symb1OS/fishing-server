package ru.namibios.fishing;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private static final String SQL_GET_USER = "select * from fishing.users";
	
	private static final String SQL_UPDATE_USER = "update fishing.users set enabled = :enabled, date_valid = :date_valid where username = :username";
	
	private static final String SQL_ADD_USER = "insert into fishing.users values(:username, :password, :enabled, :licence_key, :date_create , :date_valid)";
	
	private static final String SQL_DELETE_USER = "delete from fishing.users where username = :username";

	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbc;
	
	public List<Map<String, Object>> get(){
		return jdbc.queryForList(SQL_GET_USER);
	}
	
	public void add(MapSqlParameterSource param) {
		namedJdbc.update(SQL_ADD_USER, param);
	}
	
	public void update(MapSqlParameterSource param) {
		namedJdbc.update(SQL_UPDATE_USER, param);
	}
	
	public void delete(MapSqlParameterSource param) {
		jdbc.update(SQL_DELETE_USER);
	}
	
}