package ga.tomj.osccontrol;

import ga.tomj.osccontrol.gui.*;
import ga.tomj.osccontrol.gui.buttons.*;
import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

//This class extends PApplet, as this program was written in fully fledged Java, using the Processing libraries.
//Processing would normally do this behind the scenes by default.
public class OSCControl extends PApplet {

    private static OSCControl app;

    private OscP5 oscp5;
    private NetAddress reaperAddr;

    private String ip;
    private int receivePort, sendPort;

    private int maxChannels;

    private boolean isDoubleClick;
    private int doubleClickCounter;

    private AppMode mode;

    //This is the method which is ran by default within Java runtime when a program is started. In this case,
    //as this program was written within the full Java language, with Processing being used as a library, it is
    //necessary to point the Processing library to the main class of the project.
    public static void main(String[] args) {
        PApplet.main("ga.tomj.osccontrol.OSCControl");
    }

    //This method defines the settings for the window.
    public void settings() {
        size(1050, 650); //Set window size.
        smooth(2); //Enable anti-aliasing.
    }

    //This method is called when the program is first run and sets the default values for the IP and ports.
    public void setup() {
        UIManager.getMgr().setElements(new CopyOnWriteArrayList<>());
        background(32); //The background is drawn in the setup method as well to avoid the visible lag that occurs when OSC is connecting.
        connectOSC("127.0.0.1", 8000, 9000);

        surface.setResizable(true);
        frameRate(60); //Decrease framerate to decrease processing load.

        app = this;
        mode = AppMode.STARTUP;

        drawStartupScreen();
//        drawTestLayout();

        doubleClickCounter = 0;
        reloadData();
    }

    //This method is repeatedly run throughout the programs life.
    public void draw() {
        ArrayList<UIElement> removed = new ArrayList<>();
        background(32);
        //Render all the UI elements.
        for (UIElement e : UIManager.getMgr().getElements()) {
            if (e.getMode() == mode) {
                e.render();
                if (e instanceof StartupButton) {
                    StartupButton sb = (StartupButton) e; //Update the coordinates of the startup buttons to keep them central.
                    sb.updateCoordinates();
                }
            } else {
                removed.add(e); //If the element is not from the current mode, remove it (saves on CPU power).
            }
        }

        for (UIElement e : removed) {
            UIManager.getMgr().getElements().remove(e);
        }

        if (isDoubleClick) {
            if (doubleClickCounter < 30) {
                doubleClickCounter++;
            } else {
                isDoubleClick = false;
            }
        }
    }

    public void mousePressed() {
        if (isDoubleClick) {
            for (UIElement e : UIManager.getMgr().getElements()) {
                if (mode == e.getMode()) {
                    e.doubleClick();
                }
            }
        } else {
            for (UIElement e : UIManager.getMgr().getElements()) {
                if (mode == e.getMode()) {
                    e.mousePressed();
                }
            }
            isDoubleClick = true;
        }
        doubleClickCounter = 0;
    }

    public void mouseDragged() {
        for (UIElement e : UIManager.getMgr().getElements()) {
            if (mode == e.getMode()) {
                e.mouseDragged();
            }
        }
    }

    public void mouseReleased() {
        cursor();
        UIManager.getMgr().setElementDragged(null);
    }

    public void keyPressed() {
        if (key != CODED) {
            for (UIElement e : UIManager.getMgr().getElements()) {
                e.keyPressed(key);
            }
        }
    }

    //This method establishes the connection to reaper.
    public void connectOSC(String ip, int receivePort, int sendPort) {
        this.ip = ip;
        this.sendPort = sendPort;
        this.receivePort = receivePort;
        this.previousSendPortEntry = sendPort + "";
        this.previousReceivePortEntry = receivePort + "";

        reaperAddr = new NetAddress(ip, receivePort);
        oscp5 = new OscP5(this, sendPort);
    }

