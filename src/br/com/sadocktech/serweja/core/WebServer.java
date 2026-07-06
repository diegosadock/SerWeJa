package br.com.sadocktech.serweja.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import br.com.sadocktech.serweja.http.Request;
import br.com.sadocktech.serweja.http.Response;
import br.com.sadocktech.serweja.util.WebConfig;
import br.com.sadocktech.serweja.util.WebLogger;

public class WebServer {

	private ServerSocket serverSocket;
	private String httpMethod;
	private String resourcePath;

	public WebServer(int port) {
		try {
			WebLogger.welcome();
			this.serverSocket = new ServerSocket(port);
			WebLogger.log("SerWeJa initialized on port " + port);
			WebLogger.log("Waiting for client requests...");
		} catch (IOException ex) {
			WebLogger.log("Error initializing SerWeJa on port " + port + ": " + ex.getMessage());
			return;
		}

		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				handleRequest(clientSocket);
			} catch (IOException ex) {
				WebLogger.log("Error accepting client request: " + ex.getMessage());
			}
		}
	}

	private void handleRequest(Socket clientSocket) {
		try {
			Request request = new Request();

			InputStreamReader inReader = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader br = new BufferedReader(inReader);

			String line = br.readLine();

			if (line == null || line.isBlank()) {
				clientSocket.close();
				return;
			}

			if (line.startsWith("GET") || line.startsWith("POST")) {
				httpMethod = line.substring(0, line.indexOf(" "));
				resourcePath = line.substring(line.indexOf(" ") + 1, line.lastIndexOf(" "));

				request.setHttpMethod(httpMethod);

				int paramDelimiterIndex = resourcePath.indexOf("?");

				String resourceFile = paramDelimiterIndex == -1
						? resourcePath
						: resourcePath.substring(0, paramDelimiterIndex);

				request.setPath(resourceFile);

				if (paramDelimiterIndex > 0) {
					String parameterList = resourcePath.substring(paramDelimiterIndex + 1);
					String[] parameters = parameterList.split("&");

					for (String paramTuple : parameters) {
						String[] keyValue = paramTuple.split("=", 2);

						if (keyValue.length == 2) {
							request.addParameter(keyValue[0], keyValue[1]);
						}
					}
				}
			}

			while ((line = br.readLine()) != null && !line.isBlank()) {
				if (line.contains(":")) {
					String[] headers = line.split(":", 2);
					request.addHeader(headers[0], headers.length > 1 ? headers[1].trim() : "");
				}
			}

			if (request.getHttpMethod() != null && request.getHttpMethod().equals("POST")) {
				readPostBody(br, request);
			}

			if (isInvalidRequest(request)) {
				clientSocket.close();
				return;
			}

			WebLogger.log(request.toString());

			handleOutput(clientSocket, request);

			clientSocket.close();

		} catch (IOException ex) {
			WebLogger.log("Error handling client request: " + ex.getMessage());

			try {
				clientSocket.close();
			} catch (IOException closeEx) {
				WebLogger.log("Error closing client socket: " + closeEx.getMessage());
			}
		}
	}

	private void readPostBody(BufferedReader br, Request request) throws IOException {
		String contentLengthHeader = request.getHeader("Content-Length");
		String contentTypeHeader = request.getHeader("Content-Type");

		WebLogger.log("Reading " + contentLengthHeader + " bytes of " + contentTypeHeader);

		if (contentLengthHeader == null || contentLengthHeader.isBlank()) {
			request.setBody("");
			return;
		}

		int contentLength = Integer.parseInt(contentLengthHeader);

		if (contentLength <= 0) {
			request.setBody("");
			return;
		}

		char[] contentBuffer = new char[contentLength];

		int bytesRead = br.read(contentBuffer, 0, contentLength);

		if (bytesRead > 0) {
			request.setBody(new String(contentBuffer, 0, bytesRead));
		} else {
			request.setBody("");
		}
	}

	private boolean isInvalidRequest(Request request) {
		return request.getHttpMethod() == null || request.getPath() == null;
	}

	private void handleOutput(Socket clientSocket, Request request) {
		Path requestedPath = Paths.get(WebConfig.DOCUMENT_ROOT + request.getPath());
		String defaultPagePath = WebConfig.DOCUMENT_ROOT + "/index.html";

		if (Files.isDirectory(requestedPath)) {
			requestedPath = Paths.get(defaultPagePath);
		}

		try {
			Response response = new Response(clientSocket.getOutputStream());

			if (Files.exists(requestedPath) && !Files.isDirectory(requestedPath)) {
				sendFileResponse(response, requestedPath, 200);
			} else {
				sendNotFoundResponse(response);
			}

		} catch (IOException ex) {
			WebLogger.log("Error handling output: " + ex.getMessage());
		}
	}

	private void sendNotFoundResponse(Response response) throws IOException {
		Path notFoundPagePath = Paths.get(WebConfig.DOCUMENT_ROOT + "/404.html");

		if (Files.exists(notFoundPagePath) && !Files.isDirectory(notFoundPagePath)) {
			sendFileResponse(response, notFoundPagePath, 404);
			return;
		}

		String content = "404 Not Found";

		response.write("HTTP/1.1 404 Not Found\r\n");
		response.write("Date: " + LocalDate.now() + "\r\n");
		response.write("Content-Type: text/plain\r\n");

		response.setContent(content);
		response.close();
	}

	private void sendFileResponse(Response response, Path filePath, int statusCode) throws IOException {
		byte[] content = Files.readAllBytes(filePath);

		String extension = getExtension(filePath);
		String contentType = WebConfig.content.getOrDefault(extension, "application/octet-stream");
		String statusText = WebConfig.textCodes.getOrDefault(statusCode, "OK");

		response.write("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
		response.write("Date: " + LocalDate.now() + "\r\n");
		response.write("Content-Type: " + contentType + "\r\n");

		response.setContent(content);
		response.close();
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