package scp.main.clientconn;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import scp.main.networkencoder.NetworkEncoder;

public class ClientConnection {

	private final List<Consumer<String>> messageListeners = new ArrayList<>();
	private Socket sock;

	public void registerConsumer(Consumer<String> consumer) {
		messageListeners.add(consumer);
	}

	public void connect(String endpoint, int port, String username) throws Throwable {
		sock = new Socket(endpoint, port);
		sock.getOutputStream().write(NetworkEncoder.encodeMessage(username));
		Thread var0 = new Thread(() -> {
			while (true) {
				try {
					receiveMessage(NetworkEncoder.pollMessage(sock.getInputStream()));
					System.out.println("Received msg");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		var0.setDaemon(true);
		var0.start();
	}

	public void sendMessage(String message) throws Throwable {
		sock.getOutputStream().write(NetworkEncoder.encodeMessage(message));
		sock.getOutputStream().flush();
	}

	private void receiveMessage(String message) {
		for (Consumer<String> listener : messageListeners)
			listener.accept(message);
	}

}
