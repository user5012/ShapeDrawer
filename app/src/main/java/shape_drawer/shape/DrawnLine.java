package shape_drawer.shape;

import java.util.List;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class DrawnLine extends MyShape {
    private List<Point> points = new ArrayList<>();

    public DrawnLine() {
        super(0, 0);
    }

    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    public void draw(Graphics g) {
        super.draw(g); // Call the parent draw method to set color based on selection
        if (points.size() < 2) {
            return; // Not enough points to draw a line
        }

        Graphics2D g2d = (Graphics2D) g;
        if (isSelected()) {
            g2d.setColor(getColor());
        }

        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    @Override
    public boolean isPointInside(int x, int y) {
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            if (isPointNearLine(x, y, p1.x, p1.y, p2.x, p2.y, 5)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPointNearLine(int px, int py, int x1, int y1, int x2, int y2, int tolerance) {
        double distance = Line2D.ptSegDist(x1, y1, x2, y2, px, py);
        return distance <= tolerance;
    }

    @Override
    public int getX() {
        if (points.isEmpty()) {
            return super.getX();
        }
        return points.get(0).x;
    }

    @Override
    public int getY() {
        if (points.isEmpty()) {
            return super.getY();
        }
        return points.get(0).y;
    }

    @Override
    public void setX(int x) {
        if (points.isEmpty()) {
            return;
        }
        int deltaX = x - points.get(0).x;
        for (Point point : points) {
            point.x += deltaX;
        }
    }

    public void setY(int y) {
        if (points.isEmpty()) {
            return;
        }
        int deltaY = y - points.get(0).y;
        for (Point point : points) {
            point.y += deltaY;
        }
    }

}
