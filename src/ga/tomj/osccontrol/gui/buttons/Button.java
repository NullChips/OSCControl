package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIElement;

import java.awt.*;

public abstract class Button extends UIElement {

    private int sizeX, sizeY;
    private String label;

    private boolean pressed;
    protected boolean moving;

    private Color colour;

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
        if (mouseInElement()) { //When the mouse is hovering over a button, a white stroke is drawn.
            if (pressed) { //If the button is pressed, render the button with a fill and a white stroke.
                app.fill(colour.getRed() / 2, colour.getGreen() / 2, colour.getBlue() / 2);
                app.stroke(255);
                app.strokeCap(app.ROUND);
                app.strokeWeight(2);
                app.rectMode(app.CENTER);
                app.textAlign(app.CENTER, app.CENTER);
                app.textSize(12);
                app.rect(x, y, sizeX, sizeY, 3);
                app.fill(255);
                app.text(label, x, y - 2);
            } else { //If the button is not pressed, render the button without a fill and a white stroke.
                app.noFill();
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
            return; //Exit - the button has been rendered!
        } else if (pressed) { //If the button has been pressed, render the button with a fill.
            app.fill(colour.getRed() / 2, colour.getGreen() / 2, colour.getBlue() / 2);
            app.stroke(colour.getRed(), colour.getGreen(), colour.getBlue());
            app.strokeCap(app.ROUND);
            app.strokeWeight(2);
            app.rectMode(app.CENTER);
            app.textAlign(app.CENTER, app.CENTER);
            app.textSize(12);
            app.rect(x, y, sizeX, sizeY, 3);
            app.fill(255);
            app.text(label, x, y - 2);
        } else { //If the button has not been pressed, render the button without a fill.
            app.noFill();
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
    }

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
        if (pressed) System.out.println("pressed has been set to true");
        else System.out.println("pressed has been set to false");
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
