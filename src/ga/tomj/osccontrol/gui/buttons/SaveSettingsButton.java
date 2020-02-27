package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.gui.Text;

import java.awt.*;

public class SaveSettingsButton extends Button {

    public SaveSettingsButton(int x, int y) {
        super(x, y, 150, 35, "Save Settings", new Color(128, 128, 128));
        this.mode = AppMode.SETTINGS;
    }

    public void mousePressed() {
        if (mouseInElement()) {
            if (app.connectOSC()) {
                app.drawStartupScreen();
            } else {
                app.drawSettingsScreen();
                Text error = new Text((app.width / 2), (app.height / 2) - 100, 12, "Error, please check your settings.",
                        new Color(255, 0, 0), AppMode.SETTINGS);
            }
        }
    }
}
