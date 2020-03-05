package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIElement;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public abstract class Button extends UIElement {

    private int sizeX, sizeY;
    private String label;

    private boolean pressed;
    protected boolean moving;

    protected Color colour;

    //Constructor, sets some default values including width and height.
    public Button(int x, int y, String label, Color colour) {
        super(x, y);
        this.sizeX = 35;
        this.sizeY = 35;
        this.label = label;
        this.pressed = false;
        this.colour = colour;
        this.moving = false;
    }

    //Constructor, allows for the height and width to be set when a new button is created.
    public Button(int x, int y, int sizeX, int sizeY, String label, Color colour) {
        super(x, y);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.label = label;
        this.pressed = false;
        this.colour = colour;
    }

    public void render() {
        if (UIManager.getMgr().isEditMode()) {
            if (UIManager.getMgr().getRecentElement() == this && app.frameCount / 20 % 2 == 0 && UIManager.getMgr().isEditMode()) {
                //If the app is in edit mode and this was the most recently selected element, flash to inform the user of this.
                app.fill(colour.getRed() / 2, colour.getGreen() / 2, colour.getBlue() / 2);
                if (mouseInElement()) {
                    //If mouse is hovering over the button, draw a white border.
                    drawWhiteBorderRect();
                } else {
                    //If the mouse isn't, draw a normal border.
                    drawRect();
                }
            } else if (!(this instanceof ModeButton)) { //Mode button is always filled.
                //Draw the rectangle with no fill.
                app.noFill();
                if (mouseInElement()) {
                    //If mouse is hovering over the button, draw a white border.
                    drawWhiteBorderRect();
                } else {
                    //If the mouse isn't, draw a normal border.
                    drawRect();
                }
            } else { //Mode button
                app.fill(colour.getRed() / 2, colour.getGreen() / 2, colour.getBlue() / 2);
                if (mouseInElement()) {
                    //If mouse is hovering over the button, draw a white border.
                    drawWhiteBorderRect();
                } else {
                    //If the mouse isn't, draw a normal border.
                    drawRect();
                }
            }
        } else {
            if (mouseInElement() && UIManager.getMgr().getElementDragged() == null) { //When the mouse is hovering over a button, a white stroke is drawn.
                if (pressed) { //If the button is pressed, render the button with a fill and a white stroke.
                    app.fill(colour.getRed() / 2, colour.getGreen() / 2, colour.getBlue() / 2);
                    drawWhiteBorderRect();
                } else { //If the button is not pressed, render the button without a fill and a white stroke.
                    app.noFill();
                    drawWhiteBorderRect();
                }
                return; //Exit - the button has been rendered!
            } else if (pressed) { //If the button has been pressed, render the button with a fill.
                app.fill(colour.getRed() / 2, colour.getGreen() / 2, colour.getBlue() / 2);
                drawRect();
            } else { //If the button has not been pressed, render the button without a fill.
                app.noFill();
                drawRect();
            }
        }
    }

    protected void drawRect() {
        app.stroke(colour.getRed(), colour.getGreen(), colour.getBlue());
        app.strokeCap(app.ROUND);
        app.strokeWeight(2);
        app.rectMode(app.CENTER);
        app.textAlign(app.CENTER, app.CENTER);
        app.textSize(12);
        app.rect(x, y, sizeX, sizeY, 3);
        app.fill(255);
        app.text(label, x, y - 2);
    }

    protected void drawWhiteBorderRect() {
        app.stroke(255);
        app.strokeCap(app.ROUND);
        app.strokeWeight(2);
        app.rectMode(app.CENTER);
        app.textAlign(app.CENTER, app.CENTER);
        app.textSize(12);
        app.rect(x, y, sizeX, sizeY, 3);
        app.fill(255);
        app.text(label, x, y - 2);
    }

    //Check the position of the mouse relative to the x and y values and return if the mouse is in the button.
    public boolean mouseInElement() {
        return app.mouseX - x + sizeX / 2 >= 0 && app.mouseX - x + sizeX / 2 <= sizeX &&
                app.mouseY - y + sizeY / 2 >= 0 && app.mouseY - y + sizeY / 2 <= sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }
}
