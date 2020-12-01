package scp.main.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import scp.main.networkencoder.NetworkEncoder;
import scp.main.serverconn.ServerConnection;

public class Server {
	private final Map<Socket, User> users = new HashMap<>();

	public Server() throws Throwable {
	}

	public void receiveMessage(String message, ServerConnection connection, Socket socket) throws Throwable {
		String msg = users.get(socket).getUsername() + ": " + message;
		for (Socket s : users.keySet())
			if (s != socket)
				s.getOutputStream().write(NetworkEncoder.encodeMessage(msg));
	}

	public void handleNewConnection(ServerConnection connection, Socket newConnection) throws Throwable {
		users.put(newConnection,
				new User(newConnection, NetworkEncoder.pollMessage(newConnection.getInputStream()).substring(7)));
	}
}
