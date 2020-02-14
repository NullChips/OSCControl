package ga.tomj.osccontrol.gui;

public class Button extends UIElement {

    int sizeX, sizeY;
    String label;

    boolean pressActive;
    boolean pressed;

    public Button(int x, int y, String label) {
        super(x, y);
        sizeX = 50;
        sizeY = 35;
        this.label = label;
        this.pressActive = false;
        this.pressed = false;
    }

    private int port;

    public Button(int x, int y, int sizeX, int sizeY, String label) {
        super(x, y);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.label = label;
        this.pressActive = false;
        this.pressed = false;
    }

    public void render() {

        if (mouseInButton()) {
            if (app.mousePressed) {
                if (!pressActive) {
                    pressed = !pressed;
                    pressActive = true;
                }
            }
            if (pressActive && !app.mousePressed) pressActive = false;
            app.fill(64, 0, 0);
            app.stroke(255, 0, 0);
            app.strokeCap(app.ROUND);
            app.strokeWeight(2);
            app.rectMode(app.CENTER);
            app.textAlign(app.CENTER, app.CENTER);
            app.textSize(12);
            app.rect(x, y, sizeX, sizeY, 3);
            app.fill(255, 255, 255);
            app.text(label, x, y - 2);
            return;
        } else {
            pressActive = false;
            app.noFill();
            app.stroke(255, 0, 0);
            app.strokeCap(app.ROUND);
            app.strokeWeight(2);
            app.rectMode(app.CENTER);
            app.textAlign(app.CENTER, app.CENTER);
            app.textSize(12);
            app.rect(x, y, sizeX, sizeY);
            app.stroke(255, 255, 255);
            app.text(label, x, y - 2);
        }
    }

    private boolean mouseInButton() {
        return app.mouseX - x + sizeX / 2 >= 0 && app.mouseX - x + sizeX / 2 <= sizeX &&
                app.mouseY - y + sizeY / 2 >= 0 && app.mouseY - y + sizeY / 2 <= sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public boolean isPressed() {
        return pressed;
    }
}