    public boolean connectOSC() {
        previousReceivePortEntry = receivePortTextBox.getText();
        previousSendPortEntry = sendPortTextBox.getText();
        ip = ipTextBox.getText();

        try {
            receivePort = Integer.parseInt(previousReceivePortEntry);
        } catch (NumberFormatException e) {
            System.out.println("Receive port given was not a number!");
            return false;
        }

        try {
            sendPort = Integer.parseInt(previousSendPortEntry);
        } catch (NumberFormatException e) {
            System.out.println("Send port given was not a number!");
            return false;
        }

        reaperAddr = new NetAddress(ip, receivePort);
        oscp5 = new OscP5(this, sendPort);
        return true;
    }

    public void reloadData() {
        for (UIElement e : UIManager.getMgr().getElements()) {
            if (e instanceof MuteButton) {
                MuteButton m = (MuteButton) e;
                //This line uses an ? operator to check if the channel number of this element is greater than the current
                //number of max channels. If it is, it sets the max channels to this elements channel number.
                maxChannels = (m.getChannelNumber() > maxChannels) ? m.getChannelNumber() : maxChannels;
            }

            if (e instanceof SoloButton) {
                SoloButton s = (SoloButton) e;
                //This line uses an ? operator to check if the channel number of this element is greater than the current
                //number of max channels. If it is, it sets the max channels to this elements channel number.
                maxChannels = (s.getChannelNumber() > maxChannels) ? s.getChannelNumber() : maxChannels;
            }
        }

        oscp5.send(new OscMessage("/device/track/count").add(maxChannels), reaperAddr);
        oscp5.send(new OscMessage("/action/41743"), reaperAddr);

    }

    public void oscEvent(OscMessage message) {
        if (mode == AppMode.RUN) {
            String messageString = message.addrPattern();
            String[] splitMessage = split(messageString, "/"); //Split the message into its seperate parts.

            print("### Received an osc message.");
            println(" addrpattern: " + messageString + "   Typetag: " + message.typetag() + "   Length: " + splitMessage.length);


            for (UIElement e : UIManager.getMgr().getElements()) {
                if (e instanceof MuteButton && splitMessage.length > 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("mute")) { //Checks the message is of the right length and is a track mute message.

                    MuteButton m = (MuteButton) e; //Create a new MuteButton object from the UIElement object.
                    int channel = parseInt(splitMessage[2]);
                    if (m.getChannelNumber() == channel) {
                        m.setPressed(message.get(0).floatValue() == 1.0F);
                    }
                }

                if (e instanceof SoloButton && splitMessage.length > 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("solo")) { //Checks the message is of the right length and is a track solo message.

                    SoloButton s = (SoloButton) e; //Create a new SoloButton object from the UIElement object.
                    int channel = parseInt(splitMessage[2]);
                    if (s.getChannelNumber() == channel) {
                        s.setPressed(message.get(0).floatValue() == 1.0F);
                    }
                }

                if (e instanceof Fader && splitMessage.length == 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("volume")) {
                    Fader f = (Fader) e;
                    int channel = parseInt(splitMessage[2]);
                    if (f.getChannelNumber() == channel) {
                        f.setFaderPercent((int) (message.get(0).floatValue() * 100));
                    }
                }

                if (e instanceof Fader && splitMessage.length == 5 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("vu")) {
                    Fader f = (Fader) e;
                    int channel = parseInt(splitMessage[2]);
                    if (f.getChannelNumber() == channel && splitMessage[4].equals("L")) {
                        f.setVuL((int) (message.get(0).floatValue() * 100));
                    } else if (f.getChannelNumber() == channel && splitMessage[4].equals("R")) {
                        f.setVuR((int) (message.get(0).floatValue() * 100));
                    }
                }

                if (e instanceof Fader && splitMessage.length == 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("name")) {
                    Fader f = (Fader) e;
                    int channel = parseInt(splitMessage[2]);
                    if (f.getChannelNumber() == channel) f.setTrackName(message.get(0).stringValue());
                }

                if (e instanceof Pan && splitMessage.length == 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("pan")) {
                    Pan p = (Pan) e;
                    int channel = parseInt(splitMessage[2]);
                    if (p.getChannelNumber() == channel) {
                        float f = message.get(0).floatValue() * 100;
                        p.setPercent((int) f);
                    }
                }

                if (e instanceof TransportButton) {
                    TransportButton tb = (TransportButton) e;
                    if (tb.getTransportButtonType() == TransportButtonType.CLICK && splitMessage.length == 2 && splitMessage[1].equals("click"))
                        tb.setPressed(message.get(0).floatValue() == 1.0F);

                    if (tb.getTransportButtonType() == TransportButtonType.PLAY && splitMessage.length == 2 && splitMessage[1].equals("play"))
                        tb.setPressed(message.get(0).floatValue() == 1.0F);

                    if (tb.getTransportButtonType() == TransportButtonType.LOOP && splitMessage.length == 2 && splitMessage[1].equals("repeat"))
                        tb.setPressed(message.get(0).floatValue() == 1.0F);
                }
            }
        }
    }

