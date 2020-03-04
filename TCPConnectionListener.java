package chat.network;

public interface TCPConnectionListener {
	void onConnectionReady(TCPConnection tcpConnection);
	void onReceiveSrting(TCPConnection tcpConnection, String value);
	void onDisconnect(TCPConnection tcpConnection);
	void onExeption(TCPConnection tcpConnection, Exception e);
}
