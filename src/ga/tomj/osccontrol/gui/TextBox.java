package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.AppMode;
import processing.core.PConstants;

import java.awt.*;

public class TextBox extends UIElement {

    //Global variables.
    protected int sizeX;
    private int sizeY;
    protected int padding; //Pixels from the left of the textbox to space.
    private int textSize;
    private Color boxColour;
    private Color textColour;
    protected boolean isCurrentlyEditing;
    protected String text;


    //Constructor with a default AppMode set.
    public TextBox(int x, int y, int sizeX, int sizeY, int textSize, Color boxColour, Color textColour, String text) {
        super(x, y, AppMode.SETTINGS);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.textSize = textSize;
        this.boxColour = boxColour;
        this.textColour = textColour;
        this.isCurrentlyEditing = false;
        this.text = text;
        this.padding = 4;
    }

    //Constructor with a customisable AppMode.
    public TextBox(int x, int y, int sizeX, int sizeY, int textSize, Color boxColour, Color textColour, String text, AppMode mode) {
        super(x, y, mode);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.textSize = textSize;
        this.boxColour = boxColour;
        this.textColour = textColour;
        this.isCurrentlyEditing = false;
        this.text = text;
        this.padding = 4;
    }

    /* Rendering of TextBox adapted from:
     https://forum.processing.org/two/discussion/423/%20*%20how-to-write-a-class-for-text-area-without-using-any-library */
    public void render() {
        app.stroke(255);
        app.strokeWeight(1);
        app.fill(boxColour.getRed(), boxColour.getGreen(), boxColour.getBlue());
        app.rectMode(app.CENTER);
        app.rect(x, y, sizeX, sizeY);
        app.rectMode(app.CORNER);
        app.fill(textColour.getRed(), textColour.getGreen(), textColour.getBlue());
        app.textAlign(app.LEFT, app.CENTER);
        app.textSize(textSize);
        if (isCurrentlyEditing) {
            /*
            If currently editing, enable flashing text cursor. When the framecount divided by 10 has modulo 2 equal to 0,
            display the text cursor.
            */
            app.text(text + (app.frameCount / 10 % 2 == 0 ? "|" : ""), (x - sizeX / 2) + padding, y - (textSize / 7));
        } else {
            //If not currently editing, just render the text.
            app.text(text, (x - sizeX / 2) + padding, y - (textSize / 7));
        }
    }


    /* Handing of keyPressed adapted from:
     https://forum.processing.org/two/discussion/423/%20*%20how-to-write-a-class-for-text-area-without-using-any-library */
    public void keyPressed(char key) {
        if (isCurrentlyEditing) {
            //As the switch case requires a constant, these values are taken from the PConstants class instead of the app object.
            switch (key) {
                case PConstants.ESC:
                case PConstants.ENTER:
                case PConstants.RETURN:
                    //Exit the text box.
                    isCurrentlyEditing = false;
                    break;
                case PConstants.BACKSPACE:
                    text = text.substring(0, Math.max(0, text.length() - 1));
                    break;
                default:
                    if (app.textWidth(text + app.str(key) + "|") <= sizeX - padding) {
                        text += app.str(key);
                    }

            }
        }
    }

    public void mousePressed() {
        isCurrentlyEditing = mouseInElement();
    }

    public boolean mouseInElement() {
        return app.mouseX - x + sizeX / 2 >= 0 && app.mouseX - x + sizeX / 2 <= sizeX &&
                app.mouseY - y + sizeY / 2 >= 0 && app.mouseY - y + sizeY / 2 <= sizeY;
    }

    public String getText() {
        return text;
    }
}
