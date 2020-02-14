package ga.tomj.osccontrol;

import ga.tomj.osccontrol.gui.Button;
import ga.tomj.osccontrol.gui.UIElement;
import ga.tomj.osccontrol.gui.UIManager;
import netP5.NetAddress;
import oscP5.OscP5;
import processing.core.PApplet;

//This class extends PApplet, as this program was written in fully fledged Java, using the Processing libraries.
//Processing does this behind the scenes by default.
public class OSCControl extends PApplet {

    private static PApplet app;

    private OscP5 oscp5;
    private NetAddress reaperAddr;

    private String ip;
    private int receivePort, sendPort;

    private Button b;


    //This method is the method which is ran by default within Java runtime when a program is started. In this case,
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
        background(0); //The background is drawn in the setup method as well to avoid the visible lag that occurs when OSC is connecting.
        connectOSC("127.0.0.1", 8000, 9000);
        app = this;

        b = new Button(100, 100, "Test");
    }

    //This method is repeatedly run throughout the programs life.
    public void draw() {
        background(0);
        text(mouseX, 10, 10);
        text(mouseY, 10, 30);
        text(mouseX - b.getX() + b.getSizeX() / 2, 10, 50);
        text(mouseY - b.getY() + b.getSizeY() / 2, 10, 70);
        if (b.isPressed()) text("True", 10, 90);
        else text("False", 10, 90);

        //Render all the UI elements.
        for (UIElement e : UIManager.getMgr().getElements()) {
            e.render();
        }
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

    public static PApplet getApp() {
        return app;
    }

}
