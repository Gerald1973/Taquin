/*
 * Created on Jan 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.smilesmile1973.taquin.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.smilesmile1973.taquin.model.Plate;
import com.smilesmile1973.taquin.model.PlateEvent;
import com.smilesmile1973.taquin.model.PlateListener;
import com.smilesmile1973.taquin.worker.Fader;
import com.smilesmile1973.taquin.worker.Shaker;

/**
 * @author marechal.g
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Display extends Panel implements MouseListener,PlateListener {
	@Override
	public void mouseClicked(final MouseEvent arg0) {

	}
	@Override
	public void mouseEntered(final MouseEvent arg0) {}
	@Override
	public void mouseExited(final MouseEvent arg0) {}
	@Override
	public void mousePressed(final MouseEvent arg0) {
		this.model.swapPiece(arg0.getX(),arg0.getY() );
		this.model.refresh();
	}
	@Override
	public void mouseReleased(final MouseEvent arg0) {
		this.model.verify();
	}

 private Plate model;
 private Image image;
/**
 * @return Returns the model.
 */
public Plate getModel() {
	return this.model;
}
/**
 * @param model The model to set.
 */
public void setModel(final Plate model) {
	this.model = model;
}
 public Display(final Plate model){
 	super();
 	this.setModel(model);
 	this.getModel().addPlateListener(this);
 	this.image = this.createImage(model.getOrderedImage());
 	this.addMouseListener(this);
 	final Shaker shaker = new Shaker(model,5000,10);
 	shaker.start();
 }
 @Override
public void update(final Graphics g){
 	this.paint(g);
 }
 @Override
public void paint(final Graphics g){
 	g.drawImage(this.image,0,0,this);
 }
 	@Override
	public void performPlateAction(final PlateEvent e) {
		final Plate plate = (Plate) e.getSource();
		if (plate.isVerifier()){
			int data[];
			final int index = plate.getNumber()-1;
			data = plate.getFullDataImage();
			final Fader fader = new Fader(data,plate.getImage(),5000,Fader.TRANSPARENT_SOLID,plate.getSize(),this);
			fader.start();
			fader.join();
		}
	}
public Image getImage() {
	return this.image;
}
public void setImage(final Image image) {
	this.image = image;
}
}
