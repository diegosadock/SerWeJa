package br.com.sadocktech.serweja.util;

import java.util.HashMap;

public class WebConfig {
	
	public static final String DOCUMENT_ROOT = "C:\\Users\\diego\\OneDrive\\Documentos\\serweja";
	public static final String APP_ROOT = "C:\\Users\\diego\\OneDrive\\Documentos\\serweja-apps";
	
	public static HashMap<String, String> content = new HashMap<>() {{
		put("html", "text/html");
		put("html", "text/html");
		put("jpg", "image/jpg");
		put("png", "image/png");
		put("jpeg", "image/jpeg");
		put("ico", "image/x-icon");
		put("svg", "image/svg+xml");
		put("js", "text/javascript");
		put("txt", "text/plain");
		put("json", "application/json");
		put("pdf", "application/pdf");
		put("xml", "application/xml");
		put("gif", "image/gif");
		put("css", "text/css");
	}};
	
	public static HashMap<Integer, String> textCodes = new HashMap<>() {{
		put(200, "OK");
		put(201, "Created");
		put(202, "Accepted");
		put(204, "No Content");
		put(301, "Moved Permanently");
		put(302, "Found");
		put(304, "Not Modified");
		put(400, "Bad Request");
		put(401, "Unauthorized");
		put(403, "Forbidden");
		put(404, "Not Found");
		put(405, "Method Not Allowed");
		put(409, "Conflict");
		put(500, "Internal Server Error");
		put(501, "Not Implemented");
		put(502, "Bad Gateway");
		put(503, "Service Unavailable");
	}};
	
	public static HashMap<String, String> appPath = new HashMap<>() {{
		put("/appteste", "HelloWorld");
		put("/apptestepkg", "teste.App");
		put("/isiflix", "flix.isi.Hello");
	}};
}
