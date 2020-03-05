package ga.tomj.osccontrol.gui.buttons;

import ga.tomj.osccontrol.OSCControl;
import ga.tomj.osccontrol.gui.ChannelNumberTextBox;
import ga.tomj.osccontrol.gui.Line;
import ga.tomj.osccontrol.gui.UIManager;

import java.awt.*;

public class ModeButton extends Button {

    //Global variables.
    private static ChannelNumberTextBox currentChannelBox;
    private static int currentChannelNumber;
    private static EditModeText channelErrorMessage;

    //Constructor. Button label and colour is dependant on whether edit mode is active or not.
    public ModeButton(int x, int y) {
        super(x, y, 140, 35, "Edit Mode", new Color(0, 255, 255));
        setPressed(true); //Fill the button with colour.
        if (UIManager.getMgr().isEditMode()) {
            setLabel("Change to Play Mode");
            setColour(new Color(0, 255, 255));
        } else {
            setLabel("Change to Edit Mode");
            setColour(new Color(255, 0, 255));
        }

    }

    //Called when the button is pressed.
    public void mousePressed() {
        if (mouseInElement()) {
            UIManager.getMgr().setEditMode(!UIManager.getMgr().isEditMode()); //Toggle edit mode boolean.
            //Change button properties depending on state.
            if (UIManager.getMgr().isEditMode()) {
                setLabel("Change to Play Mode");
                setColour(new Color(0, 255, 255));
                app.getSurface().setSize(app.width, app.height + 200); //Make the window bigger to fit the buttons at the bottom.
                app.getSurface().setResizable(false); //Stop the window from being resized - buttons at the bottom will not follow.
                drawEditButtons(); //Draw the buttons at the bottom of the screen.
            } else {
                setLabel("Change to Edit Mode");
                setColour(new Color(255, 0, 255));
                app.getSurface().setSize(app.width, app.height - 200); //Remove height added to window when edit mode was enabled.
                app.getSurface().setResizable(true); //The canvas can be resized.
                app.reloadData(); //Request new data from Reaper.
            }
        }
    }

    //Draw all the elements relevant for edit mode.
    public void drawEditButtons() {
        int height = app.height - 200;

        //Draw lines.
        Color lineCol = new Color(64, 64, 64);
        Line l1 = new Line(0, height, app.width, height, lineCol);
        Line l2 = new Line(340, height, 340, height + 200, lineCol);
        Line l3 = new Line(505, height, 505, height + 200, lineCol);
        Line l4 = new Line(624, height, 624, height + 200, lineCol);

        //Draw AddElement area.
        EditModeText t1 = new EditModeText(17, height + 20, 16, "Add Elements:", app.LEFT, app.CENTER);
        AddElementButton e1 = new AddElementButton(70, height + 65, ElementType.MUTE_BUTTON);
        AddElementButton e2 = new AddElementButton(70, height + 115, ElementType.SOLO_BUTTON);
        AddElementButton e3 = new AddElementButton(70, height + 165, ElementType.RECORD_BUTTON);
        AddElementButton e4 = new AddElementButton(165, height + 65, 50, 35, ElementType.PAN);
        AddElementButton e5 = new AddElementButton(165, height + 115, 50, 35, ElementType.FADER);
        AddElementButton e9 = new AddElementButton(165, height + 165, 50, 35, ElementType.TIMECODE);
        AddElementButton e6 = new AddElementButton(260, height + 65, ElementType.PLAY_BUTTON);
        AddElementButton e7 = new AddElementButton(260, height + 115, ElementType.CLICK_BUTTON);
        AddElementButton e8 = new AddElementButton(260, height + 165, ElementType.LOOP_BUTTON);

        //Draw current channel area.
        EditModeText t2 = new EditModeText(357, height + 20, 16, "Channel Number:", app.LEFT, app.CENTER);
        currentChannelBox = new ChannelNumberTextBox(385, height + 90);
        ChannelNumberButton b1 = new ChannelNumberButton(450, height + 65, true);
        ChannelNumberButton b2 = new ChannelNumberButton(450, height + 115, false);
        ChannelSetButton b3 = new ChannelSetButton(418, height + 165);

        //Draw layout area.
        EditModeText t3 = new EditModeText(522, height + 20, 16, "Layout:", app.LEFT, app.CENTER);
        LayoutButton lb1 = new LayoutButton(565, height + 65, LayoutButtonType.DELETE_MODE);
        LayoutButton lb2 = new LayoutButton(565, height + 115, LayoutButtonType.LOAD_LAYOUT);
        LayoutButton lb3 = new LayoutButton(565, height + 165, LayoutButtonType.SAVE_LAYOUT);
    }

    //Display an error message when the contents of the ChannelNumberTextBox is not a valid int.
    public static void displayChannelErrorMessage() {
        UIManager.getMgr().getElements().remove(channelErrorMessage); //Garbage collection, stops adding more than one.
        channelErrorMessage = null;

        //Draw text alerting the user.
        channelErrorMessage = new EditModeText(357, OSCControl.getApp().height - 160, 10, "Invalid Channel",
                OSCControl.getApp().LEFT, OSCControl.getApp().CENTER);
    }

    //Remove the channelErrorMessage - called when exiting edit mode or when an appropriate channel has been set.
    public static void removeChannelErrorMessage() {
        UIManager.getMgr().getElements().remove(channelErrorMessage);
        channelErrorMessage = null;
    }

    //Called when the recentElement is set to null. Removes text in the text box.
    public static void removeCurrentChannel() {
        currentChannelNumber = -1;
        currentChannelBox.setText("");
    }

    //Getters and setters.
    public static ChannelNumberTextBox getCurrentChannelBox() {
        return currentChannelBox;
    }

    public static int getCurrentChannelNumber() {
        return currentChannelNumber;
    }

    public static void setCurrentChannelNumber(int newChannelNumber) {
        currentChannelNumber = newChannelNumber;
        currentChannelBox.setText(currentChannelNumber + "");
    }
}