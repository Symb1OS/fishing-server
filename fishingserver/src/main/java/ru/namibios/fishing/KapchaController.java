package ru.namibios.fishing;

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

import ru.namibios.fishing.model.ImageParser;
import ru.namibios.fishing.utils.Message;
import ru.namibios.fishing.utils.ResponseHandler;
import ru.namibios.fishing.utils.Status;

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
		String tmpMatrix = request.getParameter("MATRIX");
	
		logger.info("hash= " + hash);
		
		if(hash.isEmpty()){
			logger.info("Hash is empty");
			ResponseHandler.writeMapperJson(response, Status.KEY_IS_EMPTY);
			return;
		}

		int status = service.checkHash(hash);
		if(status != Status.AUTH_OK){ 
			logger.info("[" + hash + "] status - " + status);
			return;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		int[][] matrix = mapper.readValue(tmpMatrix, int[][].class);
		
		ImageParser imageParser = new ImageParser(matrix);
		imageParser.getCodes();
		String keys = imageParser.getkeyFromTemlate();
		
		logger.info(String.format(Message.MSG_RESP_KEYS, hash, keys));
		ResponseHandler.writeMapperJson(response, keys);
		
	}
	
}