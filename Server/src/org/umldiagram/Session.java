package org.umldiagram;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.umldiagram.db.SessionHelper;
import org.umldiagram.elements.AssociationElementConvertor;
import org.umldiagram.elements.ClassElementConvertor;
import org.umldiagram.xmpp.XMPPConnect;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xmpp.packet.Message.Type;

public class Session implements PacketListener {
	public int sessionId;
	public int elementId;
	public XMPPConnect connection;
	private MultiUserChat muc;
	private HashMap<Integer, GraphicElement> whiteboard;
	private HashMap<String, GraphicElement> selectedElements;
	private HashMap<String, String> addressMap; // Real address - Chat address
	
	private DocumentBuilderFactory mDbfac = null;
    private DocumentBuilder mDocBuilder = null;
    private Document mXmlDoc;
	
	private static final String CLASS = "class";
	private static final String DIRECT_ASSOCIATION = "direct_association";
	private static final String ASSOCIATION = "association";
	private static final String AGGREGATION = "aggregation";
	private static final String COMPOSITION = "composition";
	private static final String GENERALIZATION = "generalization";
	private static final String REALIZATION = "realization";
	private static final String DEPENDENCY = "dependency";
	private static final String INTERFACE = "interface";
	private String filename;
	private int noOfUsers;
	
	
	public Session(int iSessionId, XMPPConnect iConnection) {
		initialize();
		sessionId = iSessionId;
		connection = iConnection;	
		elementId = SessionHelper.getLastElementIdOfSession(sessionId);
		filename = "session" + sessionId + ".txt";
		whiteboard = new HashMap<Integer, GraphicElement>();
		selectedElements = new HashMap<String, GraphicElement>();		
		addressMap = new HashMap<String, String>();
		noOfUsers = 0;
	}
	
	public int createElement() {
		++elementId; 
		return elementId;
	}
	
	public int initializeSession() {
		++noOfUsers;
		// Create a chat room - this will be used for negociation
		if (muc == null) {
			muc = new MultiUserChat(connection.getXMPPConnection(), "session" + sessionId + "@conference.smsfeedback.com");
						
			muc.addMessageListener(this);
			try {
			   	// Create the room
			 	muc.create("UmlDesigner-Bot");
			 	muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			   		
			   	SessionHelper.createSession(sessionId);
				writeToFile("<whiteboard></whiteboard>", filename);
				elementId = 0;
				
			} catch (XMPPException e) {
				e.printStackTrace();
				sessionId = -1;
			}	
		}		
		return sessionId;
	}
	
