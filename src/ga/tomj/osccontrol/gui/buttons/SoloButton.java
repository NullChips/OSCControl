package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;
import oscP5.OscMessage;

import java.awt.*;

public class SoloButton extends Button {

    //Color used for element. Does not change, so final. Accessed without needing an instance of the class, so static.
    public static final Color COLOUR = new Color(255, 178, 33);

    //Global variable.
    private int channelNumber;

    //Constructor. Predefines certain properties of the button using the super constructor of the Button object.
    public SoloButton(int x, int y, int channelNumber) {
        super(x, y, "S", COLOUR);
        this.channelNumber = channelNumber;
        editable = true;
    }

    //Called when the mouse is pressed.
    public void mousePressed() {
        setOffsets();
        if (mouseInElement()) {
            //Set the recent element. Used for changing channel numbers in edit mode.
            UIManager.getMgr().setRecentElement(this);
            if (!UIManager.getMgr().isEditMode()) {
                //Create and send an OSC message to Reaper to solo the relevant track.
                OscMessage message = new OscMessage("/track/" + channelNumber + "/solo");
                if (isPressed())  //If the button is already pressed, tell Reaper to unsolo the channel.
                    message.add(0.0F);
                else //Tell Reaper to solo the channel.
                    message.add(1.0F);
                app.getOscp5().send(message, app.getReaperAddr()); //Send the message.
            }
        }
    }

    //Getters and setters.
    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }
}
