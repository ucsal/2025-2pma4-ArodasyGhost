package br.com.mariojp.figureeditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class EllipseFactory implements ShapeFactory {

	@Override
	public Shape createShape(Point start, Rectangle bounds) {
		// TODO Auto-generated method stub
		return new Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
	}

}
