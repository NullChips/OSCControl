package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.AppMode;

import java.awt.*;

public class Text extends UIElement {

    //This text class is used for the edit mode text, labelling the different sections.

    //Global variables.
    private int textSize;
    private String text;
    private int xAlign;
    private int yAlign;
    private Color colour;

    //Constructor with default text alignment set.
    public Text(int x, int y, int textSize, String text, Color colour, AppMode mode) {
        super(x, y);
        this.textSize = textSize;
        this.text = text;
        this.colour = colour;
        this.mode = mode;
        this.xAlign = app.CENTER;
        this.yAlign = app.CENTER;
    }

    //Constructor with customizable text alignment.
    public Text(int x, int y, int textSize, String text, int xAlign, int yAlign, Color colour, AppMode mode) {
        super(x, y);
        this.textSize = textSize;
        this.text = text;
        this.mode = mode;
        this.xAlign = xAlign;
        this.yAlign = yAlign;
        this.colour = colour;
    }

    //Draw the text.
    public void render() {
        app.textSize(textSize);
        app.textAlign(xAlign, yAlign);
        app.fill(colour.getRed(), colour.getGreen(), colour.getBlue());
        app.text(text, x, y);
    }

    //Do nothing when mouse pressed.
    public void mousePressed() {

    }

    //This method is never called, but is inherited from UIElement.
    public boolean mouseInElement() {
        return false;
    }
}
