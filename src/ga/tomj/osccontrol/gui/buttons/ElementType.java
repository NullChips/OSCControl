package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.Fader;
import ga.tomj.osccontrol.gui.Pan;
import ga.tomj.osccontrol.gui.UIManager;

import javax.swing.text.Element;

public enum ElementType {
    MUTE_BUTTON("Mute Button") {
        public void mouseClicked() {
            MuteButton m = new MuteButton(x, y, 1);
            UIManager.getMgr().setRecentElement(m);
        }
    }, SOLO_BUTTON("Solo Button") {
        public void mouseClicked() {
            SoloButton s = new SoloButton(x, y, 1);
            UIManager.getMgr().setRecentElement(s);
        }
    }, PAN("Pan") {
        public void mouseClicked() {
            Pan p = new Pan(x, y, 1);
            UIManager.getMgr().setRecentElement(p);
        }
    }, FADER("Fader") {
        public void mouseClicked() {
            Fader f = new Fader(x, y + 100, 1);
            UIManager.getMgr().setRecentElement(f);
        }
    }, PLAY_BUTTON("Play Button") {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.PLAY);
            UIManager.getMgr().setRecentElement(t);
        }
    }, CLICK_BUTTON("Click Button") {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.CLICK);
            UIManager.getMgr().setRecentElement(t);
        }
    }, LOOP_BUTTON("Loop Button") {
        public void mouseClicked() {
            TransportButton t = new TransportButton(x, y, TransportButtonType.LOOP);
            UIManager.getMgr().setRecentElement(t);
        }
    };

    private static final int x = 30;
    private static final int y = 30;

    public abstract void mouseClicked();

    private String label;

    ElementType(String label) {
        this.label = label;
    }

}


