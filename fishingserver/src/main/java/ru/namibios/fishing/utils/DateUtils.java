package ru.namibios.fishing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private DateUtils() {}
	
	public static String yyyymmdd() {
		SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
		return format.format(new Date());
	}
}
