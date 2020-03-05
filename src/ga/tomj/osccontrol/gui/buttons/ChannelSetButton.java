package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.Fader;
import ga.tomj.osccontrol.gui.Pan;
import ga.tomj.osccontrol.gui.UIElement;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public class ChannelSetButton extends Button {

    //Constructor. Predefines button attributes other than x and y coords.
    public ChannelSetButton(int x, int y) {
        super(x, y, 120, 35, "Set", new Color(192, 192, 192));
    }

    //Run when mouse is pressed.
    public void mousePressed() {
        if (mouseInElement()) {
            //Check if the contents of the ChannelNumberTextBox is a number.
            int i;
            try {
                i = Integer.parseInt(ModeButton.getCurrentChannelBox().getText());
            } catch (NumberFormatException e) {
                //Contents of text box is not a number - warn the user.
                System.out.println("Valid channel number not given!");
                ModeButton.displayChannelErrorMessage();
                return; //Exit method.
            }

            UIElement recentElement = UIManager.getMgr().getRecentElement();

            //Set the channel number of the most recent element to the contents of the text box.
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

            //Reload all data from Reaper and reset ready for another element to be changed.
            app.reloadData();
            ModeButton.removeChannelErrorMessage();
            ModeButton.setCurrentChannelNumber(i);
            UIManager.getMgr().setRecentElement(null);
        }
    }
}
