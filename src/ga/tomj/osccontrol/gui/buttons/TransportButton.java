package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;
import oscP5.OscMessage;

public class TransportButton extends Button {

    private TransportType transportType;

    public TransportButton(int x, int y, TransportType transportType) {
        super(x, y, 50, 35, transportType.getButtonName(), transportType.getColour());
        this.transportType = transportType;
        this.editable = true;
    }

    public void mousePressed() {
        setOffsets();
        if(!UIManager.getMgr().isEditMode() && mouseInElement()) {
            OscMessage message = new OscMessage(transportType.getOscMessage());
            app.getOscp5().send(message, app.getReaperAddr());
        }
    }

    public TransportType getTransportType() {
        return transportType;
    }
}
