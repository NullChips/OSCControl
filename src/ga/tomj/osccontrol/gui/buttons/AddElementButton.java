package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public class AddElementButton extends Button {

    public AddElementButton(int x, int y, String label, Color colour) {
        super(x, y, label, colour);
    }

    public void mousePressed() {
        if(mouseInElement()) {
            UIManager.getMgr().setRecentElement(this);
        }
    }
}
