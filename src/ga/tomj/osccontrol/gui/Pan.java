package ga.tomj.osccontrol.gui;

import oscP5.OscMessage;

public class Pan extends UIElement {

    private int diameter;
    private int channelNumber;
    private int mouseY;
    private int percent;
    private int previousPercent;
    private int lineX, lineY;

    public Pan(int x, int y, int channelNumber) {
        super(x, y);
        this.channelNumber = channelNumber;
        this.diameter = 30;
        this.editable = true;
        this.percent = 50;
    }

    public void render() {
        if (UIManager.getMgr().getRecentElement() == this && app.frameCount / 20 % 2 == 0) {
            app.noStroke();
            app.shapeMode(app.CENTER);
            app.fill(64, 192);
            app.rect(x, y, diameter + 3, diameter + 3);
        }
        app.stroke(80);
        if (UIManager.getMgr().getElementDragged() == this) {
            app.fill(128);
        } else {
            app.fill(200);
        }
        app.shapeMode(app.CENTER);
        app.strokeWeight(2);
        app.circle(x, y, diameter);
        app.strokeWeight(2);

        double degree = ((percent - 50) * 3) - 90;
        double rad = degree * (Math.PI / 180);

        lineX = (int) ((diameter / 3) * Math.cos(rad));
        lineY = (int) ((diameter / 3) * Math.sin(rad));
        lineX = lineX + x;
        lineY = lineY + y;

        app.line(x, y, lineX, lineY);
    }

    public void mousePressed() {
        setOffsets();
        if (mouseInElement()) {
            UIManager.getMgr().setRecentElement(this);
            i = 0;
            mouseY = app.mouseY;
            previousPercent = percent;
        }
    }

    public boolean mouseInElement() {
        return app.mouseX - x + diameter / 2 >= 0 && app.mouseX - x + diameter / 2 <= diameter &&
                app.mouseY - y + diameter / 2 >= 0 && app.mouseY - y + diameter / 2 <= diameter;
    }

    int i;

    public void mouseDragged() {
        super.mouseDragged();
        if (!UIManager.getMgr().isEditMode() && UIManager.getMgr().getElementDragged() == this) {
            app.noCursor();
            UIManager.getMgr().setElementDragged(this);
            i++;
            int difference = mouseY - app.mouseY;
            percent = previousPercent + (difference / 3);

            if (percent > 100) percent = 100;
            else if (percent < 0) percent = 0;

            sendOscMessage();
        }

        if (mouseInElement() && !UIManager.getMgr().isEditMode() && UIManager.getMgr().getElementDragged() == null) {
            UIManager.getMgr().setElementDragged(this);
        }
    }

    public void doubleClick() {
        if (mouseInElement() && !UIManager.getMgr().isEditMode()) {
            percent = 50;
            sendOscMessage();
            app.setDoubleClick(false);
        }
    }

    private void sendOscMessage() {
        OscMessage message = new OscMessage("/track/" + channelNumber + "/pan");
        float f = percent;
        f = f / 100;
        message.add(f);
        app.getOscp5().send(message, app.getReaperAddr());
    }

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
