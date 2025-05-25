package shape_drawer.shape;

import java.awt.*;

public class Circle extends MyShape {
    private int radius;

    public Circle(int radius, int x, int y) {
        super(x, y);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void draw(Graphics g) {
        super.draw(g); // Call the parent draw method to set color based on selection
        int topLeftX = getX() - radius;
        int topLeftY = getY() - radius;

        g.fillOval(topLeftX, topLeftY, radius * 2, radius * 2); // Draw the circle
    }

    @Override
    public boolean isPointInside(int pointX, int pointY) {
        int centerX = getX();
        int centerY = getY();
        return Math.pow(pointX - centerX, 2) + Math.pow(pointY - centerY, 2) <= Math.pow(radius, 2);
    }
}
