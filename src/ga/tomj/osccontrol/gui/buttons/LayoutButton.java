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


    //Overrides normal render method to allow the buton to flash.
    public void render() {
        if (mouseInElement() && UIManager.getMgr().getElementDragged() == null) { //When the mouse is hovering over a button, a white stroke is drawn.
            if (isPressed()) { //If the button is pressed, render the button with a fill and a white stroke.
                app.fill(colour.getRed() / 2, colour.getGreen() / 2, colour.getBlue() / 2);
                drawWhiteBorderRect();
            } else { //If the button is not pressed, render the button without a fill and a white stroke.
                app.noFill();
                drawWhiteBorderRect();
            }
            return; //Exit - the button has been rendered!
        } else if (isPressed()) { //If the button has been pressed, render the button with a fill.
            app.fill(colour.getRed() / 2, colour.getGreen() / 2, colour.getBlue() / 2);
            drawRect();
        } else { //If the button has not been pressed, render the button without a fill.
            app.noFill();
            drawRect();
        }
        if (type == LayoutButtonType.DELETE_MODE) {
            if (UIManager.getMgr().isDeleteMode()) {
                setPressed(app.frameCount / 10 % 2 == 0);
            } else {
                setPressed(false);

            }
        }
    }
}

