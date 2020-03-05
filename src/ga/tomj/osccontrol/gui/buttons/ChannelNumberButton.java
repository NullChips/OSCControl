package ga.tomj.osccontrol.gui.buttons;

import java.awt.*;

public class ChannelNumberButton extends Button {

    private boolean isUp;

    public ChannelNumberButton(int x, int y, boolean isUp) {
        super(x, y, 50, 35, "", new Color(192, 192, 192));
        this.isUp = isUp;
        if (isUp) {
            setLabel("Up");
        } else {
            setLabel("Down");
        }
    }

    public void mousePressed() {
        if (mouseInElement()) {
            int i;
            if (isUp) {
                try {
                    i = Integer.parseInt(ModeButton.getCurrentChannelBox().getText());
                } catch (NumberFormatException e) {
                    ModeButton.displayChannelErrorMessage();
                    System.out.println("Valid channel number not given!");
                    return;
                }
                ModeButton.removeChannelErrorMessage();
                ModeButton.setCurrentChannelNumber(i++);
            } else {
                try {
                    i = Integer.parseInt(ModeButton.getCurrentChannelBox().getText());
                } catch (NumberFormatException e) {
                    ModeButton.displayChannelErrorMessage();
                    System.out.println("Valid channel number not given!");
                    return;
                }
                ModeButton.removeChannelErrorMessage();
                if(i > 1) {
                    ModeButton.setCurrentChannelNumber(i--); //Prevents the channel number from going below 1.
                }
            }
            ModeButton.getCurrentChannelBox().setText(i + "");
        }
    }
}
