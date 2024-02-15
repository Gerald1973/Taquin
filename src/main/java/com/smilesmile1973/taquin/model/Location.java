/*
 * Created on Jan 13, 2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.smilesmile1973.taquin.model;

public class Location {
	private int line = 0;

	private int col = 0;

	public Location(final int line, final int col) {
		this.setLine(line);
		this.setCol(col);
	}

	public int getCol() {
		return this.col;
	}

	public void setCol(final int col) {
		this.col = col;
	}

	public int getLine() {
		return this.line;
	}

	public void setLine(final int line) {
		this.line = line;
	}
}