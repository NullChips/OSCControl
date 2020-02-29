package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;
import oscP5.OscMessage;

import java.awt.*;

public class RecordArmButton extends Button {

    private int channelNumber;

    public static final Color COLOUR = new Color(137, 64, 255);

    public RecordArmButton(int x, int y, int channelNumber) {
        super(x, y, "R", new Color(137, 64, 255));
        this.channelNumber = channelNumber;
        this.editable = true;
    }

    public void mousePressed() {
        setOffsets();
        if (mouseInElement()) {
            UIManager.getMgr().setRecentElement(this);
            if (!UIManager.getMgr().isEditMode()) {
                OscMessage message = new OscMessage("/track/" + channelNumber + "/recarm");
                if (isPressed())
                    message.add(0.0F);
                else
                    message.add(1.0F);
                app.getOscp5().send(message, app.getReaperAddr());
            }
        }
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }
}
