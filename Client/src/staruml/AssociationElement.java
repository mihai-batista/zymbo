package staruml;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Node;

public class AssociationElement extends Composite implements IElement {
	private Composite element;
	private IElement startElement;
	private IElement endElement;
	private String direction;
	private String selection = "none";
	private boolean lock, available;
	private AssociationElementConvertor associationElementConvertor;
	private ElementType type;
	private Label elementName, startName, endName;
	private Composite whiteboard;
	private Color lineColor;
	
	public AssociationElement(Composite parent, int style, IElement iStartElement, IElement iEndElement, int iID) {
		super(parent, style);
		
		whiteboard = parent;
		associationElementConvertor = new AssociationElementConvertor();
		associationElementConvertor.setStartElementId(iStartElement.getID());
		associationElementConvertor.setEndElementId(iEndElement.getID());
		associationElementConvertor.setID(iID);
		associationElementConvertor.setStartConnectedElement(iStartElement.getTitle());
		associationElementConvertor.setEndConnectedElement(iEndElement.getTitle());
		//associationElementConvertor.setStartIsNavigable(true);
		//associationElementConvertor.setEndIsNavigable(true);
		//associationElementConvertor.setEndAggregation(AssociationElementHelper.AGGREGATION_AGGREGATE);
		startElement = iStartElement;
		endElement = iEndElement;
		
		initialize();
	}
	
	// Directed association
	public void isDirectedAssociation() {
		associationElementConvertor.setEndIsNavigable(true);
	}
	
	// Aggregation
	public void isAggregation() {
		associationElementConvertor.setEndAggregation(AssociationElementHelper.AGGREGATION_AGGREGATE);
	}
	
	// Aggregation
	public void isComposition() {
		associationElementConvertor.setEndAggregation(AssociationElementHelper.AGGREGATION_COMPOSITE);
	}
	
	// Generalization
	public void isGeneralization() {
		associationElementConvertor.setGeneralization(true);
	}
	
	// Realization
	public void isRealization() {
		associationElementConvertor.setRealization(true);
	}
	
	// Dependency
	public void isDependency() {
		associationElementConvertor.setDependency(true);
		associationElementConvertor.setEndIsNavigable(true);
	}
	
	
	public AssociationElement(Composite parent, int style, String iXmlContent, HashMap<Integer, IElement> whiteboardContent) {
		super(parent, style);
		associationElementConvertor = new AssociationElementConvertor();
		associationElementConvertor.decodeContent(iXmlContent);
				
		startElement = whiteboardContent.get(associationElementConvertor.getStartElementId());
		endElement = whiteboardContent.get(associationElementConvertor.getEndElementId());
		
		initialize();
	}
	
	public AssociationElement(Composite parent, int style, Node iXmlNode, HashMap<Integer, IElement> whiteboardContent) {
		super(parent, style);
		associationElementConvertor = new AssociationElementConvertor();
		associationElementConvertor.decodeContent(iXmlNode);
				
		startElement = whiteboardContent.get(associationElementConvertor.getStartElementId());
		endElement = whiteboardContent.get(associationElementConvertor.getEndElementId());
		
		initialize();
	}

	@Override
	public void selectedState() {
		selection = "selected";
		redraw();
		
	}

	@Override
	public void unselectedState() {
		selection = "none";
		lineColor = new Color(null, 0, 0, 0);
		redraw();
	}

	@Override
	public void disabledState() {
		lineColor = new Color(null, 200, 200, 200);
		redraw();		
	}

	@Override
	public int getX() {
		return this.getBounds().x;
	}

	@Override
	public void setX(int iX) {
		// TODO Auto-generated method stub		
	}

	@Override
	public int getY() {
		return this.getBounds().y;
	}

