package ga.tomj.osccontrol.gui;

import java.awt.*;

public class Line extends UIElement {

    /*
    These lines used to separate sections in the edit mode. New object was created rather than just using line(x,y,x,y);
    so that these can use the UIElement checks and rendering system already in place.
    */

    //Global variables.
    private int x2;
    private int y2;
    private Color colour;
    private int weight;
    private boolean roundCap;

    //Constructor with some defaults set.
    public Line(int x, int y, int x2, int y2, Color colour) {
        super(x, y);
        this.x2 = x2;
        this.y2 = y2;
        this.colour = colour;
        this.weight = 3;
        this.roundCap = false;
    }

    //Constructor with everything customisable.
    public Line(int x, int y, int x2, int y2, Color colour, int weight, boolean roundCap) {
        super(x, y);
        this.x2 = x2;
        this.y2 = y2;
        this.colour = colour;
        this.weight = weight;
        this.roundCap = roundCap;
    }

    //Draw the line.
    public void render() {
        app.strokeWeight(weight);
        if(roundCap) { //If roundCap is true, make the line round.
            app.strokeCap(app.ROUND);
        } else {
            app.strokeCap(app.SQUARE);
        }
        app.stroke(colour.getRed(), colour.getGreen(), colour.getBlue());
        app.line(x, y, x2, y2);
    }

    //Inherited from UIElement. Never used.
    public void mousePressed() {

    }

    //Inherited from UIElement. Never used.
    public boolean mouseInElement() {
        return false;
    }
}
