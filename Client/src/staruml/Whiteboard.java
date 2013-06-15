package staruml;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import xmpp.XMPPConnect;

public class Whiteboard extends Composite implements PacketListener, MouseListener, MouseMoveListener, Observer, KeyListener {
	private XMPPConnect mConnection;
		
	private Display display;
	private ClassElement classElem1, classElem2, classElem3;
	private AssociationElement associationElem1, associationElem2;
	private static final String COMPONENT_ADDRESS = "umldesigner.smsfeedback.com";
	private static final String BOT_NICKNAME = "UmlDesigner-Bot";
	
	private static final String CLASS = "class";
	private static final String DIRECT_ASSOCIATION = "direct_association";
	private static final String ASSOCIATION = "association";
	private static final String AGGREGATION = "aggregation";
	private static final String COMPOSITION = "composition";
	private static final String GENERALIZATION = "generalization";
	private static final String REALIZATION = "realization";
	private static final String DEPENDENCY = "dependency";
	private static final String INTERFACE = "interface";

	private String userId;
	private IElement selectedElement;
	private Boolean mousePressed = false;
	private boolean readyToDraw;
	
	private Point startPoint, selectionPoint;
	IElement temporaryElement;
		
	private String command = "none";
	private ArrayList<IElement> relationElements = new ArrayList<IElement>();
	
	ArrayList<Observer> mObservers = new ArrayList<Observer>();
	
	private HashMap<Integer, IElement> whiteboardContent = new HashMap<Integer, IElement>();
	private Whiteboard whiteboard;
	private MultiUserChat muc;
	private Composite statusBar;
	
	// Xml parsing
	private DocumentBuilderFactory mDbfac = null;
    private DocumentBuilder mDocBuilder = null;
    private Document mXmlDoc;
    // Problema cu actualizarea listei cu participantii din cauza threadului de UI.
    private int updateParticipantsListIndex;
    private String sessionAddress;
	
	public Whiteboard(Composite parent, int style) {
		super(parent, style);
		whiteboard = this;
		updateParticipantsListIndex = 0;		
		mConnection = XMPPConnect.getInstance("176.34.122.48", "client", "umldesigner.smsfeedback.com", this);
		mConnection.addPacketListener(this);
		
		initializeMUC();
		mConnection.sendPresence(Presence.Type.available, "Hello my bot friend!", COMPONENT_ADDRESS);  
		
	    display = this.getDisplay();
	    Image whiteboardBck = new Image(display, "D:\\Work\\Licenta\\images\\bck.jpg");
	   	
	   	this.setBackgroundImage(whiteboardBck);
	   	this.setBackgroundMode(SWT.INHERIT_DEFAULT);
	   	this.addMouseListener(this);
		this.addMouseMoveListener(this);
		this.addKeyListener(this);
		
		readyToDraw = true;
		
		if (mConnection.getSessionType().equals("JOINED")) {
			restoreWhiteboardFromFile();
		}
		
	}

