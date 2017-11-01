package ru.namibios.fishing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import org.springframework.web.multipart.MultipartFile;

import ru.namibios.fishing.model.Chars;
import ru.namibios.fishing.model.ImageParser;
import ru.namibios.fishing.utils.ImageUtils;
import ru.namibios.fishing.utils.JSON;
import ru.namibios.fishing.utils.Message;
import ru.namibios.fishing.utils.ResponseHandler;
import ru.namibios.fishing.utils.Status;

@Controller
public class KapchaController {
	
	private static final String UPLOAD_DIR = "fishingserver/resources/";

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
		
		int[][] matrix = JSON.getInstance().readValue(tmpMatrix, int[][].class);
		
		ImageParser imageParser = new ImageParser(matrix, Chars.values());
		imageParser.parse();
		String keys = imageParser.getNumber();
		
		logger.info(String.format(Message.MSG_RESP_KEYS, hash, keys));
		ResponseHandler.writeMapperJson(response, keys);
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
	
	@RequestMapping(value="upload")
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
	
	@RequestMapping(value="/status")
	public void status(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String hash = request.getParameter("HASH");
	
	    int status = service.checkHash(hash);
		if(status != Status.AUTH_OK) {
			logger.info("[" + hash + "] - Authentification bad");
			return;
		}
				
		String path = "";
		String name = "";
		File files = new File(UPLOAD_DIR);
		for(File file : files.listFiles()){
			if(file.isFile()) {
				name = file.getName();
				if(name.startsWith(hash)) 
					path = file.getAbsolutePath();
			}
		}
		
		if(path.isEmpty()) return;
		
		BufferedImage image = ImageUtils.read(new File(path));
		byte[] bytes = ImageUtils.imageToBytes(image);
		
		response.setContentLength(bytes.length);
		response.setContentType("image/jpeg");
		response.setHeader("Content-Disposition", "attachment; filename=" + name);
		
		OutputStream outputStream = response.getOutputStream();
		
		outputStream.write(bytes, 0, bytes.length);
		outputStream.flush();
		outputStream.close();
		
	}
	
}