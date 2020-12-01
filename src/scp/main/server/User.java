package scp.main.server;

import java.net.Socket;

/**
 * Used by the server to keep track of users who connect in. Each {@link User}
 * has a username and connection (the connection is a {@link Socket}) associated
 * with them.
 * 
 * @author Gartham
 *
 */
public class User {
	private final Socket socket;
	private final String username;

	public Socket getSocket() {
		return socket;
	}

	public String getUsername() {
		return username;
	}

	public User(Socket socket, String username) {
		this.socket = socket;
		this.username = username;
	}

}
