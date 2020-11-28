package scp.main.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import scp.main.serverconn.ServerConnection;
import scp.main.serverconn.User;

public class Server {
	private final Map<Socket, User> users = new HashMap<>();

	public void receiveMessage(String message, ServerConnection connection, Socket socket) {
		// TODO Handle a message that a client has sent to you.
	}

	public void handleNewConnection(ServerConnection connection, Socket newConnection) {
		// TODO Handle an incoming connection. You will want to keep track of it by
		// making a User object to represent it and storing that user object in the
		// "users" map.

		// You will need to read the message that the client has sent to get the
		// username. Use the NetworkEncoder class's static functions to do this.
	}
}
