package scp.main.clientgui;

import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.alixia.javalibrary.javafx.tools.FXTools;
import scp.main.clientconn.ClientConnection;
import scp.main.colorencoder.ColorParser;
import scp.main.server.Server;
import scp.main.serverconn.ServerConnection;
import zeale.apps.stuff.api.appprops.ApplicationProperties;
import zeale.apps.stuff.api.guis.windows.Window;
import zeale.apps.tools.console.std.ConsoleItem;
import zeale.apps.tools.console.std.StandardConsole;
import zeale.apps.tools.console.std.StandardConsole.EmbeddedStandardConsoleView;

public class ClientGUI extends Window {

	/*
	 * private @FXML Node firstGUIElement; private @FXML Button sendButton; ...
	 */

	private @FXML TextField serverIP, serverPort, username, localPort;
	private ApplicationProperties props;

	private @FXML void launchClient() {
		String username = this.username.getText();
		ClientConnection conn = new ClientConnection();
		try {
			conn.connect(serverIP.getText(), Integer.parseInt(serverPort.getText()), username);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		Stage stayj = new Stage();
		stayj.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.F11) {
				stayj.setFullScreen(!stayj.isFullScreen());
				event.consume();
			} else if (event.getCode() == KeyCode.PRINTSCREEN) {
				try {
					WritableImage img = stayj.getScene().snapshot(
							new WritableImage((int) stayj.getScene().getWidth(), (int) stayj.getScene().getHeight()));
					ClipboardContent content = new ClipboardContent();
					content.putImage(img);
					Clipboard.getSystemClipboard().setContent(content);
					FXTools.spawnLabelAtMousePos("Screenshot copied to Clipboard!", Color.GREEN, stayj);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		StandardConsole consolee = new StandardConsole();
		consolee.clear();
		EmbeddedStandardConsoleView view = consolee.getEmbeddedView();
		Scene seen = new Scene(new StackPane(view));
		seen.getStylesheets().setAll(props.popButtonStylesheet.get(), props.themeStylesheet.get());
		stayj.setScene(seen);
		stayj.setTitle("Client: [" + serverIP.getText() + ':' + serverPort.getText() + ']');
		stayj.show();
		consolee.applyLogic(input -> {
			String txt = input.text.trim();
			if (!txt.isEmpty()) {
				List<ConsoleItem> formatting = new ColorParser().parseFormatting(txt);
				consolee.print('[' + username + "]: ", Color.ORANGE);
				for (ConsoleItem ci : formatting)
					consolee.write(ci);
				consolee.println();
				try {
					conn.sendMessage(txt);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		});
		conn.registerConsumer(t -> Platform.runLater(() -> {
			for (ConsoleItem ci : new ColorParser().parseFormatting(t))
				consolee.write(ci);
			consolee.println();
		}));
	}

	private @FXML void launchServer() {
		try {
			Server server = new Server();
			ServerConnection conn = new ServerConnection();
			conn.start(Integer.parseInt(localPort.getText()), server);
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
		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.F11) {
				stage.setFullScreen(!stage.isFullScreen());
				event.consume();
			} else if (event.getCode() == KeyCode.PRINTSCREEN) {
				try {
					WritableImage img = stage.getScene().snapshot(
							new WritableImage((int) stage.getScene().getWidth(), (int) stage.getScene().getHeight()));
					ClipboardContent content = new ClipboardContent();
					content.putImage(img);
					Clipboard.getSystemClipboard().setContent(content);
					FXTools.spawnLabelAtMousePos("Screenshot copied to Clipboard!", Color.GREEN, stage);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
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
