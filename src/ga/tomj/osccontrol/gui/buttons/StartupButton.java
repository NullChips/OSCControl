package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;

public class StartupButton extends Button {

    //This StartupButtonType defines how the StartupButton should be drawn and interacted with.
    private StartupButtonType type;

    public StartupButton(int x, int y, StartupButtonType type) {
        super(x, y, 100, 35, type.getButtonName(), type.getColour());
        this.mode = AppMode.STARTUP;
        this.type = type;
    }

    public void mousePressed() {
        if (mouseInElement()) {
            //Run the mousePressed() method defined in the StartupButtonType enum.
            type.mousePressed();
        }
    }

    public void updateCoordinates() {
        //Update the coordinates when the startup window is resized - this keeps each button central in the screen.
        x = app.width / 2;
        y = (app.height / 2) + type.getYOffset();
    }

}
