package NewTime.net.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import NewTime.net.http.HttpConnection;
import NewTime.net.http.HttpRequest;
import NewTime.net.http.HttpResponse;
import NewTime.net.http.HttpServer;
import NewTime.net.http.controllers.Controller;
import NewTime.net.http.controllers.GenericController;
import NewTime.net.http.routing.Router;

public class WebInterface {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("ProxyMann By TheNewTimeGamer");
		System.out.print("Port for webinteface: ");
		
		String line = scanner.nextLine();		
		int port = Integer.parseInt(line);
		
		System.out.println("\r\nHosting on port: " + port);
		
		try {
			new WebInterface(port);
		}catch(IOException e) {
			e.printStackTrace();
			System.err.println("Failed to launch proxymann-webinterface, closing..");
		}
	}
		
	private HttpServer server;
	
	public WebInterface(int port) throws IOException{
		this.server = new HttpServer(port);
		Router router = this.server.getRouter();
		
		router.defaultController = new ProxyMannController();
		
	}
	
}

class ProxyMannController extends GenericController {
	
	public void post(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "200 OK");
		response.body = ("tsadasd").getBytes();
		response.header.put("content-length", ""+response.body.length);
		connection.send(response);
	}
	
}
