package ga.tomj.osccontrol.gui.buttons;

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
            ModeButton.removeChannelErrorMessage();
            ModeButton.setCurrentChannelNumber(i);
            System.out.println("Set number to: " + i);
        }
    }
}
