package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.OSCControl;
import ga.tomj.osccontrol.gui.UIManager;
import oscP5.OscMessage;

import java.awt.*;

public class MuteButton extends Button {

    private int channelNumber;

    public MuteButton(int x, int y, int channelNumber) {
        super(x, y, "M", new Color(255, 0, 0));
        this.channelNumber = channelNumber;
        editable = true;
    }

    public void mousePressed() {
        setOffsets();
        if (mouseInElement() && !UIManager.getMgr().isEditMode()) {
            OscMessage message = new OscMessage("/track/" + channelNumber + "/mute");
            if (isPressed())
                message.add(0.0F);
            else
                message.add(1.0F);
            app.getOscp5().send(message, app.getReaperAddr());
        }
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }
}