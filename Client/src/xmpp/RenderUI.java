package xmpp;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.progress.UIJob;

public class RenderUI extends UIJob {
	
	private Composite myPanel;
	
	public RenderUI(String name, Composite myPanel) {
		super(name);
		this.myPanel = myPanel;
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		Button newButton = new Button(myPanel, SWT.PUSH);
		newButton.setText("Hey sexy");
		return null;
	}

}
