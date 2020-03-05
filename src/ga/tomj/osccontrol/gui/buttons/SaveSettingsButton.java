package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.gui.Text;

import java.awt.*;

public class SaveSettingsButton extends Button {

    //Constructor. Predefined arguments given into super class.
    public SaveSettingsButton(int x, int y) {
        super(x, y, 150, 35, "Save Settings", new Color(128, 128, 128));
        this.mode = AppMode.SETTINGS;
    }

    public void mousePressed() {
        if (mouseInElement()) {
            if (app.connectOSC()) { //app.connectOSC() returns true if the settings entered are valid.
                app.drawStartupScreen(); //Draws startup screen elements to return to the startup screen.
            } else { //Settings are not valid.
                app.drawSettingsScreen(); //Redraw the settings screen.
                Text error = new Text((app.width / 2), (app.height / 2) - 100, 12, "Error, please check your settings.",
                        new Color(255, 0, 0), AppMode.SETTINGS); //Inform the user that there has been an error.
            }
        }
    }
}
