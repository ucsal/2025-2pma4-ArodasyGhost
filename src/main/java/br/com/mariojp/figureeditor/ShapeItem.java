package br.com.mariojp.figureeditor;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;


public class ShapeItem {

	private Shape shape;
    private final Color color;
    private int layer;
    public ShapeItem(Shape shape, Color color, int layer) {
        this.shape = shape;
        this.color = color;
        this.layer = layer;
    }
    public Shape getShape() {
        return shape;
    }
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    
    public Color getColor() {
        return color;
    }
    public int getLayer() {
        return layer;
    }
    public void setLayer(int layer) {
        this.layer = layer;
    }
    public boolean contains(Point p) {
        return shape.contains(p);
    }
    public Rectangle getBounds() {
        return shape.getBounds();
    }
    public void move(double dx, double dy) {
        if (shape instanceof Rectangle2D) {
            Rectangle2D r = (Rectangle2D) shape;
            shape = new Rectangle2D.Double(r.getX() + dx, r.getY() + dy, r.getWidth(), r.getHeight());
        } else if (shape instanceof Ellipse2D) {
            Ellipse2D e = (Ellipse2D) shape;
            shape = new Ellipse2D.Double(e.getX() + dx, e.getY() + dy, e.getWidth(), e.getHeight());
        }
    }
}