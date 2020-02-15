package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.OSCControl;
import ga.tomj.osccontrol.gui.UIManager;
import oscP5.OscMessage;

import java.awt.*;

public class SoloButton extends Button {

    private int channelNumber;

    public SoloButton(int x, int y, int channelNumber) {
        super(x, y, "S", new Color(255, 178, 33));
        this.channelNumber = channelNumber;
        editable = true;
    }

    public void mousePressed() {
        setOffsets();
        if(mouseInElement() && !UIManager.getMgr().isEditMode()) {
            OscMessage myMessage = new OscMessage("/track/" + channelNumber + "/solo");
            if (isPressed())
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
