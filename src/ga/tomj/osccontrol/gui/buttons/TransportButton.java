package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;
import oscP5.OscMessage;

public class TransportButton extends Button {

    private TransportButtonType transportButtonType;

    public TransportButton(int x, int y, TransportButtonType transportButtonType) {
        super(x, y, 50, 35, transportButtonType.getButtonName(), transportButtonType.getColour());
        this.transportButtonType = transportButtonType;
        this.editable = true;
    }

    public void mousePressed() {
        setOffsets();
        if (mouseInElement()) {
            if (!UIManager.getMgr().isEditMode()) {
                OscMessage message = new OscMessage(transportButtonType.getOscMessage());
                app.getOscp5().send(message, app.getReaperAddr());
            }
        }
    }

    public TransportButtonType getTransportButtonType() {
        return transportButtonType;
    }
}
