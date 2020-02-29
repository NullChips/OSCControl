package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.Fader;
import ga.tomj.osccontrol.gui.Pan;
import ga.tomj.osccontrol.gui.Timecode;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public enum ElementType {
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
        }
    },PLAY_BUTTON("Play Button", TransportButtonType.PLAY.getColour()) {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.PLAY);
            UIManager.getMgr().setRecentElement(t);
        }
    }, CLICK_BUTTON("Click Button", TransportButtonType.CLICK.getColour()) {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.CLICK);
            UIManager.getMgr().setRecentElement(t);
        }
    }, LOOP_BUTTON("Loop Button", TransportButtonType.LOOP.getColour()) {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.LOOP);
            UIManager.getMgr().setRecentElement(t);
        }
    };

    private static final int x = 30;
    private static final int y = 100;

    public abstract void mouseClicked();

    private String label;
    private Color color;

    ElementType(String label, Color colour) {
        this.label = label;
        this.color = colour;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }
}


