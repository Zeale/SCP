package scp.main.clientconn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClientConnection {

	private final List<Consumer<String>> messageListeners = new ArrayList<>();

	public void connect(String endpoint, int port) {
		// TODO Write the code to connect this "Client Connection" to the server at the
		// specified endpoint and port.

		// You may need to add some fields to this class (like a Socket) to do this.
	}

	public void sendMessage(String message) {
		// TODO Write the code to send a message to the server. This method should only
		// be called if this object has already been connected to a server, so don't
		// worry about handling things if it hasn't been.
	}

	private void receiveMessage(String message) {// TODO Call this method in the code that you write which handles
													// incoming messages from the server. This method will automatically
													// notify all message listeners.

		// DON'T change this method.
		for (Consumer<String> listener : messageListeners)
			listener.accept(message);
	}

}