	@Override
	public void processPacket(Packet packet) {
		final Packet receivedPack = packet;
				
		if (packet instanceof Message) {
		final Message lReceivedMessage = (Message)packet;
			System.out.println(packet.toXML());
			// Conversatia cu botul
			if (lReceivedMessage.getType() == Message.Type.chat) {
				if (lReceivedMessage.getSubject().equals("createElementResponse")) {
					// pentru ca trebuie executat pe threadul de UI.
					
					final String[] lContent = lReceivedMessage.getBody().split("&%");
					if (lContent[0].equals(CLASS)) {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									ClassElement temporaryClassElement = new ClassElement(whiteboard, SWT.NONE, selectionPoint.x, selectionPoint.y , 100, 160, Integer.valueOf(lContent[1]));
									//temporaryClassElement.setTitle("New created");
									temporaryClassElement.setVisible(false);
								
									mConnection.sendMessage("createClass", temporaryClassElement.toXMLString() , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Numar de parametrii gresit");
								} catch (Exception e) {
									System.out.println("Create element class error = " + e.getMessage());
								}
							}	
						});	
					} else if (lContent[0].equals(ASSOCIATION))  {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									AssociationElement temporaryAssociationElement = new AssociationElement(whiteboard, SWT.NONE, relationElements.get(0), relationElements.get(1), Integer.valueOf(lContent[1]));
									temporaryAssociationElement.setTitle("administreaza");
									temporaryAssociationElement.setStartName("manager");
									temporaryAssociationElement.setStartVisibility(AssociationElementHelper.VISIBILITY_PUBLIC);
									temporaryAssociationElement.setStartMultiplicity(AssociationElementHelper.MULTIPLICITY_1);
									temporaryAssociationElement.setEndName("echipa");
									temporaryAssociationElement.setEndVisibility(AssociationElementHelper.VISIBILITY_PRIVATE);
									temporaryAssociationElement.setEndMultiplicity(AssociationElementHelper.MULTIPLICITY_1_n);
									temporaryAssociationElement.setVisible(false);
								
									mConnection.sendMessage("createAssociation", temporaryAssociationElement.toXMLString() , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
									relationElements = new ArrayList<IElement>();
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Numar de parametrii gresit");
								} catch (Exception e) {
									System.out.println("Create element association error = " + e.getMessage());
								}
							}	
						});	
					} else if (lContent[0].equals(DIRECT_ASSOCIATION))  {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									AssociationElement temporaryAssociationElement = new AssociationElement(whiteboard, SWT.NONE, relationElements.get(0), relationElements.get(1), Integer.valueOf(lContent[1]));
									temporaryAssociationElement.setTitle("administreaza");
									temporaryAssociationElement.setStartName("manager");
									temporaryAssociationElement.setStartVisibility(AssociationElementHelper.VISIBILITY_PUBLIC);
									temporaryAssociationElement.setStartMultiplicity(AssociationElementHelper.MULTIPLICITY_1);
									temporaryAssociationElement.setEndName("echipa");
									temporaryAssociationElement.setEndVisibility(AssociationElementHelper.VISIBILITY_PRIVATE);
									temporaryAssociationElement.setEndMultiplicity(AssociationElementHelper.MULTIPLICITY_1_n);
									temporaryAssociationElement.setVisible(false);
									temporaryAssociationElement.isDirectedAssociation();
								
									mConnection.sendMessage("createAssociation", temporaryAssociationElement.toXMLString() , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
									relationElements = new ArrayList<IElement>();
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Numar de parametrii gresit");
								} catch (Exception e) {
									System.out.println("Create element association error = " + e.getMessage());
								}
							}	
						});	
					} else if (lContent[0].equals(AGGREGATION))  {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									AssociationElement temporaryAssociationElement = new AssociationElement(whiteboard, SWT.NONE, relationElements.get(0), relationElements.get(1), Integer.valueOf(lContent[1]));
									temporaryAssociationElement.setVisible(false);
									temporaryAssociationElement.isAggregation();
								
									mConnection.sendMessage("createAssociation", temporaryAssociationElement.toXMLString() , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
									relationElements = new ArrayList<IElement>();
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Numar de parametrii gresit");
								} catch (Exception e) {
									System.out.println("Create element association error = " + e.getMessage());
								}
							}	
						});	
					} else if (lContent[0].equals(COMPOSITION))  {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									AssociationElement temporaryAssociationElement = new AssociationElement(whiteboard, SWT.NONE, relationElements.get(0), relationElements.get(1), Integer.valueOf(lContent[1]));
									temporaryAssociationElement.setVisible(false);
									temporaryAssociationElement.isComposition();
								
									mConnection.sendMessage("createAssociation", temporaryAssociationElement.toXMLString() , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
									relationElements = new ArrayList<IElement>();
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Numar de parametrii gresit");
								} catch (Exception e) {
									System.out.println("Create element association error = " + e.getMessage());
								}
							}	
						});	
					} else if (lContent[0].equals(GENERALIZATION))  {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									AssociationElement temporaryAssociationElement = new AssociationElement(whiteboard, SWT.NONE, relationElements.get(0), relationElements.get(1), Integer.valueOf(lContent[1]));
									temporaryAssociationElement.setVisible(false);
									temporaryAssociationElement.isGeneralization();
								
									mConnection.sendMessage("createAssociation", temporaryAssociationElement.toXMLString() , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
									relationElements = new ArrayList<IElement>();
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Numar de parametrii gresit");
								} catch (Exception e) {
									System.out.println("Create element association error = " + e.getMessage());
								}
							}	
						});	
					} else if (lContent[0].equals(REALIZATION))  {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									AssociationElement temporaryAssociationElement = new AssociationElement(whiteboard, SWT.NONE, relationElements.get(0), relationElements.get(1), Integer.valueOf(lContent[1]));
									temporaryAssociationElement.setVisible(false);
									temporaryAssociationElement.isRealization();
								
									mConnection.sendMessage("createAssociation", temporaryAssociationElement.toXMLString() , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
									relationElements = new ArrayList<IElement>();
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Numar de parametrii gresit");
								} catch (Exception e) {
									System.out.println("Create element association error = " + e.getMessage());
								}
							}	
						});	
					} else if (lContent[0].equals(DEPENDENCY))  {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									AssociationElement temporaryAssociationElement = new AssociationElement(whiteboard, SWT.NONE, relationElements.get(0), relationElements.get(1), Integer.valueOf(lContent[1]));
									temporaryAssociationElement.setVisible(false);
									temporaryAssociationElement.isDependency();
								
									mConnection.sendMessage("createAssociation", temporaryAssociationElement.toXMLString() , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
									relationElements = new ArrayList<IElement>();
								} catch (ArrayIndexOutOfBoundsException e) {
									System.out.println("Numar de parametrii gresit");
								} catch (Exception e) {
									System.out.println("Create element association error = " + e.getMessage());
								}
							}	
						});	
					}
					
				} else if (lReceivedMessage.getSubject().equals("selectElementResponse")) {
					if (Integer.parseInt(lReceivedMessage.getBody()) >= 0) {
						// MUST BE GENERAL
						
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
								if (selectedElement != null) {
									mConnection.sendMessage("unselectElementRequest", String.valueOf(selectedElement.getID()), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
								}
								selectedElement = whiteboardContent.get(Integer.parseInt(lReceivedMessage.getBody()));
								selectedElement.selectedState();
								selectedElement.setAvailability(false);
								selectedElement.setLock(true);
								mConnection.sendMessage("disableElement", String.valueOf(selectedElement.getID()) + "&%" + userId , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
								} catch (Exception e) {
									System.out.println("Select element error = " + e.getMessage());
								}
							}	
						});		
						notifyAll(new Notification(ElementType.ENABLE, ""));
					}
				} else if (lReceivedMessage.getSubject().equals("unselectElementResponse")) {
					if (Integer.parseInt(lReceivedMessage.getBody()) >= 0) {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									IElement unselectedElement = whiteboardContent.get(Integer.parseInt(lReceivedMessage.getBody()));
									unselectedElement.unselectedState();
									unselectedElement.setAvailability(true);
									unselectedElement.setLock(false);
									mConnection.sendMessage("enableElement", String.valueOf(unselectedElement.getID()) + "&%" + userId + "&%" + unselectedElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
									unselectedElement = null;
								} catch (Exception e) {
									System.out.println("Unselect element error = " + e.getMessage());
								}								
							}	
						});							
						notifyAll(new Notification(ElementType.DISABLE, ""));
					}
				}
				
				// Ce se deseneaza
			} else if (lReceivedMessage.getType() == Message.Type.groupchat) {
				if (lReceivedMessage.getSubject().equals("createClass")) {
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								ClassElement newClassElement = new ClassElement(whiteboard, SWT.NONE, lReceivedMessage.getBody());
								newClassElement.addMouseListener(whiteboard);	
								newClassElement.addMouseMoveListener(whiteboard);
								newClassElement.addKeyListener(whiteboard);
								whiteboardContent.put(newClassElement.getID(), newClassElement);
								readyToDraw = true;
							} catch (ArrayIndexOutOfBoundsException e) {
								System.out.println("Numar de parametrii gresit");
							} catch (Exception e) {
								System.out.println("Create class error = " + e.getMessage());
							}
						}	
					});	
				} else if (lReceivedMessage.getSubject().equals("createAssociation")) {
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								AssociationElement newAssociationElement = new AssociationElement(whiteboard, SWT.NONE, lReceivedMessage.getBody(), whiteboardContent);
								newAssociationElement.addMouseListener(whiteboard);	
								newAssociationElement.addMouseMoveListener(whiteboard);
								newAssociationElement.addKeyListener(whiteboard);
								whiteboardContent.put(newAssociationElement.getID(), newAssociationElement);
								readyToDraw = true;
							} catch (ArrayIndexOutOfBoundsException e) {
								System.out.println("Numar de parametrii gresit");
							} catch (Exception e) {
								System.out.println("Create association error = " + e.getMessage());
							}
						}	
					});	
				} else if (lReceivedMessage.getSubject().equals("disableElement")) {
					final String[] lContent = lReceivedMessage.getBody().split("&%");
					if (!lContent[1].equals(userId)) {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
									try {
										IElement disabledElement = whiteboardContent.get(Integer.valueOf(lContent[0]));
										disabledElement.setLock(false);
										disabledElement.setAvailability(false);
										disabledElement.setEditedBy(userId);
										disabledElement.disabledState();
									}catch (Exception e) {
										System.out.println("Disable element error = " + e.getMessage());
									}
								}	
							});	
						notifyAll(new Notification(ElementType.DISABLE, ""));
					}
				} else if (lReceivedMessage.getSubject().equals("enableElement")) {
					final String[] lContent = lReceivedMessage.getBody().split("&%");
					if (!lContent[1].equals(userId)) {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									IElement enabledElement = whiteboardContent.get(Integer.valueOf(lContent[0]));
									enabledElement.unselectedState();
									enabledElement.updateElement(lContent[2]);
									enabledElement.setLock(false);
									enabledElement.setAvailability(true);								
								} catch (Exception e) {
									System.out.println("Enable element error = " + e.getMessage());
								}
							}
						});
						notifyAll(new Notification(ElementType.DISABLE, ""));
					}
				} else if (lReceivedMessage.getSubject().equals("updateElement")) {
					final String[] lContent = lReceivedMessage.getBody().split("&%");
					if (!lContent[1].equals(userId)) {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
									try {
										IElement updatedElement = whiteboardContent.get(Integer.valueOf(lContent[0]));
										updatedElement.updateElement(lContent[2]);
										updatedElement.drawRelations();
									} catch (Exception e) {
										System.out.println("Update element error = " + e.getMessage());
									}
								}	
							});	
					}
				}
				
			}
		}	
	}	

	@Override
	public void keyReleased(KeyEvent e) {
		 String key = Character.toString(e.character);
		 System.out.println("Key released = " + key);
		 if (e.character == SWT.DEL) {
			 System.out.println("Pressed = " + key);
			 selectedElement.removeElement();
		 }
		
	}
	
	@Override
	public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
				
		Boolean canvasSelected = true;
			
		if (! (e.widget instanceof Whiteboard)) {
			canvasSelected = false;
			// Get the selectedElement and focus
			// Draw relation
			if (command.equals(ASSOCIATION) || command.equals(DIRECT_ASSOCIATION) || command.equals(AGGREGATION) 
					|| command.equals(COMPOSITION) || command.equals(GENERALIZATION) || command.equals(REALIZATION)
					|| command.equals(DEPENDENCY)) {
								
				startPoint = new Point(e.x, e.y);
				Composite selectedComposite = (Composite)e.widget;
				if (e.widget instanceof AssociationElement) {
					temporaryElement = (IElement) selectedComposite;
				} else {
					temporaryElement = (IElement) selectedComposite.getParent().getParent();
				}
				if ((temporaryElement.isAvailable() && !temporaryElement.isLocked())) {
					// Update properties window
					Notification notifySelectedElement = new Notification(temporaryElement.getType(), temporaryElement.toXMLString());
					notifyAll(notifySelectedElement);	
					
					relationElements.add(temporaryElement);
					if (relationElements.size() == 2) {
						mConnection.sendMessage("createElementRequest", command, Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
					}
					mConnection.sendMessage("selectElementRequest", String.valueOf(temporaryElement.getID()), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
					
				} else if (temporaryElement.isLocked() && !temporaryElement.isAvailable()) {
					selectedElement = temporaryElement;
					relationElements.add(selectedElement);
					if (relationElements.size() == 2) {
						mConnection.sendMessage("createElementRequest", command, Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
					}
				}
				
				if (temporaryElement.isLocked()) {
					mousePressed = true;
				}
			}
			else if (command.equals("none")) {			
				
				Composite selectedComposite = (Composite)e.widget;
				// Mouse down position
				startPoint = new Point(e.x, e.y);
			
				if (e.widget instanceof AssociationElement) {
					temporaryElement = (IElement) selectedComposite;
				} else {
					temporaryElement = (IElement) selectedComposite.getParent().getParent();
				}
				
				
				
				if (temporaryElement.isAvailable() && !temporaryElement.isLocked()) {
					Notification notifySelectedElement = new Notification(temporaryElement.getType(), temporaryElement.toXMLString());
					notifyAll(notifySelectedElement);	
					
					//if (selectedElement != null) mConnection.sendMessage("selectElementRequest", String.valueOf(temporaryElement.getID()), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
					mConnection.sendMessage("selectElementRequest", String.valueOf(temporaryElement.getID()), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
				}
				
				if (temporaryElement.isLocked()) {
					mousePressed = true;
				}
				
				
			}
			
		} else {
			if (command.equals(CLASS) && readyToDraw) {
				// Blochez toate butoanele 
				selectionPoint = new Point(e.x, e.y - 25);
				mConnection.sendMessage("createElementRequest", CLASS  , Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
				readyToDraw = false;
				command = "none";
			} else if (command.equals(ASSOCIATION) || command.equals(DIRECT_ASSOCIATION) || command.equals(AGGREGATION) || command.equals(COMPOSITION) || command.equals(GENERALIZATION) || command.equals(REALIZATION) || command.equals(DEPENDENCY)){
				command = "none";
				relationElements = new ArrayList<IElement>();
			} 
		}
		
		
						
		if (canvasSelected == true) 
			if (selectedElement != null) {
				mConnection.sendMessage("unselectElementRequest", String.valueOf(selectedElement.getID()), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
				selectedElement = null;
			}		
	}

	@Override
	public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
		//if (selectedElement != null) selectedElement.unselectedState();
		if (selectedElement != null && selectedElement.isLocked() && mousePressed) {
			System.out.println("Update must be done");
			mConnection.sendMessage("updateElement", String.valueOf(selectedElement.getID()) + "&%" + userId + "&%" + selectedElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
			mousePressed = false;
			startPoint = null;
		}		
	}
	
	@Override
	public void mouseMove(org.eclipse.swt.events.MouseEvent e) {
		if (mousePressed && selectedElement != null && selectedElement.isLocked()) {
			selectedElement.setX(selectedElement.getX() + (e.x - startPoint.x));
			selectedElement.setY(selectedElement.getY() + (e.y - startPoint.y));
			selectedElement.drawRelations();
		}
		
	}

	public void initializeMUC() {
		sessionAddress = "session" + mConnection.getSessionId() + "@conference.smsfeedback.com";
		muc = new MultiUserChat(mConnection.getXMPPConnection(), "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		java.util.Date lDate = new java.util.Date();
		Random lRandom = new Random(lDate.getTime());
		
		if (!mConnection.getNickname().isEmpty()) {
			userId = mConnection.getNickname();
		} else {
			userId = "User " + Math.abs(lRandom.nextInt());		
		}
		try {
			DiscussionHistory mucHistory = new DiscussionHistory();
		   	mucHistory.setMaxStanzas(0);
			muc.join(userId, "", mucHistory, SmackConfiguration.getPacketReplyTimeout());			
		} catch (XMPPException e) {
			e.printStackTrace();
		}		
		
		mConnection.sendMessage("myIdentity", mConnection.getmUser(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		notifyParticipantsListChangeEvent(muc);
		muc.addParticipantListener(new PacketListener() {

			@Override
			public void processPacket(Packet arg0) {
				notifyParticipantsListChangeEvent(muc);
			}
			
		});
	}
	
	public void notifyParticipantsListChangeEvent(MultiUserChat muc) {
		Iterator<String> occupants = muc.getOccupants();
		String sessionParticipants = "";
		String firstParticipant = "";
		if (occupants.hasNext()) {
			firstParticipant = occupants.next();
		}
		if (!(firstParticipant == null || firstParticipant.isEmpty())) {
			sessionParticipants = firstParticipant;
		}				
		while (occupants.hasNext()) {
			sessionParticipants += "&%" + occupants.next();										
		}
		whiteboard.notifyAll(new Notification(ElementType.ALERT, sessionParticipants));
	}
	
	public void restoreWhiteboardFromFile() {
		initialize();
		String fileContent = WhiteboardHelpers.restoreSessionFromFile(mConnection.getFilename());
		try {
			InputSource lIs = new InputSource();
		    lIs.setCharacterStream(new StringReader(fileContent));

		    Document lXmlDoc = mDocBuilder.parse(lIs);
		        
		    Node lRoot = lXmlDoc.getFirstChild();
		    final NodeList lChildNodes = lRoot.getChildNodes();		        
		    display.asyncExec(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int k = 0;
				    for (int i = 0; i < lChildNodes.getLength(); i++) {
				    	Node lNode = (Node) lChildNodes.item(i);
				        if (!lNode.getNodeName().equals("#text")) {
				        	if (lNode.getNodeName().equals(CLASS)) {
				        		ClassElement newClassElement = new ClassElement(whiteboard, SWT.NONE, lNode);
				        		newClassElement.addMouseListener(whiteboard);	
								newClassElement.addMouseMoveListener(whiteboard);
								newClassElement.addKeyListener(whiteboard);
				        		whiteboardContent.put(newClassElement.getID(), newClassElement);		        		
				        	} else if (lNode.getNodeName().equals(ASSOCIATION)) {
				        		AssociationElement newAssociationElement = new AssociationElement(whiteboard, SWT.NONE, lNode, whiteboardContent);
				        		newAssociationElement.addMouseListener(whiteboard);
				        		newAssociationElement.addMouseMoveListener(whiteboard);
				        		newAssociationElement.addKeyListener(whiteboard);
				        		whiteboardContent.put(newAssociationElement.getID(), newAssociationElement);
				        	}
				        }              
				    }
				}
		    	
		    });
		    
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
    		e.printStackTrace();
    	}
    }
	
	// OBSERVER SECTION	
	@Override
	public void update(Observable arg0, Object notification) {
		Notification receivedNotification = (Notification) notification;
		if (receivedNotification.getType() == ElementType.ASSOCIATION) {
			command = ASSOCIATION;
		} else if (receivedNotification.getType() == ElementType.CLASS) {
			command = CLASS;
		} else if (receivedNotification.getType() == ElementType.INTERFACE) {
			exportAsImage();
		} else if (receivedNotification.getType() == ElementType.DIRECT_ASSOCIATION) {
			command = DIRECT_ASSOCIATION;
		} else if (receivedNotification.getType() == ElementType.AGGREGATION) {
			command = AGGREGATION;
		} else if (receivedNotification.getType() == ElementType.COMPOSITION) {
			command = COMPOSITION;
		} else if (receivedNotification.getType() == ElementType.GENERALIZATION) {
			command = GENERALIZATION;
		} else if (receivedNotification.getType() == ElementType.REALIZATION) {
			command = REALIZATION;
		} else if (receivedNotification.getType() == ElementType.DEPENDENCY) {
			command = DEPENDENCY;
		} else if (receivedNotification.getType() == ElementType.UPDATE_ATTRIBUTES) {
			String updatedAttributes = receivedNotification.getContent();
			selectedElement.setAttributes(updatedAttributes);
			selectedElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(selectedElement.getID()) + "&%" + userId + "&%" + selectedElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
						
		} else if (receivedNotification.getType() == ElementType.UPDATE_OPERATIONS) {
			String updatedOperations = receivedNotification.getContent();
			selectedElement.setOperations(updatedOperations);
			selectedElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(selectedElement.getID()) + "&%" + userId + "&%" + selectedElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.END1_MULTIPLICITY) {
			String end1Multiplicity = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setStartMultiplicity(end1Multiplicity);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.END2_MULTIPLICITY) {
			String end2Multiplicity = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setEndMultiplicity(end2Multiplicity);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.END1_VISIBILITY) {
			String end1Visibility = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setStartVisibility(end1Visibility);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.END2_VISIBILITY) {
			String end2Visibility = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setEndVisibility(end2Visibility);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.END1_AGGREGATION) {
			String end1Aggregation = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setStartAggregation(end1Aggregation);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.END2_AGGREGATION) {
			String end2Aggregation = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setEndAggregation(end2Aggregation);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.END1_NAME) {
			String end1Name = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setStartName(end1Name);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.END2_NAME) {
			String end2Name = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setEndName(end2Name);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.ASSOCIATION_NAME) {
			String associationName = receivedNotification.getContent();
			AssociationElement tempElement = (AssociationElement) selectedElement;
			tempElement.setTitle(associationName);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.CLASS_NAME) {
			String className = receivedNotification.getContent();
			ClassElement tempElement = (ClassElement) selectedElement;
			tempElement.setTitle(className);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.CLASS_VISIBILITY) {
			String classVisibility = receivedNotification.getContent();
			ClassElement tempElement = (ClassElement) selectedElement;
			tempElement.setVisibility(classVisibility);
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		} else if (receivedNotification.getType() == ElementType.CLASS_IS_ABSTRACT) {
			String classIsAbstract = receivedNotification.getContent();
			ClassElement tempElement = (ClassElement) selectedElement;
			if (classIsAbstract.equals("yes")) {
				tempElement.setIsAbstract(true);	
			} else {
				tempElement.setIsAbstract(false);
			}
			
			tempElement.drawUpdatedElement();
			mConnection.sendMessage("updateElement", String.valueOf(tempElement.getID()) + "&%" + userId + "&%" + tempElement.toXMLString(), Type.groupchat, "session" + mConnection.getSessionId() + "@conference.smsfeedback.com");
		}
		
	}
	
	public void exportAsImage() {
	    GC gc = new GC(this);
	    Point size = this.getSize();
        final Image image = new Image(display, size.x, size.y);
        gc.copyArea(image, 0, 0);
        gc.dispose();

        ImageLoader loader = new ImageLoader();
	    loader.data = new ImageData[] {image.getImageData()};
	    loader.save("my_screen.jpg", SWT.IMAGE_JPEG);
	}
	
	public void addObserver(Observer iObs) {
		mObservers.add(iObs);
	}
		
	public void notifyAll(Object iNotification) {
		for (int i=0; i<mObservers.size(); ++i) {
			mObservers.get(i).update(null, iNotification);
		}
	}	
	
	// GETTERS AND SETTERS	
	public String getSessionAddress() {
		return sessionAddress;
	}
	
	public MultiUserChat getMultiUserChat() {
		return muc;
	}
	
	public XMPPConnect getXMPPConnection() {
		return mConnection;
	}
	
	// NOT IMPLEMENTED
	@Override
	public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
			
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
