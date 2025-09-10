
package br.com.mariojp.figureeditor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.awt.geom.*;
import java.awt.event.*;

class DrawingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final List<ShapeItem> shapes = new ArrayList<>();
	private ShapeItem selectedShape = null;

	private Point startDrag = null;
	private Point currentDrag = null;

	private Color currentColor = new Color(30, 144, 255);
	private int currentLayer = 0;

	private ShapeFactory currentFactory = new EllipseFactory();

	private Point lastMousePos = null;

	private static final int SNAP_DISTANCE = 10;

	public DrawingPanel() {

		setBackground(Color.WHITE);
		setOpaque(true);
		setDoubleBuffered(true);

		MouseAdapter mouse = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
				selectedShape = findShapeAtPoint(p);
				if (selectedShape != null) {
					lastMousePos = p;
					bringToFront(selectedShape);
					repaint();
				} else {
					startDrag = p;
					currentDrag = p;
					selectedShape = null;
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = e.getPoint();
				if (startDrag != null) {
					currentDrag = p;
					repaint();
				} else if (selectedShape != null && lastMousePos != null) {
					int dx = p.x - lastMousePos.x;
					int dy = p.y - lastMousePos.y;

					Point2D snapped = snapToGrid(selectedShape, dx, dy);
					dx = (int) snapped.getX();
					dy = (int) snapped.getY();

					selectedShape.move(dx, dy);
					lastMousePos = p;
					repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (startDrag != null && currentDrag != null) {
					Rectangle rect = makeRectangle(startDrag, currentDrag);
					if (rect.width >= 10 && rect.height >= 10) {
						Shape shape = currentFactory.createShape(startDrag, rect);
						ShapeItem newShape = new ShapeItem(shape, currentColor, ++currentLayer);
						shapes.add(newShape);
					}
				}
				startDrag = null;
				currentDrag = null;
				lastMousePos = null;
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					Point p = e.getPoint();
					ShapeItem shape = findShapeAtPoint(p);
					if (shape != null) {
						selectedShape = shape;
						repaint();
					} else {
						selectedShape = null;
						repaint();
					}
				}
			}
		};

		addMouseListener(mouse);
		addMouseMotionListener(mouse);

	}

	private Rectangle makeRectangle(Point p1, Point p2) {
		int x = Math.min(p1.x, p2.x);
		int y = Math.min(p1.y, p2.y);
		int w = Math.abs(p1.x - p2.x);
		int h = Math.abs(p1.y - p2.y);
		return new Rectangle(x, y, w, h);
	}

	private ShapeItem findShapeAtPoint(Point p) {
		List<ShapeItem> reversed = new ArrayList<>(shapes);
		Collections.reverse(reversed);
		for (ShapeItem s : reversed) {
			if (s.contains(p)) {
				return s;
			}
		}
		return null;
	}

	private void bringToFront(ShapeItem shape) {
		shapes.remove(shape);
		shape.setLayer(++currentLayer);
		shapes.add(shape);
	}

	private Point2D snapToGrid(ShapeItem shape, int dx, int dy) {
		Rectangle bounds = shape.getBounds();
		int newX = bounds.x + dx;
		int newY = bounds.y + dy;
		for (ShapeItem other : shapes) {
			if (other == shape)
				continue;
			Rectangle ob = other.getBounds();
			if (Math.abs(newX - ob.x) <= SNAP_DISTANCE) {
				newX = ob.x;
			} else if (Math.abs(newX - (ob.x + ob.width)) <= SNAP_DISTANCE) {
				newX = ob.x + ob.width;
			}
			if (Math.abs(newY - ob.y) <= SNAP_DISTANCE) {
				newY = ob.y;
			} else if (Math.abs(newY - (ob.y + ob.height)) <= SNAP_DISTANCE) {
				newY = ob.y + ob.height;
			}
		}
		return new Point2D.Double(newX - bounds.x, newY - bounds.y);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		shapes.sort(Comparator.comparingInt(ShapeItem::getLayer));

		for (ShapeItem s : shapes) {
			g2.setColor(s.getColor());
			g2.fill(s.getShape());
			g2.setColor(new Color(0, 0, 0, 70));
			g2.setStroke(new BasicStroke(1.2f));
			g2.draw(s.getShape());
		}

		if (startDrag != null && currentDrag != null) {
			Rectangle rect = makeRectangle(startDrag, currentDrag);
			Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
			g2.setStroke(dashed);
			g2.setColor(Color.GRAY);
			g2.drawOval(rect.x, rect.y, rect.width, rect.height);
		}
		if (selectedShape != null) {
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(2));
			g2.draw(selectedShape.getShape());
		}

		g2.dispose();
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(Color currentColor) {
		this.currentColor = currentColor;
	}

	public void setShapeFactory(ShapeFactory factory) {
		this.currentFactory = factory;
	}

}
