package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.OSCControl;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public enum LayoutButtonType {

    /*
    This enum defines what each type of button in the 'layout' panel in edit mode should do when clicked. It also
    defines the colour and text of each button.
     */

    DELETE_MODE("Delete Mode", new Color(255, 0, 0)) {
        public void mousePressed() {
            UIManager.getMgr().setDeleteMode(!UIManager.getMgr().isDeleteMode()); //Enable delete mode.
            UIManager.getMgr().setRecentElement(null); //Clear the recently clicked element.
        }
    }, SAVE_LAYOUT("Save Layout", new Color(192, 192, 192)) {
        public void mousePressed() {
            OSCControl.getApp().saveLayout(); //Run main saveLayout method.
        }
    }, LOAD_LAYOUT("Load Layout", new Color(192, 192, 192)) {
        public void mousePressed() {
            OSCControl.getApp().loadLayout(); //Run main loadLayout method.
        }
    };

    //Global variables.
    private Color colour;
    private String label;

    //This method must be defined by each enum type and is called when the LayoutButton is clicked.
    public abstract void mousePressed();

    LayoutButtonType(String label, Color colour) {
        this.label = label;
        this.colour = colour;
    }

    //Getters.
    public Color getColour() {
        return colour;
    }

    public String getLabel() {
        return label;
    }
}
