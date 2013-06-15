package collaborationmail;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;



import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import staruml.AssociationElementConvertor;
import staruml.ClassElementConvertor;
import staruml.ElementType;
import staruml.Notification;


public class Properties extends ViewPart implements Observer {
	public static final String ID = "collaborationMail.properties";
	private Label label;
	private TableItem item1, item2, item3, item4, item5, item6, 
					item7, item8, item9, item10, item11, item12, item13;
	
	private Composite container;
	private TabFolder tabFolder;
	private ArrayList<Observer> mObservers = new ArrayList<Observer>();
	private Table propertiesTable;
	private TabItem generalTab, contentTab;
	private Text attributesText, operationsText;
	private Properties properties;
	private Display display;
	private Label info;
	
	public Properties() {
		properties = this;
	}

	@Override
	public void createPartControl(Composite parent) {
		 container = parent;
		 display = parent.getDisplay();
		 //info = new Label(parent, SWT.NONE);
		 //info.setText("No element selected.");
		 //info.setVisible(false);
		 
		 tabFolder = new TabFolder(container, SWT.BORDER);
		 propertiesTable = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);

		 TableColumn tc1 = new TableColumn(propertiesTable, SWT.CENTER);
		 TableColumn tc2 = new TableColumn(propertiesTable, SWT.CENTER);
		 tc1.setText("Propertie");
		 tc2.setText("Value");
		 tc1.setWidth(60);
		 tc2.setWidth(140);
		 propertiesTable.setHeaderVisible(true);
		 
		 final TableEditor editor = new TableEditor(propertiesTable);
         // The editor must have the same size as the cell and must
         // not be any smaller than 50 pixels.
         editor.horizontalAlignment = SWT.LEFT;
         editor.grabHorizontal = true;
         editor.minimumWidth = 50;
         // editing the second column
         final int EDITABLECOLUMN = 1;

