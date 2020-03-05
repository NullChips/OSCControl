package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.Fader;
import ga.tomj.osccontrol.gui.Pan;
import ga.tomj.osccontrol.gui.UIElement;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public class ChannelSetButton extends Button {

    public ChannelSetButton(int x, int y) {
        super(x, y, 120, 35, "Set", new Color(192, 192, 192));
    }

    public void mousePressed() {
        if (mouseInElement()) {
            int i;
            try {
                i = Integer.parseInt(ModeButton.getCurrentChannelBox().getText());
            } catch (NumberFormatException e) {
                System.out.println("Valid channel number not given!");
                ModeButton.displayChannelErrorMessage();
                return;
            }

            UIElement recentElement = UIManager.getMgr().getRecentElement();

            if (recentElement instanceof SoloButton) {
                SoloButton e = (SoloButton) recentElement;
                e.setChannelNumber(i);
            } else if (recentElement instanceof MuteButton) {
                MuteButton e = (MuteButton) recentElement;
                e.setChannelNumber(i);
            } else if (recentElement instanceof RecordArmButton) {
                RecordArmButton e = (RecordArmButton) recentElement;
                e.setChannelNumber(i);
            } else if (recentElement instanceof Fader) {
                Fader e = (Fader) recentElement;
                e.setChannelNumber(i);
            } else if (recentElement instanceof Pan) {
                Pan e = (Pan) recentElement;
                e.setChannelNumber(i);
            }

            app.reloadData();
            ModeButton.removeChannelErrorMessage();
            ModeButton.setCurrentChannelNumber(i);
            UIManager.getMgr().setRecentElement(null);
        }
    }
}
