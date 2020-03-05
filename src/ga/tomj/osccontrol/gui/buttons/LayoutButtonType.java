package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.OSCControl;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public enum LayoutButtonType {

    DELETE_MODE("Delete Mode", new Color(255, 0, 0)) {
        public void mousePressed() {
            UIManager.getMgr().setDeleteMode(!UIManager.getMgr().isDeleteMode());
            UIManager.getMgr().setRecentElement(null);
        }
    }, SAVE_LAYOUT("Save Layout", new Color(192, 192, 192)) {
        public void mousePressed() {
            OSCControl.getApp().saveLayout();
        }
    }, LOAD_LAYOUT("Load Layout", new Color(192, 192, 192)) {
        public void mousePressed() {
            OSCControl.getApp().loadLayout();
        }
    };

    private Color colour;
    private String label;

    public abstract void mousePressed();

    LayoutButtonType(String label, Color colour) {
        this.label = label;
        this.colour = colour;
    }

    public Color getColour() {
        return colour;
    }

    public String getLabel() {
        return label;
    }
}
