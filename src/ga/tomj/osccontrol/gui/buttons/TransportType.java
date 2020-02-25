package ga.tomj.osccontrol.gui.buttons;

import java.awt.*;

public enum TransportType {
    PLAY("Play", "/play", new Color(54, 218, 255)),
    LOOP("Loop", "/repeat", new Color(120, 255, 76)),
    CLICK("Click", "/click", new Color(78, 102, 255));

    private String buttonName;
    private String oscMessage;
    private Color colour;

    TransportType(String buttonName, String oscMessage, Color colour) {
        this.buttonName = buttonName;
        this.oscMessage = oscMessage;
        this.colour = colour;
    }

    public Color getColour() {
        return colour;
    }

    public String getOscMessage() {
        return oscMessage;
    }

    public String getButtonName() {
        return buttonName;
    }

}