	@Override
	public void setY(int iY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWidth() {
		return this.getBounds().width;
	}

	@Override
	public void setWidth(int iWidth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHeight() {
		return this.getBounds().height;
	}

	@Override
	public void setHeight(int iHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		return associationElementConvertor.getID();
	}

	@Override
	public void drawElement() {
		computeDirection();			
		this.redraw();		
	}
	
	public void initialize() {
		type = ElementType.ASSOCIATION;		
		lock = false;
		available = true;
		
		element = this;
		
		startElement.addRelation(this);
		endElement.addRelation(this);
		
		// COMPUTE ELEMENT SIZE
		elementName = new Label(this, SWT.NONE);
		elementName.setText(associationElementConvertor.getElementName());
		
		startName = new Label(this, SWT.NONE);
		startName.setText(associationElementConvertor.getStartVisibility() + 
							associationElementConvertor.getStartName() + " " + 
							associationElementConvertor.getStartMultiplicity());
				
		endName = new Label(this, SWT.NONE);
		endName.setText(associationElementConvertor.getEndVisibility() + 
						associationElementConvertor.getEndName() + " " + 
						associationElementConvertor.getEndMultiplicity());
		endName.setAlignment(SWT.RIGHT);
		
		computeDirection();
				
		// SETUP BACKGROUND FOR ELEMENTS
		final Image whiteboardBck = new Image(this.getDisplay(), "D:\\Work\\Licenta\\images\\bck.jpg");
		final Image transparentImg = new Image(this.getDisplay(), "D:\\Work\\Licenta\\images\\Idea.gif");
		//element.setBackgroundImage(whiteboardBck);
		//element.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		elementName.setBackgroundImage(whiteboardBck);
		startName.setBackgroundImage(whiteboardBck);
		endName.setBackgroundImage(whiteboardBck);
		
		//this.setBackground(new Color(null, 240, 100, 15)); // debug
		
		lineColor = new Color(null, 0, 0, 0);
		element.addPaintListener(new PaintListener(){	
			@Override
			public void paintControl(PaintEvent e) {				
				//e.gc.drawImage(transparentImg,0,0);
				System.out.println("Drawing the relation");
				if (direction.equals("1")) {
					e.gc.setForeground(lineColor);
					if (selection.equals("selected")) {
						e.gc.setLineWidth(4);
					} else if (selection.equals("none")) {
						e.gc.setLineWidth(1);
					}
					
					e.gc.setLineWidth(SWT.LINE_SOLID);					
					if (associationElementConvertor.getDependency() || associationElementConvertor.getRealization()) {
						e.gc.setLineWidth(SWT.LINE_DOT);
					}
					e.gc.drawLine(0, 0, element.getBounds().width, element.getBounds().height);
					
					// DESENEZ SAGEATA BOLDATA 
					e.gc.setLineWidth(2);
					if (associationElementConvertor.getEndIsNavigable()) {
						e.gc.drawLine(element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5);
						e.gc.drawLine(element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 5, element.getBounds().height - 15);
					}
					if (associationElementConvertor.getStartIsNavigable()) {
						e.gc.drawLine(2, 2, 15, 5);
						e.gc.drawLine(2, 2, 5, 15);
					}
					
					// DRAW AGGREGATION AND COMPOSITION					
					if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 25, element.getBounds().height - 20, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 25, element.getBounds().height - 20, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {2, 2, 15, 5, 25, 20, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {2, 2, 15, 5, 25, 20, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					// DRAW GENERALIZATION OR DEPENDENCY
					if (associationElementConvertor.getGeneralization() || associationElementConvertor.getDependency()) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					}
				} else if (direction.equals("2")) {
					e.gc.setForeground(lineColor);
					if (selection.equals("selected")) {
						e.gc.setLineWidth(4);
					} else if (selection.equals("none")) {
						e.gc.setLineWidth(1);
					}
					
					e.gc.setLineWidth(SWT.LINE_SOLID);					
					if (associationElementConvertor.getDependency() || associationElementConvertor.getRealization()) {
						e.gc.setLineWidth(SWT.LINE_DOT);
					}										
					e.gc.drawLine(0, element.getBounds().height, element.getBounds().width, 0);
					
					// DRAW ARROWS
					e.gc.setLineWidth(2);
					if (associationElementConvertor.getEndIsNavigable()) {
						e.gc.drawLine(element.getBounds().width - 2, 2, element.getBounds().width - 15, 5);
						e.gc.drawLine(element.getBounds().width - 2, 2, element.getBounds().width - 8, 15);
					}
					if (associationElementConvertor.getStartIsNavigable()) {
						e.gc.drawLine(2, element.getBounds().height - 2, 5, element.getBounds().height - 15);
						e.gc.drawLine(2, element.getBounds().height - 2, 15, element.getBounds().height - 5);
					}
					
					// DRAW AGGREGATION AND COMPOSITION					
					if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 25, 20, element.getBounds().width - 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 25, 20, element.getBounds().width - 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 25, element.getBounds().height - 20, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 25, element.getBounds().height - 20, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					// DRAW GENERALIZATION OR DEPENDENCY
					if (associationElementConvertor.getGeneralization() || associationElementConvertor.getDependency()) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 8, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					}
				} else if (direction.equals("3")) {
					e.gc.setForeground(lineColor);
					if (selection.equals("selected")) {
						e.gc.setLineWidth(4);
					} else if (selection.equals("none")) {
						e.gc.setLineWidth(1);
					}
					
					e.gc.setLineWidth(SWT.LINE_SOLID);					
					if (associationElementConvertor.getDependency() || associationElementConvertor.getRealization()) {
						e.gc.setLineWidth(SWT.LINE_DOT);
					}										
					e.gc.drawLine(0, element.getBounds().height, element.getBounds().width, 0);
					
					// DRAW ARROWS
					e.gc.setLineWidth(2);
					if (associationElementConvertor.getEndIsNavigable()) {
						e.gc.drawLine(element.getBounds().width - 2, 2, element.getBounds().width - 15, 5);
						e.gc.drawLine(element.getBounds().width - 2, 2, element.getBounds().width - 8, 15);
					}
					if (associationElementConvertor.getStartIsNavigable()) {
						e.gc.drawLine(2, element.getBounds().height - 2, 8, element.getBounds().height - 15);
						e.gc.drawLine(2, element.getBounds().height - 2, 15, element.getBounds().height - 5);
					}	
					
					// DRAW AGGREGATION AND COMPOSITION					
					if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 25, 20, element.getBounds().width - 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 25, 20, element.getBounds().width - 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 25, element.getBounds().height - 20, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 25, element.getBounds().height - 20, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					// DRAW GENERALIZATION OR DEPENDENCY
					if (associationElementConvertor.getGeneralization() || associationElementConvertor.getDependency()) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 8, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					}
				} else if (direction.equals("4")) {
					e.gc.setForeground(lineColor);
					if (selection.equals("selected")) {
						e.gc.setLineWidth(4);
					} else if (selection.equals("none")) {
						e.gc.setLineWidth(1);
					}
					
					e.gc.setLineWidth(SWT.LINE_SOLID);					
					if (associationElementConvertor.getDependency() || associationElementConvertor.getRealization()) {
						e.gc.setLineWidth(SWT.LINE_DOT);
					}	
					e.gc.drawLine(0, 0, element.getBounds().width, element.getBounds().height);
					
					// DRAW ARROWS
					e.gc.setLineWidth(2);
					if (associationElementConvertor.getEndIsNavigable()) {
						e.gc.drawLine(2, 2, 15, 5);
						e.gc.drawLine(2, 2, 5, 15);
					}
					if (associationElementConvertor.getStartIsNavigable()) {
						e.gc.drawLine(element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5);
						e.gc.drawLine(element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 5, element.getBounds().height - 15);
					}	
					
					// DRAW AGGREGATION AND COMPOSITION					
					if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {2, 2, 15, 5, 25, 20, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {2, 2, 15, 5, 25, 20, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 25, element.getBounds().height - 20, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 25, element.getBounds().height - 20, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					// DRAW GENERALIZATION OR DEPENDENCY
					if (associationElementConvertor.getGeneralization() || associationElementConvertor.getDependency()) {
						int[] points = {2, 2, 15, 5, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					}
				} else if (direction.equals("5")) {
					e.gc.setForeground(lineColor);
					if (selection.equals("selected")) {
						e.gc.setLineWidth(4);
					} else if (selection.equals("none")) {
						e.gc.setLineWidth(1);
					}
					
					e.gc.setLineWidth(SWT.LINE_SOLID);					
					if (associationElementConvertor.getDependency() || associationElementConvertor.getRealization()) {
						e.gc.setLineWidth(SWT.LINE_DOT);
					}	
					e.gc.drawLine(0, 0, element.getBounds().width, element.getBounds().height);
					
					// DRAW ARROWS
					e.gc.setLineWidth(2);
					if (associationElementConvertor.getEndIsNavigable()) {
						e.gc.drawLine(2, 2, 15, 5);
						e.gc.drawLine(2, 2, 5, 15);
					}
					if (associationElementConvertor.getStartIsNavigable()) {
						e.gc.drawLine(element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5);
						e.gc.drawLine(element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 5, element.getBounds().height - 15);
					}	
					
					// DRAW AGGREGATION AND COMPOSITION					
					if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {2, 2, 15, 5, 25, 20, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {2, 2, 15, 5, 25, 20, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 25, element.getBounds().height - 20, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 25, element.getBounds().height - 20, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					// DRAW GENERALIZATION OR DEPENDENCY
					if (associationElementConvertor.getGeneralization() || associationElementConvertor.getDependency()) {
						int[] points = {2, 2, 15, 5, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					}
				} else if (direction.equals("6")) {
					e.gc.setForeground(lineColor);
					if (selection.equals("selected")) {
						e.gc.setLineWidth(4);
					} else if (selection.equals("none")) {
						e.gc.setLineWidth(1);
					}
					
					e.gc.setLineWidth(SWT.LINE_SOLID);					
					if (associationElementConvertor.getDependency() || associationElementConvertor.getRealization()) {
						e.gc.setLineWidth(SWT.LINE_DOT);
					}	
					e.gc.drawLine(0, element.getBounds().height, element.getBounds().width, 0);
					
					// DRAW ARROWS
					e.gc.setLineWidth(2);
					if (associationElementConvertor.getEndIsNavigable()) {
						e.gc.drawLine(2, element.getBounds().height - 2, 15, element.getBounds().height - 5);
						e.gc.drawLine(2, element.getBounds().height - 2, 5, element.getBounds().height - 15);
					}
					if (associationElementConvertor.getStartIsNavigable()) {
						e.gc.drawLine(element.getBounds().width - 2, 2, element.getBounds().width - 15, 5);
						e.gc.drawLine(element.getBounds().width - 2, 2, element.getBounds().width - 5, 15);
					}
					
					// DRAW AGGREGATION AND COMPOSITION					
					if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 25, element.getBounds().height - 20, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 25, element.getBounds().height - 20, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 25, 20, element.getBounds().width - 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 25, 20, element.getBounds().width - 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					// DRAW GENERALIZATION OR DEPENDENCY
					if (associationElementConvertor.getGeneralization() || associationElementConvertor.getDependency()) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					}
				} else if (direction.equals("7")) {
					e.gc.setForeground(lineColor);
					if (selection.equals("selected")) {
						e.gc.setLineWidth(4);
					} else if (selection.equals("none")) {
						e.gc.setLineWidth(1);
					}
					
					e.gc.setLineWidth(SWT.LINE_SOLID);					
					if (associationElementConvertor.getDependency() || associationElementConvertor.getRealization()) {
						e.gc.setLineWidth(SWT.LINE_DOT);
					}	
					e.gc.drawLine(0, element.getBounds().height, element.getBounds().width, 0);
					
					// DRAW ARROWS
					e.gc.setLineWidth(2);
					if (associationElementConvertor.getEndIsNavigable()) {
						e.gc.drawLine(2, element.getBounds().height - 2, 15, element.getBounds().height - 5);
						e.gc.drawLine(2, element.getBounds().height - 2, 5, element.getBounds().height - 15);
					}
					if (associationElementConvertor.getStartIsNavigable()) {
						e.gc.drawLine(element.getBounds().width - 2, 2, element.getBounds().width - 15, 5);
						e.gc.drawLine(element.getBounds().width - 2, 2, element.getBounds().width - 5, 15);
					}	
					
					// DRAW AGGREGATION AND COMPOSITION					
					if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 25, element.getBounds().height - 20, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 25, element.getBounds().height - 20, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 25, 20, element.getBounds().width - 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {element.getBounds().width - 2, 2, element.getBounds().width - 15, 5, element.getBounds().width - 25, 20, element.getBounds().width - 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					// DRAW GENERALIZATION OR DEPENDENCY
					if (associationElementConvertor.getGeneralization() || associationElementConvertor.getDependency()) {
						int[] points = {2, element.getBounds().height - 2, 15, element.getBounds().height - 5, 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					}
				} else if (direction.equals("8")) {
					e.gc.setForeground(lineColor);
					if (selection.equals("selected")) {
						e.gc.setLineWidth(4);
					} else if (selection.equals("none")) {
						e.gc.setLineWidth(1);
					}
					
					e.gc.setLineWidth(SWT.LINE_SOLID);					
					if (associationElementConvertor.getDependency() || associationElementConvertor.getRealization()) {
						e.gc.setLineWidth(SWT.LINE_DOT);
					}	
					e.gc.drawLine(0, 0, element.getBounds().width, element.getBounds().height);
				
					// DRAW ARROWS
					e.gc.setLineWidth(2);
					if (associationElementConvertor.getEndIsNavigable()) {
						e.gc.drawLine(element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5);
						e.gc.drawLine(element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 8, element.getBounds().height - 15);
					}
					if (associationElementConvertor.getStartIsNavigable()) {
						e.gc.drawLine(2, 2, 15, 5);
						e.gc.drawLine(2, 2, 5, 15);
					}
					
					// DRAW AGGREGATION AND COMPOSITION					
					if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 25, element.getBounds().height - 20, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getEndAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 25, element.getBounds().height - 20, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_AGGREGATE)) {
						int[] points = {2, 2, 15, 5, 25, 20, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					} else if (associationElementConvertor.getStartAggregation().equals(AssociationElementHelper.AGGREGATION_COMPOSITE)) {
						int[] points = {2, 2, 15, 5, 25, 20, 5, 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 160, 0, 0), new Color(null, 160, 0, 0), points);
					} 
					
					// DRAW GENERALIZATION OR DEPENDENCY
					if (associationElementConvertor.getGeneralization() || associationElementConvertor.getDependency()) {
						int[] points = {element.getBounds().width - 2, element.getBounds().height - 2, element.getBounds().width - 15, element.getBounds().height - 5, element.getBounds().width - 5, element.getBounds().height - 15};
						WhiteboardHelpers.drawAggregation(e, new Color(null, 0, 0, 0), new Color(null, 255, 255, 255), points);
					}
				} 
         	}
        });
	}
	
	private void computeDirection() {
		if ((startElement.getX() + startElement.getWidth()) < endElement.getX() && startElement.getY() < endElement.getY()) {
			element.setBounds(startElement.getX() + startElement.getWidth(), startElement.getY() + startElement.getHeight()/2, endElement.getX() - (startElement.getX() + startElement.getWidth()), (endElement.getY() + endElement.getHeight() / 2) - (startElement.getY() + startElement.getHeight()/2));
			direction = "1";
			System.out.println("Directia 1");
			displayAssociationText(true);
		} else if ((startElement.getX() + startElement.getWidth()) < endElement.getX() && startElement.getY() > endElement.getY()) {
			element.setBounds(startElement.getX() + startElement.getWidth(), endElement.getY() + endElement.getHeight()/2, endElement.getX() - (startElement.getX() + startElement.getWidth()), (startElement.getY() + startElement.getHeight() / 2) - (endElement.getY() + endElement.getHeight()/2));
			direction = "2";
			System.out.println("Directia 2");
			displayAssociationText(true);
		} else if ((startElement.getX() + startElement.getWidth()/2)  < (endElement.getX() + endElement.getWidth()/2) && startElement.getY() > endElement.getY() ) {
			element.setBounds(startElement.getX() + startElement.getWidth() / 2, endElement.getY() + endElement.getHeight(), (endElement.getX() + endElement.getWidth() / 2) - (startElement.getX() + startElement.getWidth() / 2), startElement.getY() - (endElement.getY() + endElement.getHeight()));
			direction = "3";
			System.out.println("Directia 3");
			displayAssociationText(false);
		} else if (startElement.getX() < (endElement.getX() + endElement.getWidth()) && startElement.getY() > endElement.getY()) {
			element.setBounds(endElement.getX() + endElement.getWidth() / 2 , endElement.getY() + endElement.getHeight(), (startElement.getX() + startElement.getWidth() / 2) - (endElement.getX() + endElement.getWidth() / 2), startElement.getY() - (endElement.getY() + endElement.getHeight()) );
			direction = "4";
			System.out.println("Directia 4");
			displayAssociationText(false);
		} else if (startElement.getX() > (endElement.getX() + endElement.getWidth()) && startElement.getY() > endElement.getY()) {
			element.setBounds(endElement.getX() + endElement.getWidth(), endElement.getY() + endElement.getHeight()/2, startElement.getX() - (endElement.getX() + endElement.getWidth()), (startElement.getY() + startElement.getHeight()/2) - (endElement.getY() + endElement.getHeight()/2));
			direction = "5";
			System.out.println("Direction 5");
			displayAssociationText(true);
		} else if (startElement.getX() > (endElement.getX() + endElement.getWidth()) && startElement.getY() < endElement.getY()) {
			element.setBounds(endElement.getX() + endElement.getWidth(), startElement.getY() + startElement.getHeight() / 2, startElement.getX() - (endElement.getX() + endElement.getWidth()), (endElement.getY() + endElement.getHeight()/2) - (startElement.getY() + startElement.getHeight()/2)); 
			direction = "6";
			System.out.println("Direction 6");
			displayAssociationText(true);
		} else if ((endElement.getX() + endElement.getWidth()) < (startElement.getX() + startElement.getWidth()) && startElement.getY() < endElement.getY()) {
			element.setBounds(endElement.getX() + endElement.getWidth()/2, startElement.getY() + startElement.getHeight(), (startElement.getX() + startElement.getWidth()/2) - (endElement.getX() + endElement.getWidth()/2), endElement.getY() - (startElement.getY() + startElement.getHeight()));
			direction = "7";
			System.out.println("Direction 7");
			displayAssociationText(false);
		} else if ((endElement.getX() + endElement.getWidth()) < (startElement.getX() + startElement.getWidth() + endElement.getWidth()) && startElement.getY() < endElement.getY()) {
			element.setBounds(startElement.getX() + startElement.getWidth() / 2, startElement.getY() + startElement.getHeight(), (endElement.getX() + endElement.getWidth()/2) - (startElement.getX() + startElement.getWidth()/2), endElement.getY() - (startElement.getY() + startElement.getHeight()));
			direction = "8";
			System.out.println("Direction 8");
			displayAssociationText(false);
		}		
		
		/* TODO : Aici scriu toate elementele ale caror pozitie e relativa la pozitia elementului.
		*	Verific daca intra in element,  width > width element nu afisez.
		*/
		elementName.setBounds(this.getBounds().width/2 + 7, this.getBounds().height/2 - 7, 80, 20);
		startName.setBounds(15, 0, 80, 20);
		endName.setBounds(this.getBounds().width - 95, this.getBounds().height - 20, 80, 20);
	}
	
	private void displayAssociationText(boolean visibility) {
		startName.setVisible(visibility);
		endName.setVisible(visibility);
		elementName.setVisible(visibility);
	}

	@Override
	public void drawRelations() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRelation(IElement relation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeElement() {
		startElement.removeRelation(this);
		endElement.removeRelation(this);		
		this.setVisible(false);	
	}

	@Override
	public void removeRelation(IElement relation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAvailable() {
		return available;
	}

	@Override
	public void setAvailability(boolean status) {
		available = status;
		
	}

	@Override
	public boolean isLocked() {
		return lock;
	}

	@Override
	public void setLock(boolean status) {
		lock = status;
		
	}

	@Override
	public String toXMLString() {
		return associationElementConvertor.toXMLString();
	}

	@Override
	public void updateElement(String updateContent) {
		drawElement();		
	}

	@Override
	public ElementType getType() {
		return type;
	}

	@Override
	public String getTitle() {
		return associationElementConvertor.getElementName();
	}

	@Override
	public void setTitle(String iTitle) {
		associationElementConvertor.setElementName(iTitle);		
		elementName.setText(associationElementConvertor.getElementName());
	}
	
	public void setStartName(String iName) {
		associationElementConvertor.setStartName(iName);
		startName.setText(associationElementConvertor.getStartVisibility() + 
				associationElementConvertor.getStartName() + " " + 
				associationElementConvertor.getStartMultiplicity());
	}
	
	public String getStartName() {
		return associationElementConvertor.getStartName();
	}
	
	public void setStartVisibility(String iVisibility) {
		associationElementConvertor.setStartVisibility(iVisibility);
		startName.setText(associationElementConvertor.getStartVisibility() + 
				associationElementConvertor.getStartName() + " " + 
				associationElementConvertor.getStartMultiplicity());
	}
	
	public String getStartVisibility() {
		return associationElementConvertor.getStartVisibility();
	}
	
	public void setStartIsNavigable(boolean isNavigable) {
		associationElementConvertor.setStartIsNavigable(isNavigable);
	}
	
	public boolean getStartIsNavigable() {
		return associationElementConvertor.getStartIsNavigable();
	}
	
	public void setStartAggregation(String iAggregation) {
		associationElementConvertor.setStartAggregation(iAggregation);
	}
	
	public String getStartAggregation() {
		return associationElementConvertor.getStartAggregation();
	}
	
	public void setStartMultiplicity(String iMultiplicity) {
		associationElementConvertor.setStartMultiplicity(iMultiplicity);
		startName.setText(associationElementConvertor.getStartVisibility() + 
				associationElementConvertor.getStartName() + " " + 
				associationElementConvertor.getStartMultiplicity());
	}
	
	public String getStartMultiplicity() {
		return associationElementConvertor.getStartMultiplicity();
	}
	
	public void setEndName(String iName) {
		associationElementConvertor.setEndName(iName);
		endName.setText(associationElementConvertor.getEndVisibility() + 
				associationElementConvertor.getEndName() + " " + 
				associationElementConvertor.getEndMultiplicity());
	}
	
	public String getEndName() {
		return associationElementConvertor.getEndName();
	}
	
	public void setEndVisibility(String iVisibility) {
		associationElementConvertor.setEndVisibility(iVisibility);
		endName.setText(associationElementConvertor.getEndVisibility() + 
				associationElementConvertor.getEndName() + " " + 
				associationElementConvertor.getEndMultiplicity());
	}
	
	public String getEndVisibility() {
		return associationElementConvertor.getEndVisibility();
	}
	
	public void setEndIsNavigable(boolean isNavigable) {
		associationElementConvertor.setEndIsNavigable(isNavigable);
	}
	
	public boolean getEndIsNavigable() {
		return associationElementConvertor.getEndIsNavigable();
	}
	
	public void setEndAggregation(String iAggregation) {
		associationElementConvertor.setEndAggregation(iAggregation);
	}
	
	public String getEndAggregation() {
		return associationElementConvertor.getEndAggregation();
	}
	
	public void setEndMultiplicity(String iMultiplicity) {
		associationElementConvertor.setEndMultiplicity(iMultiplicity);
		endName.setText(associationElementConvertor.getEndVisibility() + 
				associationElementConvertor.getEndName() + " " + 
				associationElementConvertor.getEndMultiplicity());
	}
	
	public String getEndMultiplicity() {
		return associationElementConvertor.getEndMultiplicity();
	}

	@Override
	public void setEditedBy(String user) {
		// TODO Auto-generated method stub
		
	}
	
	public void setStartConnectedElement(String iName) {
		associationElementConvertor.setStartConnectedElement(iName);
	}
	
	public String getStartConnectedElement() {
		return associationElementConvertor.getStartConnectedElement();
	}
	
	public void setEndConnectedElement(String iName) {
		associationElementConvertor.setEndConnectedElement(iName);
	}
	
	public String getEndConnectedElement() {
		return associationElementConvertor.getEndConnectedElement();
	}

	@Override
	public void setAttributes(String attributes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOperations(String operations) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawUpdatedElement() {
		// Content is changed
		
		// COMPUTE ELEMENT SIZE
		elementName = new Label(this, SWT.NONE);
		elementName.setText(associationElementConvertor.getElementName());
				
		startName = new Label(this, SWT.NONE);
		startName.setText(associationElementConvertor.getStartVisibility() + 
		associationElementConvertor.getStartName() + " " + 
		associationElementConvertor.getStartMultiplicity());
						
		endName = new Label(this, SWT.NONE);
		endName.setText(associationElementConvertor.getEndVisibility() + 
		associationElementConvertor.getEndName() + " " + 
		associationElementConvertor.getEndMultiplicity());
		endName.setAlignment(SWT.RIGHT);
		
	}

}
