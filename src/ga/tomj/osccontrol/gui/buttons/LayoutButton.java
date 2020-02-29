package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;

public class LayoutButton extends Button {

    private LayoutButtonType type;

    public LayoutButton(int x, int y, LayoutButtonType type) {
        super(x, y, 85, 35, type.getLabel(), type.getColour());
        this.type = type;
    }

    public void mousePressed() {
        if (mouseInElement()) {
            type.mousePressed();
        }
    }

    public void render() {
        super.render();
        if (type == LayoutButtonType.DELETE_MODE) {
            if (UIManager.getMgr().isDeleteMode()) {
                setPressed(app.frameCount / 10 % 2 == 0); //Make delete mode button flash when delete mode is active.
            } else {
                setPressed(false);
            }
        }
    }
}
