package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.Fader;
import ga.tomj.osccontrol.gui.Pan;
import ga.tomj.osccontrol.gui.Timecode;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public enum ElementType {

    /*
    This enum defines each element that can be added to the app in edit mode. Each enum type defines the name of this
    element used to display on the AddElementButton, the colour of it and what should happen when that button is clicked -
    mouseClicked() - most all mouseClicked() methods create a new instance of an object. If this object has an assignable
    channel number, this is set as the most recent element, ready to be assigned a channel by the user.
     */

    MUTE_BUTTON("Mute Button", MuteButton.COLOUR) {
        public void mouseClicked() {
            MuteButton m = new MuteButton(x, y, 1);
            UIManager.getMgr().setRecentElement(m);
        }
    }, SOLO_BUTTON("Solo Button", SoloButton.COLOUR) {
        public void mouseClicked() {
            SoloButton s = new SoloButton(x, y, 1);
            UIManager.getMgr().setRecentElement(s);
        }
    }, RECORD_BUTTON("Rec. Arm Button", RecordArmButton.COLOUR) {
        public void mouseClicked() {
            RecordArmButton r = new RecordArmButton(x, y, 1);
            UIManager.getMgr().setRecentElement(r);
        }
    }, PAN("Pan", new Color(185, 185, 185)) {
        public void mouseClicked() {
            Pan p = new Pan(x, y, 1);
            UIManager.getMgr().setRecentElement(p);
        }
    }, FADER("Fader", new Color(128,128,128)) {
        public void mouseClicked() {
            Fader f = new Fader(x, y + 100, 1);
            UIManager.getMgr().setRecentElement(f);
        }
    }, TIMECODE("Time", Timecode.COLOUR) {
        public void mouseClicked() {
            Timecode tc = new Timecode(x, y);
            UIManager.getMgr().setRecentElement(null); //No assignable channel number, so remove object previously being edited.
        }
    },PLAY_BUTTON("Play Button", TransportButtonType.PLAY.getColour()) {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.PLAY);
            UIManager.getMgr().setRecentElement(null); //No assignable channel number, so remove object previously being edited.
        }
    }, CLICK_BUTTON("Click Button", TransportButtonType.CLICK.getColour()) {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.CLICK);
            UIManager.getMgr().setRecentElement(null); //No assignable channel number, so remove object previously being edited.
        }
    }, LOOP_BUTTON("Loop Button", TransportButtonType.LOOP.getColour()) {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.LOOP);
            UIManager.getMgr().setRecentElement(null); //No assignable channel number, so remove object previously being edited.
        }
    };

    //These are the coordinates that are used to create the new element.
    private static final int x = 30;
    private static final int y = 100;

    //The mouseClicked() method that must be defined for each type of element.
    public abstract void mouseClicked();

    //Global variables.
    private String label;
    private Color color;

    //Constructor.
    ElementType(String label, Color colour) {
        this.label = label;
        this.color = colour;
    }

    //Getters.
    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }
}


