package ga.tomj.osccontrol.gui;

import oscP5.OscMessage;

import java.util.ArrayList;

public class Fader extends UIElement {

    //Global variables.
    private int faderPercent;
    private int minY, maxY;
    private int rHeight, faderHeight, faderWidth;
    private int rY;
    private int channelNumber;
    private int vuL, vuR;
    private String trackName;

    //Constructor, sets some default values.
    public Fader(int x, int y, int channelNumber) {
        super(x, y);
        this.faderPercent = 0;
        this.rHeight = 20;
        this.faderHeight = 225;
        this.faderWidth = 35;
        this.channelNumber = channelNumber;
        this.vuL = 0;
        this.vuR = 0;
        //The fader rectangle should never go above or below the end of the fader line. minY and maxY calculate the max
        //and min Y values which the rectangle can be drawn at in order to satisfy this.
        this.minY = y - (faderHeight / 2) + (rHeight / 2);
        this.maxY = y + (faderHeight / 2) - (rHeight / 2);
        this.trackName = "Track " + channelNumber; //Default track name. Will be updated from Reaper if track present.
        this.editable = true;
    }

    //Draw the entire fader.
    public void render() {
        if (!UIManager.getMgr().isEditMode()) drawVU(); //Only draw VU if not in edit mode - too distracting.
        drawFader();
        drawTrackName();
    }


    private void drawTrackName() {
        //Code below calculates the length of each line of text. If beyond 3 characters, a new line of text is created for the name.
        String[] splitName = trackName.trim().split(" "); //Splits name into words by splitting at spaces. Trims off any empty spaces at start or end.
        ArrayList<String> lines = new ArrayList<>();
        String currentLine = null; //Current line being added to.
        for (String s : splitName) { //Loop through every word in
            if (currentLine != null && currentLine.length() > 3) { //If currentLine is longer than 3 characters, create a new line.
                lines.add(currentLine);
                currentLine = null;
            }
            if (currentLine == null) { //If currentLine is empty, add on the new word.
                currentLine = s;
            } else {
                currentLine += " " + s;
            }
        }
        lines.add(currentLine); //Add on whatever is left when the loop finishes.

        //Set text properties.
        app.fill(255);
        app.textSize(10);
        app.shapeMode(app.CENTER);

        int lineNo = 1;
        for (String s : lines) { //Loop through every line and draw text on a new line.
            int lineY = (y + (faderHeight / 2) + 10) + (lineNo * 10);
            app.text(s, x, lineY);
            lineNo++;
        }
    }

    //Draw the fader part of the fader.
    private void drawFader() {
        if (UIManager.getMgr().getRecentElement() == this && app.frameCount / 20 % 2 == 0 && UIManager.getMgr().isEditMode()) {
            //Create flashing background in edit mode if this is the most recently selected element.
            app.noStroke();
            app.shapeMode(app.CENTER);
            app.fill(64, 192);
            app.rect(x, y, faderWidth + 2, faderHeight + 4);
        }
        app.strokeWeight(6);
        app.stroke(64);
        app.line(x, y - faderHeight / 2, x, y + faderHeight / 2);
        app.strokeWeight(3);
        app.stroke(192);
        app.shapeMode(app.CENTER);
        rHeight = 20; //Height of fader rectangle.
        minY = y - (faderHeight / 2) + (rHeight / 2); //Calculate y bounds of rectangle.
        maxY = y + (faderHeight / 2) - (rHeight / 2);
        calcRectY();
        if (UIManager.getMgr().getElementDragged() == this) { //If fader is currently being moved, darken rectangle.
            app.fill(100, 100, 100, 127);
        } else {
            app.fill(192, 192, 192, 127);
        }
        app.rect(x, calcRectY(), faderWidth, rHeight, 5);
    }

    //Draw VU next to fader.
    private void drawVU() {
        //Calculate VU heights based on fader height.
        int heightL = (faderHeight / 100) * vuL;
        int heightR = (faderHeight / 100) * vuR;

        app.strokeWeight(2);
        app.stroke(0, 255, 0);
        if (heightL > 0) { //If there is no volume through the VU, don't draw - stops dots appearing from rounded caps.
            app.line(x - 10, y + (faderHeight / 2), x - 10, (y + (faderHeight / 2)) - heightL);
        }
        if (heightR > 0) {
            app.line(x + 10, y + (faderHeight / 2), x + 10, (y + (faderHeight / 2)) - heightR);
        }
    }

