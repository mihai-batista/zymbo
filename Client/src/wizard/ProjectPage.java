package wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ProjectPage extends WizardPage {
	Text street;
    Text city;
    Text state;

    protected ProjectPage(String pageName) {
             super(pageName);
             setTitle("Create or join a project");
             setDescription("Use the options below");
    }
    public void createControl(Composite parent) {
             Composite composite = new Composite(parent, SWT.NONE);
             GridLayout layout = new GridLayout();
             layout.numColumns = 2;
             composite.setLayout(layout);
             setControl(composite);
             new Label(composite,SWT.NONE).setText("Street");
             street = new Text(composite,SWT.NONE);
             new Label(composite,SWT.NONE).setText("City");
             city = new Text(composite,SWT.NONE);
             new Label(composite,SWT.NONE).setText("State");
             state = new Text(composite,SWT.NONE);
    }

}
