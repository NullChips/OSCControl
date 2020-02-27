package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.OSCControl;

import java.awt.*;

public enum StartupButtonType {
    NEW_LAYOUT("New Layout", new Color(128,128,128)) {
        public void mousePressed() {
            app.setMode(AppMode.RUN);
            app.drawTestLayout();
        }
    }, LOAD_LAYOUT("Load Layout", new Color(128,128,128)) {
        public void mousePressed() {
            //TODO Implement loading layouts.
        }
    }, SETTINGS("Settings", new Color(128,128,128)) {
        public void mousePressed() {
            app.setMode(AppMode.SETTINGS);
            //TODO Draw settings page.
        }
    };

    public abstract void mousePressed();

    private String buttonName;
    private Color colour;
    protected OSCControl app;

    StartupButtonType(String buttonName, Color colour) {
        this.buttonName = buttonName;
        this.colour = colour;
        this.app = OSCControl.getApp();
    }

    public String getButtonName() {
        return buttonName;
    }

    public Color getColour() {
        return colour;
    }
}
