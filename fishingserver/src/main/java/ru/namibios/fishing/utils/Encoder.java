package ru.namibios.fishing.utils;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class Encoder {
	
	private static final String SALT = "youwillnotpass";
	
	public static String encode(String pwd) {
		 
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		return passwordEncoder.encodePassword(pwd, SALT);
		
	}
	
	public static void main(String[] args) {
		System.out.println(encode("41e5709bacac9bb98c67433e53d2502a6fa4084f"));
	}

}
