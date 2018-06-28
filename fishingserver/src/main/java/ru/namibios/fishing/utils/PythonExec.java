package ru.namibios.fishing.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonExec {

	private static final String SCRIPT_PATH = "/usr/local/fishing-resources/model/parsing.sh";
	
	private PythonExec() {}
	
	public static String exec(String fileKapcha) throws IOException {
		
		String[] cmd = new String[2];
		cmd[0] = SCRIPT_PATH; 
		cmd[1] = fileKapcha;
		
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);

		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String response = "";
		while ((response = bfr.readLine()) != null) {
			if(response.length() == 10) return response;
		}
		
		return null;
	}
	
}