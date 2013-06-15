package collaborationmail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import staruml.IElement;

public class Conversation extends ViewPart implements PacketListener {
	public static final String ID = "collaborationMail.conversation";
	private MultiUserChat muc;
	private Text conversation, input;
	private Display display;
	private String sessionAddress;
	
	public Conversation() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		display = parent.getDisplay();
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		parent.setLayout(gridLayout);
		
		conversation = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData conversationGridData = new GridData(GridData.FILL_BOTH);
		conversationGridData.verticalSpan = 3;
		conversationGridData.horizontalSpan = 5;
		conversation.setLayoutData(conversationGridData);
	    conversation.setEditable(false);
	    input = new Text(parent, SWT.BORDER | SWT.WRAP);
	    GridData inputGridData = new GridData(SWT.FILL);
		inputGridData.verticalSpan = 1;
		inputGridData.horizontalSpan = 3;
	
	    input.setLayoutData(inputGridData);
	    Button chatBtn = new Button(parent, SWT.PUSH);
	    chatBtn.setText("Send message");
	    chatBtn.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		Message sendMessage = new Message();
	    		sendMessage.setBody(input.getText());
	    		sendMessage.setSubject("sessionChat");
	    		sendMessage.setType(Type.groupchat);
	    		sendMessage.setTo(sessionAddress);
	    		try {
					muc.sendMessage(sendMessage);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
	    });
	    
	    
	    
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processPacket(Packet packet) {
		Packet receivedPacket = packet;
		final Message receivedMessage = (Message) receivedPacket;
		if (receivedMessage.getSubject().equals("sessionChat")) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					conversation.setText(conversation.getText() + receivedMessage.getFrom() + ": " + receivedMessage.getBody() +  "\n");						
				}	
			});			
		}
	}
	
	public void initializeMUC(MultiUserChat iMuc, String iSessionAddress) {
		muc = iMuc;
		muc.addMessageListener(this);
		sessionAddress = iSessionAddress;
	}

}
