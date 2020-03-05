package ga.tomj.osccontrol.gui;

import oscP5.OscMessage;

public class Pan extends UIElement {

    //Global variables.
    private int diameter;
    private int channelNumber;
    private int mouseY;
    private int percent;
    private int previousPercent;
    private int lineX, lineY;

    //Constructor.
    public Pan(int x, int y, int channelNumber) {
        super(x, y);
        this.channelNumber = channelNumber;
        this.diameter = 30; //Default diameter.
        this.editable = true;
        this.percent = 50; //Default percentage when first drawn.
    }

    public void render() {
        if (UIManager.getMgr().getRecentElement() == this && app.frameCount / 20 % 2 == 0 && UIManager.getMgr().isEditMode()) {
            //Flashing background when the most recently clicked element.
            app.noStroke();
            app.shapeMode(app.CENTER);
            app.fill(64, 192);
            app.rect(x, y, diameter + 5, diameter + 5);
        }

        app.stroke(80);
        if (UIManager.getMgr().getElementDragged() == this) { //If the pan is currently being moved, fill the background darker.
            app.fill(128);
        } else {
            app.fill(200);
        }
        //Draw the circle of the pan.
        app.shapeMode(app.CENTER);
        app.strokeWeight(2);
        app.circle(x, y, diameter);
        app.strokeWeight(2);

        //Convert the percentage into degrees, then into radians. Double used due to Math.PI being a double.
        double degree = ((percent - 50) * 3) - 90;
        double rad = degree * (Math.PI / 180);

        //Calculate the X and Y of the lines within a cirle.
        lineX = (int) ((diameter / 3) * Math.cos(rad));
        lineY = (int) ((diameter / 3) * Math.sin(rad));
        //Add X and Y to the circle coordinates calculated.
        lineX += x;
        lineY += y;
        //Draw line.
        app.line(x, y, lineX, lineY);
    }

    public void mousePressed() {
        setOffsets();
        if (mouseInElement()) {
            //Set
            UIManager.getMgr().setRecentElement(this);
            mouseY = app.mouseY; //Get the mouse Y position when the pan is first clicked - before a drag.
            previousPercent = percent; //Store the current percent, ready for a new percent to be given.
        }
    }

    //Check mouse position relative to the x and y coords of the pan.
    public boolean mouseInElement() {
        return app.mouseX - x + diameter / 2 >= 0 && app.mouseX - x + diameter / 2 <= diameter &&
                app.mouseY - y + diameter / 2 >= 0 && app.mouseY - y + diameter / 2 <= diameter;
    }

    public void mouseDragged() {
        super.mouseDragged();
        //If the element being dragged is this pan, and edit mode is not active, control the pan.
        if (!UIManager.getMgr().isEditMode() && UIManager.getMgr().getElementDragged() == this) {
            app.noCursor(); //Hide the cursor.
            UIManager.getMgr().setElementDragged(this);
            int difference = mouseY - app.mouseY; //Calculate the difference between the initial position of the mouse and the current position.
            percent = previousPercent + (difference / 3); //Use this difference to calculate the new percentage.

            //Stop the percentage from going above 100 or below 0.
            if (percent > 100) {
                percent = 100;
            } else if (percent < 0) {
                percent = 0;
            }
            sendOscMessage(); //Send the new data to Reaper.
        }

        if (mouseInElement() && !UIManager.getMgr().isEditMode() && UIManager.getMgr().getElementDragged() == null) {
            //If the element being dragged has not yet been set to this, set it to this.
            UIManager.getMgr().setElementDragged(this);
        }
    }

    public void doubleClick() {
        if (mouseInElement() && !UIManager.getMgr().isEditMode()) { //On double click, reset to 50%.
            percent = 50;
            sendOscMessage();
            app.setDoubleClick(false); //Reset double click.
        }
    }

    //Converts the percentage into a float from 0.0 to 1.0 and sends this vaule to Reaper.
    private void sendOscMessage() {
        OscMessage message = new OscMessage("/track/" + channelNumber + "/pan");
        float f = percent;
        f = f / 100;
        message.add(f);
        app.getOscp5().send(message, app.getReaperAddr());
    }

    //Getters and setters.
    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }
}
