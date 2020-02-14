package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.OSCControl;
import processing.core.PConstants;

public abstract class UIElement {

    protected int x, y;
    protected OSCControl app;

    protected UIElement(int x, int y) {
        this.x = x;
        this.y = y;
        app = OSCControl.getApp();
        UIManager.getMgr().getElements().add(this); //Add this UI element into the ArrayList of all UI elements.
    }

    //This forces each type of UIElement object to have their own render method.
    public abstract void render();

    public abstract void mousePressed();

    public void removeElement() {
        UIManager.getMgr().getElements().remove(this);
        app.rectMode(PConstants.CENTER);
        app.rect(1, 2, 3, 5);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
