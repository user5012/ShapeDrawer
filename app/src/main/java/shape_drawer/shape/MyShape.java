package shape_drawer.shape;

import java.awt.*;

public abstract class MyShape {
    private int x;
    private int y;
    private boolean _isSelected;
    private Color color = Color.BLACK; // Default color

    public MyShape(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return isSelected() ? Color.RED : Color.BLACK; // Change color based on selection
    }

    public void draw(Graphics g) {
        g.setColor(color);
    }

    public boolean isPointInside(int pointX, int pointY) {
        return false;
    }

    public void selectShape() {
        _isSelected = true;
        // change color to indicate selection
        color = Color.RED; // Change color to red when selected

    }

    public void deselectShape() {
        _isSelected = false;
        // change color to indicate deselection
        color = Color.BLACK; // Change color back to black when deselected
    }

    public boolean isSelected() {
        return _isSelected;
    }
}
