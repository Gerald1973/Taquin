/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.smilesmile1973.taquin.model;

import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.smilesmile1973.taquin.worker.Shaked;

/**
 * @author marechal.g
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Plate implements Shaked {
	private Piece[][] pieces;

	private int number = 5;

	private BufferedImage image;

	private int[] dataImage;

	private int idMax = 0;

	private Piece tmpPiece;

	private MemoryImageSource memoryImageSource = null;

	private boolean verifier = false;

	public boolean isVerifier() {
		return this.verifier;
	}

	public void setVerifier(final boolean verifier) {
		this.verifier = verifier;
	}

	public Plate() {
		this.setNumber(this.number);
	}

	public Plate(final int number, final String urlPicture) {
		this.setNumber(number);
		this.idMax = this.getNumber() * this.getNumber();
		this.setImage(urlPicture);
		this.fillPieces();
	}

	public int getNumber() {
		return this.number;
	}

	public void changeImage(final int number, final String urlPicture) {
		this.setNumber(number);
		this.idMax = this.getNumber() * this.getNumber();
		this.setImage(urlPicture);
		this.fillPieces();
		this.setVerifier(false);
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public static void main(final String args[]) {
		new Plate();
	}

	public synchronized void setImage(final String imageURL) {
		URL url;
		try {
			url = new URL(imageURL);
			this.image = ImageIO.read(url);
			this.dataImage = new int[this.image.getHeight() * this.image.getWidth()];
			final PixelGrabber pg = new PixelGrabber(this.image, 0, 0, this.image.getWidth(),
					this.image.getHeight(), this.dataImage, 0, this.image.getWidth());
			pg.grabPixels();
			this.memoryImageSource = new MemoryImageSource(this.getSize(), this.getSize(),
					this.dataImage, 0, this.getSize());
			this.memoryImageSource.setAnimated(true);
			this.memoryImageSource.setFullBufferUpdates(true);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public int[] getBoundsPixels(final int x, final int y, final int width, final int height) {
		final int scanLine = this.image.getWidth();
		int start;
		int end;
		final int[] result = new int[height * width];
		int c = 0;
		for (int i = y; i < (y + height); i++) {
			start = i * scanLine + x;
			end = start + width;
			for (int j = start; j < end; j++) {
				result[c] = this.dataImage[j];
				c++;
			}
		}
		return result;
	}

	public void setBoundsPixel(final int x, final int y, final int width, final int height, final int[] data) {
		final int scanLine = this.image.getWidth();
		int start;
		int end;
		int c = 0;
		for (int i = y; i < (y + height); i++) {
			start = i * scanLine + x;
			end = start + width;
			for (int j = start; j < end; j++) {
				this.dataImage[j] = data[c];
				c++;
			}
		}
	}

	public void fillPieces() {
		int number = 0;
		int size = 0;
		int posx = 0;
		int posy = 0;
		int tmp[];
		Piece tmp2 = null;
		Piece piece;
		number = this.getNumber();
		this.pieces = new Piece[number][number];
		size = this.image.getHeight() / number;
		for (int i = 0; i < number; i++) {
			posy = size * i;
			for (int j = 0; j < number; j++) {
				posx = size * j;
				tmp = new int[size * size];
				tmp = this.getBoundsPixels(posx, posy, size, size);
				piece = new Piece(j * number + i, size, tmp);
				this.pieces[i][j] = piece;
			}
		}
		// tmpPiece
		tmp = new int[size * size];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = 0xFF000000;
		}
		this.tmpPiece = new Piece(this.pieces.length * this.pieces[0].length, size, tmp);
		try {
			tmp2 = (Piece) this.pieces[this.pieces.length - 1][this.pieces[0].length - 1]
					.clone();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		this.pieces[this.pieces.length - 1][this.pieces[0].length - 1] = this.tmpPiece;
		this.tmpPiece = tmp2;

	}

	private int forbiddenDir = 3;

	@Override
	public synchronized void shake() {
		int dir; // 1=Up 2=right 3=Down 4=Left
		do {
			dir = (int) (Math.random() * 5);
		} while ((dir + 2 % 4) == this.forbiddenDir);
		this.forbiddenDir = dir;
		final Piece blackPiece = this.findBlackPiece();
		final int l = blackPiece.getLocation().getLine();
		final int c = blackPiece.getLocation().getCol();
		if ((dir == 1) && (l > 0)) {
			this.swapPiece(l, c, l - 1, c);
		} else if ((dir == 4) && (c > 0)) {
			this.swapPiece(l, c, l, c - 1);
		} else if ((dir == 3) && (c < this.getNumber() - 1)) {
			this.swapPiece(l, c, l, c + 1);
		} else if ((dir == 2) && (l < this.getNumber() - 1)) {
			this.swapPiece(l, c, l + 1, c);
		}
		this.refresh();
	}

	public Piece findBlackPiece() {
		Piece result = null;
		for (int i = 0; i < this.getNumber(); i++) {
			for (int j = 0; j < this.getNumber(); j++) {
				if (this.pieces[i][j].getId() == this.idMax) {
					result = this.pieces[i][j];
					result.setLocation(new Location(i, j));
				}
			}
		}
		return result;
	}

	public synchronized void refresh() {
		int number = 0;
		int posx = 0;
		int posy = 0;
		number = this.getNumber();
		for (int i = 0; i < number; i++) {
			for (int j = 0; j < number; j++) {
				posy = this.pieces[i][j].getSize() * i;
				posx = this.pieces[i][j].getSize() * j;
				this.setBoundsPixel(posx, posy, this.pieces[i][j].getSize(), this.pieces[i][j]
						.getSize(), this.pieces[i][j].getData());
			}

		}
		this.memoryImageSource.newPixels();
	}

	public MemoryImageSource getOrderedImage() {
		this.refresh();
		return this.memoryImageSource;
	}

	public int getSize() {
		return this.image.getHeight();
	}

	public MemoryImageSource getImage() {
		return this.memoryImageSource;
	}

	/**
	 * @return Returns the pieces.
	 */
	public Piece[][] getPieces() {
		return this.pieces;
	}

	/**
	 * @return Returns the dataImage.
	 */
	public int[] getDataImage() {
		return this.dataImage;
	}

	public Piece getPiece(final int posX, final int posY) {
		final int size = this.getSize() / this.getNumber();
		return this.pieces[posY / size][posX / size];
	}

	public synchronized void swapPiece(final int posX, final int posY) {
		final int size = this.getSize() / this.getNumber();
		final int line = posY / size;
		final int col = posX / size;
		boolean exchL = false;
		boolean exchR = false;
		boolean exchU = false;
		boolean exchD = false;
		if (line > 0) {
			exchU = this.pieces[line - 1][col].getId() == this.idMax;
		}
		if (line < this.pieces.length - 1) {
			exchD = this.pieces[line + 1][col].getId() == this.idMax;
		}
		if (col > 0) {
			exchL = this.pieces[line][col - 1].getId() == this.idMax;
		}
		if (col < this.pieces[0].length - 1) {
			exchR = this.pieces[line][col + 1].getId() == this.idMax;
		}

		if (exchU) {
			this.swapPiece(line, col, line - 1, col);
		} else if (exchD) {
			this.swapPiece(line, col, line + 1, col);
		} else if (exchL) {
			this.swapPiece(line, col, line, col - 1);
		} else if (exchR) {
			this.swapPiece(line, col, line, col + 1);
		}
	}

	public synchronized void swapPiece(final int l, final int c, final int l2, final int c2) {
		final Piece tmp = this.pieces[l][c];
		this.pieces[l][c] = this.pieces[l2][c2];
		this.pieces[l2][c2] = tmp;
	}

	public synchronized void verify() {
		final int number = this.getNumber();
		final int id[] = new int[number * number];
		int c = 0;
		for (int i = 0; i < number; i++) {
			for (int j = 0; j < number; j++) {
				id[c] = this.pieces[j][i].getId();
				c++;
			}
		}
		c = 1;
		while ((c < id.length) && (id[c] > id[c - 1])) {
			c++;
		}
		if (c == id.length) {
			this.setVerifier(true);
		} else {
			this.setVerifier(false);
		}
		this.notifyListeners();

	}

	// Gestion des abonn�s.
	private final Vector vector = new Vector(0, 1);

	public void addPlateListener(final PlateListener listener) {
		this.vector.add(listener);
	}

	public void removePlateListener(final PlateListener listener) {
		this.vector.remove(listener);
	}

	private void notifyListeners() {
		final int size = this.vector.size();
		PlateListener listener;
		final PlateEvent event = new PlateEvent(this);
		for (int i = 0; i < size; i++) {
			listener = (PlateListener) this.vector.elementAt(i);
			listener.performPlateAction(event);
		}
	}

	public Piece getTmpPiece() {
		return this.tmpPiece;
	}

	public int[] getFullDataImage() {
		int[] result = null;
		try {
			result = new int[this.image.getHeight() * this.image.getWidth()];
			final PixelGrabber pg = new PixelGrabber(this.image, 0, 0, this.image.getWidth(),
					this.image.getHeight(), result, 0, this.image.getWidth());
			pg.grabPixels();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}