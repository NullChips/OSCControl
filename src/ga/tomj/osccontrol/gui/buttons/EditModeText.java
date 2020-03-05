package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.gui.Text;

import java.awt.*;

public class EditModeText extends Text {

    /*
    This object is used in EditMode to draw the labels for each section. This class is a simple extension of Text,
    with some values predefined.
     */

    //Constructor, with default text alignment.
    public EditModeText(int x, int y, int textSize, String text) {
        super(x, y, textSize, text, new Color(255,255,255), AppMode.RUN);
    }

    //Constructor with customisable text alignment.
    public EditModeText(int x, int y, int textSize, String text, int xAlign, int yAlign) {
        super(x, y, textSize, text, xAlign, yAlign, new Color(255,255,255), AppMode.RUN);
    }
}