    //Calculate where the fader rectangle needs to be based on the fader percent. Return this Y value as an int.
    private int calcRectY() {
        float minYF = (float) minY;
        float maxYF = (float) maxY;
        float rYF = minY + ((maxYF - minYF) / 100) * (100 - faderPercent);
        rY = (int) rYF;
        return rY;
    }


    //Calculate the percentage of the fader when given a new Y value of the rectangle - called when rectangle is dragged.
    private int calcFaderPercentage(int newY) {
        float minYF = (float) minY - faderHeight;
        float maxYF = (float) maxY - faderHeight;
        float newYF = (float) newY - faderHeight;
        float percF = ((maxYF - newYF) / (maxYF - minYF) * 100); //Percentage float.
        faderPercent = (int) percF; //Convert to int.
        return faderPercent;

    }

    //Set the fader level when the mouse is clicked in the fader. Allows for quick switching of levels.
    public void mousePressed() {
        setOffsets();
        if (mouseInElement()) {
            UIManager.getMgr().setRecentElement(this);
            if (!UIManager.getMgr().isEditMode()) {
                if (app.mouseY > minY && app.mouseY < maxY) {
                    calcFaderPercentage(app.mouseY);
                    sendOscMessage();
                } else if (app.mouseY < minY) { //If mouseY is below the min value for rectangle Y, set the fader to 0%.
                    faderPercent = 0;
                    sendOscMessage();
                } else { //If mouseY is above the max value for rectangle Y, set the fader to 100%.
                    faderPercent = 100;
                    sendOscMessage();
                }
            }
        }
    }


    public void mouseDragged() {
        super.mouseDragged();
        if (!UIManager.getMgr().isEditMode() && UIManager.getMgr().getElementDragged() == this) { //If fader is currently being dragged.
            if (app.mouseY > minY && app.mouseY < maxY) { //If the mouse is within the Y bounds of the rectangle, then calculate the percentage from its position.
                calcFaderPercentage(app.mouseY);
                sendOscMessage();
            } else if (app.mouseY < minY) { //If mouse goes too high, set percent to 100.
                faderPercent = 100;
                sendOscMessage();
            } else { //If mouse goes too low, set percent to 0.
                faderPercent = 0;
                sendOscMessage();
            }
        }

        if (mouseInElement() && !UIManager.getMgr().isEditMode() && UIManager.getMgr().getElementDragged() == null) {
            //If the fader isn't yet set as the element that is being dragged, set it to this and calculate the percentage.
            UIManager.getMgr().setElementDragged(this);
            calcFaderPercentage(app.mouseY);
        }
    }

    //Reset on double click.
    public void doubleClick() {
        if (mouseInElement() && !UIManager.getMgr().isEditMode()) {
            faderPercent = 71; //This is the value which gives a level of 0 in Reaper.
            sendOscMessage();
            app.setDoubleClick(false); //Cancel double click.
        }
    }

    //Send an OSC message to Reaper with the current percentage value in a float from 0.0 to 1.0.
    private void sendOscMessage() {
        float f = faderPercent;
        f = f / 100;
        OscMessage message = new OscMessage("/track/" + channelNumber + "/volume");
        message.add(f);
        app.getOscp5().send(message, app.getReaperAddr());
    }

    //Calculate position of mouse relative to x and y and return if the mouse is in the fader.
    public boolean mouseInElement() {
        return app.mouseX - x + faderWidth / 2 >= 0 && app.mouseX - x + faderWidth / 2 <= faderWidth &&
                app.mouseY - y + faderHeight / 2 >= 0 && app.mouseY - y + faderHeight / 2 <= faderHeight;
    }

    //Getters and setters.
    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public void setFaderPercent(int faderPercent) {
        this.faderPercent = faderPercent;
    }

    public int getVuL() {
        return vuL;
    }

    public void setVuL(int vuL) {
        this.vuL = vuL;
    }

    public int getVuR() {
        return vuR;
    }

    public void setVuR(int vuR) {
        this.vuR = vuR;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
}
