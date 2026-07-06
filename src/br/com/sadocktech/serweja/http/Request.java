package br.com.sadocktech.serweja.http;

import java.util.HashMap;

public class Request {
	private String httpMethod;
	private String path;
	private HashMap<String, String> requestParameters;
	private HashMap<String, String> requestHeaders;
	private String body;
	
	public Request() {
		super();
		this.requestParameters = new HashMap<>();
		this.requestHeaders = new HashMap<>();
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void addParameter(String key, String value) {
		if (this.requestParameters == null) {
			this.requestParameters = new HashMap<>();
		}
		this.requestParameters.put(key.toLowerCase(), value);
	}
	
	public String getParameter(String key) {
		if (this.requestParameters != null) {
			return this.requestParameters.get(key.toLowerCase());
		}
		return null;
	}
	
	public String getHeader(String key) {
		if (this.requestHeaders != null) {
			return this.requestHeaders.get(key.toLowerCase());
		}
		return null;
	}
	
	public void addHeader(String key, String value) {
		if (this.requestHeaders == null) {
			this.requestHeaders = new HashMap<>();
		}
		this.requestHeaders.put(key.toLowerCase(), value);
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    sb.append("Request {");
	    sb.append("\n  httpMethod = ").append(httpMethod);
	    sb.append("\n  path = ").append(path);

	    if (requestParameters != null && !requestParameters.isEmpty()) {
	        StringBuilder params = new StringBuilder();

	        for (String key : requestParameters.keySet()) {
	            if (params.length() > 0) {
	                params.append("&");
	            }

	            params.append(key)
	                  .append("=")
	                  .append(requestParameters.get(key));
	        }

	        sb.append("\n  parameters = [").append(params).append("]");
	    }

	    if (requestHeaders != null && !requestHeaders.isEmpty()) {
	        sb.append("\n  headers = {");

	        for (String key : requestHeaders.keySet()) {
	            sb.append("\n    ")
	              .append(key)
	              .append(" = ")
	              .append(requestHeaders.get(key));
	        }

	        sb.append("\n  }");
	    }

	    if (body != null && !body.isBlank()) {
	        sb.append("\n  body = ");
	        sb.append(body);
	    }

	    sb.append("\n}");

	    return sb.toString();
	}

}
