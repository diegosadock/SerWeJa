package br.com.sadocktech.serweja.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Response {
	private OutputStream out;
	private static final String TERMINATOR = "\r\n";
	private String content;
	private HashMap<String, String> headers;

	public Response(OutputStream out) {
		this.headers = new HashMap<>();
		this.out = out;
	}

	public void write(String message) {
		try {
			out.write(message.getBytes(StandardCharsets.UTF_8));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void setHeader(String key, String value) throws IOException {
		this.headers.put(key, value);
		this.write(key + ": " + value + TERMINATOR);
	}

	public void setContent(String content) {
		byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
		setContent(contentBytes);
	}
	
	public void setContent(byte[] content) {
		try {
			this.write("Content-Length: " + content.length + TERMINATOR);
			this.write("Connection: close" + TERMINATOR);
			this.write(TERMINATOR);

			out.write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void close() {
		try {
			out.flush();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}