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

public class AssociationElementConvertor extends GraphicElement {
	private ArrayList<String> mAssociationElementInfosName;
	private ArrayList<String> mAssociationElementInfosValue;
	private final String DEFAULT_VALUE = "1";
	
	private DocumentBuilderFactory mDbfac = null;
    private DocumentBuilder mDocBuilder = null;
    private Document mXmlDoc;

    public AssociationElementConvertor(int elementId, boolean status) { 
    	super(elementId, status);
    	initialize();
    	mAssociationElementInfosName = new ArrayList<String>();
		mAssociationElementInfosValue = new ArrayList<String>();
		
		mAssociationElementInfosName.add(AssociationElementHelper.ELEMENT_ID_POS , AssociationElementHelper.ELEMENT_ID);
		mAssociationElementInfosValue.add(AssociationElementHelper.ELEMENT_ID_POS, String.valueOf(elementId));
				
		mAssociationElementInfosName.add(AssociationElementHelper.START_ELEMENT_ID_POS , AssociationElementHelper.START_ELEMENT_ID);
		mAssociationElementInfosValue.add(AssociationElementHelper.START_ELEMENT_ID_POS, DEFAULT_VALUE);
		
		mAssociationElementInfosName.add(AssociationElementHelper.END_ELEMENT_ID_POS, AssociationElementHelper.END_ELEMENT_ID);
		mAssociationElementInfosValue.add(AssociationElementHelper.END_ELEMENT_ID_POS, DEFAULT_VALUE);
		
		mAssociationElementInfosName.add(AssociationElementHelper.ELEMENT_TITLE_POS, AssociationElementHelper.ELEMENT_TITLE);
		mAssociationElementInfosValue.add(AssociationElementHelper.ELEMENT_TITLE_POS, DEFAULT_VALUE);
		
		mAssociationElementInfosName.add(AssociationElementHelper.START_NAME_POS, AssociationElementHelper.START_NAME);
		mAssociationElementInfosValue.add(AssociationElementHelper.START_NAME_POS, AssociationElementHelper.DEFAULT_NAME);
		
		mAssociationElementInfosName.add(AssociationElementHelper.START_VISIBILITY_POS, AssociationElementHelper.START_VISIBILITY);
		mAssociationElementInfosValue.add(AssociationElementHelper.START_VISIBILITY_POS, AssociationElementHelper.DEFAULT_VISIBILITY);
		
		mAssociationElementInfosName.add(AssociationElementHelper.START_IS_NAVIGABLE_POS, AssociationElementHelper.START_IS_NAVIGABLE);
		mAssociationElementInfosValue.add(AssociationElementHelper.START_IS_NAVIGABLE_POS, AssociationElementHelper.DEFAULT_IS_NAVIGABLE);
		
		mAssociationElementInfosName.add(AssociationElementHelper.START_AGGREGATION_POS, AssociationElementHelper.START_AGGREGATION);
		mAssociationElementInfosValue.add(AssociationElementHelper.START_AGGREGATION_POS, AssociationElementHelper.DEFAULT_AGGREGATION);
		
		mAssociationElementInfosName.add(AssociationElementHelper.START_MULTIPLICITY_POS, AssociationElementHelper.START_MULTIPLICITY);
		mAssociationElementInfosValue.add(AssociationElementHelper.START_MULTIPLICITY_POS, AssociationElementHelper.DEFAULT_MULTIPLICITY);
		
		mAssociationElementInfosName.add(AssociationElementHelper.START_CONNECTED_ELEMENT_POS, AssociationElementHelper.START_CONNECTED_ELEMENT);
		mAssociationElementInfosValue.add(AssociationElementHelper.START_CONNECTED_ELEMENT_POS, AssociationElementHelper.DEFAULT_CONNECTED_ELEMENT);
		
		mAssociationElementInfosName.add(AssociationElementHelper.END_NAME_POS, AssociationElementHelper.END_NAME);
		mAssociationElementInfosValue.add(AssociationElementHelper.END_NAME_POS, AssociationElementHelper.DEFAULT_NAME);
		
		mAssociationElementInfosName.add(AssociationElementHelper.END_VISIBILITY_POS, AssociationElementHelper.END_VISIBILITY);
		mAssociationElementInfosValue.add(AssociationElementHelper.END_VISIBILITY_POS, AssociationElementHelper.DEFAULT_VISIBILITY);
		
		mAssociationElementInfosName.add(AssociationElementHelper.END_IS_NAVIGABLE_POS, AssociationElementHelper.END_IS_NAVIGABLE);
		mAssociationElementInfosValue.add(AssociationElementHelper.END_IS_NAVIGABLE_POS, AssociationElementHelper.DEFAULT_IS_NAVIGABLE);
		
		mAssociationElementInfosName.add(AssociationElementHelper.END_AGGREGATION_POS, AssociationElementHelper.END_AGGREGATION);
		mAssociationElementInfosValue.add(AssociationElementHelper.END_AGGREGATION_POS, AssociationElementHelper.DEFAULT_AGGREGATION);
		
		mAssociationElementInfosName.add(AssociationElementHelper.END_MULTIPLICITY_POS, AssociationElementHelper.END_MULTIPLICITY);
		mAssociationElementInfosValue.add(AssociationElementHelper.END_MULTIPLICITY_POS, AssociationElementHelper.DEFAULT_MULTIPLICITY);
    
		mAssociationElementInfosName.add(AssociationElementHelper.END_CONNECTED_ELEMENT_POS, AssociationElementHelper.END_CONNECTED_ELEMENT);
		mAssociationElementInfosValue.add(AssociationElementHelper.END_CONNECTED_ELEMENT_POS, AssociationElementHelper.DEFAULT_CONNECTED_ELEMENT);
		
		mAssociationElementInfosName.add(AssociationElementHelper.GENERALIZATION_POS, AssociationElementHelper.GENERALIZATION);
		mAssociationElementInfosValue.add(AssociationElementHelper.GENERALIZATION_POS, AssociationElementHelper.DEFAULT_IS_NAVIGABLE);
		
		mAssociationElementInfosName.add(AssociationElementHelper.REALIZATION_POS, AssociationElementHelper.REALIZATION);
		mAssociationElementInfosValue.add(AssociationElementHelper.REALIZATION_POS, AssociationElementHelper.DEFAULT_IS_NAVIGABLE);
    
		mAssociationElementInfosName.add(AssociationElementHelper.DEPENDENCY_POS, AssociationElementHelper.DEPENDENCY);
		mAssociationElementInfosValue.add(AssociationElementHelper.DEPENDENCY_POS, AssociationElementHelper.DEFAULT_IS_NAVIGABLE);
    }
    
