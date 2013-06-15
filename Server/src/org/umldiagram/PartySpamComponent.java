package org.umldiagram;

import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.whack.ExternalComponentManager;
import org.umldiagram.db.SessionHelper;
import org.umldiagram.xmpp.XMPPConnect;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;
import org.xmpp.packet.Message.Type;


public class PartySpamComponent implements org.xmpp.component.Component, PacketListener {
	private XMPPConnect mConnection;
	private ExternalComponentManager mMgr = null;
	private int mSessionIndex;
	private HashMap<Integer, Session> sessions = new HashMap<Integer, Session>();
	private HashMap<String, Integer> userInSession = new HashMap<String, Integer>();
	
		
	public String getName() {
		return ("SmsFeedback");
	}

	public String getDescription() {
		return ("Sms logic");
	}

	public void processPacket(Packet iReceivedPacket) {
		System.out.println(iReceivedPacket.toXML());
		if (iReceivedPacket instanceof Message) {
			Message lReceivedMessage = (Message) iReceivedPacket;
			
			/* just for debugging purposes, will be deleted later */
			//System.out.println(lReceivedMessage.toXML());
			if (lReceivedMessage.getType() == Message.Type.createSessionRequest) {
				// Check if the user is in DB. If not, add it.				
				String lUsername = lReceivedMessage.getFrom().toBareJID();				
				
				if (!SessionHelper.checkUser(lUsername)) {
					SessionHelper.createUser(lUsername);
				}
				
				// Increment session counter and initialize the session
				++mSessionIndex;
				SessionHelper.updateLastSessionId(mSessionIndex);
				
				Session newSession = new Session(mSessionIndex, mConnection);
				int lCreatedSessionId = newSession.initializeSession();
				newSession.restoreSession();
				// If the session is successfully created then register it.
				if (lCreatedSessionId != -1) {
					sessions.put(mSessionIndex, newSession);
					SessionHelper.addUserInSession(SessionHelper.getIdOfUser(lUsername), lCreatedSessionId);
					userInSession.put(lReceivedMessage.getFrom().toString(), mSessionIndex);
				} 
				sendMessage(String.valueOf(lCreatedSessionId), "response", Type.createSessionResponse, lReceivedMessage.getFrom(), lReceivedMessage.getTo());
						
			} else if (lReceivedMessage.getType() == Message.Type.joinSessionRequest) {
				String lSessionId = lReceivedMessage.getBody();				
				String lUsername = lReceivedMessage.getFrom().toBareJID();
				
				if (!SessionHelper.checkUser(lUsername)) {
					SessionHelper.createUser(lUsername);
				}
				
				if (sessions.get(Integer.valueOf(lSessionId)) == null) {
					Session joinSession = new Session(Integer.valueOf(lSessionId), mConnection);
					int lJoinedSessionId = joinSession.joinSession();
					joinSession.restoreSession();					
					transferFile(joinSession.getSessionFilename(), "WhiteboardContent", lReceivedMessage.getFrom());
					
					if (lJoinedSessionId != -1) {
						if (!SessionHelper.checkUserInSession(SessionHelper.getIdOfUser(lUsername), lJoinedSessionId)) {
							SessionHelper.addUserInSession(SessionHelper.getIdOfUser(lUsername), lJoinedSessionId);
						}
						sessions.put(lJoinedSessionId, joinSession);
						userInSession.put(lReceivedMessage.getFrom().toString(), lJoinedSessionId);
					} 	
					sendMessage(String.valueOf(lJoinedSessionId), "response", Type.joinSessionResponse, lReceivedMessage.getFrom(), lReceivedMessage.getTo());
				} else {
					if (!SessionHelper.checkUserInSession(SessionHelper.getIdOfUser(lUsername), Integer.parseInt(lSessionId))) {
						SessionHelper.addUserInSession(SessionHelper.getIdOfUser(lUsername), Integer.parseInt(lSessionId));
					} 	
					Session lTempSession = sessions.get(Integer.valueOf(lSessionId));
					transferFile(lTempSession.getTheLastVersion(), "WhiteboardContent", lReceivedMessage.getFrom());
					sendMessage(lSessionId, "response", Type.joinSessionResponse, lReceivedMessage.getFrom(), lReceivedMessage.getTo());
				}						
			} else if (lReceivedMessage.getType() == Message.Type.requestSessionsList) {
				String lUser = lReceivedMessage.getBody();
				
				ArrayList<Integer> lSessions = SessionHelper.getUserSessions(SessionHelper.getIdOfUser(lReceivedMessage.getFrom().toBareJID()));
				String lSessionsString = "";
				if (lSessions.size() > 0) {
					lSessionsString += lSessions.get(0);
					for (int i=1; i<lSessions.size(); ++i) {
						lSessionsString = lSessionsString + "&%" + lSessions.get(i);
					}
				}				
				sendMessage(lSessionsString, "Sessions list", Message.Type.responseSessionsList, lReceivedMessage.getFrom(), lReceivedMessage.getTo());
			} else if (lReceivedMessage.getType() == Message.Type.requestCreateElement) {
				Session lRequestedSession = sessions.get(Integer.getInteger(lReceivedMessage.getBody()));
				sendMessage(String.valueOf(lRequestedSession.createElement()), "response", Message.Type.responseCreateElement, lReceivedMessage.getFrom(), lReceivedMessage.getTo());
			}
			
			else if (lReceivedMessage.getType() == Message.Type.chat) {
				if (lReceivedMessage.getSubject().equals("requestCreateElement")) {
					Session lRequestedSession = sessions.get(Integer.getInteger(lReceivedMessage.getBody()));
					System.out.println("Send message to = " + lReceivedMessage.getFrom().toBareJID());
					sendMessage(String.valueOf(lRequestedSession.createElement()), "response", Message.Type.responseCreateElement, lReceivedMessage.getFrom(), lReceivedMessage.getTo());
				}
				//sendMessage("response", "success", Type.chat, lReceivedMessage.getFrom(), lReceivedMessage.getTo());
				
				
			}
			
		} else if (iReceivedPacket instanceof Presence) {
			Presence lPresence = (Presence) iReceivedPacket;
			if (lPresence.getType() == Presence.Type.unavailable) {
				if (userInSession.get(lPresence.getFrom().toString()) != null) {
					sessions.get(userInSession.get(lPresence.getFrom().toString())).userLeftTheSession(lPresence.getFrom().toString());
				}
			}
		}

	}
	
