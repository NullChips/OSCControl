package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.AppMode;

import java.awt.*;

public class Text extends UIElement {

    private int textSize;
    private String text;
    private int xAlign;
    private int yAlign;
    private Color colour;

    public Text(int x, int y, int textSize, String text, Color colour, AppMode mode) {
        super(x, y);
        this.textSize = textSize;
        this.text = text;
        this.colour = colour;
        this.mode = mode;
        this.xAlign = app.CENTER;
        this.yAlign = app.CENTER;
    }

    public Text(int x, int y, int textSize, String text, int xAlign, int yAlign, Color colour, AppMode mode) {
        super(x, y);
        this.textSize = textSize;
        this.text = text;
        this.mode = mode;
        this.xAlign = xAlign;
        this.yAlign = yAlign;
        this.colour = colour;
    }

    public void render() {
        app.textSize(textSize);
        app.textAlign(xAlign, yAlign);
        app.fill(colour.getRed(), colour.getGreen(), colour.getBlue());
        app.text(text, x, y);
    }

    public void mousePressed() {

    }

    public boolean mouseInElement() {
        return false;
    }
}
