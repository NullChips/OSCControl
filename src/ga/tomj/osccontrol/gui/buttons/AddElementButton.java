package ga.tomj.osccontrol.gui.buttons;

public class AddElementButton extends Button {

    //ElementType defines the button text, colour, and what should happen when the button is pressed.
    private ElementType element;

    //Constructor. ElementType should be defined here. Size is predefined in the super constructor.
    public AddElementButton(int x, int y, ElementType element) {
        super(x, y, 110, 35, element.getLabel(), element.getColor());
        this.element = element;
        setPressed(true); //Used to in the rectangle.
    }

    //Constructor. ElementType should be defined here. Size must be defined in the arguments.
    public AddElementButton(int x, int y, int sizeX, int sizeY, ElementType element) {
        super(x, y, sizeX, sizeY, element.getLabel(), element.getColor());
        this.element = element;
        setPressed(true); //Used to fill in the rectangle.
    }

    public void mousePressed() {
        if (mouseInElement()) {
            //Run the mouseClicked() method in the ElementType enum.
            element.mouseClicked();
        }
    }
}
