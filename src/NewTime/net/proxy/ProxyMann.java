package NewTime.net.proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyMann {

	public static void main(String[] args) {
		new ProxyMann();
	}
	
	private ArrayList<Proxy> proxies = new ArrayList<Proxy>();
	
	public ProxyMann() {
		try {
			InetAddress ip = InetAddress.getByName("moonquest.com");
			this.proxies.add(new Proxy(4444, ip.getHostAddress(), 80));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public Proxy createProxy(int hostPort, String targetIp, int targetPort) {
		Proxy proxy = null;
		try {
			proxy = new Proxy(hostPort, targetIp, targetPort);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return proxy;
	}
	
	
}

class Proxy implements Runnable {
	
	private String name;
	private int hostPort;
	private String targetIp;
	private int targetPort;
	
	private ServerSocket server;
	private ArrayList<ProxyConnection> connections = new ArrayList<ProxyConnection>();	
	
	private Thread thread;
		
	public Proxy(int hostPort, String targetIp, int targetPort) throws IOException {
		this.hostPort = hostPort;
		this.targetIp = targetIp;
		this.targetPort = targetPort;
		
		server = new ServerSocket(hostPort);
		
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTargetIp() {
		return this.targetIp;
	}
	
	public int getTargetPort() {
		return this.targetPort;
	}
	
	public int getHostPort() {
		return this.hostPort;
	}
	
	public void run() {
		while(true) {
			try {
				listen();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void listen() throws IOException {
		Socket socket = server.accept();
		System.out.println("Linking: " + socket.getInetAddress().toString() + " -> " + this.targetIp + ":" + this.targetPort);
		Socket internalSocket = new Socket(this.targetIp, this.targetPort);
		
		ProxyConnection connection = new ProxyConnection(socket, internalSocket);
		
		connections.add(connection);
		
		for(int i = connections.size()-1; i >= 0; i--) {
			if(!connections.get(i).isOpen()) {
				connections.remove(i);
				System.out.println("Removed connection: " + i);
			}
		}
		
	}
	
}

class ProxyConnection {
	
	private boolean open = true;
	
	Socket one, two;
	DataInputStream oneIn, twoIn;
	DataOutputStream oneOut, twoOut;
	
	Thread oneThread, twoThread;
	
	int tickRate = 100000; // Bytes per second
	
	public ProxyConnection(Socket one, Socket two) throws IOException {
		this.one = one;
		this.two = two;
		
		this.oneIn = new DataInputStream(one.getInputStream());
		this.oneOut = new DataOutputStream(one.getOutputStream());
		
		this.twoIn = new DataInputStream(two.getInputStream());
		this.twoOut = new DataOutputStream(two.getOutputStream());
		
		this.oneThread = new Thread() {
			public void run() {
				while(true) {
					listenOne();
				}
			}
		};
		
		this.twoThread = new Thread() {
			public void run() {
				while(true) {
					listenTwo();
				}
			}
		};
		
		oneThread.start();
		twoThread.start();
	}
	
	public void listenOne() {
		try {
			byte b = oneIn.readByte();
			twoOut.write(b);
		}catch(Exception e) {
			close();
		}
	}
	
	public void listenTwo() {
		try {
			byte b = twoIn.readByte();
			oneOut.write(b);
		}catch(Exception e) {
			close();
		}
	}

	public void close() {
		if(!open) {return;}
		try {
			System.out.println("Unlinking: " + one.getInetAddress().toString() + " -> " + two.getInetAddress().toString() + ":" + two.getPort());
			
			this.oneThread.interrupt();
			this.twoThread.interrupt();
			
			this.one.close();
			this.two.close();
		
			this.oneIn.close();
			this.oneOut.close();
		
			this.twoIn.close();
			this.twoOut.close();
		}catch(Exception e) {}
		
		oneThread = null;
		twoThread = null;
		one = null;
		two = null;
		
		oneIn = null;
		oneOut = null;
		
		twoIn = null;
		twoOut = null;
		
		this.open = false;
	}
	
	public boolean isOpen() {
		return this.open;
	}
	
}
