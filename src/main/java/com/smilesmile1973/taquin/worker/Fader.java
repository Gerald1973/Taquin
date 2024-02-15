/*
 * Created on Jan 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.smilesmile1973.taquin.worker;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;

/**
 * @author marechal.g
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Fader implements Runnable {
	public static int SOLID_TRANSPARENT = 1;
	public static int TRANSPARENT_SOLID = 2;
	private int[] data;
	private MemoryImageSource source;
	private long startTime;
	private long endTime;
	private int time;
	private Thread thread;
	private int type = 0;
	private int scanSize;
	private Component c;
	public Fader (int[] data,MemoryImageSource source, int time,int type,int scanSize,Component c){
		this.data = data;
		this.source = source;
		this.time = time;
		source.setAnimated(true);
		startTime = System.currentTimeMillis();
		endTime = startTime +  time;
		this.type = type;
		this.scanSize = scanSize;
		this.c = c;
		this.thread = new Thread(this,"fader");
		
		
	}
	public void start(){
		if (thread == null){
			thread = new Thread(this,"fader");
		}
		thread.start();
	}
	public void run() {
		int tmpTrans;
		int tmp;
		int i = 0;
		int j = 0;
		Graphics g = c.getGraphics();
		ColorModel RGBDefault = ColorModel.getRGBdefault();
		int delay = Math.round( (float) time / 16f);
		try{
			int length = data.length;
			if (type == Fader.TRANSPARENT_SOLID){
				for (i = 0; i < length;i++){
					data[i] = data[i] & 0x00FFFFFF;
				}
				i = 0;
				source.newPixels(data,RGBDefault,0,this.scanSize);
				for (i = 0; i < 256 ; i += 16){
					for (j=0; j < length;j++){
						tmpTrans = i << 24;
						data[j]  = (data[j] & 0x00FFFFFF) |tmpTrans;
					}
					source.newPixels(data,RGBDefault,0,this.scanSize);
					c.update(g);
					Thread.sleep(delay);
				}
			}
			thread = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void join(){
		try{
			if (thread != null){
				thread.join();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
