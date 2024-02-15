/*
 * Created on Jan 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.smilesmile1973.taquin.model;

import java.util.EventListener;

/**
 * @author marechal.g
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface PlateListener extends EventListener {
	public void performPlateAction(PlateEvent e);
}
