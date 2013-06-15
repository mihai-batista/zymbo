package staruml;

import java.awt.Stroke;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class ClassElement extends Composite implements IElement {
	
    private boolean available, lock;
    private Composite container, header, attributes, body, selection;
    private Label classTitle, selectionUser, attributesLabel, operationsLabel;
    private Color yellow, darkYellow, black, green, red, darkRed, gray, 
    				springGreen, aquaMarine, lightSkyBlue, white, darkSkyBlue, 
    				darkOrange, stormyBlue, softBlue, sexyBlue, lightBlue, washedBlue, 
    				darkSkyBlueLighter, aquaMarineLighter, golden, goldenLighter, grayRat;
	private ArrayList<IElement> relations = new ArrayList<IElement>();
	private ElementType type;
	private ClassElementConvertor classElementConvertor;
	
	private int ID;
	public ClassElement(Composite parent, int style, int iX, int iY, int iWidth, int iHeight, int iID) {
		super(parent, SWT.NO_REDRAW_RESIZE);
		
		type = ElementType.CLASS;
		available = true;
		lock = false;
		
		classElementConvertor = new ClassElementConvertor();
		classElementConvertor.setX(iX);
		classElementConvertor.setY(iY);
		classElementConvertor.setWidth(iWidth);
		classElementConvertor.setHeight(iHeight);
		classElementConvertor.setID(iID);
		classElementConvertor.setAttributes("+String name\n+int ID"); 
		classElementConvertor.setOperations("+String getID()\n+void processPacket()\n+int computeWidth\n+String toString()\n+void updateElement()");
		drawElement();
	}
		
	public ClassElement(Composite parent, int style, String iXmlContent) {
		super(parent, SWT.NO_REDRAW_RESIZE);
		type = ElementType.CLASS;
		available = true;
		lock = false;
		
		classElementConvertor = new ClassElementConvertor();
		classElementConvertor.decodeContent(iXmlContent);
		drawElement();		
	}
	
	public ClassElement(Composite parent, int style, Node iXmlNode) {
		super(parent, SWT.NO_REDRAW_RESIZE);
		type = ElementType.CLASS;
		available = true;
		lock = false;
		
		classElementConvertor = new ClassElementConvertor();
		classElementConvertor.decodeContent(iXmlNode);
		drawElement();		
	}
	
	public String toXMLString() {
		return classElementConvertor.toXMLString();
	}
	
	@Override
	public void selectedState() {
		header.setBackground(darkSkyBlue);
		attributes.setBackground(darkSkyBlueLighter);
		body.setBackground(darkSkyBlueLighter);	
		selection.setVisible(false);
		classTitle.setForeground(white);
		attributesLabel.setForeground(white);
		operationsLabel.setForeground(white);
	}

	@Override
	public void unselectedState() {
		header.setBackground(golden);
		attributes.setBackground(goldenLighter);
		body.setBackground(goldenLighter);
		selection.setVisible(false);
		classTitle.setForeground(white);
		attributesLabel.setForeground(darkRed);
		operationsLabel.setForeground(darkRed);
	}

	@Override
	public void disabledState() {
		header.setBackground(grayRat);
		attributes.setBackground(gray);
		body.setBackground(gray);
		selection.setVisible(true);
		classTitle.setForeground(white);
		attributesLabel.setForeground(black);
		operationsLabel.setForeground(black);
	}
	
	public void addMouseListener(MouseListener iMouseListener) {
		container.addMouseListener(iMouseListener);
		header.addMouseListener(iMouseListener);
		attributes.addMouseListener(iMouseListener);
		body.addMouseListener(iMouseListener);
	}
	
	public void addMouseMoveListener(MouseMoveListener iMouseMoveListener) {
		container.addMouseMoveListener(iMouseMoveListener);
		attributes.addMouseMoveListener(iMouseMoveListener);
		header.addMouseMoveListener(iMouseMoveListener);
		body.addMouseMoveListener(iMouseMoveListener);
	}
	
	public void drawBorders(Composite composite, Color color) {
		final Composite element = composite;
		final Color elementColor = color;
		composite.addPaintListener(new PaintListener(){
          	@Override
			public void paintControl(PaintEvent e) {
				int x = element.getBounds().x;
                int y = element.getBounds().y;
                e.gc.setForeground(elementColor);
                
                e.gc.drawLine(0, 0, x + element.getBounds().width, 0); // top
                e.gc.drawLine(0, element.getBounds().height - 1, element.getBounds().width, element.getBounds().height - 1); // bottom
                e.gc.drawLine(0, 0, 0, element.getBounds().height); // left
                e.gc.drawLine(element.getBounds().width - 1, 0, element.getBounds().width - 1, element.getBounds().height); // right
         	}
        });
	}
	
	@Override
	public void drawElement() {
		// How this element looks
		yellow = new Color(null, 255, 255, 185);
		darkYellow = new Color(null, 240, 203, 49);
		black = new Color(null, 0, 0, 0);
		green = new Color(null, 8, 176, 27);
		red = new Color(null, 176, 51, 8);
		darkRed = new Color(null, 101, 31, 7);
		gray = new Color(null, 200, 200, 200);
		springGreen = new Color(null, 74, 160, 44);
		aquaMarine = new Color(null, 52, 135, 129);
		lightSkyBlue = new Color(null, 160, 207, 236);
		white = new Color(null, 255, 255, 255);
		darkSkyBlue = new Color(null, 86, 109, 126);
		darkOrange = new Color(null, 248, 128, 23);
		stormyBlue = new Color(null, 135, 175, 199);
		softBlue = new Color(null, 94, 118, 126);
		sexyBlue = new Color(null, 53, 126, 199);
		lightBlue = new Color(null, 86, 165, 236);
		washedBlue = new Color(null, 130, 202, 255);
		darkSkyBlueLighter = new Color(null, 116, 139, 156);
		aquaMarineLighter = new Color(null, 82, 165, 159);
		golden = new Color(null, 100, 195, 33);
		goldenLighter = new Color(null, 255, 215, 53);
		grayRat = new Color(null, 114, 110, 109);
		
		// COMPUTE WIDTH AND HEIGHT
		int maxWidth = Math.max(computeWidth(classElementConvertor.getTitle()), Math.max(computeWidth(classElementConvertor.getAttributes()), computeWidth(classElementConvertor.getOperations())));
		classElementConvertor.setWidth(maxWidth + 10);
		
						
		container = new Composite(this, SWT.NONE | SWT.BORDER_SOLID);
		container.setLayout(null);
		final Image whiteboardBck = new Image(this.getDisplay(), "D:\\Work\\Licenta\\images\\bck.jpg");
		container.setBackgroundImage(whiteboardBck);
		
		selection = new Composite(container, SWT.BORDER_SOLID);
		selection.setBounds(50, 0, classElementConvertor.getWidth()-50, 25);
		selectionUser = new Label(selection, SWT.NONE);
		selectionUser.setBounds(0, 0, selection.getBounds().width, selection.getBounds().height);
		selectionUser.setAlignment(SWT.RIGHT);
		selectionUser.setText(classElementConvertor.getEditedBy());
		selection.setVisible(false);
					
		// header
		header = new Composite(container, SWT.BORDER_SOLID);
		header.setLayout(null);
		header.setBackground(golden);
		header.setBounds(0, 25, classElementConvertor.getWidth(), 30);
		drawBorders(header, darkRed);
				
		// header
		attributes = new Composite(container, SWT.BORDER_SOLID);
		attributes.setLayout(null);
		attributes.setBackground(goldenLighter);
		attributesLabel = new Label(attributes, SWT.NONE);
		attributesLabel.setText(classElementConvertor.getAttributes());
		attributesLabel.setForeground(darkRed);
		attributesLabel.setBounds(3, 3, computeWidth(classElementConvertor.getAttributes()), computeHeight(classElementConvertor.getAttributes()));
		attributes.setBounds(0, 55, classElementConvertor.getWidth(), computeHeight(classElementConvertor.getAttributes()) + 10);
		
		drawBorders(attributes, darkRed);
									
		// body
		body = new Composite(container, SWT.BORDER_SOLID);
		body.setLayout(null);
		body.setBackground(goldenLighter);
		operationsLabel = new Label(body, SWT.NONE);
		operationsLabel.setText(classElementConvertor.getOperations());
		operationsLabel.setForeground(darkRed);
		operationsLabel.setBounds(3, 3, computeWidth(classElementConvertor.getOperations()), computeHeight(classElementConvertor.getOperations()));
		body.setBounds(0, selection.getBounds().height + header.getBounds().height + attributes.getBounds().height, classElementConvertor.getWidth(), computeHeight(classElementConvertor.getOperations()) + 10);
		
		drawBorders(body, darkRed);
					
		classTitle = new Label(header, SWT.NONE);
		classTitle.setText(classElementConvertor.getTitle());
		classTitle.setForeground(white);		
		// BOLD
		 Font initialFont = classTitle.getFont();
         FontData[] fontData = initialFont.getFontData();
         for (int i = 0; i < fontData.length; i++) {
           fontData[i].setStyle(SWT.BOLD);
         }             
         final Font boldFont = new Font(this.getDisplay(), fontData);
         
         // BOLD & ITALIC 
      	 FontData[] fontData2 = initialFont.getFontData();
         for (int i = 0; i < fontData.length; i++) {
        	 fontData2[i].setStyle(SWT.BOLD);
        	 fontData2[i].setStyle(SWT.ITALIC);
         }             
         final Font boldAndItalicFont = new Font(this.getDisplay(), fontData2);
         
         if (classElementConvertor.getIsAbstract()) {
        	 classTitle.setFont(boldAndItalicFont);
         } else {
        	 classTitle.setFont(boldFont); 
         }
        
		classTitle.setBounds(classElementConvertor.getWidth() / 2 - computeWidth(classElementConvertor.getTitle())/2, 5, computeWidth(classElementConvertor.getTitle()), 20);
			
		if (!getEditedBy().equals("none")) {
			setLock(false);
			setAvailability(false);
			setEditedBy(getEditedBy());
			disabledState();
		}
		
		container.setBounds(0, 0, classElementConvertor.getWidth(), selection.getBounds().height + 
							header.getBounds().height + attributes.getBounds().height + body.getBounds().height);
		this.setBounds(classElementConvertor.getX(), classElementConvertor.getY(), classElementConvertor.getWidth(), selection.getBounds().height + 
							header.getBounds().height + attributes.getBounds().height + body.getBounds().height);
		classElementConvertor.setHeight(selection.getBounds().height + header.getBounds().height + attributes.getBounds().height + body.getBounds().height);
	}
	
	private int computeWidth(String text) {
		String[] elements = text.split("\n");
		int maxLength = -5;
		for (int i=0; i<elements.length; ++i) 
			if (elements[i].length() > maxLength) maxLength = elements[i].length();
		return maxLength * 8;
	}
	
	private int computeHeight(String text) {
		return text.split("\n").length * 20;
	}
	
	public void drawUpdatedElement() {
		// COMPUTE WIDTH AND HEIGHT
		int maxWidth = Math.max(computeWidth(classElementConvertor.getTitle()), Math.max(computeWidth(classElementConvertor.getAttributes()), computeWidth(classElementConvertor.getOperations())));
		classElementConvertor.setWidth(maxWidth + 10);
				
		selection.setBounds(50, 0, classElementConvertor.getWidth()-50, 25);
		
		// TITLE
		header.setBounds(0, 25, classElementConvertor.getWidth(), 30);
		
		// BOLD
		Font initialFont = classTitle.getFont();
		FontData[] fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setStyle(SWT.BOLD);
		}             
		final Font boldFont = new Font(this.getDisplay(), fontData);
		         
		// BOLD & ITALIC 
		FontData[] fontData2 = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData2[i].setStyle(SWT.BOLD);
			fontData2[i].setStyle(SWT.ITALIC);
		}             
		final Font boldAndItalicFont = new Font(this.getDisplay(), fontData2);
		         
		if (classElementConvertor.getIsAbstract()) {
		  	 classTitle.setFont(boldAndItalicFont);
		} else {
		   	 classTitle.setFont(boldFont); 
		}

		
		classTitle.setBounds(classElementConvertor.getWidth() / 2 - computeWidth(classElementConvertor.getTitle())/2, 5, computeWidth(classElementConvertor.getTitle()), 20);
		classTitle.setText(classElementConvertor.getTitle());
		
		// ATTRIBUTES
		attributesLabel.setText(classElementConvertor.getAttributes());
		attributesLabel.setBounds(3, 3, computeWidth(classElementConvertor.getAttributes()), computeHeight(classElementConvertor.getAttributes()));
		attributes.setBounds(0, 55, classElementConvertor.getWidth(), computeHeight(classElementConvertor.getAttributes()) + 10);
		
		// BODY
		operationsLabel.setBounds(3, 3, computeWidth(classElementConvertor.getOperations()), computeHeight(classElementConvertor.getOperations()));
		operationsLabel.setText(classElementConvertor.getOperations());
		body.setBounds(0, selection.getBounds().height + header.getBounds().height + attributes.getBounds().height, classElementConvertor.getWidth(), computeHeight(classElementConvertor.getOperations()) + 10);
				
		container.setBounds(0, 0, classElementConvertor.getWidth(), selection.getBounds().height + 
				header.getBounds().height + attributes.getBounds().height + body.getBounds().height);	
		this.setBounds(classElementConvertor.getX(), classElementConvertor.getY(), classElementConvertor.getWidth(), selection.getBounds().height + 
				header.getBounds().height + attributes.getBounds().height + body.getBounds().height);
		classElementConvertor.setHeight(selection.getBounds().height + header.getBounds().height + attributes.getBounds().height + body.getBounds().height);
	}
	
	public void updateElement(String iXmlContent) {
		classElementConvertor.decodeContent(iXmlContent);
		drawUpdatedElement();	
	}

	@Override
	public void drawRelations() {
		for (int i=0; i < relations.size(); ++i) {
			relations.get(i).drawElement();
		}		
	}
	
	public void addRelation(IElement relation) {
		relations.add(relation);
	}
	
	public void removeRelation(IElement relation) {
		relations.remove(relation);
	}

	@Override
	public void removeElement() {
		this.setVisible(false);		
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
	public ElementType getType() {
		return type;
	}
	
	@Override
	public int getID() {
		return classElementConvertor.getID();
	}

	public int getX() {
		return classElementConvertor.getX();
	}

	public void setX(int iX) {
		classElementConvertor.setX(iX);
		setBounds(classElementConvertor.getX(), classElementConvertor.getY(), classElementConvertor.getWidth(), classElementConvertor.getHeight());
		
	}

	public int getY() {
		return classElementConvertor.getY();
	}

	public void setY(int iY) {
		classElementConvertor.setY(iY);
		setBounds(classElementConvertor.getX(), classElementConvertor.getY(), classElementConvertor.getWidth(), classElementConvertor.getHeight());
	}

	public int getWidth() {
		return classElementConvertor.getWidth();
	}

	public void setWidth(int iWidth) {
		classElementConvertor.setWidth(iWidth);
		setBounds(classElementConvertor.getX(), classElementConvertor.getY(), classElementConvertor.getWidth(), classElementConvertor.getHeight());
	}

	public int getHeight() {
		return classElementConvertor.getHeight();
	}

	public void setHeight(int iHeight) {
		classElementConvertor.setHeight(iHeight);
		setBounds(classElementConvertor.getX(), classElementConvertor.getY(), classElementConvertor.getWidth(), classElementConvertor.getHeight());
	}
	
	public void setTitle(String text) {
		classElementConvertor.setTitle(text);
		this.classTitle.setText(text);
    }
	
	public String getTitle() {
		return classElementConvertor.getTitle();
	}
	
	public void setEditedBy(String user) {
		classElementConvertor.setEditedBy(user);
		selectionUser.setText(user);
	}
	
	public String getEditedBy() {
		return classElementConvertor.getEditedBy();
	}

	@Override
	public void setAttributes(String attributes) {
		classElementConvertor.setAttributes(attributes);
		
	}

	@Override
	public void setOperations(String operations) {
		classElementConvertor.setOperations(operations);
		
	}
	
	public void setVisibility(String visibility) {
		classElementConvertor.setVisibility(visibility);
	}

	public void setIsAbstract(boolean isAbstract) {
		classElementConvertor.setIsAbstract(isAbstract);
		
	}
}




	
	
	
	

	


