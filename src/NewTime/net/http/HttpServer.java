package NewTime.net.http;

import java.io.IOException;
import java.net.Socket;

import NewTime.net.http.routing.Router;
import NewTime.net.tcp.TcpServer;

public class HttpServer extends TcpServer {

	public static HttpServer host(int port) {
		HttpServer server = null;
		try {
			server = new HttpServer(port);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return server;
	}
	
	protected Router router;
	
	public HttpServer(int port) throws IOException {
		super(port);
		this.router = new Router();
	}
	
	public void onConnection(Socket socket) {
		HttpConnection connection = HttpConnection.create(this, socket);
		if(connection != null) {
			this.connections.add(connection);
		}
	}
	
	public Router getRouter() {
		return this.router;
	}
	
	public void setRouter(Router router) {
		this.router = router;
	}

}
