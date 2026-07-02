package br.com.sadocktech.serweja.util;

import java.util.HashMap;

public class WebConfig {
	
	public static final String DOCUMENT_ROOT = "C:\\Users\\diego\\OneDrive\\Documentos\\serweja";
	public static HashMap<String, String> content = new HashMap<>() {{
		put("html", "text/html");
		put("html", "text/html");
		put("jpg", "image/jpg");
		put("png", "image/png");
		put("jpeg", "image/jpeg");
		put("js", "text/javascript");
		put("txt", "text/plain");
		put("json", "application/json");
		put("pdf", "application/pdf");
		put("xml", "application/xml");
		put("gif", "image/gif");
		put("css", "text/css");
	}};
}
