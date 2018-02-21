package ru.namibios.fishing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import ru.namibios.fishing.model.Chars;
import ru.namibios.fishing.model.ImageParser;
import ru.namibios.fishing.utils.DateUtils;
import ru.namibios.fishing.utils.JSON;
import ru.namibios.fishing.utils.Message;
import ru.namibios.fishing.utils.PythonExec;
import ru.namibios.fishing.utils.ResponseHandler;
import ru.namibios.fishing.utils.Status;

@Controller
public class KapchaController {
	
	private static final String UPLOAD_DIR = "/home/symbios/fishingserver/resources/";
	private static final String UPLOAD_KAPCHA_DIR = UPLOAD_DIR + "kapcha/";

	private static final Logger logger = LoggerFactory.getLogger(KapchaController.class);

	@Autowired
	private Service service;
	
	@RequestMapping(value="/authorized")
	public @ResponseBody Map<String, Object> auth(HttpServletRequest request, HttpServletResponse response){
		
		String hash = request.getParameter("HASH");
		logger.info(hash);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if (hash.isEmpty()){
			map.put("status", false);
			map.put("message", "Ключ пустой");
			return map;
		} else{
			Map<String, Object> status = service.checkValid(hash);
			long exist = (long) status.get("exist");
			long dateValid = (long) status.get("date_valid");
			if (exist == 1) {
				if (dateValid == 1) {
					map.put("status", true);
				}else {
					map.put("status", false);
					map.put("message", "Срок действия ключа истек");
				}
			} else {
				map.put("status", false);
				map.put("message", "Невалидный ключ");
			}
			return map;
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
		
		int[][] matrix = JSON.getInstance().readValue(tmpMatrix, int[][].class);
		
		ImageParser imageParser = new ImageParser(matrix, Chars.values());
		imageParser.parse();
		String keys = imageParser.getNumber();
		
		logger.info(String.format(Message.MSG_RESP_KEYS, hash, keys));
		ResponseHandler.writeMapperJson(response, keys);
	}
	
	@RequestMapping(value="/byte_kapcha")
	public void byteKapcha(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("HASH") String hash,
			@RequestParam("SCREEN") MultipartFile multipartFile) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException{
		
		InputStream in = new ByteArrayInputStream(multipartFile.getBytes());
		BufferedImage image = ImageIO.read(in);
		
		String folderday = DateUtils.yyyymmdd();
		
		File dir = new File(UPLOAD_KAPCHA_DIR + folderday);
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		String filename = UPLOAD_KAPCHA_DIR + folderday + "/" + String.valueOf(new Date().getTime() + ".jpg");
		File file = new File(filename);
		ImageIO.write(image, "jpg", file);
		
		String key = PythonExec.exec(filename);
		key = key.replaceAll("0", "w").replaceAll("1", "s").replaceAll("2", "a").replaceAll("3", "d").replaceAll("4", "");
		
		logger.info(String.format(Message.MSG_RESP_KEYS, hash, key));
		ResponseHandler.writeMapperJson(response, key);
	}
	
	@RequestMapping(value="/test")
	public void test(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
		
		String hash = request.getParameter("HASH");
		String tmpMatrix = request.getParameter("MATRIX");
	
		int[][] matrix = JSON.getInstance().readValue(tmpMatrix, int[][].class);
		
		ImageParser imageParser = new ImageParser(matrix, Chars.values());
		imageParser.parse();
		String keys = imageParser.getNumber();
		
		logger.info(String.format(Message.MSG_RESP_KEYS, hash, keys));
		ResponseHandler.writeMapperJson(response, keys);
		
	}
	
	@RequestMapping(value="/upload")
	public void uploadImage(@RequestParam("HASH") String hash,
							@RequestParam("SCREEN") MultipartFile multipartFile) throws JsonParseException, JsonMappingException, IOException {
		
		logger.info("Start upload file for [" + hash + "]");
		
	    int status = service.checkHash(hash);
		if(status != Status.AUTH_OK) {
			logger.info("[" + hash + "] - Authentification bad");
			return;
		}
		
		InputStream in = new ByteArrayInputStream(multipartFile.getBytes());
		BufferedImage image = ImageIO.read(in);
		
		File file = new File(UPLOAD_DIR + hash + ".jpg");
		ImageIO.write(image, "jpg", file);
	}
	
}