package xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

public class XMPPConnect {

	private XMPPConnection mConnection;
	// according to
	// http://developer.android.com/guide/developing/devices/emulator.html
	// 10.0.2.2 is equivalent to 127.0.0.1
	private static final int XMPP_PORT = 5222;
	private int mSessionId;

	private String mComponentName;
	private static XMPPConnect mCon = null;
	private String mUser;
	private static boolean mConnected = false;
	private String filename;
	private String sessionType; // JOINED || CREATED
	private String nickname;
	

	/*
	 *  For anonymous login
	 */ 
	private XMPPConnect(String iXMPP_host, String iXMPP_resource,
			String iXMPP_component, PacketListener iPacketListener)
			throws XMPPException {
		// implicit 
		mSessionId = -1;
		mUser = "nobody";
		nickname = "";
		
		mComponentName = iXMPP_component;
		ConnectionConfiguration config = new ConnectionConfiguration(
				iXMPP_host, XMPP_PORT, iXMPP_resource);

		mConnection = new XMPPConnection(config);
		mConnection.connect();
		mConnection.addPacketListener(iPacketListener, null);
				
		mConnection.loginAnonymously();
		mConnected = true;
	}
	
	/*
	 * Login with credentials
	 */
	
	private XMPPConnect(String iXMPP_host, String username, String password, String iXMPP_resource,
			String iXMPP_component, PacketListener iPacketListener)
			throws XMPPException {
		
		mSessionId = -1;
		mUser = "nobody";
		
		mComponentName = iXMPP_component;
		ConnectionConfiguration config = new ConnectionConfiguration(
				iXMPP_host, XMPP_PORT, iXMPP_resource);
		
		mConnection = new XMPPConnection(config);
		mConnection.connect();
		mConnection.addPacketListener(iPacketListener, null);

		// Login to XMPP Server Anonymously - XMPP Server must permit this type
		// of login */
		mConnection.login(username, password);
		mConnected = true;
	}

	public static synchronized XMPPConnect getInstance(String iXMPP_host, 
			String iXMPP_resource, String iXMPP_component,
			PacketListener iPacketListener) {
		if (mCon == null) {
			try {
				mCon = new XMPPConnect(iXMPP_host,iXMPP_resource, iXMPP_component,iPacketListener);
			} catch (XMPPException e) {
				// failed creating a new instance
			}
		}
		return mCon;

	}
	
	public static synchronized XMPPConnect getInstance(String iXMPP_host, String username,
			String password, String iXMPP_resource, String iXMPP_component,
			PacketListener iPacketListener) {
		if (mCon == null) {
			try {
				mCon = new XMPPConnect(iXMPP_host, username, password, iXMPP_resource, iXMPP_component,iPacketListener);
			} catch (XMPPException e) {
				// failed creating a new instance
			}
		}
		return mCon;

	}
	
	public static synchronized XMPPConnect getInstance() {
		return mCon;
	}

	public void disconnect()
	{
		mCon.disconnect();
	}
	
	public void sendMessage(String iSubject, String iBody, Message.Type iType, String iReceiver) {
		// Send a message stanza. - more info here:
		// http://xmpp.org/rfcs/rfc3920.html (XML stanzas section)
		mConnection.sendPacket(this.createMessage(iSubject, iBody, iType,
				iReceiver));
	}

	public void sendPresence(Presence.Type iType, String iClientStatus, String iReceiver) {
		mConnection.sendPacket(this.createPresence(iType, iClientStatus,
				iReceiver));
	}

	// Helper methods for creating message and presence packets.
	private Message createMessage(String iSubject, String iBody,
			Message.Type iType, String iReceiver) {
		/*
		 * Current issue: 1. Create the message in the manner showed below. 2.
		 * Send the message using sendPacket. 3. The component receives the
		 * message and replies. 4. The client receives the message but the
		 * processMessage method it's not triggered.
		 */
		Message lMessage = new Message();
		lMessage.addSubject(null, iSubject);
		lMessage.addBody(null, iBody);
		lMessage.setType(iType);
		lMessage.setTo(iReceiver);
		return lMessage;
	}

	private Presence createPresence(Presence.Type iType, String iStatus,
			String iReceiver) {
		Presence lPresence = new Presence(iType);
		lPresence.setStatus(iStatus);
		lPresence.setTo(iReceiver);
		return lPresence;
	}	
	
	public XMPPConnection getXMPPConnection() {
		return mConnection;
	}
	
	public void addPacketListener(PacketListener iPacketListener) {
		mConnection.addPacketListener(iPacketListener, null);
	}
	
	public void setSessionId(int iSessionId) {
		mSessionId = iSessionId;
	}
	
	public int getSessionId() {
		return mSessionId;
	}

	public String getmUser() {
		return mUser;
	}

	public void setmUser(String mUser) {
		this.mUser = mUser;
	}
	
	public static boolean isConnected() {
		return mConnected;
	}	

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
