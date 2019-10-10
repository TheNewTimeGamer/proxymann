package NewTime.net.http;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import NewTime.net.http.routing.Router;
import NewTime.net.http.util.FileManager;
import NewTime.net.tcp.TcpConnection;

public class HttpConnection extends TcpConnection {

	public static HttpConnection create(HttpServer server, Socket socket) {
		HttpConnection connection = null;
		try {
			connection = new HttpConnection(server, socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static HttpConnection create(HttpServer server, String ip, int port) {
		HttpConnection connection = null;
		try {
			connection = new HttpConnection(server, ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}
		
	public HttpConnection(HttpServer server, Socket socket) throws IOException {
		super(server, socket);
	}
	
	public HttpConnection(HttpServer server, String ip, int port) throws IOException {
		super(server, ip, port);
	}

	public void onData(byte[] data) {
		HttpRequest request = HttpRequest.build(data);
		Router router = getServer().getRouter();
		
		if(request != null) {
			if(router != null) {
				router.route(this, request);
			}
		}else {
			HttpResponse response = new HttpResponse("http/1.1", "400 Bad request");
			response.header.put("content-type", "text/html");
			response.body = (FileManager.getErrorFile(400));
			response.header.put("content-length", "" + response.body.length);
			send(response);
		}
	}
	
	public boolean send(HttpResponse request) {
		try {
			byte[] data = request.getBinary();
			out.write(data);
			out.flush();
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public HttpServer getServer() {
		return (HttpServer) super.getServer();
	}
	
}