	private void sendMessage(String iBody, String iSubject, Message.Type iType, JID iTo, JID iFrom) {
		Message lResponseMessage = new Message();
		lResponseMessage.setType(iType);
		lResponseMessage.setBody(iBody);
		lResponseMessage.setSubject(iSubject);
		lResponseMessage.setTo(iTo);
		lResponseMessage.setFrom(iFrom);
		mMgr.sendPacket(this, lResponseMessage);
		//JID newJID = new JID("asda@dsad.com");
	}
	
	public void initialize(JID iJid, ComponentManager iComponentManager)
			throws ComponentException {
		System.out.println("Initializing component.");
		mConnection = XMPPConnect.getInstance("176.34.122.48", "twilio@smsfeedback.com", "123456", "client", "umldesigner.smsfeedback.com", this);
		mMgr = (ExternalComponentManager) iComponentManager;
		mSessionIndex = SessionHelper.getLastSessionId();
	}

	public void start() {
		System.out.println("Component started.");
	}

	public void shutdown() {
		System.out.println("Component is shutted down.");
		SessionHelper.updateLastSessionId(mSessionIndex);
		for (Session session : sessions.values()) {
		   session.updateSessionContentFile();
		}
	}
	
	public void transferFile(String fileName, String fileDescription, JID destination) {
		try {
			// Create the file transfer manager
			FileTransferManager manager = new FileTransferManager(mConnection.getXMPPConnection());
			// Create the outgoing file transfer
			OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(destination.toString());
			// Send the file
			File f = new File(fileName);
			System.out.println(f.getName() + " has size " + f.getTotalSpace() + " to " + destination.toString());
	      	transfer.sendFile(f, fileDescription);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// S-ar putea sa trebuiasca sa schimb listenerul deocamdata e ok.
	@Override
	public void processPacket(org.jivesoftware.smack.packet.Packet iReceivedPacket) {
				
		
	}
}