	public void decodeContent(String iXmlContent) {
		mAssociationElementInfosName = new ArrayList<String>();
		mAssociationElementInfosValue = new ArrayList<String>();
				
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
		        	mAssociationElementInfosName.add(i-k, lNode.getNodeName());
		        	mAssociationElementInfosValue.add(i-k, lNode.getTextContent());
		        	
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
		mAssociationElementInfosName = new ArrayList<String>();
		mAssociationElementInfosValue = new ArrayList<String>();
				
		try {
			NodeList lChildNodes = iXmlNode.getChildNodes();
		        
		    int k = 0;
		    for (int i = 0; i < lChildNodes.getLength(); i++) {
		    	Node lNode = (Node) lChildNodes.item(i);
		        if (!lNode.getNodeName().equals("#text")) {
		        	++k;
		        	mAssociationElementInfosName.add(i-k, lNode.getNodeName());
		        	mAssociationElementInfosValue.add(i-k, lNode.getTextContent());
		        	
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
            Element lSmsNode = mXmlDoc.createElement("association");
            mXmlDoc.appendChild(lSmsNode);

            for (int i=0; i<mAssociationElementInfosName.size(); ++i) {
            	Element lNode = mXmlDoc.createElement(mAssociationElementInfosName.get(i));
                lSmsNode.appendChild(lNode);
            	
                //add text in this node
                Text lText = mXmlDoc.createTextNode(mAssociationElementInfosValue.get(i).toString());
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
		mAssociationElementInfosValue.set(AssociationElementHelper.ELEMENT_ID_POS, String.valueOf(iID));
	}
	
	public int getID() {
		return Integer.valueOf(mAssociationElementInfosValue.get(AssociationElementHelper.ELEMENT_ID_POS));
	}

	public int getStartElementId() {
		return Integer.valueOf(mAssociationElementInfosValue.get(AssociationElementHelper.START_ELEMENT_ID_POS));
	}

	public void setStartElementId(int iX) {
		mAssociationElementInfosValue.set(AssociationElementHelper.START_ELEMENT_ID_POS, String.valueOf(iX));
	}

	public int getEndElementId() {
		return Integer.valueOf(mAssociationElementInfosValue.get(AssociationElementHelper.END_ELEMENT_ID_POS));
	}

	public void setEndElementId(int iY) {
		mAssociationElementInfosValue.set(AssociationElementHelper.END_ELEMENT_ID_POS, String.valueOf(iY));
	}
	
	public void setStartName(String iName) {
		mAssociationElementInfosValue.set(AssociationElementHelper.START_NAME_POS, iName);
	}
	
	public String getStartName() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.START_NAME_POS);
	}
	
	public void setStartVisibility(String iVisibility) {
		mAssociationElementInfosValue.set(AssociationElementHelper.START_VISIBILITY_POS, iVisibility);
	}
	
	public String getStartVisibility() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.START_VISIBILITY_POS);
	}
	
