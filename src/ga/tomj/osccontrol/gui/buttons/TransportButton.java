package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;
import oscP5.OscMessage;

public class TransportButton extends Button {

    //TransportButtonType specifies what transport the button should perform.
    private TransportButtonType transportButtonType;

    //Constructor, gives pre-defined values into the super class and requests a TransportButtonType is specified.
    public TransportButton(int x, int y, TransportButtonType transportButtonType) {
        super(x, y, 50, 35, transportButtonType.getButtonName(), transportButtonType.getColour());
        this.transportButtonType = transportButtonType;
        this.editable = true;
    }

    //When the mouse is pressed, send an OSC message which has the address given in TransportButtonType.
    public void mousePressed() {
        setOffsets();
        if (mouseInElement()) {
            if (!UIManager.getMgr().isEditMode()) {
                OscMessage message = new OscMessage(transportButtonType.getOscMessage());
                app.getOscp5().send(message, app.getReaperAddr());
            }
        }
    }

    //Getters.
    public TransportButtonType getTransportButtonType() {
        return transportButtonType;
    }
}
