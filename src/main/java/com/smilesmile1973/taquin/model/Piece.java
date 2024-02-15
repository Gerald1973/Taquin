/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.smilesmile1973.taquin.model;

/**
 * @author marechal.g
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Piece {
	private Location location;

	private int id;

	private int[] data;

	private int size;

	/**
	 * @return Returns the size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            The size to set.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	public Piece(int id, int size, int[] data) {
		setId(id);
		setData(data);
		setSize(size);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the data.
	 */
	public int[] getData() {
		return this.data;
	}

	/**
	 * @param data
	 *            The data to set.
	 */
	public void setData(int[] data) {
		this.data = data;
	}

	public Object clone() throws CloneNotSupportedException {
		int data[] = new int[getData().length];
		System.arraycopy(getData(), 0, data, 0, getData().length);
		return new Piece(this.getId(), this.getSize(), data);
	}

}