package scp.main.clientgui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import scp.main.clientconn.ClientConnection;
import scp.main.colorencoder.ColorParser;
import scp.main.server.Server;
import scp.main.serverconn.ServerConnection;
import zeale.apps.stuff.api.appprops.ApplicationProperties;
import zeale.apps.stuff.api.guis.windows.Window;
import zeale.apps.tools.console.std.ConsoleItem;
import zeale.apps.tools.console.std.StandardConsole;

public class ClientGUI extends Window {

	/*
	 * private @FXML Node firstGUIElement; private @FXML Button sendButton; ...
	 */

	private @FXML TextField serverIP, serverPort, username, localPort;
	private ApplicationProperties props;

	private @FXML void launchClient() {
		String username = this.username.getText();
		ClientConnection conn = new ClientConnection();
		conn.connect(serverIP.getText(), Integer.parseInt(serverIP.getText()), username);
		Stage stayj = new Stage();

		StandardConsole consolee = new StandardConsole();
		Scene seen = new Scene(new StackPane());
		seen.getStylesheets().setAll(props.popButtonStylesheet.get(), props.themeStylesheet.get());
		stayj.setScene(seen);
		stayj.setTitle("Client: [" + serverIP.getText() + ':' + serverPort.getText() + ']');
		stayj.show();
		consolee.applyLogic(input -> {
			String txt = input.text.trim();
			consolee.println('[' + username + "]: " + txt, Color.ORANGE);
			if (!txt.isEmpty())
				conn.sendMessage(txt);
		});
		conn.registerConsumer(t -> {
			ColorParser cp = new ColorParser();
			Platform.runLater(() -> {
				for (ConsoleItem ci : cp.parseFormatting(t))
					consolee.write(ci);
			});
		});
	}

	private @FXML void launchServer() {
		try {
			Server server = new Server();
			ServerConnection conn = new ServerConnection();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private @FXML void initialize() {
	}

	@Override
	public void destroy() {
	}

	@Override
	protected void show(Stage stage, ApplicationProperties properties) throws WindowLoadFailureException {
		props = properties;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientGUI.fxml"));
		loader.setController(this);
		try {
			Parent parent = loader.load();
			Scene value = new Scene(parent);
			value.getStylesheets().setAll(properties.popButtonStylesheet.get(), properties.themeStylesheet.get());
			stage.setScene(value);
		} catch (IOException e) {
			throw new WindowLoadFailureException(e);
		}
	}

	private @FXML void close() {
		Platform.exit();
	}

}
