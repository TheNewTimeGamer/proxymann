package NewTime.net.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Scanner;

import NewTime.net.http.HttpConnection;
import NewTime.net.http.HttpRequest;
import NewTime.net.http.HttpResponse;
import NewTime.net.http.HttpServer;
import NewTime.net.http.controllers.Controller;
import NewTime.net.http.controllers.GenericController;
import NewTime.net.http.routing.Router;
import NewTime.net.http.util.FileManager;

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
	private ProxyMann instance;
	
	public WebInterface(int port) throws IOException{
		this.server = new HttpServer(port);
		Router router = this.server.getRouter();
			
		instance = new ProxyMann();
		
		router.defaultController = new ProxyMannController(instance);
		
	}
	
}

class ProxyMannController extends GenericController {
	
	private ProxyMann instance;
	
	public ProxyMannController(ProxyMann instance) {
		this.instance = instance;
	}
	
	public void get(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "200 OK");
		response.body = buildPage();
		response.header.put("content-length", ""+response.body.length);
		connection.send(response);
	}
	
	public void post(HttpConnection connection, HttpRequest request) {
		HttpResponse response = new HttpResponse("HTTP/1.1", "200 OK");
		handleRequest(request);		
		response.body = buildPage();
		response.header.put("content-length", ""+response.body.length);
		connection.send(response);
	}
	
	private void handleRequest(HttpRequest request) {
		
		String name = null;
		String ip = null; 
		String port= null;
		String host= null;
		
		String body = new String(request.body);
		String[] params = body.split("&");
		for(int i = 0; i < params.length; i++) {
			String[] args = params[i].split("=");
			if(args[0].equals("name")) {
				name = args[1];
			}
			if(args[0].equals("targetip")) {
				ip = args[1];
			}
			if(args[0].equals("targetport")) {
				port = args[1];
			}
			if(args[0].equals("hostport")) {
				host = args[1];
			}
		}
		
		System.out.println("Creating Proxy: " + ip + ":" + port + "" + host);
		if(name == null || ip == null || port == null || host == null) {
			System.err.println("WRONG PARAM: " + name + " " + ip + " " + port + " " + host);
			return;
		}
		instance.createProxy(Integer.parseInt(host), ip, Integer.parseInt(port));
	}
	
	private byte[] buildPage() {
		byte[] data = FileManager.getFileContent("res/public/panel.html");
		
		Proxy[] proxies = instance.getProxies();
		
		String raw = new String(data);
		String full = "";
		for(int i = 0; i < proxies.length; i++) {
			if(proxies[i] == null) {continue;}
			full = full + "<form class='proxy'>";
			full = full + "<label>" + proxies[i].getName() + "</label>";
			full = full + "<label>" + proxies[i].getTargetIp() + ":" + proxies[i].getTargetPort() + "</label>";
			full = full + "<label>" + proxies[i].getHostPort() + "</label>";
			full = full + "<button type='submit'>Delete</button>";
			full = full + "</form>";
		}
		
		raw.replace("<PROXYLIST>", full);
		
		return data;
	}
	
}
