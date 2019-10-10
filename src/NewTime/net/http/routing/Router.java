package NewTime.net.http.routing;

import java.util.ArrayList;

import NewTime.net.http.HttpConnection;
import NewTime.net.http.HttpRequest;
import NewTime.net.http.HttpResponse;
import NewTime.net.http.controllers.Controller;
import NewTime.net.http.controllers.GenericController;

public class Router {

	public Controller defaultController = new GenericController();
	public ArrayList<Controller> controllers = new ArrayList<Controller>();
	
	public void route(HttpConnection connection, HttpRequest request) {
		for(int i = 0; i < controllers.size(); i++) {
			Controller c = controllers.get(i);
			if(c != null) {
				if(request.header.get("action").equals(c.action)){
					invoke(c, connection, request);
					return;
				}
			}
		}
		invoke(defaultController, connection, request);
	}
	
	private void invoke(Controller controller, HttpConnection connection, HttpRequest request) {
		switch(request.header.get("method")) {
			case "GET":
				controller.get(connection, request);
				break;
			case "POST":
				controller.post(connection, request);
				break;
			case "PATCH":
				controller.patch(connection, request);
				break;
			case "PUT":
				controller.put(connection, request);
				break;
			case "DELETE":
				controller.delete(connection, request);
				break;		
			case "HEAD":
				controller.head(connection, request);
				break;
			case "CONNECT":
				controller.connect(connection, request);
				break;
			case "OPTIONS":
				controller.options(connection, request);
				break;
			case "TRACE":
				controller.trace(connection, request);
				break;
			default:
				HttpResponse response = new HttpResponse("HTTP/1.1", "501 Not Implemented");
				response.body = ("501 Not Implemented").getBytes();
				response.header.put("content-length", ""+response.body.length);
				connection.send(response);
				break;
		}
	}
	
}
