package NewTime.net.http.controllers;

import java.io.File;

import NewTime.net.http.HttpConnection;
import NewTime.net.http.HttpRequest;
import NewTime.net.http.HttpResponse;
import NewTime.net.http.util.FileManager;

public class GenericController extends Controller {

	public GenericController() {
		super(null);
	}

	public void get(HttpConnection connection, HttpRequest request) {		
		String url = request.header.get("action");
		if(url.equals("/")) {
			url = "/index.html";
		}
	
		HttpResponse out = HttpResponse.buildFromFile("public" + url);
		System.out.println("Sending: " + "public" + url);
		System.out.println(out.toString());
		connection.send(out);
	}
	
	public void post(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "405 Method Not Allowed");
		response.body = ("405 Method Not Allowed").getBytes();
		connection.send(response);
	}

	public void patch(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "405 Method Not Allowed");
		response.body = ("405 Method Not Allowed").getBytes();
		connection.send(response);
	}

	public void put(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "405 Method Not Allowed");
		response.body = ("405 Method Not Allowed").getBytes();
		connection.send(response);
	}

	public void delete(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "405 Method Not Allowed");
		response.body = ("405 Method Not Allowed").getBytes();
		connection.send(response);
	}

	public void head(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "405 Method Not Allowed");
		response.body = ("405 Method Not Allowed").getBytes();
		connection.send(response);
	}

	public void connect(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "405 Method Not Allowed");
		response.body = ("405 Method Not Allowed").getBytes();
		connection.send(response);
	}

	public void options(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "405 Method Not Allowed");
		response.body = ("405 Method Not Allowed").getBytes();
		connection.send(response);
	}

	public void trace(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "405 Method Not Allowed");
		response.body = ("405 Method Not Allowed").getBytes();
		connection.send(response);
	}

}
