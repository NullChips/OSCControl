package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.OSCControl;
import processing.core.PConstants;

public abstract class UIElement {

    protected int x, y;
    protected OSCControl app;
    private int xOffset;
    private int yOffset;
    protected boolean editable;

    protected UIElement(int x, int y) {
        this.x = x;
        this.y = y;
        app = OSCControl.getApp();
        UIManager.getMgr().getElements().add(this); //Add this UI element into the ArrayList of all UI elements.
        editable = false;
    }

    //This forces each type of UIElement object to have their own render method.
    public abstract void render();

    //This forces each type of UIElement object to have their own method when the mouse is pressed.
    public abstract void mousePressed();

    //This method will be used to check the relative position of the mouse to the position of the center of the element.
    //If the mouse position is inside the element, this returns true.
    public abstract boolean mouseInElement();

    public void removeElement() {
        UIManager.getMgr().getElements().remove(this);
        app.rectMode(PConstants.CENTER);
        app.rect(1, 2, 3, 5);
    }

    protected void setOffsets() {
        xOffset = app.mouseX - x;
        yOffset = app.mouseY - y;
    }

    public void mouseDragged() {
        if (UIManager.getMgr().getElementDragged() == this && UIManager.getMgr().isEditMode()) {
            x = app.mouseX - xOffset;
            y = app.mouseY - yOffset;
            return;
        } else if (UIManager.getMgr().isEditMode() && editable && mouseInElement() &&
                UIManager.getMgr().getElementDragged() == null) {
            UIManager.getMgr().setElementDragged(this);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