         propertiesTable.addSelectionListener(new SelectionAdapter() {
           public void widgetSelected(SelectionEvent e) {
             // Clean up any previous editor control
             Control oldEditor = editor.getEditor();
             if (oldEditor != null)
               oldEditor.dispose();

             // Identify the selected row
             TableItem item = (TableItem) e.item;
             if (item == null)
               return;

             if (item.getText().equals("Element name")) {
            	 Text newEditor = new Text(propertiesTable, SWT.NONE);
	             newEditor.setText(item.getText(EDITABLECOLUMN));
	             newEditor.addModifyListener(new ModifyListener() {
	               public void modifyText(ModifyEvent me) {
	                 Text text = (Text) editor.getEditor();
	                 editor.getItem().setText(EDITABLECOLUMN, text.getText());		                 
	               }
	             }); 
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End1.Name")) {
            	 Text newEditor = new Text(propertiesTable, SWT.NONE);
	             newEditor.setText(item.getText(EDITABLECOLUMN));
	             newEditor.addModifyListener(new ModifyListener() {
	               public void modifyText(ModifyEvent me) {
	                 Text text = (Text) editor.getEditor();
	                 editor.getItem().setText(EDITABLECOLUMN, text.getText());
	                 Notification notification = new Notification(ElementType.END1_NAME, text.getText());
                     properties.notifyObservers(notification);
	               }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End1.Visibility")) {
            	 Combo newEditor = new Combo(propertiesTable, SWT.NONE);
	             newEditor.add("PUBLIC");
	             newEditor.add("PRIVATE");
	             newEditor.add("PROTECTED");
	             newEditor.add("PACKAGE");
	             newEditor.addModifyListener(new ModifyListener() {
	             	public void modifyText(ModifyEvent me) {
	                     Combo text = (Combo) editor.getEditor();
	                     editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
	                                        
	                     Notification notification = new Notification(ElementType.END1_VISIBILITY, convertVisibility(text.getItem(text.getSelectionIndex())));
	                     properties.notifyObservers(notification);
	                   }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End1.Is Navigable")) {
            	 Combo newEditor = new Combo(propertiesTable, SWT.NONE);
	             newEditor.add("yes");
	             newEditor.add("no");
	             newEditor.addModifyListener(new ModifyListener() {
	             	public void modifyText(ModifyEvent me) {
	                     Combo text = (Combo) editor.getEditor();
	                     editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
	                     Notification notification = new Notification(ElementType.END1_IS_NAVIGABLE, text.getItem(text.getSelectionIndex()));
	                     properties.notifyObservers(notification);
	                   }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End1.Aggregation")) {
            	 Combo newEditor = new Combo(propertiesTable, SWT.NONE);
	             newEditor.add("NONE");
	             newEditor.add("AGGREGATE");
	             newEditor.add("COMPOSITE");
	             newEditor.addModifyListener(new ModifyListener() {
	             	public void modifyText(ModifyEvent me) {
	                     Combo text = (Combo) editor.getEditor();
	                     editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
	                     Notification notification = new Notification(ElementType.END1_AGGREGATION, text.getItem(text.getSelectionIndex()));
	                     properties.notifyObservers(notification);
	                   }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End1.Multiplicity")) {
            	 Combo newEditor = new Combo(propertiesTable, SWT.NONE);
	             newEditor.add("");
	             newEditor.add("0..1");
	             newEditor.add("1");
	             newEditor.add("0..*");
	             newEditor.add("1..*");
	             newEditor.add("*");
	             newEditor.addModifyListener(new ModifyListener() {
	             	public void modifyText(ModifyEvent me) {
	                     Combo text = (Combo) editor.getEditor();
	                     editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
	                     Notification notification = new Notification(ElementType.END1_MULTIPLICITY, text.getItem(text.getSelectionIndex()));
	                     properties.notifyObservers(notification);
	                   }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End2.Name")) {
            	 Text newEditor = new Text(propertiesTable, SWT.NONE);
	             newEditor.setText(item.getText(EDITABLECOLUMN));
	             newEditor.addModifyListener(new ModifyListener() {
	               public void modifyText(ModifyEvent me) {
	                 Text text = (Text) editor.getEditor();
	                 editor.getItem().setText(EDITABLECOLUMN, text.getText());
	                 Notification notification = new Notification(ElementType.END2_NAME, text.getText());
                     properties.notifyObservers(notification);
	               }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End2.Visibility")) {
            	 Combo newEditor = new Combo(propertiesTable, SWT.NONE);
	             newEditor.add("PUBLIC");
	             newEditor.add("PRIVATE");
	             newEditor.add("PROTECTED");
	             newEditor.add("PACKAGE");
	             newEditor.addModifyListener(new ModifyListener() {
	             	public void modifyText(ModifyEvent me) {
	                     Combo text = (Combo) editor.getEditor();
	                     editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
	                     Notification notification = new Notification(ElementType.END2_VISIBILITY, convertVisibility(text.getItem(text.getSelectionIndex())));
	                     properties.notifyObservers(notification);
	                   }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End2.Is Navigable")) {
            	 Combo newEditor = new Combo(propertiesTable, SWT.NONE);
	             newEditor.add("yes");
	             newEditor.add("no");
	             newEditor.addModifyListener(new ModifyListener() {
	             	public void modifyText(ModifyEvent me) {
	                     Combo text = (Combo) editor.getEditor();
	                     editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
	                     Notification notification = new Notification(ElementType.END2_IS_NAVIGABLE, text.getItem(text.getSelectionIndex()));
	                     properties.notifyObservers(notification);
	                   }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End2.Aggregation")) {
            	 Combo newEditor = new Combo(propertiesTable, SWT.NONE);
	             newEditor.add("NONE");
	             newEditor.add("AGGREGATE");
	             newEditor.add("COMPOSITE");
	             newEditor.addModifyListener(new ModifyListener() {
	             	public void modifyText(ModifyEvent me) {
	                     Combo text = (Combo) editor.getEditor();
	                     editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
	                     Notification notification = new Notification(ElementType.END2_AGGREGATION, text.getItem(text.getSelectionIndex()));
	                     properties.notifyObservers(notification);
	                   }
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("End2.Multiplicity")) {
            	 Combo newEditor = new Combo(propertiesTable, SWT.NONE);
	             newEditor.add("");
	             newEditor.add("0..1");
	             newEditor.add("1");
	             newEditor.add("0..*");
	             newEditor.add("1..*");
	             newEditor.add("*");
	             newEditor.addModifyListener(new ModifyListener() {
	             	public void modifyText(ModifyEvent me) {
	                     Combo text = (Combo) editor.getEditor();
	                     editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
	                     Notification notification = new Notification(ElementType.END2_MULTIPLICITY, text.getItem(text.getSelectionIndex()));
	                     properties.notifyObservers(notification);
	             	}
	             });
	             newEditor.setFocus();
	             editor.setEditor(newEditor, item, EDITABLECOLUMN);
             } else if (item.getText().equals("Name")) {
		        	Text newEditor = new Text(propertiesTable, SWT.NONE);
			        newEditor.setText(item.getText(EDITABLECOLUMN));
			        newEditor.addModifyListener(new ModifyListener() {
			          public void modifyText(ModifyEvent me) {
			            Text text = (Text) editor.getEditor();
			            editor.getItem().setText(EDITABLECOLUMN, text.getText());
			            Notification notification = new Notification(ElementType.CLASS_NAME, text.getText());
	                    properties.notifyObservers(notification);
			          }
			        });
			        newEditor.setFocus();
			        editor.setEditor(newEditor, item, EDITABLECOLUMN);
		        } else if (item.getText().equals("Visibility")) {
		        	Combo newEditor = new Combo(propertiesTable, SWT.NONE);
			        newEditor.add("PUBLIC");
			        newEditor.add("PRIVATE");
			        newEditor.add("PROTECTED");
			        newEditor.add("PACKAGE");
			        newEditor.addModifyListener(new ModifyListener() {
			        	public void modifyText(ModifyEvent me) {
			                Combo text = (Combo) editor.getEditor();
			                editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
			                Notification notification = new Notification(ElementType.CLASS_VISIBILITY, convertVisibility(text.getItem(text.getSelectionIndex())));
		                    properties.notifyObservers(notification);
			              }
			        });
			        newEditor.setFocus();
			        editor.setEditor(newEditor, item, EDITABLECOLUMN);
		        } else if (item.getText().equals("Is abstract")) {
		        	Combo newEditor = new Combo(propertiesTable, SWT.NONE);
			        newEditor.add("yes");
			        newEditor.add("no");
			        
			        newEditor.addModifyListener(new ModifyListener() {
			        	public void modifyText(ModifyEvent me) {
			                Combo text = (Combo) editor.getEditor();
			                editor.getItem().setText(EDITABLECOLUMN, text.getItem(text.getSelectionIndex()));
			                Notification notification = new Notification(ElementType.CLASS_IS_ABSTRACT, text.getItem(text.getSelectionIndex()));
		                    properties.notifyObservers(notification);
			              }
			        });
			        newEditor.setFocus();
			        editor.setEditor(newEditor, item, EDITABLECOLUMN);
		        }

                          
           }
         });


		 item1 = new TableItem(propertiesTable, SWT.NONE);
		 item2 = new TableItem(propertiesTable, SWT.NONE);
		 item3 = new TableItem(propertiesTable, SWT.NONE);
		 item4 = new TableItem(propertiesTable, SWT.NONE);
		 item5 = new TableItem(propertiesTable, SWT.NONE);
		 item6 = new TableItem(propertiesTable, SWT.NONE);
		 item7 = new TableItem(propertiesTable, SWT.NONE);
		 item8 = new TableItem(propertiesTable, SWT.NONE);
		 item9 = new TableItem(propertiesTable, SWT.NONE);
		 item10 = new TableItem(propertiesTable, SWT.NONE);
		 item11 = new TableItem(propertiesTable, SWT.NONE);
		 item12 = new TableItem(propertiesTable, SWT.NONE);
		 item13 = new TableItem(propertiesTable, SWT.NONE);
	         
	     generalTab = new TabItem(tabFolder, SWT.NULL);
		 generalTab.setText("General");
	     generalTab.setControl(propertiesTable);
	     
	     contentTab = new TabItem(tabFolder, SWT.NULL);
	     contentTab.setText("Attributes & Operations");
		 Composite tabContainer = new Composite(tabFolder, SWT.NONE);
		 contentTab.setControl(tabContainer);
		 tabContainer.setLayout(null);
		 Label attributesLabel = new Label(tabContainer, SWT.NONE);
		 attributesLabel.setBounds(5, 5, 100, 20);
		 attributesLabel.setText("Attributes:");
		 attributesText = new Text(tabContainer, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		 attributesText.setBounds(5, 30, 265, 250);
		 attributesText.addFocusListener(new FocusListener() {

			 @Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void focusLost(FocusEvent e) {
					Text tempText = (Text) e.widget;
					Notification notification = new Notification(ElementType.UPDATE_ATTRIBUTES, tempText.getText());
					notifyObservers(notification);
				}
				
			});
			
		 Label operationsLabel = new Label(tabContainer, SWT.NONE);
		 operationsLabel.setBounds(5, 335, 100, 20);
		 operationsLabel.setText("Operations:");
		 operationsText = new Text(tabContainer, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		 operationsText.setBounds(5, 360, 265, 250);
		 operationsText.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void focusLost(FocusEvent e) {
					Text tempText = (Text) e.widget;
					Notification notification = new Notification(ElementType.UPDATE_OPERATIONS, tempText.getText());
					notifyObservers(notification);
				}
				
			});

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private String convertVisibility(String inputVisibility) {
		String visibility = "+";
        if (inputVisibility.equals("PUBLIC")) {
       	 visibility = "+";
        } else if (inputVisibility.equals("PRIVATE")) {
       	 visibility = "-";
        } else if (inputVisibility.equals("PROTECTED")) {
       	 visibility = "#";
        } else if (inputVisibility.equals("PACKAGE")) {
       	 visibility = "~";
        }
        return visibility;
	}

	@Override
	public void update(Observable observable, Object notification) {
		Notification receivedNotification = (Notification) notification;
		if (receivedNotification.getType() == ElementType.CLASS) {
			 
								
			ClassElementConvertor classElementConvertor = new ClassElementConvertor();
			classElementConvertor.decodeContent(receivedNotification.getContent());
			String visibility = "PUBLIC";
			if (classElementConvertor.getVisibility().equals("-")) {
				visibility = "PRIVATE";
			} else if (classElementConvertor.getVisibility().equals("#")) {
				visibility = "PROTECTED";
			} else if (classElementConvertor.getVisibility().equals("~")) {
				visibility = "PACKAGE";
			}
			
			String isAbstract = "yes";
			if (!classElementConvertor.getIsAbstract()) isAbstract = "no";
			
			item1.setText(new String[] { "Name", classElementConvertor.getTitle()});
			item2.setText(new String[] { "Visibility", visibility});
			item3.setText(new String[] { "Is abstract", isAbstract});
			item4.setText("");
			item5.setText("");
			item6.setText("");
			item7.setText("");
			item8.setText("");
			item9.setText("");
			item10.setText("");
			item11.setText("");
			item12.setText("");
			item13.setText("");
						
			// TAB 2
			attributesText.setText(classElementConvertor.getAttributes());
			operationsText.setText(classElementConvertor.getOperations());
			
					
		} else if (receivedNotification.getType() == ElementType.ASSOCIATION) {
			// Create environment for association 
			
	         
	        AssociationElementConvertor associationElementConvertor = new AssociationElementConvertor();
			associationElementConvertor.decodeContent(receivedNotification.getContent());
			String lStartVisibility  = "";
			String lEndVisibility = "";
			if (associationElementConvertor.getStartVisibility().equals("+")) {
				lStartVisibility = "PUBLIC";
			} else if (associationElementConvertor.getStartVisibility().equals("-")) {
				lStartVisibility = "PRIVATE";
			} else if (associationElementConvertor.getStartVisibility().equals("#")) {
				lStartVisibility = "PROTECTED";
			} else if (associationElementConvertor.getStartVisibility().equals("~")) {
				lStartVisibility = "PACKAGE";
			}
			
			if (associationElementConvertor.getEndVisibility().equals("+")) {
				lEndVisibility = "PUBLIC";
			} else if (associationElementConvertor.getEndVisibility().equals("-")) {
				lEndVisibility = "PRIVATE";
			} else if (associationElementConvertor.getEndVisibility().equals("#")) {
				lEndVisibility = "PROTECTED";
			} else if (associationElementConvertor.getEndVisibility().equals("~")) {
				lEndVisibility = "PACKAGE";
			}
			
			item1.setText(new String[] { "Element name", associationElementConvertor.getElementName()});
			item2.setText(new String[] { "", "" });
			item3.setText(new String[] { "End1.Name", associationElementConvertor.getStartName()});
			item4.setText(new String[] { "End1.Visibility", lStartVisibility});
			item5.setText(new String[] { "End1.Is Navigable", (associationElementConvertor.getStartIsNavigable()) ?"yes":"no"});
			item6.setText(new String[] { "End1.Aggregation", associationElementConvertor.getStartAggregation() });
			item7.setText(new String[] { "End1.Multiplicity", associationElementConvertor.getStartMultiplicity() });
			item8.setText(new String[] { "", ""});
			item9.setText(new String[] { "End2.Name", associationElementConvertor.getEndName() });
			item10.setText(new String[] { "End2.Visibility", lEndVisibility});
			item11.setText(new String[] { "End2.Is Navigable", associationElementConvertor.getEndIsNavigable()?"yes":"no"});
			item12.setText(new String[] { "End2.Aggregation", associationElementConvertor.getEndAggregation()});
			item13.setText(new String[] { "End2.Multiplicity", associationElementConvertor.getEndMultiplicity()});
		
		} else if (receivedNotification.getType() == ElementType.DISABLE) {
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					tabFolder.setVisible(false);
					container.setVisible(false);
					//info.setVisible(true);
				}
			});
			
			//container.setEnabled(false);
			//container.setVisible(false);
		} else if (receivedNotification.getType() == ElementType.ENABLE) {
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					tabFolder.setVisible(true);
					container.setVisible(true);
					//info.setVisible(false);					
				}
			});
			// container.setEnabled(true);
			// container.setVisible(true);
		}
		
	} 
	
	public void addObserver(Observer iObs) {
		mObservers.add(iObs);
	}
	
	public void notifyObservers(Object iNotification) {
		for (int i=0; i<mObservers.size(); ++i) {
			mObservers.get(i).update(null, iNotification);
		}
	}

}
