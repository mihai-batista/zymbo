package collaborationmail;

import java.util.ArrayList;
import java.util.Observer;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import staruml.Whiteboard;

import xmpp.RenderUI;
import xmpp.XMPPConnect;

public class View extends ViewPart {

	public static final String ID = "collaborationMail.view";
	
	private Composite viewPanel = null;
	private Whiteboard whiteboard;	
	
	/**
	 * The text control that's displaying the content of the email message.
	 */
	private Text messageText;
	
	public void createPartControl(Composite parent) {
		whiteboard = new Whiteboard(parent, SWT.NONE);	    	
	}

	public void setFocus() {
		//messageText.setFocus();
	}

		
	public void addObserver(Observer iObs) {
		whiteboard.addObserver(iObs);
	}
	
	public Observer getWhiteboard() {
		return whiteboard;
	}
	
	public MultiUserChat getWhiteboardMUC() {
		return whiteboard.getMultiUserChat();
	}
	
	public String getSessionAddress() {
		return whiteboard.getSessionAddress();
	}
}
