package scp.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.alixia.javalibrary.javafx.bindings.BindingTools;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import zeale.apps.stuff.api.appprops.ApplicationProperties;
import zeale.apps.stuff.api.guis.windows.Window;
import zeale.apps.tools.console.logic.ConsoleLogic;
import zeale.apps.tools.console.std.StandardConsole;
import zeale.apps.tools.console.std.StandardConsole.StandardConsoleUserInput;

public class LogInWindow extends Window {

	private @FXML CheckBox clientCheckBox;
	private @FXML TextField hostPrompt, portPrompt;
	private @FXML VBox box;
	private @FXML Button loginbutton;
	private Stage stage;

	private @FXML void initialize() {
		loginbutton.textProperty()
				.bind(BindingTools.mask(clientCheckBox.selectedProperty(), a -> a ? "Connect" : "Start"));
		clientCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue)
					box.getChildren().add(0, hostPrompt);
				else
					box.getChildren().remove(hostPrompt);
			}
		});
	}

	@Override
	public void destroy() {
	}

	private @FXML void logIn() {
		StandardConsole console = new StandardConsole();
		console.clear();
		try {
			Stage stage = new Stage();
			stage.setScene(new Scene(console.getEmbeddedView()));
			stage.show();
			stage.centerOnScreen();
			if (clientCheckBox.isSelected()) {
				stage.setTitle("SCP Client");
				Socket socket;
				try {
					socket = new Socket(InetAddress.getByName(hostPrompt.getText()),
							Integer.parseInt(portPrompt.getText().trim()));
				} catch (UnknownHostException e) {
					console.print("Unknown host: ", Color.FIREBRICK);
					console.println(hostPrompt.getText(), Color.RED);
					return;
				} catch (NumberFormatException e) {
					console.print("Malformed port: ", Color.FIREBRICK);
					console.println(portPrompt.getText(), Color.RED);
					return;
				}
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						while (true) {
							try {
								String str = readString(socket.getInputStream());
								console.println(str, Color.GOLD);
							} catch (IOException e) {
								console.println("An error occurred while reading a message. socket=[" + socket
										+ "], error-message=[" + e.getLocalizedMessage() + "]");
							}
						}
					}
				});
				console.applyLogic(new ConsoleLogic<StandardConsoleUserInput>() {

					@Override
					public void handle(StandardConsoleUserInput input) {
						String str = input.text.trim();
						if (str.isEmpty())
							return;
						try {
							writeString(str, socket.getOutputStream());
							console.println(str, Color.DARKORCHID);
						} catch (IOException e) {
							console.println("An error occurred while sending a message: " + e.getLocalizedMessage(),
									Color.FIREBRICK);
						}
					}
				});
				t.setDaemon(true);
				t.start();
			} else {
				stage.setTitle("SCP Server");
				ServerSocket socket;
				try {
					socket = new ServerSocket(Integer.parseInt(portPrompt.getText().trim()));
				} catch (NumberFormatException e) {
					console.print("Malformed port: ", Color.FIREBRICK);
					console.println(portPrompt.getText(), Color.RED);
					return;
				}

				List<Socket> socks = new ArrayList<>(3);
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						while (true) {
							try {
								Socket s = socket.accept();
								Thread t = new Thread(new Runnable() {

									@Override
									public void run() {
										socks.add(s);
										while (true) {
											try {
												String str = readString(s.getInputStream());
												for (Socket s : socks)
													try {
														writeString(str, s.getOutputStream());
													} catch (IOException e) {
														console.println(
																"An error occurred while sending a message on a socket. socket=["
																		+ s + "], error-message=["
																		+ e.getLocalizedMessage() + "]");
													}
											} catch (IOException e) {
												console.println(
														"An error occurred while reading a message on a socket. socket=["
																+ s + "], error-message=[" + e.getLocalizedMessage()
																+ "]");
											}
										}
									}
								});
								t.setDaemon(true);
								t.start();
							} catch (IOException e) {
								console.println("An error occurred with handling a socket: " + e.getLocalizedMessage(),
										Color.FIREBRICK);
							}
						}
					}
				});
				t.setDaemon(true);
				t.start();
			}
		} catch (Exception e) {
			console.println("An error occurred: " + e.getLocalizedMessage(), Color.FIREBRICK);
		}
	}

	public static String readString(InputStream is) throws IOException {
		byte[] bytes = new byte[is.read()];
		is.read(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static void writeString(String str, OutputStream os) throws IOException {
		byte[] barr = str.getBytes(StandardCharsets.UTF_8);
		os.write(barr.length);
		os.write(barr);
	}

	@Override
	protected void show(Stage stage, ApplicationProperties properties) throws WindowLoadFailureException {
		this.stage = stage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInWindow.fxml"));
		loader.setController(this);
		try {
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().addAll(properties.popButtonStylesheet.get(), properties.themeStylesheet.get());
//			scene.getStylesheets().add("scp/main/styles.css");
			stage.setScene(scene);
		} catch (IOException e) {
			throw new WindowLoadFailureException(e);
		}
	}

}
