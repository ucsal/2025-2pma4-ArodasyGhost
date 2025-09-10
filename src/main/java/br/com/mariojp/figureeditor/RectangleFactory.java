package br.com.mariojp.figureeditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class RectangleFactory implements ShapeFactory{

	@Override
	public Shape createShape(Point start, Rectangle bounds) {
		// TODO Auto-generated method stub
		return new Rectangle2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
	}

}
