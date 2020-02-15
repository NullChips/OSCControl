package ga.tomj.osccontrol;

import ga.tomj.osccontrol.gui.Fader;
import ga.tomj.osccontrol.gui.UIElement;
import ga.tomj.osccontrol.gui.UIManager;
import ga.tomj.osccontrol.gui.buttons.ModeButton;
import ga.tomj.osccontrol.gui.buttons.MuteButton;
import ga.tomj.osccontrol.gui.buttons.SoloButton;
import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

//This class extends PApplet, as this program was written in fully fledged Java, using the Processing libraries.
//Processing would normally do this behind the scenes by default.
public class OSCControl extends PApplet {

    private static OSCControl app;

    private OscP5 oscp5;
    private NetAddress reaperAddr;

    private String ip;
    private int receivePort, sendPort;

    private int maxChannels;


    //This is the method which is ran by default within Java runtime when a program is started. In this case,
    //as this program was written within the full Java language, with Processing being used as a library, it is
    //necessary to point Processing to the main class of the project.
    public static void main(String[] args) {
        PApplet.main("ga.tomj.osccontrol.OSCControl");
    }

    //This method defines the settings for the window.
    public void settings() {
        size(1050, 650);
        smooth(2);
    }

    //This method is called when the program is first run and sets the default values for the IP and ports.
    public void setup() {
        background(32); //The background is drawn in the setup method as well to avoid the visible lag that occurs when OSC is connecting.
        connectOSC("127.0.0.1", 8000, 9000);
        app = this;
        //MuteButton master = new MuteButton(50, 100, 0);
        for (int i = 0; i < 16; i++) {
            int x = (50 * (i + 1)) - 15;
            MuteButton m = new MuteButton(x, 125, i + 1);
            SoloButton s = new SoloButton(x, 75, i + 1);
            Fader f = new Fader(x, 275, i + 1);
        }
        ModeButton mo = new ModeButton(85, 25);

        maxChannels = 0;
        reloadData();
    }

    //This method is repeatedly run throughout the programs life.
    public void draw() {
        background(32);
        //Render all the UI elements.
        for (UIElement e : UIManager.getMgr().getElements()) {
            e.render();
        }
    }

    public void mousePressed() {
        for (UIElement e : UIManager.getMgr().getElements()) {
            e.mousePressed();
        }
    }

    public void mouseDragged() {
        for (UIElement e : UIManager.getMgr().getElements()) {
            e.mouseDragged();
        }
    }

    public void mouseReleased() {
        cursor();
        UIManager.getMgr().setElementDragged(null);
    }

    public void keyPressed() {
        if (key != CODED) {

        }
    }

    //This method establishes the connection to reaper.
    public void connectOSC(String ip, int receivePort, int sendPort) {
        this.ip = ip;
        this.sendPort = sendPort;
        this.receivePort = receivePort;

        reaperAddr = new NetAddress(ip, receivePort);
        oscp5 = new OscP5(this, sendPort);
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
                    float f = message.get(0).floatValue();
                    boolean b = f == 1.0F;
                    m.setPressed(b);
                }
            }

            if (e instanceof SoloButton && splitMessage.length > 4 && splitMessage[1].equals("track") &&
                    splitMessage[3].equals("solo")) { //Checks the message is of the right length and is a track solo message.

                SoloButton s = (SoloButton) e; //Create a new SoloButton object from the UIElement object.
                int channel = parseInt(splitMessage[2]);
                if (s.getChannelNumber() == channel) {
                    float f = message.get(0).floatValue();
                    boolean b = f == 1.0F;
                    s.setPressed(b);
                }
            }

            if (e instanceof Fader && splitMessage.length == 4 && splitMessage[1].equals("track") &&
                    splitMessage[3].equals("volume")) {
                Fader f = (Fader) e;
                int channel = parseInt(splitMessage[2]);
                if (f.getChannelNumber() == channel) {
                    float fl = message.get(0).floatValue();
                    fl = fl * 100;
                    f.setFaderPercent((int) fl);
                }
            }

            if (e instanceof Fader && splitMessage.length == 5 && splitMessage[1].equals("track") &&
                    splitMessage[3].equals("vu")) {
                Fader f = (Fader) e;
                int channel = parseInt(splitMessage[2]);
                if (f.getChannelNumber() == channel && splitMessage[4].equals("L")) {
                    float fl = message.get(0).floatValue();
                    fl = fl * 100;
                    f.setVuL((int) fl);
                } else if (f.getChannelNumber() == channel && splitMessage[4].equals("R")) {
                    float fl = message.get(0).floatValue();
                    fl = fl * 100;
                    f.setVuR((int) fl);
                }
            }

        }
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


}
