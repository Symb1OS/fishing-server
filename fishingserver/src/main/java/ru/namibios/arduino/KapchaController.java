package ru.namibios.arduino;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.namibios.arduino.model.ImageParser;
import ru.namibios.arduino.utils.Status;
import ru.namibios.arduino.utils.Message;
import ru.namibios.arduino.utils.ResponseHandler;

@Controller
public class KapchaController {
	
	private static final Logger logger = LoggerFactory.getLogger(KapchaController.class);

	@Autowired
	private Service service;
	
	@RequestMapping(value="/authorized")
	public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String hash = request.getParameter("HASH");
		logger.info(hash);

		if(hash.isEmpty()){
			ResponseHandler.writeMapperJson(response, Status.KEY_IS_EMPTY);
		}else{
			int status = service.checkHash(hash);
			if(status == Status.AUTH_OK){
				logger.info(String.format(Message.MSG_AUTH_SUCCESS, hash));
			}else{
				logger.info(String.format(Message.MSG_AUTH_FAILURE, hash));
			}
			
			ResponseHandler.writeMapperJson(response, status);
		}
	}
	
	@RequestMapping(value="/kapcha")
	public void kapcha(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
		
		String hash = request.getParameter("HASH");
		String matrix = request.getParameter("MATRIX");
	
		logger.info("hash= " + hash);
		
		if(hash.isEmpty()){
			logger.info("Hash is empty");
			ResponseHandler.writeMapperJson(response, Status.KEY_IS_EMPTY);
			return;
		}

		int status = Status.AUTH_OK; //service.checkHash(hash);
		if(status != Status.AUTH_OK){ 
			logger.info("[" + hash + "] status - " + status);
			return;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		int[][] tmp = mapper.readValue(matrix, int[][].class);
		
		ImageParser imageParser = new ImageParser(tmp);
		imageParser.getCodes();
		String keys = imageParser.getkeyFromTemlate();
		
		logger.info(String.format(Message.MSG_RESP_KEYS, hash, keys));
		ResponseHandler.writeMapperJson(response, keys);
		
	}
	
}