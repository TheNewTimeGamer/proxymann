package NewTime.net.http;

import java.io.File;

import NewTime.net.http.util.FileManager;

public class HttpResponse extends HttpRequest {

	private String protocol;
	private String status;
	
	public HttpResponse(String protocol, String status) {
		this.protocol = protocol;
		this.status = status;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.protocol + " " + this.status);
		builder.append("\r\n");
		builder.append(getHeaderAsString());		
		builder.append("\r\n");
		
		if(body != null) {
			builder.append(new String(body));
		}
		
		return builder.toString();
	}
	
	public byte[] getBinary() {
		byte[] temp = this.body;
		this.body = null;
		byte[] header = this.toString().getBytes();
		this.body = temp;
		byte[] buffer = new byte[temp.length + header.length];
		for(int i = 0; i < header.length; i++) {
			buffer[i] = header[i];
		}
		for(int i = 0; i < temp.length; i++) {
			buffer[i+header.length] = temp[i];
		}
		return buffer;
	}
	
	public String getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public static HttpResponse buildFromFile(File file) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "200 OK");
		byte[] data = FileManager.getFileContent(file);
		if(data == null) {
			data = FileManager.getErrorFile(404);
			response.status = "404 Not Found";
		}
		
		if(file.getAbsolutePath().endsWith(".jpg")) {
			response.header.put("Accept-Range", "bytes");
			response.header.put("Content-Type", "image/jpeg");
			response.body = data;
		}else {
			response.header.put("Content-Type", "text/html");
			response.body = data;
		}
		
		response.header.put("content-length", "" + data.length);	
				
		return response;
	}
	
	public static HttpResponse buildFromFile(String path) {
		return buildFromFile(new File(path));
	}
	
}
