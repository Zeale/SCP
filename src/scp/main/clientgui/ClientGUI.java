package scp.main.clientgui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zeale.apps.stuff.api.appprops.ApplicationProperties;
import zeale.apps.stuff.api.guis.windows.Window;

public class ClientGUI extends Window {

	/*
	 * private @FXML Node firstGUIElement; private @FXML Button sendButton; ...
	 */

	private @FXML TextField serverIP, serverPort, username, localPort;

	private @FXML void launchClient() {
		// TODO Auto-generated method stub
	}

	private @FXML void launchServer() {
		// TODO Auto-generated method stub
	}

	private @FXML void initialize() {
		// TODO perform GUI initialization using FXML nodes above.
	}

	@Override
	public void destroy() {
	}

	@Override
	protected void show(Stage stage, ApplicationProperties properties) throws WindowLoadFailureException {
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
