/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.smilesmile1973.taquin.worker;

/**
 * @author marechal.g
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Shaker implements Runnable {
	private int length = 10000;
	private int speed = 10;
	private Shaked shaked;
	private long endTime = 0;
	private Thread thread;
	public Shaker(final Shaked shaked,final int length,final int speed) {
		this.thread =new Thread(this,"Shaker-Thread");
		this.setShaked(shaked);
		this.setLength(length);
		this.setSpeed(speed);
	}

	public void start(){
		if (this.thread == null){
			this.thread = new Thread(this,"Shaker-Thread");
		}
		if (!this.thread.isAlive()){
			this.thread.start();
		}
	}

	@Override
	public void run() {
		try{
			this.endTime = System.currentTimeMillis() + this.length;
			while (this.thread == Thread.currentThread() ){
				this.getShaked().shake();
				Thread.sleep(this.getSpeed());
				if (System.currentTimeMillis() > this.endTime ){
					this.stop();
				}
			}
		}catch(final Exception e){
			e.printStackTrace();
		}
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(final int length) {
		this.length = length;
	}

	public int getSpeed() {
		return this.speed;
	}

	public void setSpeed(final int speed) {
		this.speed = speed;
	}
	/**
	 * @return Returns the shaked.
	 */
	public Shaked getShaked() {
		return this.shaked;
	}
	/**
	 * @param shaked The shaked to set.
	 */
	public void setShaked(final Shaked shaked) {
		this.shaked = shaked;
	}

	public synchronized void  stop(){
		this.thread = null;
	}
}