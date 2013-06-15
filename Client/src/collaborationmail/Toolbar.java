package collaborationmail;

import java.util.ArrayList;
import java.util.Observer;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import staruml.ElementType;
import staruml.Notification;

import buttons.CustomButton;

public class Toolbar extends ViewPart {
	Button mPackage, mClass, mInterface, mAssociation, mDirectAssociation, mAggregation, mComposition, mGeneralization, mDependency, mRealization;
	public static final String ID = "collaborationMail.toolbar";
	ListViewer viewer;
	ArrayList<Observer> mObservers = new ArrayList<Observer>();
	
	private static final String CLASS = "class";
	private static final String DIRECT_ASSOCIATION = "direct_association";
	private static final String ASSOCIATION = "association";
	private static final String AGGREGATION = "aggregation";
	private static final String COMPOSITION = "composition";
	private static final String GENERALIZATION = "generalization";
	private static final String REALIZATION = "realization";
	private static final String DEPENDENCY = "dependency";
	private static final String INTERFACE = "interface";
	
	Toolbar toolbar;
	
	public Toolbar() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		
		 toolbar = this;
		 Composite container = new Composite(parent, SWT.NONE);
		 		 
		 RowLayout rowLayout = new RowLayout();
         //rowLayout.wrap = true;
         rowLayout.pack = false;
         //rowLayout.justify = true;
         rowLayout.type = SWT.VERTICAL;
         rowLayout.marginLeft = 5;
         rowLayout.marginTop = 5;
         rowLayout.marginRight = 5;
         rowLayout.marginBottom = 5;
         rowLayout.spacing = 0;
         rowLayout.fill = true;
         rowLayout.center = true;
         //rowLayout.justify = true;
         
         // Images
         final Image whiteboardBck = new Image(parent.getDisplay(), "D:\\Work\\Licenta\\images\\bck.jpg");
         Image classImg = new Image(parent.getDisplay(), "D:\\Licenta\\documentatie\\icons\\class.jpg");
         //Image associationImg = new Image(container.getDisplay(), "");
         //Image directAssociationImg = new Image(container.getDisplay(), "");
         //Image aggregationImg = new Image(container.getDisplay(), "");
         //Image compositionImg = new Image(container.getDisplay(), "");
         
         
         container.setLayout(rowLayout);
         //CustomButton custom = new CustomButton(container, SWT.NONE);
         
         mClass = new Button(container, SWT.PUSH | SWT.FILL);
         mClass.setText("Class");
         mClass.setImage(classImg);
         mClass.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toolbar.notifyAll(new Notification(ElementType.CLASS, ""));			
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
         });
         
         mInterface = new Button(container, SWT.PUSH);
         mInterface.setText("Interface");
         mInterface.addSelectionListener(new SelectionListener() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				toolbar.notifyAll(new Notification(ElementType.INTERFACE, ""));			
 			}
 			@Override
 			public void widgetDefaultSelected(SelectionEvent e) {
 				// TODO Auto-generated method stub
 				
 			}
          });
         
         mAssociation = new Button(container, SWT.PUSH);
         mAssociation.setText("Association");
         mAssociation.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				toolbar.notifyAll(new Notification(ElementType.ASSOCIATION, ""));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
        	 
         });
         mDirectAssociation = new Button(container, SWT.PUSH);
         mDirectAssociation.setText("Directed association");
         mDirectAssociation.addSelectionListener(new SelectionListener() {

 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				toolbar.notifyAll(new Notification(ElementType.DIRECT_ASSOCIATION, ""));		
 			}

 			@Override
 			public void widgetDefaultSelected(SelectionEvent e) {
 				// TODO Auto-generated method stub
 				
 			}
         	 
          });
         mAggregation = new Button(container, SWT.PUSH);
         mAggregation.setText("Aggregation");
         mAggregation.addSelectionListener(new SelectionListener() {

  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				toolbar.notifyAll(new Notification(ElementType.AGGREGATION, ""));
  			}

  			@Override
  			public void widgetDefaultSelected(SelectionEvent e) {
  				// TODO Auto-generated method stub
  				
  			}
          	 
           });
         mComposition = new Button(container, SWT.PUSH);
         mComposition.setText("Composition");
         mComposition.addSelectionListener(new SelectionListener() {

  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				toolbar.notifyAll(new Notification(ElementType.COMPOSITION, ""));			
  			}

  			@Override
  			public void widgetDefaultSelected(SelectionEvent e) {
  				// TODO Auto-generated method stub
  				
  			}
          	 
           });
         mGeneralization = new Button(container, SWT.PUSH);
         mGeneralization.setText("Generalization");
         mGeneralization.addSelectionListener(new SelectionListener() {

  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				toolbar.notifyAll(new Notification(ElementType.GENERALIZATION, ""));			
  			}

  			@Override
  			public void widgetDefaultSelected(SelectionEvent e) {
  				// TODO Auto-generated method stub
  				
  			}
          	 
           });
         mDependency = new Button(container, SWT.PUSH);
         mDependency.setText("Dependency");
         mDependency.addSelectionListener(new SelectionListener() {

  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				toolbar.notifyAll(new Notification(ElementType.DEPENDENCY, ""));			
  			}

  			@Override
  			public void widgetDefaultSelected(SelectionEvent e) {
  				// TODO Auto-generated method stub
  				
  			}
          	 
           });
         mRealization = new Button(container, SWT.PUSH);
         mRealization.setText("Realization");
         mRealization.addSelectionListener(new SelectionListener() {

  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				toolbar.notifyAll(new Notification(ElementType.REALIZATION, ""));			
  			}

  			@Override
  			public void widgetDefaultSelected(SelectionEvent e) {
  				// TODO Auto-generated method stub
  				
  			}
          	 
           });
			
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	public void addObserver(Observer iObs) {
		mObservers.add(iObs);
	}
	
	public void notifyAll(Object iNotification) {
		for (int i=0; i<mObservers.size(); ++i) {
			mObservers.get(i).update(null, iNotification);
		}
	}

}
