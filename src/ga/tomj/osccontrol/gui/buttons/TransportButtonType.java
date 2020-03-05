package ga.tomj.osccontrol.gui.buttons;

import java.awt.*;

public enum TransportButtonType {
    /*
    Enum that specifies the color, OSC address and name of the button for each type of transport button. Each TransportButton
    object is assigned a type in the constructor.
     */

    //Enum states.
    PLAY("Play", "/play", new Color(54, 218, 255)),
    LOOP("Loop", "/repeat", new Color(120, 255, 76)),
    CLICK("Click", "/click", new Color(78, 102, 255));

    //Global variables.
    private String buttonName;
    private String oscMessage;
    private Color colour;

    //Constructor
    TransportButtonType(String buttonName, String oscMessage, Color colour) {
        this.buttonName = buttonName;
        this.oscMessage = oscMessage;
        this.colour = colour;
    }

    //Getters.
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
