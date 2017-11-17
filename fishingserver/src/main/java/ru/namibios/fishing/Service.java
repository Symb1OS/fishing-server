package ru.namibios.fishing;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.namibios.fishing.utils.Status;

public class Service {

	@Autowired
	private NamedParameterJdbcTemplate njdbc;
	
	private static final String SQL_SELECT_CHECK_AUTHORIZATION = 
			"select " + 
			"	case when exists (select licence_key from fishing.users where licence_key = :licence_key) " +
			"	and date_valid > curdate() then 1 else 0 end as status " +
			"from fishing.users where licence_key = :licence_key ";
	
	private static final String SQL_SELECT_CHECK_VALID = 
			"select " + 
			"	(select case when (count(*) > 0) then 1 else 0 end  from fishing.users where licence_key = :licence_key) as exist, " + 
			"	coalesce((select case when date_valid > curdate() then 1 else 0 end from fishing.users where licence_key = :licence_key), 0) as date_valid " + 
			"from fishing.users limit 1";
	
	public int checkHash(String hash){
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("licence_key", hash);
		
		int status = njdbc.queryForObject(SQL_SELECT_CHECK_AUTHORIZATION, param, Integer.class); 
		return status == 1 ? Status.AUTH_OK : Status.AUTH_BAD; 
	}
	
	public Map<String, Object> checkValid(String hash) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("licence_key", hash);
		
		return njdbc.queryForMap(SQL_SELECT_CHECK_VALID, param);
		
	}
}