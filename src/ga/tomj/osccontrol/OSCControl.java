package ga.tomj.osccontrol;

import ga.tomj.osccontrol.gui.MuteButton;
import ga.tomj.osccontrol.gui.SoloButton;
import ga.tomj.osccontrol.gui.UIElement;
import ga.tomj.osccontrol.gui.UIManager;
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


    //This is the method which is ran by default within Java runtime when a program is started. In this case,
    //as this program was written within the full Java language, with Processing being used as a library, it is
    //necessary to point Processing to the main class of the project.
    public static void main(String[] args) {
        PApplet.main("ga.tomj.osccontrol.OSCControl");
    }

    //This method defines the settings for the window.
    public void settings() {
        size(1050, 650);
    }

    //This method is called when the program is first run and sets the default values for the IP and ports.
    public void setup() {
        background(38, 50, 56); //The background is drawn in the setup method as well to avoid the visible lag that occurs when OSC is connecting.
        connectOSC("127.0.0.1", 8000, 9000);
        app = this;
        //MuteButton master = new MuteButton(50, 100, 0);
        for (int i = 0; i < 10; i++) {
            MuteButton m = new MuteButton(50 * (i + 1), 50, i + 1);
            SoloButton s = new SoloButton(50 * (i + 1), 100, i + 1);
        }
    }

    //This method is repeatedly run throughout the programs life.
    public void draw() {
        background(19, 25, 28);
        //Render all the UI elements.
        for (UIElement e : UIManager.getMgr().getElements()) {
            e.render();
        }
    }

    public void mousePressed() {
        for (UIElement e : UIManager.getMgr().getElements()) {
            e.mousePressed();
        }
        OscMessage m = new OscMessage("/device/track/count");
        m.add(10);
        oscp5.send(m, reaperAddr);

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

    public static OSCControl getApp() {
        return app;
    }

    public OscP5 getOscp5() {
        return oscp5;
    }

    public NetAddress getReaperAddr() {
        return reaperAddr;
    }

    public void oscEvent(OscMessage message) {
        println("### Received an osc message.");
        print(" addrpattern: " + message.addrPattern());

        String messageString = message.addrPattern();
        String[] splitMessage = split(messageString, "/"); //Split the message into its seperate parts.

        for (UIElement e : UIManager.getMgr().getElements()) {
            if (e instanceof MuteButton && splitMessage.length > 4 && splitMessage[1].equals("track") &&
                    splitMessage[3].equals("mute")) { //Checks the message is of the right length and is a track mute message.

                MuteButton m = (MuteButton) e; //Create a new MuteButton object from the UIElement object.
                int channel = parseInt(splitMessage[2]);
                if (m.getChannelNumber() == channel) m.setPressed(!m.isPressed());
            }

            if (e instanceof SoloButton && splitMessage.length > 4 && splitMessage[1].equals("track") &&
                    splitMessage[3].equals("solo")) { //Checks the message is of the right length and is a track solo message.

                SoloButton s = (SoloButton) e; //Create a new SoloButton object from the UIElement object.
                int channel = parseInt(splitMessage[2]);
                if (s.getChannelNumber() == channel) s.setPressed(!s.isPressed());
            }
        }
    }

}
