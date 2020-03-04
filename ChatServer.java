package chat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;

public class ChatServer implements TCPConnectionListener {
	private static int port;
	
	
	public static void main(String[] args) {
		new ChatServer(port);
	}

	private final ArrayList<TCPConnection> connections = new ArrayList<>();
	
	public ChatServer(int port) {
		this.port = port;
		System.out.println("Server Running...");
		try(ServerSocket serverSocket = new ServerSocket(port)){
			while (true) {
				try {
					new TCPConnection(this, serverSocket.accept());
				} catch (IOException e) {
					System.out.println("TCPConnection exceprion: " + e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized void onConnectionReady(TCPConnection tcpConnection) {
		connections.add(tcpConnection);
		sendToAllClients("Client connected: " + tcpConnection);
	}

	@Override
	public synchronized void onReceiveSrting(TCPConnection tcpConnection, String value) {
		sendToAllClients(value);
	}

	@Override
	public synchronized void onDisconnect(TCPConnection tcpConnection) {
		connections.remove(tcpConnection);
		sendToAllClients("Client disconnected: " + tcpConnection);
		

	}

	@Override
	public synchronized void onExeption(TCPConnection tcpConnection, Exception e) {
		System.out.println("TCPConnection exception " + e);
	}
		
	
	private void sendToAllClients(String value) {
		System.out.println(value);
		final int cnt = connections.size();
		for (int i = 0; i < cnt; i++) {
			connections.get(i).sendString(value);
		}
	}
	
	
}