	public int joinSession() {
		++noOfUsers;
		elementId = SessionHelper.getLastElementIdOfSession(sessionId);
		if (SessionHelper.checkSessionById(sessionId)) {
			if (muc == null) {						
				muc = new MultiUserChat(connection.getXMPPConnection(), "session" + sessionId + "@conference.smsfeedback.com");
				muc.addMessageListener(this);				
				try {
					// Create the room
				   	muc.create("UmlDesigner-Bot");
				   	muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));		   											
				} catch (XMPPException e) {
					e.printStackTrace();
					sessionId = -1;
				}
			} 
		}
		return sessionId;
	}
	
	public String restoreSessionFromFile() {
		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(filename));
			try {
		      while (scanner.hasNextLine()){
		    	  text.append(scanner.nextLine() + NL);
		      }
			} finally{
				scanner.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return text.toString();		  
	}
	
	public void restoreSession() {
		//String sessionContent = SessionHelper.getSessionContent(sessionId);
		String sessionContent = restoreSessionFromFile();
		
		try {
			InputSource lIs = new InputSource();
		    lIs.setCharacterStream(new StringReader(sessionContent));

		    Document lXmlDoc = mDocBuilder.parse(lIs);
		        
		    Node lRoot = lXmlDoc.getFirstChild();
		    NodeList lChildNodes = lRoot.getChildNodes();
		        
		    int k = 0;
		    for (int i = 0; i < lChildNodes.getLength(); i++) {
		    	Node lNode = (Node) lChildNodes.item(i);
		        if (!lNode.getNodeName().equals("#text")) {
		        	if (lNode.getNodeName().equals(CLASS)) {
		        		ClassElementConvertor tempClassConvertor = new ClassElementConvertor(-1, false);
		        		tempClassConvertor.decodeContent(lNode);
		        		whiteboard.put(tempClassConvertor.getID(), tempClassConvertor);		        		
		        	}
		        }              
		    }
		 }
		 catch (Exception e) {
			 System.out.println("XML -> data error");
			 e.printStackTrace();
		 }
	}
	
	@Override
	public void processPacket(Packet arg0) {
		System.out.println("In session = " + arg0.toXML());
		Message lReceivedMessage = (Message)arg0;
		System.out.println("Subiect = " + lReceivedMessage.getSubject() + " session id is " + this.sessionId);
		if (lReceivedMessage.getSubject().equals("createElementRequest")) {
			try {
				Message lResponseMessage = new Message();
				int elementId = this.createElement();
				if (lReceivedMessage.getBody().equals(CLASS)) {
					ClassElementConvertor newClassCreated = new ClassElementConvertor(elementId, false);
					addElement(newClassCreated);
				} else if (lReceivedMessage.getBody().equals(ASSOCIATION) || lReceivedMessage.getBody().equals(DIRECT_ASSOCIATION) ||
						lReceivedMessage.getBody().equals(COMPOSITION) || lReceivedMessage.getBody().equals(AGGREGATION) ||
						lReceivedMessage.getBody().equals(GENERALIZATION) || lReceivedMessage.getBody().equals(REALIZATION) ||
						lReceivedMessage.getBody().equals(DEPENDENCY)) {
					AssociationElementConvertor newAssociationCreated = new AssociationElementConvertor(elementId, false);
					addElement(newAssociationCreated);					
				}
				// Persist in DB
				SessionHelper.updateLastElementIdOfSession(sessionId, elementId);
				
				lResponseMessage.setBody(lReceivedMessage.getBody() + "&%" + String.valueOf(elementId));
				lResponseMessage.setSubject("createElementResponse");
				lResponseMessage.setType(Message.Type.chat);
				lResponseMessage.setTo(lReceivedMessage.getFrom());
				muc.sendMessage(lResponseMessage);
			} catch (XMPPException e) {
				e.printStackTrace();
			}		
		} else if (lReceivedMessage.getSubject().equals("selectElementRequest")) {
			try {
				System.out.println("Element id =" + lReceivedMessage.getBody());
				System.out.println("Element selected = " + isElementSelected(Integer.valueOf(lReceivedMessage.getBody())));
				Message lResponseMessage = new Message();
				if (isElementSelected(Integer.valueOf(lReceivedMessage.getBody()))) {
					lResponseMessage.setBody("-1");
				} else {
					selectElement(Integer.valueOf(lReceivedMessage.getBody()));
					lResponseMessage.setBody(lReceivedMessage.getBody());
					selectedElements.put(lReceivedMessage.getFrom(), getElement(Integer.valueOf(lReceivedMessage.getBody())));
					getElement(Integer.valueOf(lReceivedMessage.getBody())).setEditedBy(extractName(lReceivedMessage.getFrom()));
					//writeToFile(this.getWhiteboardAsXmlString(), filename);
				}
				lResponseMessage.setSubject("selectElementResponse");
				lResponseMessage.setType(Message.Type.chat);
				lResponseMessage.setTo(lReceivedMessage.getFrom());
				muc.sendMessage(lResponseMessage);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		} else if (lReceivedMessage.getSubject().equals("unselectElementRequest")) {
			try {
				System.out.println("Element id =" + lReceivedMessage.getBody());
				System.out.println("Element unselected = " + isElementSelected(Integer.valueOf(lReceivedMessage.getBody())));
				Message lResponseMessage = new Message();
				if (isElementSelected(Integer.valueOf(lReceivedMessage.getBody()))) {
					unselectElement(Integer.valueOf(lReceivedMessage.getBody()));
					lResponseMessage.setBody(lReceivedMessage.getBody());
					if (selectedElements.get(lReceivedMessage.getFrom()).getID() == Integer.valueOf(lReceivedMessage.getBody())) {
						selectedElements.remove(lReceivedMessage.getFrom());
					}					
					getElement(Integer.valueOf(lReceivedMessage.getBody())).setEditedBy("none");
					//writeToFile(this.getWhiteboardAsXmlString(), filename);
				} else {
					lResponseMessage.setBody("-1");
				}
				lResponseMessage.setSubject("unselectElementResponse");
				lResponseMessage.setType(Message.Type.chat);
				lResponseMessage.setTo(lReceivedMessage.getFrom());
				muc.sendMessage(lResponseMessage);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		} else if (lReceivedMessage.getSubject().equals("createClass")) {
			ClassElementConvertor tempClassConvertor = new ClassElementConvertor(-1, false);
			tempClassConvertor.decodeContent(lReceivedMessage.getBody());
			ClassElementConvertor retrievedClass = (ClassElementConvertor) getElement(tempClassConvertor.getID());
			retrievedClass.decodeContent(lReceivedMessage.getBody());	
			//System.out.println(this.getWhiteboardAsXmlString());
			//SessionHelper.updateContentOfSession(this.sessionId, this.getWhiteboardAsXmlString());
			//writeToFile(this.getWhiteboardAsXmlString(), filename);
		} else if (lReceivedMessage.getSubject().equals("createAssociation")) {
			AssociationElementConvertor tempAssociationConvertor = new AssociationElementConvertor(-1, false);
			tempAssociationConvertor.decodeContent(lReceivedMessage.getBody());
			AssociationElementConvertor retrievedAssociation = (AssociationElementConvertor) getElement(tempAssociationConvertor.getID());
			retrievedAssociation.decodeContent(lReceivedMessage.getBody());
			//System.out.println(this.getWhiteboardAsXmlString());
			//SessionHelper.updateContentOfSession(this.sessionId, this.getWhiteboardAsXmlString());
			//writeToFile(this.getWhiteboardAsXmlString(), filename);
		} else if (lReceivedMessage.getSubject().equals("updateElement")) {
			String[] lContent = lReceivedMessage.getBody().split("&%");
			GraphicElement updatedElement = getElement(Integer.valueOf(lContent[0]));
			updatedElement.decodeContent(lContent[2]);
			//writeToFile(this.getWhiteboardAsXmlString(), filename);
		} else if (lReceivedMessage.getSubject().equals("myIdentity")) {
			addressMap.put(lReceivedMessage.getBody(), lReceivedMessage.getFrom());
		}
	}
	
	public void userLeftTheSession(String userLeftRealAddress) {
		String chatAddress = addressMap.get(userLeftRealAddress);
		
		if (selectedElements.get(chatAddress) != null) {
			GraphicElement selectedElement = selectedElements.get(chatAddress);
			selectedElement.setSelected(false);
			selectedElement.setEditedBy("none");
			System.out.println("User made some dirt");
			// NOTIFY PARTICIPATNS ABOUT THE CHANGE
			
			try {
				Message lResponseMessage = new Message();
				lResponseMessage.setBody(selectedElement.getID() + "&%nobody&%" + selectedElement.toXMLString());
				lResponseMessage.setSubject("enableElement");
				lResponseMessage.setType(Message.Type.groupchat);
				lResponseMessage.setTo("session" + sessionId + "@conference.smsfeedback.com");
				muc.sendMessage(lResponseMessage);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			selectedElements.remove(chatAddress);
		}	
		noOfUsers = noOfUsers - 1;
		if (noOfUsers == 0) {
			updateSessionContentFile();
		}
		addressMap.remove(userLeftRealAddress);
		System.out.println("Cleaning after the user");
	}
	
	public void updateSessionContentFile() {
		writeToFile(this.getWhiteboardAsXmlString(), filename);
	}
	
	public void addElement(GraphicElement element) {
		whiteboard.put(element.getID(), element);
	}
	
	public GraphicElement getElement(int ID) {
		return whiteboard.get(ID);
	}
	
	public boolean isElementSelected(int id) {
		return whiteboard.get(new Integer(id)).isSelected();
	}
	
	public void selectElement(int id) {
		whiteboard.get(new Integer(id)).setSelected(true);
	}
	
	public void unselectElement(int id) {
		whiteboard.get(new Integer(id)).setSelected(false);
	}
	
	public Element getWhiteboardAsXmlNode() {
		Iterator it = whiteboard.entrySet().iterator();
		
		try {        
			mXmlDoc = mDocBuilder.newDocument();
            Element lWhiteboardNode = mXmlDoc.createElement("whiteboard");
            mXmlDoc.appendChild(lWhiteboardNode);

            while (it.hasNext()) {
    	        Map.Entry pairs = (Map.Entry) it.next();
    	        GraphicElement lGraphicElement = (GraphicElement) pairs.getValue();
    	        lWhiteboardNode.appendChild(mXmlDoc.importNode(lGraphicElement.toXMLNode(), true));
    	        //it.remove(); // avoids a ConcurrentModificationException
    	    }           
            
            return lWhiteboardNode;
		} catch (Exception e) {
            System.out.println("toXMLNode = " + e);
            return null;
        }  

	}	
	
	public String getWhiteboardAsXmlString() {
		try {
			this.getWhiteboardAsXmlNode(); // adauga sms-ul 
			
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
	
	public String getTheLastVersion() {
		Date currentDate = new Date();
		String lastFile = "session" + sessionId + "v" + currentDate.getTime() + ".txt";
		writeToFile(this.getWhiteboardAsXmlString(), lastFile);
		return lastFile;
	}
	
	public void writeToFile(String iContent, String iFilename) {
		try{
			FileWriter fstream = new FileWriter(iFilename);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(iContent);
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public void initialize() {
    	mDbfac = DocumentBuilderFactory.newInstance();
        try {
    		mDocBuilder = mDbfac.newDocumentBuilder();
    	} catch (ParserConfigurationException e) {
    		e.printStackTrace();
    	}
    }
	
	public String getSessionFilename() {
		return filename;
	}
	
	public String extractName(String address) {
		int slashPosition = address.indexOf("/");
		return address.substring(slashPosition + 1, address.length());
	}
}
