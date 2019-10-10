package NewTime.net.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

	public static HttpRequest build(String raw) {
		HttpRequest request = null;
		try {
			request = new HttpRequest(raw);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return request;
	}
	
	public static HttpRequest build(byte[] data) {
		HttpRequest request = null;
		try {
			request = new HttpRequest(data);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return request;
	}
	
	public HashMap<String, String> header = new HashMap<String, String>();
	public HashMap<String, String> parameters = new HashMap<String, String>();
	
	public byte[] body;
	
	public HttpRequest() {}
	
	public HttpRequest(byte[] data) throws Exception {
		process(new String(data));
	}
	
	public HttpRequest(String request) throws Exception {
		process(request);
	}
	
	private void process(String request) throws Exception {
		String[] parts = request.split("\r\n\r\n");
		request = parts[0];
		if(parts.length > 1) {
			body = parts[1].getBytes();
		}
		String[] lines = request.split("\r\n");
		
		String[] actionArgs = lines[0].split(" ");
		
		header.put("method", actionArgs[0]);
		
		String action = actionArgs[1];
		if(action.contains("?")) {
			String[] urlArgs = action.split("\\?", 2);
			action = urlArgs[0];
			if(urlArgs[1].contains("&")) {
				String[] localParameters = urlArgs[1].split("&");
				for(int i = 0; i < localParameters.length; i++) {
					String[] parameter = localParameters[i].split("=");
					parameters.put(parameter[0], parameter[1]);
				}
			}else {
				String[] parameter = urlArgs[1].split("=");
				parameters.put(parameter[0], parameter[1]);
			}
		}		
		header.put("action", action);
		
		String protocol = actionArgs[2];
		header.put("protocol", protocol);
		
		for(int i = 1; i < lines.length; i++) {
			String[] args = lines[i].split(": ", 2);
			header.put(args[0], args[1]);			
		}
		
	}
	
	public String[][] getHeaderAsArray(){
		String[][] headerArray = new String[header.size()][2];		
		int i = 0;
		for(Map.Entry<String, String> entry : header.entrySet()) {
			headerArray[i][0] = entry.getKey();
			headerArray[i][1] = entry.getValue();
			i++;
		}
		return headerArray;
	}
	
	public String[][] getParametersAsArray(){
		String[][] parameterArray = new String[parameters.size()][2];		
		int i = 0;
		for(Map.Entry<String, String> entry : parameters.entrySet()) {
			parameterArray[i][0] = entry.getKey();
			parameterArray[i][1] = entry.getValue();
			i++;
		}
		return parameterArray;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(header.get("method"));
		builder.append(" ");
		builder.append(header.get("action"));
		builder.append(getParametersAsString());		
		builder.append(" ");
		builder.append(header.get("protocol"));
		builder.append("\r\n");
		
		builder.append(getHeaderAsString());		
		builder.append("\r\n");
		
		if(body != null) {
			builder.append(new String(body));
		}
		
		return builder.toString();
	}
	
	public String getHeaderAsString() {
		StringBuilder builder = new StringBuilder();
		for(Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey();
			if(key.equals("method") || key.equals("action") || key.equals("protocol")) {continue;}
			builder.append(key + ": " + entry.getValue() + "\r\n");
		}
		return builder.toString();
	}
	
	public String getParametersAsString() {
		StringBuilder builder = new StringBuilder();
		if(parameters.size() <= 0) {return "";}
		builder.append("?");
		for(Map.Entry<String, String> entry : parameters.entrySet()) {
			builder.append(entry.getKey() + "=" + entry.getValue());
			builder.append("&");
		}
		String full = builder.toString();
		if(full.endsWith("&")) {
			full = full.substring(0, full.length()-1);
		}
		return full;
	}
	
}
