package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public class ModeButton extends Button {

    public ModeButton(int x, int y) {
        super(x, y, 140, 35, "Edit M ode", new Color(0, 255, 255));
        setPressed(true);
        if (UIManager.getMgr().isEditMode()) {
            setLabel("Change to Play Mode");
            setColour(new Color(0, 255, 255));
        } else {
            setLabel("Change to Edit Mode");
            setColour(new Color(255, 0, 255));
        }

    }

    public void mousePressed() {
        if (mouseInElement()) {
            UIManager.getMgr().setEditMode(!UIManager.getMgr().isEditMode());
            if (UIManager.getMgr().isEditMode()) {
                setLabel("Change to Play Mode");
                setColour(new Color(0, 255, 255));
            } else {
                setLabel("Change to Edit Mode");
                setColour(new Color(255, 0, 255));
                app.reloadData();
            }
        }
    }
}