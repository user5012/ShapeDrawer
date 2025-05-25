package shape_drawer.shape;

import java.awt.*;

public class Rectangle extends MyShape {
    private int width;
    private int height;

    public Rectangle(int width, int height, int x, int y) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void draw(Graphics g) {
        super.draw(g); // Call the parent draw method to set color based on selection
        int topLeftX = getX() - width / 2; // Center the rectangle
        int topLeftY = getY() - height / 2; // Center the rectangle
        g.fillRect(topLeftX, topLeftY, width, height); // Draw the rectangle
    }

    @Override
    public boolean isPointInside(int pointX, int pointY) {
        return pointX >= getX() && pointX <= getX() + width && pointY >= getY() && pointY <= getY() + height;
    }
}