	public void setStartIsNavigable(boolean isNavigable) {
		if (isNavigable) {
			mAssociationElementInfosValue.set(AssociationElementHelper.START_IS_NAVIGABLE_POS, "1");
		} else {
			mAssociationElementInfosValue.set(AssociationElementHelper.START_VISIBILITY_POS, "0");
		}
	}
	
	public boolean getStartIsNavigable() {
		if (mAssociationElementInfosValue.get(AssociationElementHelper.START_IS_NAVIGABLE_POS).equals("1")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setStartAggregation(String iAggregation) {
		mAssociationElementInfosValue.set(AssociationElementHelper.START_AGGREGATION_POS, iAggregation);
	}
	
	public String getStartAggregation() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.START_AGGREGATION_POS);
	}
	
	public void setStartMultiplicity(String iMultiplicity) {
		mAssociationElementInfosValue.set(AssociationElementHelper.START_MULTIPLICITY_POS, iMultiplicity);
	}
	
	public String getStartMultiplicity() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.START_MULTIPLICITY_POS);
	}
	
	public void setEndName(String iName) {
		mAssociationElementInfosValue.set(AssociationElementHelper.END_NAME_POS, iName);
	}
	
	public String getEndName() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.END_NAME_POS);
	}
	
	public void setEndVisibility(String iVisibility) {
		mAssociationElementInfosValue.set(AssociationElementHelper.END_VISIBILITY_POS, iVisibility);
	}
	
	public String getEndVisibility() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.END_VISIBILITY_POS);
	}
	
	public void setEndIsNavigable(boolean isNavigable) {
		if (isNavigable) {
			mAssociationElementInfosValue.set(AssociationElementHelper.END_IS_NAVIGABLE_POS, "1");
		} else {
			mAssociationElementInfosValue.set(AssociationElementHelper.END_VISIBILITY_POS, "0");
		}
	}
	
	public boolean getEndIsNavigable() {
		if (mAssociationElementInfosValue.get(AssociationElementHelper.END_IS_NAVIGABLE_POS).equals("1")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setEndAggregation(String iAggregation) {
		mAssociationElementInfosValue.set(AssociationElementHelper.END_AGGREGATION_POS, iAggregation);
	}
	
	public String getEndAggregation() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.END_AGGREGATION_POS);
	}
	
	public void setEndMultiplicity(String iMultiplicity) {
		mAssociationElementInfosValue.set(AssociationElementHelper.END_MULTIPLICITY_POS, iMultiplicity);
	}
	
	public String getEndMultiplicity() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.END_MULTIPLICITY_POS);
	}
	
	public void setElementName(String iTitle) {
		mAssociationElementInfosValue.set(AssociationElementHelper.ELEMENT_TITLE_POS, iTitle);
	}
	
	public String getElementName() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.ELEMENT_TITLE_POS);
	}
	
	public void setStartConnectedElement(String iName) {
		mAssociationElementInfosValue.set(AssociationElementHelper.START_CONNECTED_ELEMENT_POS, iName);
	}
	
	public String getStartConnectedElement() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.START_CONNECTED_ELEMENT_POS);
	}
	
	public void setEndConnectedElement(String iName) {
		mAssociationElementInfosValue.set(AssociationElementHelper.END_CONNECTED_ELEMENT_POS, iName);
	}
	
	public String getEndConnectedElement() {
		return mAssociationElementInfosValue.get(AssociationElementHelper.END_CONNECTED_ELEMENT_POS);
	}
	
	public void setGeneralization(boolean isGeneralization) {
		if (isGeneralization) {
			mAssociationElementInfosValue.set(AssociationElementHelper.GENERALIZATION_POS, "1");
		} else {
			mAssociationElementInfosValue.set(AssociationElementHelper.GENERALIZATION_POS, "0");
		}
	}
	
	public boolean getGeneralization() {
		if (mAssociationElementInfosValue.get(AssociationElementHelper.GENERALIZATION_POS).equals("1")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setRealization(boolean isRealization) {
		if (isRealization) {
			mAssociationElementInfosValue.set(AssociationElementHelper.REALIZATION_POS, "1");
		} else {
			mAssociationElementInfosValue.set(AssociationElementHelper.REALIZATION_POS, "0");
		}
	}
	
	public boolean getRealization() {
		if (mAssociationElementInfosValue.get(AssociationElementHelper.REALIZATION_POS).equals("1")) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setDependency(boolean isRealization) {
		if (isRealization) {
			mAssociationElementInfosValue.set(AssociationElementHelper.DEPENDENCY_POS, "1");
		} else {
			mAssociationElementInfosValue.set(AssociationElementHelper.DEPENDENCY_POS, "0");
		}
	}
	
	public boolean getDependency() {
		if (mAssociationElementInfosValue.get(AssociationElementHelper.DEPENDENCY_POS).equals("1")) {
			return true;
		} else {
			return false;
		}
	}
	

	    
}


