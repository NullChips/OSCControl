package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.OSCControl;
import oscP5.OscMessage;

import java.awt.*;

public class SoloButton extends Button {

    int channelNumber;

    public SoloButton(int x, int y, int channelNumber) {
        super(x, y, "S", new Color(255, 178, 33));
        this.channelNumber = channelNumber;
    }

    public void mousePressed() {
        if(mouseInButton()) {
            OscMessage myMessage = new OscMessage("/track/" + channelNumber + "/solo");
            if (pressed)
                myMessage.add(0.0F);
            else
                myMessage.add(1.0F);
            OSCControl.getApp().getOscp5().send(myMessage, OSCControl.getApp().getReaperAddr());
        }
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }
}
