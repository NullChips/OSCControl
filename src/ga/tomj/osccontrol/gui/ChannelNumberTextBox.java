package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.gui.buttons.ModeButton;
import ga.tomj.osccontrol.gui.buttons.MuteButton;
import ga.tomj.osccontrol.gui.buttons.RecordArmButton;
import ga.tomj.osccontrol.gui.buttons.SoloButton;
import processing.core.PConstants;

import java.awt.*;

public class ChannelNumberTextBox extends TextBox {

    /*
    An instance of this class is used in edit mode to specify which channel an elemenet should be assigned. This is an
    extension of the textbox class, with a couple of slight modifications.
     */

    //Constructor. Most values which need to be given to the super class are pre-defined as this is only called once.
    public ChannelNumberTextBox(int x, int y) {
        super(x, y, 50, 50, 20, new Color(192, 192, 192), new Color(0, 0, 0), "", AppMode.RUN);
    }

    //When a key is pressed.
    public void keyPressed(char key) {
        if (isCurrentlyEditing) {
            //As the switch case requires a constant, these values are taken from the PConstants class instead of the app object.
            switch (key) {
                //When esc, enter or return are used, take the input of the box as being entered.
                case PConstants.ESC:
                case PConstants.ENTER:
                case PConstants.RETURN:
                    if (isValidChannel()) { //Check the number is an integer.
                        isCurrentlyEditing = false;
                        int i = Integer.parseInt(ModeButton.getCurrentChannelBox().getText());
                        if(i > 0) {
                            ModeButton.removeChannelErrorMessage();

                            UIElement recentElement = UIManager.getMgr().getRecentElement();

                            //Set the channel number of the relevant element.
                            if (recentElement instanceof SoloButton) {
                                SoloButton e = (SoloButton) recentElement;
                                e.setChannelNumber(i);
                            } else if (recentElement instanceof MuteButton) {
                                MuteButton e = (MuteButton) recentElement;
                                e.setChannelNumber(i);
                            } else if (recentElement instanceof RecordArmButton) {
                                RecordArmButton e = (RecordArmButton) recentElement;
                                e.setChannelNumber(i);
                            } else if (recentElement instanceof Fader) {
                                Fader e = (Fader) recentElement;
                                e.setChannelNumber(i);
                            } else if (recentElement instanceof Pan) {
                                Pan e = (Pan) recentElement;
                                e.setChannelNumber(i);
                            }

                            //Reload all data and reset window to previous state.
                            app.reloadData();
                            ModeButton.removeChannelErrorMessage();
                            ModeButton.setCurrentChannelNumber(i);
                            UIManager.getMgr().setRecentElement(null);
                        }
                    } else {
                        //If not a number, display an error message.
                        ModeButton.displayChannelErrorMessage();
                    }
                    break;
                case PConstants.BACKSPACE:
                    //If backspace is pressed, delete a character.
                    text = text.substring(0, Math.max(0, text.length() - 1));
                    break;
                default:
                    if (text.length() < 3) {
                        //Check if the text is too big to fit inside the text box. If it isn't, then add the character that was pressed.
                        text += app.str(key);
                    }
            }
        }
    }

    //Check if the string is a valid number.
    private boolean isValidChannel() {
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
