package wizard;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.baseadaptor.bundlefile.MRUBundleFileList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import xmpp.XMPPConnect;

public class AccountPage extends WizardPage implements PacketListener {
	private Text mUsernameText, mNicknameText, mPasswordText, mSessionText;
    private Combo mSessionCombo;
    
    private Button mLoginButton, mCreateButton, mJoinButton, mStartSessionButton;
    private Display mDisplay;
    private XMPPConnect mConnection;
    private PacketListener mPacketListener = this;
    private AccountPage page;
    
    // flow control variables 
    private boolean fileReceived;
    private boolean sessionJoinedAck;
    
    // Options
    String lRadioOption = "create";
    String lJoinValue = "empty";   
    
    private static final String COMPONENT_ADDRESS = "umldesigner.smsfeedback.com";
        
    protected AccountPage(String pageName) {
             super(pageName);
             setTitle("My account");
             setDescription("Login with an existing account or create one");
             setPageComplete(false);
             //page = this;
             fileReceived = false;
             sessionJoinedAck = false;           
    }
    public void createControl(Composite parent) {
             final Composite composite = new Composite(parent, SWT.NONE);
             mDisplay = composite.getDisplay();
             
             // Font cu bold
             Label usernameLabel = new Label(composite,SWT.NONE);
             usernameLabel.setText("Username:");
             Font usernameFont = usernameLabel.getFont();
             FontData[] fontData = usernameFont.getFontData();
             for (int i = 0; i < fontData.length; i++) {
               fontData[i].setStyle(SWT.BOLD);
             }             
             final Font newFont = new Font(composite.getDisplay(), fontData);
             
                          
             GridLayout layout = new GridLayout();
             layout.marginHeight = 10;
             layout.marginWidth = 10;
             layout.numColumns = 3;
             composite.setLayout(null);
             setControl(composite);
             
             // Username             
             usernameLabel.setBounds(10, 15, 70, 25);             
             usernameLabel.setFont(newFont);
             mUsernameText = new Text(composite, SWT.BORDER);
             mUsernameText.setBounds(80, 10, 150, 25);
             mUsernameText.setText("smsapp@smsfeedback.com");
             
             // Example
             Label exampleLabel = new Label(composite,SWT.NONE);
             exampleLabel.setText("Ex: user@smsfeedback.com");
             exampleLabel.setBounds(240, 17, 170, 25);
             Font exampleFont = exampleLabel.getFont();
             FontData[] fontDataEx = exampleFont.getFontData();
             for (int i = 0; i < fontDataEx.length; i++) {
               fontDataEx[i].setStyle(SWT.ITALIC);
             }
             Font newFontEx = new Font(composite.getDisplay(), fontDataEx);
             exampleLabel.setFont(newFontEx);
             
             // Password
             Label passwordLabel = new Label(composite,SWT.NONE);
             passwordLabel.setText("Password:");
             passwordLabel.setBounds(10, 45, 70, 25);
             passwordLabel.setFont(newFont);
             mPasswordText = new Text(composite, SWT.PASSWORD | SWT.BORDER);
             mPasswordText.setBounds(80, 40, 150, 25);
             mPasswordText.setText("123456");
             
             // Nickname
             Label nicknameLabel = new Label(composite,SWT.NONE);
             nicknameLabel.setText("Nickname:");
             nicknameLabel.setBounds(10, 45, 70, 25);
             nicknameLabel.setFont(newFont);
             mNicknameText = new Text(composite, SWT.BORDER);
             mNicknameText.setBounds(80, 70, 150, 25);
             mNicknameText.setText("123456");
             
             
             // Login button
             mLoginButton = new Button(composite, SWT.PUSH | SWT.FILL);
             mLoginButton.setText("Login");
             mLoginButton.setBounds(240, 70, 90, 30);
             
             final Label lNotificationLabel = new Label(composite,SWT.NONE);
                      
             
             mLoginButton.addSelectionListener(new SelectionAdapter() {
            	 public void widgetSelected(SelectionEvent event) {
                                
                     mConnection = XMPPConnect.getInstance("176.34.122.48", mUsernameText.getText(), mPasswordText.getText(), "client", "umldesigner.smsfeedback.com", mPacketListener);
                     mConnection.setNickname(mNicknameText.getText());                
                     if (mConnection.getXMPPConnection().isConnected()) {
                         // Setup file transfer environement                     	 
                    	 try {                    		
                    		 ServiceDiscoveryManager sm =new ServiceDiscoveryManager(mConnection.getXMPPConnection());
                    		 final FileTransferManager manager = new FileTransferManager(mConnection.getXMPPConnection());
                    		 manager.addFileTransferListener(new FileTransferListener() {
	                        	 @Override
	                        	 public void fileTransferRequest(FileTransferRequest request) {
	                                try {
	                        			IncomingFileTransfer transfer = request.accept();                                   
	                        			transfer.recieveFile(new File(request.getFileName()));
	                        			
	                        			while(!transfer.isDone()) {
	                        	            if(transfer.getStatus().equals(Status.error)) {
	                        	                  System.out.println("ERROR!!! " + transfer.getError());
	                        	            } else {
	                        	                  System.out.println(transfer.getStatus());
	                        	                  System.out.println(transfer.getProgress());
	                        	            }
	                        	        }
	                        			fileReceived = true;    
	                        			mConnection.setFilename(request.getFileName());
	                        			if (fileReceived && sessionJoinedAck) {
	                        				mDisplay.asyncExec(new Runnable() {
	                        					@Override
	                        					public void run() {
	                        						setPageComplete(true);							
	                        					}
	                						
	                        				});
	                        			}
	    							} catch (XMPPException e) {
	    								e.printStackTrace();
	    							}
	                                   
	                             }						
                    		 });   
                    	 }catch (Exception e) {
                    		 System.out.println("Error = " + e.getMessage());
                    		 e.printStackTrace();
                    	 }
                    	 
                    	 // Setup GUI
                    	 
                    	 Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
                         separator.setBounds(10, 110, 480, 10);
                         
                         Label sessionLabel = new Label(composite, SWT.NULL);
                         sessionLabel.setText("Session settings: ");
                         sessionLabel.setBounds(10, 120, 150, 20);
                         sessionLabel.setFont(newFont);
                         
                         String[] ITEMS = { "Loading..."};
                         mSessionCombo = new Combo(composite, SWT.DROP_DOWN);
                         mSessionCombo.setItems(ITEMS);
                         mSessionCombo.setBounds(240, 164, 60, 15);
                         mSessionCombo.setVisible(false);
                         
                         mSessionText = new Text(composite, SWT.BORDER);
                         mSessionText.setBounds(310, 164, 60, 22);
                         mSessionText.setText("");
                         mSessionText.setVisible(false);
                         
                         mSessionCombo.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								try {
									String lSessionId = mSessionCombo.getItem(mSessionCombo.getSelectionIndex()).toString();
									mSessionText.setText(lSessionId);
								} catch (IllegalArgumentException exception) {
									mSessionText.setText("");
								}
								
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								// TODO Auto-generated method stub
								
							}
                        	 
                         });
                         
                         mCreateButton = new Button(composite, SWT.RADIO);
                         mCreateButton.setText("Create new UML diagram session");
                         mCreateButton.setBounds(30, 140, 200, 20);
                         mCreateButton.setSelection(true);
                         mConnection.setSessionType("CREATED");
                         
                         mCreateButton.addSelectionListener(new SelectionListener() {
                        	@Override
							public void widgetSelected(SelectionEvent e) {
								mSessionCombo.setVisible(false);
								mSessionText.setVisible(false);
								lRadioOption = "create";
								mConnection.setSessionType("CREATED");
							}
							
							@Override
							public void widgetDefaultSelected(SelectionEvent e) {}
						});         
                         
                         mJoinButton = new Button(composite, SWT.RADIO);
                         mJoinButton.setText("Join existing UML diagram session");
                         mJoinButton.setBounds(30, 165, 200, 20);
                         mJoinButton.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								mSessionCombo.setVisible(true);	
								mSessionText.setVisible(true);
								lRadioOption = "join";
								mConnection.setSessionType("JOINED");
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {}
                        	 
                         });
                         
                                                  
                         mStartSessionButton = new Button(composite, SWT.PUSH | SWT.FILL);
                         mStartSessionButton.setText("Start session");
                         mStartSessionButton.setBounds(150, 185, 80, 30);
                         mStartSessionButton.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								if (lRadioOption.equals("create")) {
									System.out.println("Create new session");
									mConnection.sendMessage("Create new session", "Do it.", Message.Type.createSessionRequest, COMPONENT_ADDRESS);
								} else if (lRadioOption.equals("join")) {
									if (!mSessionText.getText().isEmpty()) {
										mConnection.sendMessage("Join session", mSessionText.getText(), Message.Type.joinSessionRequest, COMPONENT_ADDRESS);
									}
								}								
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								// TODO Auto-generated method stub
								
							}
                        	 
                         });                        
                           
                         mConnection.sendMessage("Get user's sessions", "Give me the list", Message.Type.requestSessionsList, COMPONENT_ADDRESS);
                    	 lNotificationLabel.setText("Sucesfully authenticated");
     						
                         
                     }else{
                    	 lNotificationLabel.setText("Authetication failed. Check the credentials and try again!");
                     }
                     
                    	
            	 }
             });
          
    }
	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message) {
			Message receivedMessage = (Message)packet;
			if (receivedMessage.getType() == Message.Type.responseSessionsList) {
				String lSessionsList = receivedMessage.getBody();
				if (!lSessionsList.equals("empty")) {
					final String[] lSessionsArray = lSessionsList.split("&%");
					System.out.println(lSessionsArray[0]);		
					mDisplay.asyncExec(new Runnable() {
						@Override
						public void run() {
							mSessionCombo.setItems(lSessionsArray);							
						}
						
					});
				}
			} else if (receivedMessage.getType() == Message.Type.createSessionResponse) {
				String lSubject = receivedMessage.getSubject();
				mConnection.setmUser(receivedMessage.getTo());
				
				if (Integer.valueOf(receivedMessage.getBody()) > 0) {
					mConnection.setSessionId(Integer.valueOf(receivedMessage.getBody()));
					System.out.println("Create session id = " + receivedMessage.getBody());
					mDisplay.asyncExec(new Runnable() {
    					@Override
    					public void run() {
    						setPageComplete(true);							
    					}						
    				});
				}
			} else if (receivedMessage.getType() == Message.Type.joinSessionResponse) {
				String lSubject = receivedMessage.getSubject();
				mConnection.setmUser(receivedMessage.getTo());
				
				if (Integer.valueOf(receivedMessage.getBody()) > 0) {
					mConnection.setSessionId(Integer.valueOf(receivedMessage.getBody()));
					System.out.println("Join session id = " + receivedMessage.getBody());

					sessionJoinedAck = true;
        			// finish the process
        			if (fileReceived && sessionJoinedAck) {
        				mDisplay.asyncExec(new Runnable() {
        					@Override
        					public void run() {
        						setPageComplete(true);							
        					}						
        				});
        			}
					
				}
			}
		}
	}	
}



