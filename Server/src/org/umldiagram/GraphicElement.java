package org.umldiagram;

import org.umldiagram.elements.ClassElementHelper;
import org.w3c.dom.Element;

public class GraphicElement {
	private int ID;
	private boolean selected;
	
	public GraphicElement(int iID, boolean iSelected) {
		ID = iID;
		selected = iSelected;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Element toXMLNode() {
		return null;
	}
	
	public String toXMLString() {
		return "";
	}
	
	public void decodeContent(String iXmlContent) {
		// the class should be abstract - and this one just a declaration
	}
	
	public void setEditedBy(String user) {
		// not implemented
	}
	
	public String getEditedBy() {
		// not implemented
		return null;
	}
	
}
