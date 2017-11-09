package ru.namibios.fishing.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SettingsController {
	
	private static final Logger logger = Logger.getLogger(SettingsController.class);
	
	@Autowired
	private Service service;

	@RequestMapping("/settings")
	public ModelAndView settings() {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("settings");
		
		return mav;
		
	}
	
	@RequestMapping(value = "/loadHash", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> loadHash(){
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Map<String, Object> map = new HashMap<>();
		
		Map<String, Object> settings = service.loadHash(username);
		String hash = (String) settings.get("licence_key");
		
		
		if ( hash == null || hash.isEmpty() ) {
			map.put("licence_key", "Ключ отсутствует");
			map.put("date_valid", "1970-01-01");
			return map;
		}
				
		return settings;
	}
	
	@RequestMapping("/changePassword")
	public @ResponseBody Map<String, Object> changePassword(HttpServletRequest request, HttpServletResponse response){

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String oldPassword = request.getParameter("oldPassword");
		String password = request.getParameter("password");
		
		HashMap<String, Object> map = new HashMap<>();
		if(password.isEmpty() || password.length() < 8) {
			logger.info(String.format("[%s] Invalid password - ", username, password));
			map.put("success", false);
			map.put("message", "Невалидный пароль");
			return map;
		}
		
		boolean isOldPasValid = service.checkOldPassword(username, oldPassword);
		if(isOldPasValid) {
			logger.info("Old password is valid");
			int count = service.changePassword(username, password);
			logger.info(String.format("Updated %d rows", count));
			map.put("success", true);
			map.put("message", "Пароль успешно изменен");
			
		}else {
			map.put("success", false);
			map.put("message", "Неверный пароль");
		}
		
		return map;
	}
}