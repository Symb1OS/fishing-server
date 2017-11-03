package ru.namibios.fishing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ru.namibios.fishing.utils.Const;
import ru.namibios.fishing.utils.ImageUtils;
import ru.namibios.fishing.utils.Status;

@Controller
public class MonitoringController {
	
	private static final Logger logger = Logger.getLogger(MonitoringController.class);
	
	@Autowired
	private Service service;
	
	@RequestMapping("app")
	public ModelAndView app() {
	
		ModelAndView mav = new ModelAndView();
		mav.setViewName("app");
		
		return mav;
	}
	
	
	@RequestMapping("/status")
	public ModelAndView status(HttpServletRequest request, HttpServletResponse response) {
		
		String hash = request.getParameter("HASH");
		hash = "bef1c08eedddbe9f9d83a0f07d0d26ce9b360a55";
	  /*  int status = service.checkHash(hash);
		if(status != Status.AUTH_OK) {
			logger.info("[" + hash + "] - Authentification bad");
			return null;
		}*/
				
		String path = "";
		String name = "";
		File files = new File(Const.UPLOAD_DIR);
		for(File file : files.listFiles()){
			if(file.isFile()) {
				name = file.getName();
				if(name.startsWith(hash)) 
					path = file.getAbsolutePath();
			}
		}
		
		if(path.isEmpty()) return null;
		
		BufferedImage image = ImageUtils.read(new File(path));
		byte[] bytes = ImageUtils.imageToBytes(image);
		
		ModelAndView mav = new ModelAndView("status");
		mav.addObject("screen", "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes));
		return mav;
		
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
		
		File file = new File(Const.UPLOAD_DIR + hash + ".jpg");
		ImageIO.write(image, "jpg", file);
	}

}