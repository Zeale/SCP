package scp.main;

import java.awt.MouseInfo;

import org.alixia.chatroom.api.printables.StyledPrintable;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import zeale.apps.tools.console.std.ConsoleItem;
import zeale.apps.tools.console.std.StandardConsole;
import zeale.apps.tools.console.std.StandardConsole.EmbeddedStandardConsoleView;

public class TextTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		System.out.println(Font.getFontNames());
		StandardConsole console = new StandardConsole();
		EmbeddedStandardConsoleView view = console.getEmbeddedView();
		primaryStage.setScene(new Scene(view));
		primaryStage.show();
		console.clear();
		print("Steve", console);
		console.println("This is a normal message.", Color.ORANGE);
		print("Steve", console);
		console.println("This, too, is a normal message.", Color.ORANGE);
		print("Steve", console);
		console.print("This message has ", Color.ORANGE);
		console.print("embedded, ", Color.BURLYWOOD);
		console.print("colored, ", Color.FIREBRICK);
		console.print("text", Color.GREENYELLOW);
		console.println(".", Color.ORANGE);
		console.println();
		console.println();

		print("Steve", console);
		console.print("This message has", Color.ORANGE);
		console.write(new ConsoleItem().setFontSize(18).setText(" larger than normal ").setColor(Color.ORANGE));
		console.println("text.", Color.ORANGE);

		print("Steve", console);
		console.write(new ConsoleItem() {
			public javafx.scene.text.Text getNode() {
				Text text = super.getNode();
				text.fillProperty().unbind();
				text.setFill(new LinearGradient(0, 0.5, 1, 0.5, true, CycleMethod.REPEAT, new Stop(0, Color.RED),
						new Stop(0.2, Color.ORANGE), new Stop(0.4, Color.YELLOW), new Stop(0.6, Color.GREEN),
						new Stop(0.8, Color.BLUE), new Stop(1, Color.PURPLE)));
				text.setOnMouseClicked(
						event -> spawnLabelAtMousePos("Click!", Color.hsb(Math.random() * 360, 1, 1), primaryStage));
				return text;
			}
		}.setText("This text is large, has a font, and is colored with a gradient!\n").setFontSize(30)
				.setFontFamily("Brush Script MT"));

//		Stage secondStage = new Stage();
//		secondStage.setScene(new Scene(console.getEmbeddedView()));
//		secondStage.show();
//
//		console.applyLogic(input -> {
//			print("You", console);
//			console.println(input.text, Color.ORANGE);
//		});

	}

	private void print(String name, StyledPrintable printer) {
		printer.print('[' + name + "]: ", Color.CORNFLOWERBLUE);
	}

	/**
	 * Spawns a floating piece of text that flies upwards a little then disappears.
	 * The source point of the text is specified via the {@code x} and {@code y}
	 * parameters.
	 *
	 * @param text  The text to render.
	 * @param color The color of the rendered text.
	 * @param x     The starting x position of the text.
	 * @param y     The starting y position of the text.
	 */
	public static void spawnLabel(Stage stage, final String text, final Color color, final double x, final double y) {
		final Popup pc = new Popup();
		final Label label = new Label(text);
		label.setMouseTransparent(true);
		final TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), label);
		final FadeTransition opacityTransition = new FadeTransition(Duration.seconds(2), label);

		pc.getScene().setRoot(label);
		/* Style label */
		label.setTextFill(color);
		label.setBackground(null);
		double fontSize = 16;
		label.setStyle("-fx-font-weight: bold; -fx-font-size: " + fontSize + "px;");
		/* Set Popup positions */
		pc.setX(x);
		pc.setWidth(label.getMaxWidth());
		pc.setY(y - 50);
		/* Build transitions */
		translateTransition.setFromY(30);
		translateTransition.setFromX(0);
		translateTransition.setToX(0);
		translateTransition.setToY(5);
		translateTransition.setInterpolator(Interpolator.EASE_OUT);
		opacityTransition.setFromValue(0.7);
		opacityTransition.setToValue(0.0);
		opacityTransition.setOnFinished(e -> pc.hide());
		/* Show the Popup */
		pc.show(stage);
		pc.setHeight(50);
		/* Play the transitions */
		translateTransition.play();
		opacityTransition.play();
	}

	public static void spawnLabelAtMousePos(final String text, final Color color, Stage stage) {
		spawnLabel(stage, text, color, MouseInfo.getPointerInfo().getLocation().getX(),
				MouseInfo.getPointerInfo().getLocation().getY());
	}

}
