package ru.namibios.fishing;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ru.namibios.fishing.utils.JSON;

@Controller
public class UserController {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/user", method=RequestMethod.GET)
	public ModelAndView face() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("user");
		
		return mav;
	}
	
	@RequestMapping("/admin/useradd")
	public void addUser(HttpServletRequest request, HttpServletResponse response) {
		logger.info("add");
		
	}
	
	@RequestMapping("/admin/userupdate")
	public void updateUser(HttpServletRequest request, HttpServletResponse response) {
		logger.info("update");
		
		MapSqlParameterSource  param = new MapSqlParameterSource();
		param.addValue("enabled", "");
		param.addValue("date_valid", "");
		param.addValue("username", "");
		
		userService.update(param);
	}
	
	@RequestMapping("/admin/userdelete")
	public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
		logger.info("delete");
		
	}
	
	@RequestMapping("/admin/userread")
	public @ResponseBody String getUser(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		List<Map<String, Object>> list = userService.get();
		
		for (Map<String, Object> map : list) {
			for (Map.Entry<String, Object>  entry: map.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		}
		
		return JSON.getInstance().writeValueAsString(list);
	}
	
}