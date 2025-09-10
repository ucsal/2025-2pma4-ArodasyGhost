package br.com.mariojp.figureeditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

public interface ShapeFactory {

Shape createShape(Point start, Rectangle bounds);
	
}
