package org.umldiagram.xmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

public class XMPPConnectionTest implements PacketListener {
	public static void main(String[] args) {
		XMPPConnectionTest test = new XMPPConnectionTest();
	}
	
	public XMPPConnectionTest() {
		XMPPConnect lConnection = XMPPConnect.getInstance("176.34.122.48", "client", "logic.smsfeedback.com", this);		
	}

	@Override
	public void processPacket(Packet arg0) {
		System.out.println("Packet received");
		
	}
}
