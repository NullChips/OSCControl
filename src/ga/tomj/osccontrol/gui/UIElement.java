package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.OSCControl;

public abstract class UIElement {

    /*
    Abstract UIElement class to allow everything drawn on the window to follow a default templte. This will be inherited
    by all classes which draw something on the screen.
    */

    //Global variables.
    protected int x, y;
    protected OSCControl app; //This is required to access the Processing methods inherited by PApplet in the main class.
    private int xOffset;
    private int yOffset;
    protected boolean editable;
    protected AppMode mode;

    //Constructors.
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

    //Constructor that allows AppMode to be defined.
    protected UIElement(int x, int y, AppMode mode) {
        this.x = x;
        this.y = y;
        this.mode = mode;
        app = OSCControl.getApp();
        if(x > -1000 && y > -1000) {
            UIManager.getMgr().getElements().add(this);
        }
        editable = false;
    }

    //This forces each type of UIElement object to have their own render method.
    public abstract void render();

    //This forces each type of UIElement object to have their own method when the mouse is pressed.
    public abstract void mousePressed();

    //This method will be used to check the relative position of the mouse to the position of the center of the element.
    //If the mouse position is inside the element, this returns true.
    public abstract boolean mouseInElement();

    //Set the offsets of where the mouse is relative to the XY coords of the element. Prevents snapping to mouse when dragging.
    protected void setOffsets() {
        xOffset = app.mouseX - x;
        yOffset = app.mouseY - y;
    }

    public void mouseDragged() {
        if (UIManager.getMgr().getElementDragged() == this && UIManager.getMgr().isEditMode()) {
            //Drag the element around when in edit mode.
            x = app.mouseX - xOffset;
            y = app.mouseY - yOffset;
            return;
        } else if (UIManager.getMgr().isEditMode() && editable && mouseInElement() &&
                UIManager.getMgr().getElementDragged() == null) {
            //Set the element that's being dragged to this element. Prevents multiple items being moved at once.
            UIManager.getMgr().setElementDragged(this);
        }
    }

    //Checks if delete mode is on. If it is and the element is clicked, remove it.
    public void checkAndDelete() {
        if(UIManager.getMgr().isDeleteMode() && UIManager.getMgr().isEditMode() && editable && mouseInElement()) {
            UIManager.getMgr().getElements().remove(this);
        }
    }

    //Double click method, defaults to a normal mouse press. Can be overridden by subclasses.
    public void doubleClick() {
        mousePressed();
    }

    //Key pressed method, defaults to nothing. Can be overridden by subclasses.
    public void keyPressed(char key) {

    }

    //Getters.
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
