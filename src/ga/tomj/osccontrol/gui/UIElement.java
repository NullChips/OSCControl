package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.OSCControl;
import processing.core.PConstants;

public abstract class UIElement {

    protected int x, y;
    protected OSCControl app;
    private int xOffset;
    private int yOffset;
    protected boolean editable;
    protected AppMode mode;

    protected UIElement(int x, int y) {
        this.x = x;
        this.y = y;
        this.mode = AppMode.RUN; //Default to RUN AppMode if none is specified.
        app = OSCControl.getApp();
        if(x > -1000 && y > -1000) { //Checks against default values given when reading XML layouts.
            UIManager.getMgr().getElements().add(this); //Add this UI element into the ArrayList of all UI elements.
        }
        editable = false;
    }

    protected UIElement(int x, int y, AppMode mode) {
        this.x = x;
        this.y = y;
        this.mode = mode;
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

    public void checkAndDelete() {
        if(UIManager.getMgr().isDeleteMode() && UIManager.getMgr().isEditMode() && editable && mouseInElement()) {
            UIManager.getMgr().getElements().remove(this);
        }
    }

    public void doubleClick() {
        mousePressed();
    }

    public void keyPressed(char key) {

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public AppMode getMode() {
        return mode;
    }
}
