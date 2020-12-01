package scp.main.serverconn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import scp.main.networkencoder.NetworkEncoder;
import scp.main.server.Server;

public class ServerConnection {

	private Server server;

	public void start(int port, Server server) throws Throwable {
		@SuppressWarnings("resource") // 8)
		ServerSocket serv = new ServerSocket(port);
		this.server = server;
		Thread acceptor = new Thread(() -> {
			while (true)
				try {
					Socket sock = serv.accept();
					server.handleNewConnection(ServerConnection.this, sock);
					Thread handler = new Thread(() -> {
						while (true)
							try {
								receiveMessage(sock, NetworkEncoder.pollMessage(sock.getInputStream()));
							} catch (Throwable e) {
								throw new RuntimeException(e);
							}
					});
					handler.setDaemon(true);
					handler.start();
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
		});
		acceptor.setDaemon(true);
		acceptor.start();
	}

	public void sendMessage(String message, Socket socket) throws Throwable {
		socket.getOutputStream().write(NetworkEncoder.encodeMessage(message));
		socket.getOutputStream().flush();
	}

	private void receiveMessage(Socket socket, String message) throws IOException, Throwable {
		server.receiveMessage(message, this, socket);
	}

}
