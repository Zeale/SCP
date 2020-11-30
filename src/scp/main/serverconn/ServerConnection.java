package scp.main.serverconn;

import java.io.IOException;
import java.net.Socket;

import scp.main.networkencoder.NetworkEncoder;
import scp.main.server.Server;

public class ServerConnection {

	private Server server;

	public void start(int port, Server server) {
		// TODO Write the code to start up the ServerSocket that will listen for
		// incoming clients. This will also need to setup the threads and other stuff
		// that calls #receiveMessage(...) whenever a message is received from a client.
	}

	public void sendMessage(String message, Socket socket) throws Throwable {
		socket.getOutputStream().write(NetworkEncoder.encodeMessage(message));
		socket.getOutputStream().flush();
	}

	private void receiveMessage(Socket socket, String message) throws IOException, Throwable {
		server.receiveMessage(NetworkEncoder.pollMessage(socket.getInputStream()), this, socket);
	}

}
