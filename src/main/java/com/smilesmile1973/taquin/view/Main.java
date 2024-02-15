/*
 * Created on Jan 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.smilesmile1973.taquin.view;

import java.net.URL;

import javax.swing.JFrame;

import com.smilesmile1973.taquin.model.Plate;
import com.smilesmile1973.taquin.model.PlateEvent;
import com.smilesmile1973.taquin.model.PlateListener;
import com.smilesmile1973.taquin.worker.Shaker;

import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;

/**
 * @author marechal.g
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Main extends JFrame implements PlateListener {
	private final Display display;
	private final Plate   plate;
	private URL url;
	private final int[] difficulties = {2,3,3,4,4,5,5,6,6,6,10};
	@Override
	public void performPlateAction(final PlateEvent e) {
		if (this.plate.isVerifier() && this.c < images.length-2){
			this.c++;
			this.url = this.getClass().getResource("/" + images[this.c]);
			this.plate.changeImage(this.difficulties[this.c],this.url.toString());
			this.display.setImage(this.createImage(this.plate.getOrderedImage()));
			this.display.update(this.display.getGraphics());
			final Shaker shaker = new Shaker(this.plate,2000,10);
			shaker.start();
			this.plate.refresh();
		}
	}
	private static String[] images = {
			"nickie_00.jpg",
			"nickie_01.jpg",
			"nickie_02.jpg",
			"nickie_03.jpg",
			"nickie_04.jpg",
			"nickie_05.jpg",
			"nickie_06.jpg",
			"nickie_07.jpg",
			"nickie_08.jpg",
			"nickie_09.jpg",
			"nickie_10.jpg"};
	private int c = 0;
	public Main(){
		this.url = this.getClass().getResource("/" + images[this.c]);
		this.plate = new Plate(this.difficulties[this.c],this.url.toString());
		this.display = new Display(this.plate);
		final int w[] = {3,this.display.getModel().getSize(),3};
		final int h[] = {3,this.display.getModel().getSize(),3};
		final HIGLayout layout = new HIGLayout(w,h);
		final HIGConstraints constraint = new HIGConstraints();
		this.getContentPane().setLayout(layout);
		this.getContentPane().add(this.display,constraint.rcwh(2,2,1,1));
		this.plate.addPlateListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("<== Taquin (c) 2005 ==>");
		this.setVisible(true);
		this.pack();
		this.setResizable(false);
	}

	public static void main(final String args[]){
		new Main();
	}
}
