package scp.main.colorencoder;

import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.ORANGE;
import static javafx.scene.paint.Color.PURPLE;
import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.YELLOW;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import zeale.apps.tools.console.std.ConsoleItem;
import zeale.mouse.utils.CharacterParser;

public class ColorParser {

	public final List<ConsoleItem> parseFormatting(String text) {
		CharacterParser parser = CharacterParser.from(text);
		StringBuilder curr = new StringBuilder();
		ConsoleItem currentItem = null;

		List<ConsoleItem> txts = new ArrayList<>();
		boolean escaped = false;

		while (true) {
			int c = parser.nxt();
			if (c == '\\') {
				if (!(escaped ^= true))
					curr.append('\\');
			} else if (c == '[') {
				if (escaped) {
					curr.append('[');
					escaped = false;
					continue;
				}
				if (currentItem == null)
					currentItem = new ConsoleItem().setColor(Color.WHITE);
				if (curr.length() != 0)
					txts.add(currentItem.clone().setText(curr.toString()));
				curr = new StringBuilder();
				c = Character.toLowerCase(parser.pk());
				switch (c) {
				case '#':
					int r, g, b;
					parser.next();
					if ((r = Character.digit(Character.toLowerCase(parser.nxt()), 16)) == -1)
						throw new RuntimeException("Invalid char in color code value.");
					r <<= 4;
					if ((c = Character.digit(Character.toLowerCase(parser.nxt()), 16)) == -1)
						throw new RuntimeException("Invalid char in color code value.");
					r |= c;

					if ((g = Character.digit(Character.toLowerCase(parser.nxt()), 16)) == -1)
						throw new RuntimeException("Invalid char in color code value.");
					g <<= 4;
					if ((c = Character.digit(Character.toLowerCase(parser.nxt()), 16)) == -1)
						throw new RuntimeException("Invalid char in color code value.");
					g |= c;

					if ((b = Character.digit(Character.toLowerCase(parser.nxt()), 16)) == -1)
						throw new RuntimeException("Invalid char in color code value.");
					b <<= 4;
					if ((c = Character.digit(Character.toLowerCase(parser.nxt()), 16)) == -1)
						throw new RuntimeException("Invalid char in color code value.");
					b |= c;

					currentItem.setColor(Color.rgb(r, g, b));

					if ((c = parser.nxt()) != ']')
						throw new RuntimeException("Missing close bracket for formatting code.");

					break;
				case 'b':
					parser.nxt();
					expect("old]", parser);
					currentItem.setWeight(FontWeight.BOLD);
					break;
				case 'i':
					parser.nxt();
					expect("talicized]", parser);
					currentItem.setPosture(FontPosture.ITALIC);
					break;
				case 'u':
					parser.nxt();
					expect("nderlined]", parser);
					currentItem.setUnderline(true);
					break;
				case 'r':
					parser.nxt();
					expect("ainbow]", parser);
					currentItem = new ConsoleItem() {
						public Text getNode() {
							Text node = super.getNode();
							node.fillProperty().unbind();
							class ColorTrans extends Transition {
								{
									setCycleDuration(Duration.seconds(0.5));
								}
								private Color first, second;

								@Override
								protected void interpolate(double frac) {
									node.setFill(first.interpolate(second, frac));
								}

								public ColorTrans(Color first, Color second) {
									this.first = first;
									this.second = second;
								}
							}
							SequentialTransition seq = new SequentialTransition(new ColorTrans(RED, ORANGE),
									new ColorTrans(ORANGE, YELLOW), new ColorTrans(YELLOW, GREEN),
									new ColorTrans(GREEN, BLUE), new ColorTrans(BLUE, PURPLE),
									new ColorTrans(PURPLE, RED));
							seq.setCycleCount(Animation.INDEFINITE);
							seq.play();

							return node;
						}
					}.subsume(currentItem);
					break;
				case 'f':
					parser.nxt();
					expect("ont:", parser); {
					StringBuilder bbbbbb = new StringBuilder();
					while ((c = parser.nxt()) != ']')
						if (c == -1)
							throw new RuntimeException("Nonterminated formatting code.");
						else
							bbbbbb.append((char) c);
					currentItem.setFontFamily(bbbbbb.toString());
				}
					break;
				case 's':
					parser.nxt();
					expect("ize:", parser);
					StringBuilder bbbbbb = new StringBuilder();
					while ((c = parser.nxt()) != ']')
						if (c == -1)
							throw new RuntimeException("Nonterminated formatting code.");
						else
							bbbbbb.append((char) c);
					currentItem.setFontSize(Double.parseDouble(bbbbbb.toString()));
					break;
				case -1:
					curr.append('[');
					break;
				}

			} else if (c == -1) {
				if (curr.length() != 0)
					if (currentItem != null) {
						currentItem.setText(curr.toString());
						txts.add(currentItem);
					} else
						txts.add(new ConsoleItem().setColor(Color.WHITE).setText(curr.toString()));
				return txts;
			} else {
				escaped = false;
				curr.append((char) c);
			}
		}
	}

	private void expect(String in, CharacterParser parser) {
		for (int i = 0; i < in.length(); i++)
			if (Character.toLowerCase(parser.nxt()) != in.charAt(i))
				throw new RuntimeException("Invalid char in formatting code.");
	}

}
