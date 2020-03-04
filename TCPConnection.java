package chat.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;



public class TCPConnection {
	
	private final Socket socket;
	private final Thread rxThread;
	private final TCPConnectionListener eventListener;
	private final BufferedReader in;
	private final BufferedWriter out;
	
	
	public TCPConnection(TCPConnectionListener eventListener,  String IpAddr, int port) throws IOException{
		this(eventListener, new Socket(IpAddr, port)); 
		
	}
	public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
		this.eventListener = eventListener;
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
		rxThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					eventListener.onConnectionReady(TCPConnection.this);
					while(!rxThread.isInterrupted()) {
						String msg = in.readLine();
						eventListener.onReceiveSrting(TCPConnection.this ,  msg);
					}
					
					
				} catch (IOException e) {
					
					eventListener.onExeption(TCPConnection.this, e);
				} finally {
					eventListener.onDisconnect(TCPConnection.this);
				}
				
			}
		});
		rxThread.start();
		
	}
	
	public synchronized void sendString(String value) {
		try {
			out.write(value + "\t\n");
			out.flush();
		} catch (IOException e) {
			eventListener.onExeption(TCPConnection.this, e);
			disconnect();
		}
	}
	
	public synchronized void disconnect() {
		rxThread.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			eventListener.onExeption(TCPConnection.this, e);
		}
	}
	@Override
	public String toString() {
		return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
	}
	
	public InetAddress getIp() {
		return socket.getInetAddress();
	}
	
	
}
