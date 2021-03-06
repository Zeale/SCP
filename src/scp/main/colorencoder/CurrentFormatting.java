package scp.main.colorencoder;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import zeale.apps.tools.console.std.ConsoleItem;

public class CurrentFormatting implements Cloneable {
	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isItalicized() {
		return italicized;
	}

	public void setItalicized(boolean italicized) {
		this.italicized = italicized;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public double getFontsize() {
		return fontsize;
	}

	public void setFontsize(double fontsize) {
		this.fontsize = fontsize;
	}

	public String getFontname() {
		return fontname;
	}

	public void setFontname(String fontname) {
		this.fontname = fontname;
	}

	public Paint getColor() {
		return color;
	}

	public void setColor(Paint color) {
		this.color = color;
	}

	private boolean bold, italicized, underline;
	private double fontsize;
	private String fontname;
	private Paint color;

	public CurrentFormatting(boolean bold, boolean italicized, boolean underline, double fontsize, String fontname,
			Paint color) {
		this.bold = bold;
		this.italicized = italicized;
		this.underline = underline;
		this.fontsize = fontsize;
		this.fontname = fontname;
		this.color = color;
	}

	public CurrentFormatting(double fontsize, String fontname, Paint color) {
		this.fontsize = fontsize;
		this.fontname = fontname;
		this.color = color;
	}

	public CurrentFormatting(Paint color) {
		this.color = color;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new CurrentFormatting(bold, italicized, underline, fontsize, fontname, color);
	}

	public ConsoleItem generateText(String txt) {
		ConsoleItem text = new ConsoleItem();
		if (underline)
			text.setUnderline(true);
		if (color != null)
			text.setColor((Color) color);
		if (fontname != null)
			text.setFontFamily(fontname);
		if (bold)
			text.setWeight(FontWeight.BOLD);
		if (italicized)
			text.setPosture(FontPosture.ITALIC);
		return text;
	}

}
