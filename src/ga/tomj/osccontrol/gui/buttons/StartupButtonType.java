package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.OSCControl;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public enum StartupButtonType {

    /*
    Enum that specifies each type of button shown on the startup screen. Each enum has an abstract method that must be
    defined. This method is ran when the button is pressed.
     */

    NEW_LAYOUT("New Layout", new Color(128,128,128), -50) {
        public void mousePressed() {
            //Sets the window into a blank layout, with only a mode button present. Ready for other elements to be added.
            app.setMode(AppMode.RUN);
            UIManager.getMgr().setEditMode(true);
            ModeButton mo = new ModeButton(85, 25);
            mo.setPressed(true);
            app.getSurface().setSize(app.width, app.height + 200);
            app.getSurface().setResizable(false);
            mo.drawEditButtons();

        }
    }, LOAD_LAYOUT("Load Layout", new Color(128,128,128), 0) {
        public void mousePressed() {
            app.loadLayout();
        }
    }, SETTINGS("Settings", new Color(128,128,128), 50) {
        public void mousePressed() {
            //Open the settings window.
            app.setMode(AppMode.SETTINGS);
            app.drawSettingsScreen();
        }
    };

    //Abstract method that must be defined by each enum type.
    public abstract void mousePressed();

    //Global variables.
    private String buttonName;
    private Color colour;
    private int yOffset;
    protected OSCControl app;

    //Constructor.
    StartupButtonType(String buttonName, Color colour, int yOffset) {
        this.buttonName = buttonName;
        this.colour = colour;
        this.yOffset = yOffset;
        this.app = OSCControl.getApp();
    }

    //Getters.
    public String getButtonName() {
        return buttonName;
    }

    public Color getColour() {
        return colour;
    }

    public int getYOffset() {
        return yOffset;
    }
}
