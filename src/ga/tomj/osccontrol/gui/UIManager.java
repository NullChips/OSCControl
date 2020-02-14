package ga.tomj.osccontrol.gui;

import java.util.ArrayList;

public class UIManager {
    //This ArrayList will contain all me.nch.osccontrol.gui.UIElement objects created. An ArrayList has been chosen as this allows for
    //a flexible and dynamic array which can be changed as the user adds or removes objects.
    private static ArrayList<UIElement> elements;

    private static UIManager mgr;

    private UIManager() {
        if (elements == null) elements = new ArrayList<>();
    }

    public static UIManager getMgr() {
        if (mgr == null) mgr = new UIManager();
        return mgr;
    }

    public ArrayList<UIElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<UIElement> e) {
        elements = e;
    }

}