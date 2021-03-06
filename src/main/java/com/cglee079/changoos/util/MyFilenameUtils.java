package com.cglee079.changoos.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;

public class MyFilenameUtils {
	public static String getExt(String filename) {
		String extension = "";
		int i = filename.lastIndexOf('.');
		if (i > 0) {
			extension = filename.substring(i + 1);
		}
		return extension;
	}

	public static String getRandomFilename(String ext) {
		StringBuilder filename = new StringBuilder();
		filename.append(RandomStringUtils.randomAlphanumeric(6) + "_");
		filename.append(RandomStringUtils.randomAlphanumeric(6) + "_");
		filename.append(RandomStringUtils.randomAlphanumeric(6) + "_");
		filename.append(TimeStamper.stamp("YYMMdd_HHmmss"));
		filename.append("." + ext);
		return filename.toString().toUpperCase();
	}
	
	public static String getRandomImagename(String imageExt) {
		StringBuilder filename = new StringBuilder();
		filename.append(RandomStringUtils.randomAlphanumeric(6));
		filename.append(TimeStamper.stamp("YYMMddHHmmss"));
		filename.append("." + imageExt);
		return filename.toString().toUpperCase();
	}
	
	public static String sanitizeRealFilename(String name) {
		return name.replaceAll("[:\\\\/*?|<>\"]", "_");
	}

	public static String encodeFilename(HttpServletRequest request, String filename) {
		String header = request.getHeader("User-Agent");
		String encodedFilename = "";
		String browser = "";

		if (header.indexOf("MSIE") > -1) {
			browser = "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			browser = "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			browser = "Opera";
		} else if (header.indexOf("Trident/7.0") > -1) {
			browser = "Firefox";
		}

		try {
			if (browser.equals("Opera")) {
				encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1");
			} else if (browser.equals("Chrome")) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < filename.length(); i++) {
					char c = filename.charAt(i);
					if (c > '~') { sb.append(URLEncoder.encode("" + c, "UTF-8")); } 
					else { sb.append(c); }
				}
				encodedFilename = sb.toString();
			} else {
				encodedFilename = URLEncoder.encode(filename,"UTF-8").replace("+", "%20");
			}
		} catch (UnsupportedEncodingException e) {
			encodedFilename = filename;
		}
		
		return encodedFilename;
	}

}
