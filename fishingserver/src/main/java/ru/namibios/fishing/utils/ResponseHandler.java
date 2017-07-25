package ru.namibios.fishing.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

public class ResponseHandler {
	
	public static void writeMapperJson(HttpServletResponse response, Object data){
		
		ObjectMapper mapper = new ObjectMapper();
		try{
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(mapper.writeValueAsString(data));
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}