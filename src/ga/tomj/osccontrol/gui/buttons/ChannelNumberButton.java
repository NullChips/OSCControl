package ga.tomj.osccontrol.gui.buttons;

import java.awt.*;

public class ChannelNumberButton extends Button {

    private boolean isUp; //This boolean defines if the button should be used to raise or lower the channel number.

    //Constructor.
    public ChannelNumberButton(int x, int y, boolean isUp) {
        super(x, y, 50, 35, "", new Color(192, 192, 192));
        this.isUp = isUp;
        if (isUp) {
            setLabel("Up");
        } else {
            setLabel("Down");
        }
    }

    //When the mouse is pressed, check if the text in the textbox is a valid int. If so, adjust accordingly.
    public void mousePressed() {
        if (mouseInElement()) {
            int i;
            try {
                i = Integer.parseInt(ModeButton.getCurrentChannelBox().getText());
            } catch (NumberFormatException e) {
                //Display a message to the user if the text box does not contain a valid number.
                ModeButton.displayChannelErrorMessage();
                return; //Exit, no further action can be taken.
            }
            ModeButton.removeChannelErrorMessage();

            //Increase or decrease the number depending on the mode of the button.
            if (isUp) {
                ModeButton.setCurrentChannelNumber(i++);
            } else if (i > 1) {
                ModeButton.setCurrentChannelNumber(i--); //Prevents the channel number from going below 1.
            }
            ModeButton.getCurrentChannelBox().setText(i + ""); //Set the text box to contain the current number.
        }
    }
}
