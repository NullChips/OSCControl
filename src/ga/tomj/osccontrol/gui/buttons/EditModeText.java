package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.AppMode;
import ga.tomj.osccontrol.gui.Text;

import java.awt.*;

public class EditModeText extends Text {

    public EditModeText(int x, int y, int textSize, String text) {
        super(x, y, textSize, text, new Color(255,255,255), AppMode.RUN);
    }

    public EditModeText(int x, int y, int textSize, String text, int xAlign, int yAlign) {
        super(x, y, textSize, text, xAlign, yAlign, new Color(255,255,255), AppMode.RUN);
    }
}
