/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package tests.freemind;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import freemind.controller.Controller;
import freemind.extensions.HookFactory;
import freemind.extensions.HookInstanciationMethod;
import freemind.extensions.HookRegistration;
import freemind.extensions.ModeControllerHook;
import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHook;
import freemind.main.Tools;
import freemind.main.XMLParseException;
import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapMapModel;
import freemind.modes.mindmapmode.MindMapMode;
import freemind.view.mindmapview.IndependantMapViewCreator;
import freemind.view.mindmapview.MapView;

/**
 * @author foltin
 * @date 12.08.2011
 */
public class ExportTests extends FreeMindTestBase {
	private static final String TESTMAP_MM = "tests/freemind/testmap.mm";

	public void testExportPng() throws Exception {
		String inputFileName = TESTMAP_MM;
		String outputFileName = "/tmp/test.png";

		System.setProperty("java.awt.headless", "true");
		JPanel parent = new JPanel();
		Rectangle bounds = new Rectangle(0,0,400,600);
		parent.setBounds(bounds);
		IndependantMapViewCreator creator = new IndependantMapViewCreator();
		MapView mapView = creator.createMapViewForFile(inputFileName, parent, mFreeMindMain);
		// layout components:
		mapView.getRoot().getMainView().doLayout();
		parent.setOpaque(true);
		parent.setDoubleBuffered(false); // for better performance
		parent.doLayout();
		parent.validate(); // this might not be necessary
		System.out.println(mapView.getBounds());
		System.out.println(mapView.getInnerBounds());
		mapView.preparePrinting();
		Rectangle dim = mapView.getBounds();
		Rectangle dimI = mapView.getInnerBounds();
		parent.setBounds(dim);
		// do print
		BufferedImage backBuffer = new BufferedImage(dim.width, dim.height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = backBuffer.createGraphics();
		g.translate(-dim.x, -dim.y);
		g.clipRect(dim.x, dim.y, dim.width, dim.height);
		parent.print(g); // this might not be necessary
		backBuffer = backBuffer
				.getSubimage(dimI.x, dimI.y, dimI.width, dimI.height);

		FileOutputStream out1 = new FileOutputStream(outputFileName);
		ImageIO.write(backBuffer, "png", out1);
		out1.close();

		System.out.println("Done.");
	}

	public static void main(String[] args) throws FileNotFoundException,
			XMLParseException, IOException, URISyntaxException {
		FreeMindMainMock mFreeMindMain = new FreeMindMainMock();
		JDialog fm = new JDialog();
		fm.setTitle("Title");
		fm.setModal(true);
		JPanel parent = new JPanel();
		fm.add(parent, BorderLayout.CENTER);
		fm.setBounds(0, 0, 300, 400);
		parent.setBounds(0, 0, 400, 600);
		IndependantMapViewCreator creator = new IndependantMapViewCreator();
		MapView mapView = creator.createMapViewForFile(TESTMAP_MM, parent, mFreeMindMain);
		parent.add(mapView, BorderLayout.CENTER);
		mapView.setBounds(0, 0, 400, 600);
		Tools.waitForEventQueue();
		mapView.addNotify();
		// layout components:
		// mapView.getRoot().getMainView().doLayout();
		fm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		fm.setVisible(true);

	}

}