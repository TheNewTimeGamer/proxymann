package NewTime.net.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TcpConnection implements Runnable {

	public static TcpConnection create(TcpServer server, Socket socket) {
		TcpConnection connection = null;
		try {
			connection = new TcpConnection(server, socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static TcpConnection create(TcpServer server, String ip, int port) {
		TcpConnection connection = null;
		try {
			connection = new TcpConnection(server, ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}

	protected Socket socket;
	protected DataInputStream in;
	protected DataOutputStream out;
	
	protected Thread thread;

	protected boolean alive = true;
	
	private TcpServer server;
	
	public TcpConnection(TcpServer server, Socket socket) throws IOException {
		this.server = server;		
		
		this.socket = socket;
		this.in = new DataInputStream(socket.getInputStream());
		this.out = new DataOutputStream(socket.getOutputStream());
		
		this.thread = new Thread(this);
		this.thread.start();
	}

	public TcpConnection(TcpServer server, String ip, int port) throws IOException {
		this.server = server;
		
		this.socket = new Socket(ip, port);
		this.in = new DataInputStream(this.socket.getInputStream());
		this.out = new DataOutputStream(this.socket.getOutputStream());
		
		this.thread = new Thread(this);
		this.thread.start();
	}

	public void run() {
		long last = System.currentTimeMillis();
		while (true) {
			try {
				byte[] data = listen();
				if (data != null) {
					onData(data);
					last = System.currentTimeMillis();
				}else {
					if(System.currentTimeMillis()-last >= (5000)) {
						close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				close();
			}
		}
	}

	private byte[] listen() throws IOException {
		byte[] buffer = null;
		if (in != null && in.available() > 0) {
			buffer = new byte[in.available()];
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = in.readByte();
			}
		}
		return buffer;
	}

	public void onData(byte[] data) {
		System.out.println("Received Data:");
		System.out.println(new String(data));
	}
	
	public void close() {
		if(thread != null) {
			thread.interrupt();		
		}
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.thread = null;
		
		this.in = null;
		this.out = null;
		this.socket = null;
		
		this.alive = false;
		System.out.println("close");
	}

	public boolean isAlive() {
		return this.alive;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public DataInputStream getInputStream() {
		return this.in;
	}
	
	public DataOutputStream getOutputStream() {
		return this.out;
	}
	
	public TcpServer getServer() {
		return this.server;
	}
	
}
