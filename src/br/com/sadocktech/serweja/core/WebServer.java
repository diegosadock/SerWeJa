package br.com.sadocktech.serweja.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import br.com.sadocktech.serweja.util.WebConfig;

public class WebServer {

	private ServerSocket serverSocket;
	private String httpMethod;
	private String resourcePath;

	public WebServer(int port) {
		try {
			this.serverSocket = new ServerSocket(port);
			System.out.println("SerWeJa initialized on port " + port);
			System.out.println("Waiting for client requests...");
		} catch (IOException ex) {
			System.err.println("Error initializing SerWeJa on port " + port + ": " + ex.getMessage());
			return;
		}

		while (true) {
			try {
				Socket clientSocket = serverSocket.accept(); // aqui eu aceito trocar dados com o cliente, ou seja, o
																// navegador do usuário
				handleRequest(clientSocket);
				clientSocket.close();
			} catch (IOException ex) {
				System.err.println("Error accepting client request: " + ex.getMessage());
				continue;
			}
		}
	}

	private void handleRequest(Socket clientSocket) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String line;
			do {
				line = br.readLine();
				if (line != null && line.startsWith("GET") || line.startsWith("POST")) {
					httpMethod = line.substring(0, line.indexOf(" "));
					resourcePath = line.substring(line.indexOf(" ") + 1, line.lastIndexOf(" "));

					handleOutput(clientSocket, httpMethod, resourcePath);
				}
//				System.out.println("DEBUG: " + line);
			} while (!line.isBlank());
			clientSocket.close();
		} catch (IOException ex) {
			System.err.println("Error handling client request: " + ex.getMessage());
			return;
		}

	}

	private void handleOutput(Socket clientSocket, String httpMethod, String resourcePath) {
		System.out.println("HTTP Method: " + httpMethod);
		System.out.println("Resource Path: " + resourcePath);

		Path requestedPath = Paths.get(WebConfig.DOCUMENT_ROOT + resourcePath);
		String defaultPagePath = WebConfig.DOCUMENT_ROOT + "/index.html";

		if (Files.isDirectory(requestedPath)) {
			requestedPath = Paths.get(defaultPagePath);
		}

		try {
			OutputStream out = clientSocket.getOutputStream();

			if (Files.exists(requestedPath) && !Files.isDirectory(requestedPath)) {
				sendFileResponse(out, requestedPath, 200, "OK");
			} else {
				sendNotFoundResponse(out);
			}
			out.flush();
			out.close();
		} catch (IOException ex) {
			System.err.println("Error handling output: " + ex.getMessage());
		}

	}

	private void sendNotFoundResponse(OutputStream out) throws IOException {
		Path notFoundPagePath = Paths.get(WebConfig.DOCUMENT_ROOT + "/404.html");

		if (Files.exists(notFoundPagePath)) {
			sendFileResponse(out, notFoundPagePath, 404, "Not Found");
		} else {
			byte[] content = "404 Not Found".getBytes();

	        out.write(("HTTP/1.1 404 Not Found\r\n").getBytes());
	        out.write(("Date: " + LocalDate.now() + "\r\n").getBytes());
	        out.write(("Content-Type: text/plain\r\n").getBytes());
	        out.write(("Content-Length: " + content.length + "\r\n").getBytes());
	        out.write(("Connection: close\r\n").getBytes());
	        out.write("\r\n".getBytes());

	        out.write(content);
		}

	}

	private void sendFileResponse(OutputStream out, Path filePath, int statusCode, String statusText)
			throws IOException {
		byte[] content = Files.readAllBytes(filePath);

		String extension = getExtension(filePath);
		String contentType = WebConfig.content.getOrDefault(extension, "application/octet-stream");

		out.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n").getBytes());
		out.write(("Date: " + LocalDate.now() + "\r\n").getBytes());
		out.write(("Content-Type: " + contentType + "\r\n").getBytes());
		out.write(("Content-Length: " + content.length + "\r\n").getBytes());
		out.write(("Connection: close\r\n").getBytes());
		out.write("\r\n".getBytes());

		out.write(content);
	}

	private String getExtension(Path filePath) {
		String fileName = filePath.getFileName().toString();

		int dotIndex = fileName.lastIndexOf('.');

		if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
			return "";
		}

		return fileName.substring(dotIndex + 1);
	}

	public WebServer() {
		this(80);
	}

}
