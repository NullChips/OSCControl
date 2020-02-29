package ga.tomj.osccontrol.gui;

import java.util.concurrent.CopyOnWriteArrayList;

public class UIManager {
    /*This ArrayList will contain all UIElement objects created. An ArrayList has been chosen as this allows for
    a flexible and dynamic array which can be changed as the user adds or removes objects. */
    private CopyOnWriteArrayList<UIElement> elements;
    private boolean editMode;
    private UIElement elementDragged;
    private UIElement recentElement;

    private static UIManager mgr;

    private UIManager() {
        /* Because the elements ArrayList is accessed from different threads (draw(), mouseClick(), etc) a
        a CopyOnWriteArrayList is used. This allows for the arraylist to be accessed and modified from different threads
        easily. */
        if (elements == null) elements = new CopyOnWriteArrayList<>();
        editMode = false;
        elementDragged = null;
        recentElement = null;
    }

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

    public void setRecentElement(UIElement recentElement) {
        this.recentElement = recentElement;
    }
}