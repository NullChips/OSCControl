package ga.tomj.osccontrol;

import ga.tomj.osccontrol.gui.UIManager;
import ga.tomj.osccontrol.gui.*;
import ga.tomj.osccontrol.gui.buttons.*;
import netP5.NetAddress;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        mode = AppMode.STARTUP; //Set the app mode on startup.

        drawStartupScreen(); //Draw the startup elements.

        doubleClickCounter = 0; //Set double click counter, ready to receive double clicks.
    }

    //This method is repeatedly run throughout the programs life.
    public void draw() {
        background(32);

        //Render all the UI elements.
        for (UIElement e : UIManager.getMgr().getElements()) {
            if (!UIManager.getMgr().isEditMode()) {
                //If the app is not in edit mode, remove the elements that are drawn during the edit mode.
                if (e instanceof Line || e instanceof AddElementButton || e instanceof EditModeText ||
                        e instanceof ChannelNumberTextBox || e instanceof ChannelNumberButton ||
                        e instanceof ChannelSetButton || e instanceof LayoutButton) {
                    UIManager.getMgr().getElements().remove(e);
                }
            }

            //If the element is not from the current mode, remove it (garbage collection, saves on CPU and memory resources).
            if (e.getMode() == mode) {
                e.render();
                if (e instanceof StartupButton) {
                    StartupButton sb = (StartupButton) e; //Update the coordinates of the startup buttons to keep them central.
                    sb.updateCoordinates();
                }
            } else {
                UIManager.getMgr().getElements().remove(e);
            }
        }
        if (isDoubleClick) {
            if (doubleClickCounter < 30) {
                doubleClickCounter++; //Increase counter for double click if below certain value.
            } else {
                isDoubleClick = false; //If counter is now too high for a double click, stop counting.
            }
        }
    }

    public void mousePressed() {
        if (isDoubleClick) { //If double click, run double click method for all elements.
            for (UIElement e : UIManager.getMgr().getElements()) {
                if (mode == e.getMode()) {
                    e.doubleClick();
                    e.checkAndDelete(); //Checks if app is in delete mode, and if so, deletes the element.
                }
            }
        } else { //Otherwise, run the normal mouse pressed method, then start counting for a double click.
            for (UIElement e : UIManager.getMgr().getElements()) {
                if (mode == e.getMode()) {
                    e.mousePressed();
                    e.checkAndDelete();
                }
            }
            isDoubleClick = true;
        }
        doubleClickCounter = 0; //Reset counter.
    }

    public void mouseDragged() { //When mouse dragged, run the mouseDragged method for all elements.
        for (UIElement e : UIManager.getMgr().getElements()) {
            if (mode == e.getMode()) {
                e.mouseDragged();
            }
        }
    }

    public void mouseReleased() {
        cursor(); //Show cursor (cursor is hidden when using a pan).
        UIManager.getMgr().setElementDragged(null); //No element is being dragged anymore.
    }

    public void keyPressed() {
        if (key != CODED) {
            for (UIElement e : UIManager.getMgr().getElements()) {
                e.keyPressed(key); //Send the key to each element and run their keyPressed methods.
            }
        }
    }

    //This method establishes the connection to Reaper.
    public void connectOSC(String ip, int receivePort, int sendPort) {
        this.ip = ip;
        this.sendPort = sendPort;
        this.receivePort = receivePort;
        this.previousSendPortEntry = sendPort + ""; //These two variables are used in the settings window to keep the value
        this.previousReceivePortEntry = receivePort + ""; //that has been typed in if it is not a valid number.

        reaperAddr = new NetAddress(ip, receivePort); //Address used to packets (received by Reaper).
        oscp5 = new OscP5(this, sendPort); //OscP5 object used to send messages.
    }

    /*
    This method establishes the connection to Reaper when a new IP and port have been entered in the settings Window.
    If the numbers entered are actually numbers, it returns true. Otherwise, return false.
    */
    public boolean connectOSC() {
        //Set strings for settings window to current IP and port values.
        previousReceivePortEntry = receivePortTextBox.getText();
        previousSendPortEntry = sendPortTextBox.getText();
        ip = ipTextBox.getText();

        //Check if the ports are numbers.
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

        //Reconnect using the new IP and ports.
        reaperAddr = new NetAddress(ip, receivePort);
        oscp5 = new OscP5(this, sendPort);
        return true;
    }

    public void reloadData() {
        //Loop through all elements and find the one with the highest channel number.
        for (UIElement e : UIManager.getMgr().getElements()) {
            if (e instanceof MuteButton) {
                MuteButton m = (MuteButton) e;
                //This line uses an ? operator to check if the channel number of this element is greater than the current
                //number of max channels. If it is, it sets the max channels to this elements channel number.
                maxChannels = (m.getChannelNumber() > maxChannels) ? m.getChannelNumber() : maxChannels;
            } else if (e instanceof SoloButton) {
                SoloButton s = (SoloButton) e;
                maxChannels = (s.getChannelNumber() > maxChannels) ? s.getChannelNumber() : maxChannels;
            } else if (e instanceof RecordArmButton) {
                RecordArmButton r = (RecordArmButton) e;
                maxChannels = (r.getChannelNumber() > maxChannels) ? r.getChannelNumber() : maxChannels;
            } else if (e instanceof Fader) {
                Fader f = (Fader) e;
                maxChannels = (f.getChannelNumber() > maxChannels) ? f.getChannelNumber() : maxChannels;
            } else if (e instanceof Pan) {
                Pan p = (Pan) e;
                maxChannels = (p.getChannelNumber() > maxChannels) ? p.getChannelNumber() : maxChannels;
            }
        }

        //Tell Repear we want info about this amount of channels.
        oscp5.send(new OscMessage("/device/track/count").add(maxChannels), reaperAddr);
        oscp5.send(new OscMessage("/action/41743"), reaperAddr); //Request all data Reaper has.

    }

    //Adapted from OSC Tutorial by Kit Lane & Bruce Wiggins.
    public void oscEvent(OscMessage message) {
        if (mode == AppMode.RUN) {
            String messageString = message.addrPattern();
            String[] splitMessage = split(messageString, "/"); //Split the message into its separate parts.

            //Print information each time a message is received.
            print("### Received an osc message.");
            println(" addrpattern: " + messageString + "   Typetag: " + message.typetag() + "   Length: " + splitMessage.length);


            /*
            Each of the if statements below checks if the message recieved has a certain address.
            Once this condition is satisfied, then it will update the elements related to this message accordingly. This
            is used to prevent the use of elements that do not currently have a channel in Reaper - Reaper will not send
            a message in response. Not all if statements are commented, as some are near duplicates of previous statements.
             */
            if (splitMessage.length == 3 && splitMessage[1].equals("time") &&
                    splitMessage[2].equals("str")) {
                Timecode.setTc(message.get(0).stringValue());
            }

            for (UIElement e : UIManager.getMgr().getElements()) {
                if (e instanceof MuteButton && splitMessage.length > 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("mute")) { //Checks the message is of the right length and is a track mute message.

                    MuteButton m = (MuteButton) e; //Create a new MuteButton object from the UIElement object.
                    int channel = parseInt(splitMessage[2]);
                    if (m.getChannelNumber() == channel) {
                        //If the value of the float is 1, set the button to be pressed, else set to be not pressed.
                        m.setPressed(message.get(0).floatValue() == 1.0F);
                    }
                }

                if (e instanceof SoloButton && splitMessage.length > 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("solo")) {

                    SoloButton s = (SoloButton) e;
                    int channel = parseInt(splitMessage[2]);
                    if (s.getChannelNumber() == channel) {
                        s.setPressed(message.get(0).floatValue() == 1.0F);
                    }
                }

                if (e instanceof RecordArmButton && splitMessage.length > 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("recarm")) {

                    RecordArmButton r = (RecordArmButton) e;
                    int channel = parseInt(splitMessage[2]);
                    if (r.getChannelNumber() == channel) {
                        r.setPressed(message.get(0).floatValue() == 1.0F);
                    }
                }

                if (e instanceof Fader && splitMessage.length == 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("volume")) {
                    Fader f = (Fader) e;
                    int channel = parseInt(splitMessage[2]);
                    if (f.getChannelNumber() == channel) {
                        //Fader values are sent from 0.0 to 1.0 from Reaper, so multiply this by 100 to set the percent.
                        f.setFaderPercent((int) (message.get(0).floatValue() * 100));
                    }
                }

                if (e instanceof Fader && splitMessage.length == 5 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("vu")) {
                    Fader f = (Fader) e;
                    int channel = parseInt(splitMessage[2]);
                    if (f.getChannelNumber() == channel && splitMessage[4].equals("L")) {
                        //VU values are sent from 0.0 to 1.0 from Reaper, so multiply this by 100 to set the VU percent.
                        f.setVuL((int) (message.get(0).floatValue() * 100));
                    } else if (f.getChannelNumber() == channel && splitMessage[4].equals("R")) {
                        f.setVuR((int) (message.get(0).floatValue() * 100));
                    }
                }

                if (e instanceof Fader && splitMessage.length == 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("name")) {
                    Fader f = (Fader) e;
                    int channel = parseInt(splitMessage[2]);
                    if (f.getChannelNumber() == channel) {
                        f.setTrackName(message.get(0).stringValue());
                    }
                }

                if (e instanceof Pan && splitMessage.length == 4 && splitMessage[1].equals("track") &&
                        splitMessage[3].equals("pan")) {
                    Pan p = (Pan) e;
                    int channel = parseInt(splitMessage[2]);
                    if (p.getChannelNumber() == channel) {
                        //Pan values are sent from 0.0 to 1.0 from Reaper, so multiply this by 100 to set the pan percent.
                        float f = message.get(0).floatValue() * 100;
                        p.setPercent((int) f);
                    }
                }

                if (e instanceof TransportButton) {
                    TransportButton tb = (TransportButton) e;
                    //If the float value returns a 1.0, set the relevant transport button to be pressed.

                    if (tb.getTransportButtonType() == TransportButtonType.CLICK && splitMessage.length == 2 && splitMessage[1].equals("click")) {
                        tb.setPressed(message.get(0).floatValue() == 1.0F);
                    } else if (tb.getTransportButtonType() == TransportButtonType.PLAY && splitMessage.length == 2 && splitMessage[1].equals("play")) {
                        tb.setPressed(message.get(0).floatValue() == 1.0F);
                    } else if (tb.getTransportButtonType() == TransportButtonType.LOOP && splitMessage.length == 2 && splitMessage[1].equals("repeat")) {
                        tb.setPressed(message.get(0).floatValue() == 1.0F);
                    }
                }
            }
        }

    }

    //Draw the elements for the startup screen.
    public void drawStartupScreen() {
        UIManager.getMgr().getElements().clear();
        mode = AppMode.STARTUP;

        //Garbage collect textboxes from settings screen.
        ipTextBox = null;
        receivePortTextBox = null;
        sendPortTextBox = null;

        surface.setResizable(true);
        surface.setSize(1050, 650);
        StartupButton newLayout = new StartupButton(width / 2, (height / 2) - 50, StartupButtonType.NEW_LAYOUT);
        StartupButton loadLayout = new StartupButton(width / 2, (height / 2), StartupButtonType.LOAD_LAYOUT);
        StartupButton settings = new StartupButton(width / 2, (height / 2 + 50), StartupButtonType.SETTINGS);

    }

    //Draw elements for test layout used whilst program was in development.
    public void drawTestLayout() {
        for (int i = 0; i < 16; i++) {
            int x = (50 * (i + 1)) - 15;
            RecordArmButton r = new RecordArmButton(x, 75, i + 1);
            SoloButton s = new SoloButton(x, 125, i + 1);
            MuteButton m = new MuteButton(x, 175, i + 1);
            Pan p = new Pan(x, 225, i + 1);
            Fader f = new Fader(x, 375, i + 1);
        }
        ModeButton mo = new ModeButton(85, 25);
        TransportButton t1 = new TransportButton(195, 25, TransportButtonType.PLAY);
        TransportButton t2 = new TransportButton(260, 25, TransportButtonType.CLICK);
        TransportButton t3 = new TransportButton(325, 25, TransportButtonType.LOOP);
        Timecode tc = new Timecode(410, 25);
        reloadData();
    }

    //Global variables for the settings screen.
    private TextBox ipTextBox;
    private TextBox receivePortTextBox;

    private TextBox sendPortTextBox;
    private String previousReceivePortEntry;

    private String previousSendPortEntry;

    //Draw the elements for the settings screen.
    public void drawSettingsScreen() {
        UIManager.getMgr().getElements().clear();

        surface.setSize(450, 300);
        surface.setResizable(false);

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

    /*
    Save as and open dialog boxes adapted from Processing reference: https://www.processing.org/reference/selectInput_.html
    and https://www.processing.org/reference/selectOutput_.html
     */
    public void saveLayout() {
        selectOutput("Save As:", "saveFileSelected");
    }

    public void loadLayout() {
        selectInput("Open Layout:", "openFileSelected");
    }

    //XML saving method. Adapted from: https://stackoverflow.com/questions/7373567/how-to-read-and-write-xml-files
    public void saveFileSelected(File saveFile) {
        if (saveFile != null) { //If the save location exists - save dialog wasn't exited or closed.
            String path = saveFile.getPath();
            if (!path.endsWith(".oscl")) { //See if user entered the file extension. If not, add it.
                path += ".oscl";
            }

            Document doc;
            Element e;

            try {

                //Use DBF to create a new DocumentBuilder. Use this DB to create the new XML document.
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = db.newDocument();
                Element root = doc.createElement("layout"); //Create the root XML element.

                Element settings = doc.createElement("settings"); //Create the settings element.
                e = doc.createElement("size_x");
                e.appendChild(doc.createTextNode(width + ""));
                settings.appendChild(e);

                e = doc.createElement("size_y");
                //If the app is in edit mode, then discount the height that is added onto the window by this.
                if (UIManager.getMgr().isEditMode()) {
                    e.appendChild(doc.createTextNode((height - 200) + ""));
                } else {
                    e.appendChild(doc.createTextNode(height + ""));
                }
                settings.appendChild(e);
                root.appendChild(settings);

                /*
                Loop through every savable UI element and create the XML elements for that. Code is near identical for each,
                so only one is commented.
                 */
                Element uiElements = doc.createElement("elements");
                for (UIElement uiElement : UIManager.getMgr().getElements()) {
                    if (uiElement instanceof SoloButton) {
                        SoloButton uie = (SoloButton) uiElement;
                        Element uiXmlElement = doc.createElement("solo");

                        uiXmlElement = createXYData(uiXmlElement, uie, doc); //Save X and Y elements.

                        e = doc.createElement("channel");
                        e.appendChild(doc.createTextNode(uie.getChannelNumber() + "")); //Save channel element.
                        uiXmlElement.appendChild(e);

                        uiElements.appendChild(uiXmlElement); //Add the UIElement just parsed to the master XML element.
                    }

                    if (uiElement instanceof MuteButton) {
                        MuteButton uie = (MuteButton) uiElement;
                        Element uiXmlElement = doc.createElement("mute");

                        uiXmlElement = createXYData(uiXmlElement, uie, doc);

                        e = doc.createElement("channel");
                        e.appendChild(doc.createTextNode(uie.getChannelNumber() + ""));
                        uiXmlElement.appendChild(e);

                        uiElements.appendChild(uiXmlElement);
                    }

                    if (uiElement instanceof RecordArmButton) {
                        RecordArmButton uie = (RecordArmButton) uiElement;
                        Element uiXmlElement = doc.createElement("record_arm");

                        uiXmlElement = createXYData(uiXmlElement, uie, doc);

                        e = doc.createElement("channel");
                        e.appendChild(doc.createTextNode(uie.getChannelNumber() + ""));
                        uiXmlElement.appendChild(e);

                        uiElements.appendChild(uiXmlElement);
                    }

                    if (uiElement instanceof Pan) {
                        Pan uie = (Pan) uiElement;
                        Element uiXmlElement = doc.createElement("pan");

                        uiXmlElement = createXYData(uiXmlElement, uie, doc);

                        e = doc.createElement("channel");
                        e.appendChild(doc.createTextNode(uie.getChannelNumber() + ""));
                        uiXmlElement.appendChild(e);

                        uiElements.appendChild(uiXmlElement);
                    }

                    if (uiElement instanceof Fader) {
                        Fader uie = (Fader) uiElement;
                        Element uiXmlElement = doc.createElement("fader");

                        uiXmlElement = createXYData(uiXmlElement, uie, doc);

                        e = doc.createElement("channel");
                        e.appendChild(doc.createTextNode(uie.getChannelNumber() + ""));
                        uiXmlElement.appendChild(e);

                        uiElements.appendChild(uiXmlElement);
                    }

                    if (uiElement instanceof TransportButton) {
                        TransportButton uie = (TransportButton) uiElement;
                        Element uiXmlElement = doc.createElement(uie.getTransportButtonType().getButtonName().toLowerCase());

                        uiXmlElement = createXYData(uiXmlElement, uie, doc);
                        uiElements.appendChild(uiXmlElement);
                    }

                    if (uiElement instanceof Timecode) {
                        Element uiXmlElement = doc.createElement("timecode");

                        uiXmlElement = createXYData(uiXmlElement, uiElement, doc);
                        uiElements.appendChild(uiXmlElement);
                    }
                }

                root.appendChild(uiElements); //Add all UI XML elements to the root element of the XML file.
                doc.appendChild(root); //Add the finished root element to the document.

                Transformer tr = TransformerFactory.newInstance().newTransformer(); //Transform the doc to XML and save.
                tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(path)));

            //If saving fails for any reason, print the stacktrace and alert the user.
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
                alert("File saving failed!");
            } catch (TransformerConfigurationException ex) {
                ex.printStackTrace();
                alert("File saving failed!");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                alert("File saving failed!");
            } catch (TransformerException ex) {
                ex.printStackTrace();
                alert("File saving failed!");
            }
        }
    }

    public void openFileSelected(File openFile) {
        if (openFile != null) { //If the file exists.
            if (!openFile.getPath().endsWith(".oscl")) { //Check if the file is a .oscl filetype.
                alert("Please select a valid .oscl file.");
                return;
            }
            //Remove all elements currently in the window to load in the new layout. Then, configure the window.
            UIManager.getMgr().getElements().clear();
            UIManager.getMgr().setEditMode(false);
            UIManager.getMgr().setDeleteMode(false);
            surface.setResizable(true);
            mode = AppMode.RUN;
            ModeButton mo = new ModeButton(85, 25);

            Document doc;
            try {
                //DBF creates new DocumentBuilder. DB is then used to parse the file which has been selected.
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = db.parse(openFile);

                //Find the root element.
                Element root = doc.getDocumentElement();
                NodeList settings = doc.getElementsByTagName("settings");

                //Default values if size values are not found.
                int sizeX = 1050;
                int sizeY = 650;
                /*
                Each type of element is returned as a NodeList object for all occurrences in the chosen element. These
                are looped through to find the relevant info. Each level of elements must be looped through as a direct
                path cannot be taken to find the relevant info.
                 */
                NodeList settingsList = root.getElementsByTagName("settings");
                for (int i = 0; i < settingsList.getLength(); i++) {
                    if (settingsList.item(i).getNodeType() == Node.ELEMENT_NODE) { //If the node is a XML element.
                        Element settingsElements = (Element) settingsList.item(i);
                        NodeList settingsList2 = settingsElements.getChildNodes();
                        for (int j = 0; j < settingsList2.getLength(); j++) {
                            if (settingsList2.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element e = (Element) settingsList2.item(j);
                                switch (e.getTagName()) { //Find saved width and height of the window.
                                    case "size_x":
                                        sizeX = Integer.parseInt(e.getTextContent());
                                        break;
                                    case "size_y":
                                        sizeY = Integer.parseInt(e.getTextContent());
                                        break;
                                }
                            }
                        }
                    }
                }
                surface.setSize(sizeX, sizeY); //Set the size of the window to the values found.

                NodeList elementList = root.getElementsByTagName("elements"); //List of all UI elements saved.
                for (int i = 0; i < elementList.getLength(); i++) {
                    if (elementList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element uiElements = (Element) elementList.item(i);
                        NodeList loopList;

                        loopList = uiElements.getElementsByTagName("mute");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <mute> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;
                            int channel = 0;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "channel":
                                                channel = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                MuteButton ui = new MuteButton(x, y, channel);
                            }
                        }

                        loopList = uiElements.getElementsByTagName("solo");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <solo> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;
                            int channel = 0;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "channel":
                                                channel = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                SoloButton ui = new SoloButton(x, y, channel);
                            }
                        }

                        loopList = uiElements.getElementsByTagName("record_arm");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <record_arm> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;
                            int channel = 0;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "channel":
                                                channel = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                RecordArmButton ui = new RecordArmButton(x, y, channel);
                            }
                        }

                        loopList = uiElements.getElementsByTagName("fader");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <fader> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;
                            int channel = 0;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "channel":
                                                channel = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                Fader ui = new Fader(x, y, channel);
                            }
                        }

                        loopList = uiElements.getElementsByTagName("pan");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <pan> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;
                            int channel = 0;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "channel":
                                                channel = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                Pan ui = new Pan(x, y, channel);
                            }
                        }

                        loopList = uiElements.getElementsByTagName("timecode");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <time> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                Timecode ui = new Timecode(x, y);
                            }
                        }

                        loopList = uiElements.getElementsByTagName("play");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <play> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                TransportButton ui = new TransportButton(x, y, TransportButtonType.PLAY);
                            }
                        }

                        loopList = uiElements.getElementsByTagName("click");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <click> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                TransportButton ui = new TransportButton(x, y, TransportButtonType.CLICK);
                            }
                        }

                        loopList = uiElements.getElementsByTagName("loop");
                        for (int j = 0; j < loopList.getLength(); j++) { //Loop through all <loop> XML elements.
                            /*
                            Variables must be initialized to make the UI element - cannot be null. UIElement
                            checks these values before adding the UIElement to the ArrayList of elements.
                            */
                            int x = -1000;
                            int y = -1000;

                            if (loopList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element uiXMLElement = (Element) loopList.item(j);
                                NodeList loopList2 = uiXMLElement.getChildNodes();
                                for (int k = 0; k < loopList2.getLength(); k++) {
                                    if (loopList2.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Element e = (Element) loopList2.item(k);
                                        switch (e.getTagName()) {
                                            case "x":
                                                x = Integer.parseInt(e.getTextContent());
                                                break;
                                            case "y":
                                                y = Integer.parseInt(e.getTextContent());
                                                break;
                                        }
                                    }
                                }
                                TransportButton ui = new TransportButton(x, y, TransportButtonType.LOOP);
                            }
                        }
                    }
                }

                //Catches any errors. If there are, print the stacktrace and warn the user.
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                alert("File loading failed!");
            } catch (SAXException e) {
                e.printStackTrace();
                alert("File loading failed!");
            } catch (IOException e) {
                e.printStackTrace();
                alert("File loading failed!");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                alert("File loading failed!");
            }
        }
        reloadData(); //Reload the OSC data for the new layout - max channels may have increased.
    }

    /*
    Saves X and Y data for an element into XML.
    Adapted from: https://stackoverflow.com/questions/7373567/how-to-read-and-write-xml-files
    */
    private Element createXYData(Element uiXmlElement, UIElement uiElement, Document doc) {
        Element e;

        e = doc.createElement("x");
        e.appendChild(doc.createTextNode(uiElement.getX() + ""));
        uiXmlElement.appendChild(e);

        e = doc.createElement("y");
        e.appendChild(doc.createTextNode(uiElement.getY() + ""));
        uiXmlElement.appendChild(e);

        return uiXmlElement;
    }

    private void alert(String message) {
        //Code for message box from: https://processing.org/discourse/beta/num_1262441938.html
        JOptionPane.showMessageDialog(null, message);
    }


    //Getters and setters.
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