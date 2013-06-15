package staruml;

import org.eclipse.swt.events.MouseListener;

public interface IElement {
	public void selectedState();
	public void unselectedState();
	public void disabledState();
	public int getX();
	public void setX(int iX);
	public int getY();
	public void setY(int iY);
	public int getWidth();
	public void setWidth(int iWidth);
	public int getHeight();
	public void setHeight(int iHeight);
	public int getID();
	public String getTitle();
	public void setTitle(String title);
	public void setAttributes(String attributes);
	public void setOperations(String operations);
	public void drawUpdatedElement();
	public void drawElement();
	public void drawRelations();
	public void addRelation(IElement relation);
	public void removeRelation(IElement relation);
	
	public void removeElement();
	public boolean isAvailable();
	public void setAvailability(boolean status);
	public boolean isLocked();
	public void setLock(boolean status);
	public String toXMLString();
	public void updateElement(String updateContent);
	public ElementType getType();
	public void setEditedBy(String user);
	
}
