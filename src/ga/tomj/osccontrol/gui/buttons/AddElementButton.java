package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;

public class AddElementButton extends Button {

    private ElementType element;

    public AddElementButton(int x, int y, ElementType element) {
        super(x, y, 110, 35, element.getLabel(), element.getColor());
        this.element = element;
        setPressed(true);
    }

    public AddElementButton(int x, int y, int sizeX, int sizeY, ElementType element) {
        super(x, y, sizeX, sizeY, element.getLabel(), element.getColor());
        this.element = element;
        setPressed(true);
    }

    public void mousePressed() {
        if (mouseInElement()) {
            UIManager.getMgr().setRecentElement(this);
            element.mouseClicked();
        }
    }
}
