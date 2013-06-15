package org.umldiagram.elements;

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

import org.umldiagram.GraphicElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class ClassElementConvertor extends GraphicElement {
	private ArrayList<String> mClassElementInfosName;
	private ArrayList<String> mClassElementInfosValue;
	private final String DEFAULT_VALUE = "1";
	
	private DocumentBuilderFactory mDbfac = null;
    private DocumentBuilder mDocBuilder = null;
    private Document mXmlDoc;
    private ClassElementHelper classElementHelper;

    public ClassElementConvertor(int ID, boolean isSelected) { 
    	super(ID, isSelected);
    	classElementHelper = new ClassElementHelper();
    	initialize();
    	
    	mClassElementInfosName = new ArrayList<String>();
		mClassElementInfosValue = new ArrayList<String>();
				
		mClassElementInfosName.add(classElementHelper.ELEMENT_ID_POS , classElementHelper.ELEMENT_ID);
		mClassElementInfosValue.add(classElementHelper.ELEMENT_ID_POS, String.valueOf(ID));
		
		mClassElementInfosName.add(classElementHelper.X_POS, classElementHelper.X_PARAM);
		mClassElementInfosValue.add(classElementHelper.X_POS, DEFAULT_VALUE);
		
		mClassElementInfosName.add(classElementHelper.Y_POS, classElementHelper.Y_PARAM);
		mClassElementInfosValue.add(classElementHelper.Y_POS, DEFAULT_VALUE);
		
		mClassElementInfosName.add(classElementHelper.WIDTH_POS, classElementHelper.WIDTH_PARAM);
		mClassElementInfosValue.add(classElementHelper.WIDTH_POS, DEFAULT_VALUE);

		mClassElementInfosName.add(classElementHelper.HEIGHT_POS, classElementHelper.HEIGHT_PARAM);
		mClassElementInfosValue.add(classElementHelper.HEIGHT_POS, DEFAULT_VALUE);
		
		Random lRandomNumberGenerator = new Random();
		mClassElementInfosName.add(classElementHelper.TITLE_POS, classElementHelper.TITLE_PARAM);
		mClassElementInfosValue.add(classElementHelper.TITLE_POS, "abc" + lRandomNumberGenerator.nextInt());    	
    
		mClassElementInfosName.add(classElementHelper.EDITED_BY_POS, classElementHelper.EDITED_BY);
		mClassElementInfosValue.add(classElementHelper.EDITED_BY_POS, "none");
		
		mClassElementInfosName.add(classElementHelper.VISIBILITY_POS, classElementHelper.VISIBILITY);
		mClassElementInfosValue.add(classElementHelper.VISIBILITY_POS, classElementHelper.VISIBILITY_DEFAULT);
		
		mClassElementInfosName.add(classElementHelper.IS_ABSTRACT_POS, classElementHelper.IS_ABSTRACT);
		mClassElementInfosValue.add(classElementHelper.IS_ABSTRACT_POS, classElementHelper.IS_ABSTRACT_DEFAULT);
		
		mClassElementInfosName.add(classElementHelper.ATTRIBUTES_POS, classElementHelper.ATTRIBUTES);
		mClassElementInfosValue.add(classElementHelper.ATTRIBUTES_POS, classElementHelper.ATTRIBUTES_DEFAULT);

		mClassElementInfosName.add(classElementHelper.OPERATIONS_POS, classElementHelper.OPERATIONS);
		mClassElementInfosValue.add(classElementHelper.OPERATIONS_POS, classElementHelper.OPERATIONS_DEFAULT);
    }
    
	public void decodeContent(String iXmlContent) {
		mClassElementInfosName = new ArrayList<String>();
		mClassElementInfosValue = new ArrayList<String>();
				
		try {
			InputSource lIs = new InputSource();
		    lIs.setCharacterStream(new StringReader(iXmlContent));

		    Document lXmlDoc = mDocBuilder.parse(lIs);
		        
		    Node lRoot = lXmlDoc.getFirstChild();
		    NodeList lChildNodes = lRoot.getChildNodes();
		        
		    int k = 0;
		    for (int i = 0; i < lChildNodes.getLength(); i++) {
		    	Node lNode = (Node) lChildNodes.item(i);
		        if (!lNode.getNodeName().equals("#text")) {
		        	++k;
		        	mClassElementInfosName.add(i-k, lNode.getNodeName());
		        	mClassElementInfosValue.add(i-k, lNode.getTextContent());
		        	
		       }
		        //System.out.println("Numele nodului = " + lNode.getNodeName());
	        	//System.out.println("Valoarea = " + lNode.getTextContent());		                 
		    }
		 }
		 catch (Exception e) {
			 System.out.println("XML -> data error");
			 e.printStackTrace();
		 }
	}
	
	public void decodeContent(Node iXmlNode) {
		mClassElementInfosName = new ArrayList<String>();
		mClassElementInfosValue = new ArrayList<String>();
				
		try {
			NodeList lChildNodes = iXmlNode.getChildNodes();
		        
		    int k = 0;
		    for (int i = 0; i < lChildNodes.getLength(); i++) {
		    	Node lNode = (Node) lChildNodes.item(i);
		        if (!lNode.getNodeName().equals("#text")) {
		        	++k;
		        	mClassElementInfosName.add(i-k, lNode.getNodeName());
		        	mClassElementInfosValue.add(i-k, lNode.getTextContent());
		        	
		       }
		        //System.out.println("Numele nodului = " + lNode.getNodeName());
	        	//System.out.println("Valoarea = " + lNode.getTextContent());		                 
		    }
		 }
		 catch (Exception e) {
			 System.out.println("XML -> data error");
			 e.printStackTrace();
		 }
	}
	
	
	public void initialize() {
    	mDbfac = DocumentBuilderFactory.newInstance();
        try {
    		mDocBuilder = mDbfac.newDocumentBuilder();
    	} catch (ParserConfigurationException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
	
	public Element toXMLNode() {
		try {        
			mXmlDoc = mDocBuilder.newDocument();
            Element lSmsNode = mXmlDoc.createElement("class");
            mXmlDoc.appendChild(lSmsNode);

            for (int i=0; i<mClassElementInfosName.size(); ++i) {
            	Element lNode = mXmlDoc.createElement(mClassElementInfosName.get(i));
                lSmsNode.appendChild(lNode);
            	
                //add text in this node
                Text lText = mXmlDoc.createTextNode(mClassElementInfosValue.get(i).toString());
                lNode.appendChild(lText);            	
            }
            
            return lSmsNode;

		} catch (Exception e) {
            System.out.println("toXMLNode = " + e);
            return null;
        }
		
	}
	
	public String toXMLString() {
		try {
			this.toXMLNode(); // adauga sms-ul 
			
            TransformerFactory lTransfac = TransformerFactory.newInstance();
            Transformer lTrans = lTransfac.newTransformer();
            lTrans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            lTrans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter lSw = new StringWriter();
            StreamResult result = new StreamResult(lSw);
            DOMSource source = new DOMSource(mXmlDoc);
            lTrans.transform(source, result);
            
            return lSw.toString();
			
		} catch (Exception e) {
			return "Eroare XML";
		}
	}
	
	public void setID(int iID) {
		mClassElementInfosValue.set(classElementHelper.ELEMENT_ID_POS, String.valueOf(iID));
	}
	
	public int getID() {
		return Integer.valueOf(mClassElementInfosValue.get(classElementHelper.ELEMENT_ID_POS));
	}

	public int getX() {
		return Integer.valueOf(mClassElementInfosValue.get(classElementHelper.X_POS));
	}

	public void setX(int iX) {
		mClassElementInfosValue.set(classElementHelper.X_POS, String.valueOf(iX));
	}

	public int getY() {
		return Integer.valueOf(mClassElementInfosValue.get(classElementHelper.Y_POS));
	}

	public void setY(int iY) {
		mClassElementInfosValue.set(classElementHelper.Y_POS, String.valueOf(iY));
	}

	public int getWidth() {
		return Integer.valueOf(mClassElementInfosValue.get(classElementHelper.WIDTH_POS));
	}

	public void setWidth(int iWidth) {
		mClassElementInfosValue.set(classElementHelper.WIDTH_POS, String.valueOf(iWidth));
	}

	public int getHeight() {
		return Integer.valueOf(mClassElementInfosValue.get(classElementHelper.HEIGHT_POS));
	}

	public void setHeight(int iHeight) {
		mClassElementInfosValue.set(classElementHelper.HEIGHT_POS, String.valueOf(iHeight));
	}
	
	public void setTitle(String text) {
		mClassElementInfosValue.set(classElementHelper.TITLE_POS, text);
	}
	
	public String getTitle() {
		return mClassElementInfosValue.get(classElementHelper.TITLE_POS);
	}
	
	public void setEditedBy(String user) {
		mClassElementInfosValue.set(classElementHelper.EDITED_BY_POS, user);
	}
	
	public String getEditedBy() {
		return mClassElementInfosValue.get(classElementHelper.EDITED_BY_POS);
	}

    
}


