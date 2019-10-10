package NewTime.net.http.controllers;

import NewTime.net.http.HttpConnection;
import NewTime.net.http.HttpRequest;

public abstract class Controller {

	public final String action;
	
	public Controller(String action) {
		this.action = action;
	}
	
	public abstract void get(HttpConnection connection, HttpRequest request);
	public abstract void post(HttpConnection connection, HttpRequest request);
	public abstract void patch(HttpConnection connection, HttpRequest request);
	public abstract void put(HttpConnection connection, HttpRequest request);
	public abstract void delete(HttpConnection connection, HttpRequest request);
	public abstract void head(HttpConnection connection, HttpRequest request);
	public abstract void connect(HttpConnection connection, HttpRequest request);
	public abstract void options(HttpConnection connection, HttpRequest request);
	public abstract void trace(HttpConnection connection, HttpRequest request);
	
}
