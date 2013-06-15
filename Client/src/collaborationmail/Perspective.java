package collaborationmail;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import article.imageviewer.views.ImageView;

public class Perspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "collaborationMail.perspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		IFolderLayout left = layout.createFolder("Conversation", IPageLayout.BOTTOM, 0.75f, editorArea);
		left.addView(NavigationView.ID);
		left.addView(Conversation.ID);
		//layout.addStandaloneView(NavigationView.ID,  true, IPageLayout.LEFT, 0.20f, editorArea);
		IFolderLayout folder = layout.createFolder("Whiteboard", IPageLayout.LEFT, 0.85f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		IFolderLayout right = layout.createFolder("Toolbar", IPageLayout.RIGHT, 0.95f, editorArea);
		right.addView(Properties.ID);
		right.addView(Toolbar.ID);
		right.addView(ImageView.ID);		
		layout.getViewLayout(NavigationView.ID).setCloseable(true);
	}
}
