package NewTime.net.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TcpServer implements Runnable {

	public static TcpServer host(int port) {
		TcpServer server = null;
		try {
			server = new TcpServer(port);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return server;
	}
	
	protected ServerSocket server;
	
	protected ArrayList<TcpConnection> connections = new ArrayList<TcpConnection>();
	
	protected Thread thread;
	protected Timer cleanup;
	
	public TcpServer(int port) throws IOException {
		this.server = new ServerSocket(port);
		
		this.thread = new Thread(this);
		this.thread.start();
		
		this.cleanup = new Timer(true);
		this.cleanup.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				int i = cleanup();
				if(i > 0) {
					System.out.println("Thread cleanup: " + i);
				}
			}			
		}, 0, 2000);
		
	}
	
	public int cleanup() {
		int count = 0;
		for(int i = 0; i < connections.size(); i++) {
			if(!connections.get(i).isAlive()) {
				connections.remove(i);
				count++;
			}
		}
		return count;
	}
	
	public void run() {
		while(true) {
			try {
				listen();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void listen() throws IOException {
		Socket socket = server.accept();
		if(socket == null) {return;}
		onConnection(socket);
	}
	
	public void onConnection(Socket socket) {
		TcpConnection connection = TcpConnection.create(this, socket);
		if(connection != null) {
			connections.add(connection);
		}
	}
	
}
