package be.hogent.tarsos.dsp.example.visualisation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import be.hogent.tarsos.dsp.example.visualisation.layers.Layer;



public class LinkedPanel extends JPanel {

	private static final long serialVersionUID = -5055686566048886896L;

	private final List<Layer> layers;
	private final ViewPort viewPort;
	private final CoordinateSystem cs;

	public LinkedPanel(CoordinateSystem coordinateSystem) {
		super();
		setMinimumSize(new Dimension(100,100));
		layers = new ArrayList<Layer>();
		cs = coordinateSystem;
		viewPort = new ViewPort(this.cs);
		this.setVisible(true);
	}
	
	public CoordinateSystem getCoordinateSystem() {
		return cs;
	}

	public ViewPort getViewPort() {
		return viewPort;
	}

	public void addLayer(Layer l) {
		this.layers.add(l);	
		if(l instanceof MouseMotionListener){
			this.addMouseMotionListener((MouseMotionListener) l);
		}
		if(l instanceof MouseListener){
			this.addMouseListener((MouseListener) l);
		}
		if(l instanceof MouseWheelListener){
			this.addMouseWheelListener((MouseWheelListener) l);
		}
	}
	
	public void removeLayer(Layer layer) {
		layers.remove(layer);
	}
	
	public void removeLayers(){
		this.layers.clear();
	}

	public AffineTransform getTransform() {
		double xDelta = cs.getDelta(Axis.X);
		double yDelta = cs.getDelta(Axis.Y);
		AffineTransform transform = new AffineTransform();
		transform.translate(0, getHeight());
		transform.scale(getWidth() / xDelta, -getHeight() / yDelta);
		transform.translate(-cs.getMin(Axis.X),-cs.getMin(Axis.Y));
		return transform;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D graphics = (Graphics2D) g.create();
		graphics.setTransform(this.updateTransform(graphics.getTransform()));
		if (!layers.isEmpty()) {
			for (Layer layer : layers) {
				layer.draw(graphics);
			}
		}
	}

	public AffineTransform updateTransform(AffineTransform transform) {
		double xDelta = cs.getDelta(Axis.X);
		double yDelta = cs.getDelta(Axis.Y);
		transform.translate(0, getHeight());
		transform.scale(getWidth() / xDelta, -getHeight() / yDelta);
		transform.translate(-cs.getMin(Axis.X),-cs.getMin(Axis.Y));
		return transform;
	}
}
