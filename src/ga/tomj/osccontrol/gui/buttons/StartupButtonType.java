package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.OSCControl;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public enum StartupButtonType {
    NEW_LAYOUT("New Layout", new Color(128,128,128), -50) {
        public void mousePressed() {
            app.setMode(AppMode.RUN);
            ModeButton mo = new ModeButton(85, 25);
            app.getSurface().setSize(app.width, app.height + 200);
            app.getSurface().setResizable(false);
            mo.drawEditButtons();
            UIManager.getMgr().setEditMode(true);
        }
    }, LOAD_LAYOUT("Load Layout", new Color(128,128,128), 0) {
        public void mousePressed() {
            app.loadLayout();
        }
    }, SETTINGS("Settings", new Color(128,128,128), 50) {
        public void mousePressed() {
            app.setMode(AppMode.SETTINGS);
            app.drawSettingsScreen();
        }
    };

    public abstract void mousePressed();

    private String buttonName;
    private Color colour;
    private int yOffset;
    protected OSCControl app;

    StartupButtonType(String buttonName, Color colour, int yOffset) {
        this.buttonName = buttonName;
        this.colour = colour;
        this.yOffset = yOffset;
        this.app = OSCControl.getApp();
    }

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
