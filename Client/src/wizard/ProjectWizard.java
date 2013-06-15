package wizard;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import collaborationmail.Conversation;
import collaborationmail.NavigationView;
import collaborationmail.OpenViewAction;
import collaborationmail.Properties;
import collaborationmail.Toolbar;
import collaborationmail.View;

public class ProjectWizard extends Wizard {
	AccountPage accountPage;
    ProjectPage projectPage;
    IWorkbenchWindow window;

    public ProjectWizard(IWorkbenchWindow iWindow) {
    	window = iWindow;
    }
    
    public void addPages() {
             accountPage = new AccountPage("My account");
             addPage(accountPage);
             
    }
    public boolean performFinish() {
    	try {
			window.getActivePage().showView(View.ID, "1", IWorkbenchPage.VIEW_ACTIVATE);
			
			//window.getActivePage().showView(Toolbar.ID, "2", IWorkbenchPage.VIEW_ACTIVATE);
			
			// Adding observers			
			Properties lPropertiesView = null;
			View lView = null;
			Toolbar lToolbar = null;
			NavigationView lNavigationView = null;
			Conversation lConversation = null;
			
			IViewPart[] views = window.getActivePage().getViews(); 
			for (int i=0; i<views.length; ++i) {
				if (views[i].getClass().getSimpleName().equals("Properties")) {
					lPropertiesView = (Properties)views[i];
				} else if (views[i].getClass().getSimpleName().equals("View")) {
					lView = (View)views[i];
				} else if (views[i].getClass().getSimpleName().equals("Toolbar")) {
					lToolbar = (Toolbar)views[i];
				} else if (views[i].getClass().getSimpleName().equals("NavigationView")) {
					lNavigationView = (NavigationView)views[i];
				} else if (views[i].getClass().getSimpleName().equals("Conversation")) {
					lConversation = (Conversation)views[i];
				}
				
			}
			
			lView.addObserver(lPropertiesView);
			lToolbar.addObserver(lView.getWhiteboard());
			lView.addObserver(lNavigationView);
			lConversation.initializeMUC(lView.getWhiteboardMUC(), lView.getSessionAddress());
			lPropertiesView.addObserver(lView.getWhiteboard());
			
			
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
        System.out.println("Wizard finished.");     
    	return true;
    }

}
