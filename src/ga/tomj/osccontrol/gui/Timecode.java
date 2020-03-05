package ga.tomj.osccontrol.gui;

import java.awt.*;

public class Timecode extends UIElement {

    //Color used for element. Does not change, so final. Accessed without needing an instance of the class, so static.
    public static final Color COLOUR = new Color(255, 248, 0);

    private static String tc;

    private int sizeX;
    private int sizeY;

    public Timecode(int x, int y) {
        super(x, y);
        this.editable = true;
        if(tc == null) {
            this.tc = "0:00.000";
        }
        this.sizeX = 90;
        this.sizeY = 35;
    }

    //Render the timecode element.
    public void render() {
        app.stroke(COLOUR.getRed(), COLOUR.getGreen(), COLOUR.getBlue());
        app.strokeCap(app.ROUND);
        app.strokeWeight(2);
        app.rectMode(app.CENTER);
        app.textAlign(app.CENTER, app.CENTER);
        app.textSize(16);
        app.noFill();
        app.rect(x, y, sizeX, sizeY, 3);
        app.fill(255);
        app.text(tc, x, y - 2); //Y offset to manually center text.
    }

    public void mousePressed() {
        setOffsets();
    }

    //Check the relative position of the mouse with the central XY coords of the element. Return boolean if the mouse is in the element.
    public boolean mouseInElement() {
        return app.mouseX - x + sizeX / 2 >= 0 && app.mouseX - x + sizeX / 2 <= sizeX &&
                app.mouseY - y + sizeY / 2 >= 0 && app.mouseY - y + sizeY / 2 <= sizeY;
    }

    //Getters and setters.
    public static String getTc() {
        return tc;
    }

    public static void setTc(String timecode) {
        tc = timecode;
    }
}
