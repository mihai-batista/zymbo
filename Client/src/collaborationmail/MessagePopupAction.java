package collaborationmail;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;

import wizard.ProjectWizard;


public class MessagePopupAction extends Action {

    private final IWorkbenchWindow window;

    MessagePopupAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
        setImageDescriptor(collaborationmail.Activator.getImageDescriptor("/icons/sample3.gif"));
    }

    public void run() {
        /*MessageDialog.openInformation(window.getShell(), "Open", "Open Message Dialog!");*/
    	ProjectWizard projectWizard = new ProjectWizard(window);
    	WizardDialog dialog = new WizardDialog(window.getShell(), projectWizard);
        dialog.create();
        dialog.open();
    }
}