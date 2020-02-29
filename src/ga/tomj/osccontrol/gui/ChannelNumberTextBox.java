package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.gui.buttons.ModeButton;
import processing.core.PConstants;

import java.awt.*;

public class ChannelNumberTextBox extends TextBox {
    public ChannelNumberTextBox(int x, int y) {
        super(x, y, 50, 50, 20, new Color(192, 192, 192), new Color(0, 0, 0), "", AppMode.RUN);
    }

    public void keyPressed(char key) {
        if (isCurrentlyEditing) {
            //As the switch case requires a constant, these values are taken from the PConstants class instead of the app object.
            switch (key) {
                case PConstants.ESC:
                case PConstants.ENTER:
                case PConstants.RETURN:
                    if (validChannel()) {
                        isCurrentlyEditing = false;
                        ModeButton.removeChannelErrorMessage();
                        int i = Integer.parseInt(ModeButton.getCurrentChannelBox().getText());
                        ModeButton.setCurrentChannelNumber(i);
                        System.out.println("Set number to: " + i);
                    } else {
                        ModeButton.displayChannelErrorMessage();
                        System.out.println("There was an error!");
                    }
                    break;
                case PConstants.BACKSPACE:
                    text = text.substring(0, Math.max(0, text.length() - 1));
                    break;
                default:
                    if (app.textWidth(text + app.str(key) + "|") <= sizeX - padding) {
                        text += app.str(key);
                    }

            }
        }
    }

    private boolean validChannel() {
        try {
            int i = Integer.parseInt(ModeButton.getCurrentChannelBox().getText());
        } catch (NumberFormatException e) {
            System.out.println("Valid channel number not given!");
            return false;
        }
        return true;
    }

    public void mousePressed() {
        isCurrentlyEditing = mouseInElement();
    }

    public void setText(String text) {
        this.text = text;
    }

}
