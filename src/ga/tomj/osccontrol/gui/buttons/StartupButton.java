package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;

public class StartupButton extends Button {

    private StartupButtonType type;

    public StartupButton(int x, int y, StartupButtonType type) {
        super(x, y, 100, 35, type.getButtonName(), type.getColour());
        this.mode = AppMode.STARTUP;
        this.type = type;
    }

    public void mousePressed() {
        if(mouseInElement()) {
            type.mousePressed();
        }
    }
}
