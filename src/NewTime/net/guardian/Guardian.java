package NewTime.net.guardian;

import NewTime.net.http.HttpServer;

public class Guardian {

	public static void main(String[] args) {
		new Guardian(80);
	}
	
	HttpServer server;
	
	public Guardian(int port) {
		server = HttpServer.host(port);
	}
	
}
