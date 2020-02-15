package ga.tomj.osccontrol.gui;

import oscP5.OscMessage;

public class Fader extends UIElement {

    private int faderPercent;
    private int minY, maxY;
    private int rHeight, faderHeight, faderWidth;
    private int rY;
    private int channelNumber;
    private int vuL, vuR;

    public Fader(int x, int y, int channelNumber) {
        super(x, y);
        this.faderPercent = 0;
        this.rHeight = 20;
        this.faderHeight = 225;
        this.faderWidth = 35;
        this.channelNumber = channelNumber;
        this.vuL = 0;
        this.vuR = 0;
        this.minY = y - (faderHeight / 2) + (rHeight / 2);
        this.maxY = y + (faderHeight / 2) - (rHeight / 2);
        this.editable = true;
    }

    public void render() {
        if(!UIManager.getMgr().isEditMode()) drawVU();
        app.strokeWeight(6);
        app.stroke(64);
        app.line(x, y - faderHeight / 2, x, y + faderHeight / 2);
        app.strokeWeight(3);
        app.stroke(192);
        app.shapeMode(app.CENTER);
        rHeight = 20;
        minY = y - (faderHeight / 2) + (rHeight / 2);
        maxY = y + (faderHeight / 2) - (rHeight / 2);
        calcRectY();
        app.fill(192, 192, 192, 127);
        app.rect(x, calcRectY(), faderWidth, rHeight, 5);
    }

    private void drawVU() {
        int heightL = (faderHeight / 100) * vuL;
        int heightR = (faderHeight / 100) * vuR;

        app.strokeWeight(2);
        app.stroke(0, 255, 0);
        if (heightL > 0) app.line(x - 10, y + (faderHeight / 2), x - 10, (y + (faderHeight / 2)) - heightL);
        if (heightR > 0) app.line(x + 10, y + (faderHeight / 2), x + 10, (y + (faderHeight / 2)) - heightR);
    }

    private int calcRectY() {
        float minYF = (float) minY;
        float maxYF = (float) maxY;
        float rYF = (int) minY + ((maxYF - minYF) / 100) * (100 - faderPercent);
        rY = (int) rYF;
        return rY;
    }

    private int calcFaderPercentage(int newY) {
        float minYF = (float) minY - faderHeight;
        float maxYF = (float) maxY - faderHeight;
        float newYF = (float) newY - faderHeight;
        float percF = ((maxYF - newYF) / (maxYF - minYF) * 100);
        faderPercent = (int) percF;
        return faderPercent;

    }

    public void mousePressed() {
        setOffsets();
        if (!UIManager.getMgr().isEditMode() && mouseInElement()) {
            if (app.mouseY > minY && app.mouseY < maxY) {
                calcFaderPercentage(app.mouseY);
                sendOscMessage();
            } else if (app.mouseY < minY) {
                faderPercent = 0;
                sendOscMessage();
            } else {
                faderPercent = 100;
                sendOscMessage();
            }
        }
    }

    @Override
    public void mouseDragged() {
        super.mouseDragged();
        if (!UIManager.getMgr().isEditMode() && UIManager.getMgr().getElementDragged() == this) {
            if (app.mouseY > minY && app.mouseY < maxY) {
                calcFaderPercentage(app.mouseY);
                sendOscMessage();
            } else if (app.mouseY < minY) {
                faderPercent = 100;
                sendOscMessage();

            } else {
                faderPercent = 0;
                sendOscMessage();
            }
        }

        if (mouseInElement() && !UIManager.getMgr().isEditMode() && UIManager.getMgr().getElementDragged() == null) {
            UIManager.getMgr().setElementDragged(this);
            calcFaderPercentage(app.mouseY);
        }
    }

    private void sendOscMessage() {
        float f = faderPercent;
        f = f / 100;
        OscMessage message = new OscMessage("/track/" + channelNumber + "/volume");
        message.add(f);
        app.getOscp5().send(message, app.getReaperAddr());
    }

    public boolean mouseInElement() {
        return app.mouseX - x + faderWidth / 2 >= 0 && app.mouseX - x + faderWidth / 2 <= faderWidth &&
                app.mouseY - y + faderHeight / 2 >= 0 && app.mouseY - y + faderHeight / 2 <= faderHeight;
    }

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
}
