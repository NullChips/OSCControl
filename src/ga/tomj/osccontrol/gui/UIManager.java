package ga.tomj.osccontrol.gui;

import ga.tomj.osccontrol.gui.buttons.ModeButton;
import ga.tomj.osccontrol.gui.buttons.MuteButton;
import ga.tomj.osccontrol.gui.buttons.RecordArmButton;
import ga.tomj.osccontrol.gui.buttons.SoloButton;

import java.util.concurrent.CopyOnWriteArrayList;

public class UIManager {

    //Global variables.
    /*
    This ArrayList will contain all UIElement objects created. An ArrayList has been chosen rather than a standard array,
    as this allows for a flexible and dynamic array which can be changed as the user adds or removes objects.
    */
    private CopyOnWriteArrayList<UIElement> elements;
    private boolean editMode;
    private boolean deleteMode;
    private UIElement elementDragged;
    private UIElement recentElement;

    private static UIManager mgr;

    //Constructor.
    private UIManager() {
        /* Because the elements ArrayList is accessed from different threads (draw(), mouseClick(), etc) a
        a CopyOnWriteArrayList is used. This allows for the arraylist to be accessed and modified from different threads
        easily. */
        if (this.elements == null) {
            this.elements = new CopyOnWriteArrayList<>(); //If the elements list has not yet been created, create it.
        }
        //Set default values.
        this.editMode = false;
        this.deleteMode = false;
        this.elementDragged = null;
        this.recentElement = null;
    }


    /*
    This class is a singleton meaning that there can only be one instance of it created. This instance is then accessed
    through a single static method.
     */

    //Getters and setters.
    public static UIManager getMgr() {
        if (mgr == null) mgr = new UIManager();
        return mgr;
    }

    public CopyOnWriteArrayList<UIElement> getElements() {
        return elements;
    }

    public void setElements(CopyOnWriteArrayList<UIElement> e) {
        elements = e;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public UIElement getElementDragged() {
        return elementDragged;
    }

    public void setElementDragged(UIElement elementDragged) {
        this.elementDragged = elementDragged;
    }

    public UIElement getRecentElement() {
        return recentElement;
    }

    /*
    Sets the most recent element clicked after performing some checks. Once set, update the channel number text box
    shown in edit mode to the channel number of the element selected.
    */
    public void setRecentElement(UIElement recentElement) {
        if (!deleteMode && editMode) {
            this.recentElement = recentElement;

            if (recentElement == null) {
                ModeButton.removeCurrentChannel();
            }

            if (recentElement instanceof MuteButton) {
                MuteButton e = (MuteButton) recentElement;
                ModeButton.setCurrentChannelNumber(e.getChannelNumber());
            }

            if (recentElement instanceof SoloButton) {
                SoloButton e = (SoloButton) recentElement;
                ModeButton.setCurrentChannelNumber(e.getChannelNumber());
            }

            if (recentElement instanceof RecordArmButton) {
                RecordArmButton e = (RecordArmButton) recentElement;
                ModeButton.setCurrentChannelNumber(e.getChannelNumber());
            }

            if (recentElement instanceof Fader) {
                Fader e = (Fader) recentElement;
                ModeButton.setCurrentChannelNumber(e.getChannelNumber());
            }

            if (recentElement instanceof Pan) {
                Pan e = (Pan) recentElement;
                ModeButton.setCurrentChannelNumber(e.getChannelNumber());
            }
        }
    }

    public boolean isDeleteMode() {
        return deleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
    }
}