    public void drawStartupScreen() {
        UIManager.getMgr().getElements().clear();
        mode = AppMode.STARTUP;

        ipTextBox = null;
        receivePortTextBox = null;
        sendPortTextBox = null;

        surface.setResizable(true);
        surface.setSize(1050, 650);
        StartupButton newLayout = new StartupButton(width / 2, (height / 2) - 50, StartupButtonType.NEW_LAYOUT);
        StartupButton loadLayout = new StartupButton(width / 2, (height / 2), StartupButtonType.LOAD_LAYOUT);
        StartupButton settings = new StartupButton(width / 2, (height / 2 + 50), StartupButtonType.SETTINGS);

    }

    public void drawTestLayout() {
        for (int i = 0; i < 16; i++) {
            int x = (50 * (i + 1)) - 15;
            SoloButton s = new SoloButton(x, 75, i + 1);
            MuteButton m = new MuteButton(x, 125, i + 1);
            Pan p = new Pan(x, 175, i + 1);
            Fader f = new Fader(x, 325, i + 1);
        }
        ModeButton mo = new ModeButton(85, 25);
        TransportButton t1 = new TransportButton(195, 25, TransportButtonType.PLAY);
        TransportButton t2 = new TransportButton(260, 25, TransportButtonType.CLICK);
        TransportButton t3 = new TransportButton(325, 25, TransportButtonType.LOOP);
        reloadData();
    }

    private TextBox ipTextBox;
    private TextBox receivePortTextBox;
    private TextBox sendPortTextBox;

    private String previousReceivePortEntry;
    private String previousSendPortEntry;

    public void drawSettingsScreen() {
        UIManager.getMgr().getElements().clear();

        surface.setSize(450, 300);
        surface.setResizable(false);

        //(width / 2) - 40

        Text settingsText = new Text((width / 2), 20, 24, "Network Settings:",
                new Color(255, 255, 255), AppMode.SETTINGS);

        Text ipText = new Text((width / 2) - 80, (height / 2) - 52, 15, "IP:", RIGHT, CENTER,
                new Color(255, 255, 255), AppMode.SETTINGS);
        ipTextBox = new TextBox(width / 2, (height / 2) - 50, 150, 35, 15, new Color(128, 128, 128),
                new Color(0), ip);

        Text sendPortText = new Text((width / 2) - 80, (height / 2) - 2, 15, "Device Port:", RIGHT, CENTER,
                new Color(255, 255, 255), AppMode.SETTINGS);
        sendPortTextBox = new TextBox(width / 2, (height / 2), 150, 35, 15, new Color(128, 128, 128),
                new Color(0), previousSendPortEntry);

        Text receivePortText = new Text((width / 2) - 80, (height / 2) + 52, 15, "Listen Port:", RIGHT, CENTER,
                new Color(255, 255, 255), AppMode.SETTINGS);
        receivePortTextBox = new TextBox(width / 2, (height / 2) + 50, 150, 35, 15, new Color(128, 128, 128),
                new Color(0), previousReceivePortEntry);

        SaveSettingsButton ssb = new SaveSettingsButton(width / 2, (height / 2) + 100);
    }

    public static OSCControl getApp() {
        return app;
    }

    public OscP5 getOscp5() {
        return oscp5;
    }

    public NetAddress getReaperAddr() {
        return reaperAddr;
    }

    public void setDoubleClick(boolean doubleClick) {
        isDoubleClick = doubleClick;
    }

    public AppMode getMode() {
        return mode;
    }

    public void setMode(AppMode mode) {
        this.mode = mode;
    }
}