/*
 * FreeMind - A Program for creating and viewing Mindmaps Copyright (C)
 * 2000-2004 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * 
 * See COPYING for Details
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Created on 05.10.2004
 */
/* $Id: EdgeStyleAction.java,v 1.1.2.2.2.4 2008/04/12 20:11:39 christianfoltin Exp $ */

package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.generated.instance.EdgeStyleFormatAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.main.Tools;
import freemind.modes.EdgeAdapter;
import freemind.modes.MindMap;
import freemind.modes.MindMapEdge;
import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class EdgeStyleAction extends NodeGeneralAction implements NodeActorXml {
	private String mStyle;

	public EdgeStyleAction(MindMapController controller, String style) {
		super(controller, null, null);
		setName(/* controller.getText("edge_style") + */controller
				.getText(style));
		this.mStyle = style;
		addActor(this);
	}

	public ActionPair apply(MindMap model, MindMapNode selected) {
		return getActionPair(selected, mStyle);
	}

	public Class getDoActionClass() {
		return EdgeStyleFormatAction.class;
	}

	public void setEdgeStyle(MindMapNode node, String style) {
		modeController.getActionFactory().startTransaction(
				(String) getValue(NAME));
		modeController.getActionFactory().executeAction(
				getActionPair(node, style));
		modeController.getActionFactory().endTransaction(
				(String) getValue(NAME));

	}

	private ActionPair getActionPair(MindMapNode selected, String style) {
		EdgeStyleFormatAction styleAction = createNodeStyleFormatAction(
				selected, style);
		String oldStyle = selected.getEdge().getStyle();
		if (!selected.getEdge().hasStyle()) {
			oldStyle = null;
		}
		EdgeStyleFormatAction undoStyleAction = createNodeStyleFormatAction(
				selected, oldStyle);
		return new ActionPair(styleAction, undoStyleAction);
	}

	private EdgeStyleFormatAction createNodeStyleFormatAction(
			MindMapNode selected, String style) {
		EdgeStyleFormatAction edgeStyleAction = new EdgeStyleFormatAction();
		edgeStyleAction.setNode(getNodeID(selected));
		edgeStyleAction.setStyle(style);
		return edgeStyleAction;
	}

	public void act(XmlAction action) {
		if (action instanceof EdgeStyleFormatAction) {
			EdgeStyleFormatAction edgeStyleAction = (EdgeStyleFormatAction) action;
			MindMapNode node = getNodeFromID(edgeStyleAction.getNode());
			String newStyle = edgeStyleAction.getStyle();
			MindMapEdge edge = node.getEdge();
			if (!Tools.safeEquals(edge.hasStyle() ? edge.getStyle() : null,
					newStyle)) {
				((EdgeAdapter) edge).setStyle(newStyle);
				modeController.nodeChanged(node);
			}
		}
	}
}