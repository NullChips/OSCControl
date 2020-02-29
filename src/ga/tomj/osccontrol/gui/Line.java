package ga.tomj.osccontrol.gui;

import java.awt.*;

public class Line extends UIElement {

    private int x2;
    private int y2;
    private Color colour;
    private int weight;
    private boolean roundCap;

    public Line(int x, int y, int x2, int y2, Color colour) {
        super(x, y);
        this.x2 = x2;
        this.y2 = y2;
        this.colour = colour;
        this.weight = 3;
        this.roundCap = false;
    }

    public Line(int x, int y, int x2, int y2, Color colour, int weight, boolean roundCap) {
        super(x, y);
        this.x2 = x2;
        this.y2 = y2;
        this.colour = colour;
        this.weight = weight;
        this.roundCap = roundCap;
    }

    public void render() {
        app.strokeWeight(weight);
        if(roundCap) {
            app.strokeCap(app.ROUND);
        } else {
            app.strokeCap(app.SQUARE);
        }
        app.stroke(colour.getRed(), colour.getGreen(), colour.getBlue());
        app.line(x, y, x2, y2);
    }

    public void mousePressed() {

    }

    public boolean mouseInElement() {
        return false;
    }
}
