package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.gui.UIManager;

public class LayoutButton extends Button {

    //LayoutButtonType defines how the button should be drawn and what should happen when
    private LayoutButtonType type;

    public LayoutButton(int x, int y, LayoutButtonType type) {
        super(x, y, 85, 35, type.getLabel(), type.getColour());
        this.type = type;
    }

    //When the mouse is pressed, run the relevant mousePressed() method for the LayoutButtonType.
    public void mousePressed() {
        if (mouseInElement()) {
            type.mousePressed();
        }
    }


    //Overrides normal render method to allow the delete mode button to flash when active.
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
                //Set the button to be pressed (filled with colour) if the modulo 2 of the frame count divided by ten is 0. (Flashes every 10 frames).
                setPressed(app.frameCount / 10 % 2 == 0);
            } else {
                //Delete mode isn't active, so don't fill the button.
                setPressed(false);

            }
        }
    }
}

