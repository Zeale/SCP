package scp.main.clientgui;

import javafx.application.Platform;
import javafx.fxml.FXML;
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void show(Stage stage, ApplicationProperties properties) throws WindowLoadFailureException {
		// TODO Auto-generated method stub

	}

	private @FXML void close() {
		Platform.exit();
	}

}
