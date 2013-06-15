package staruml;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class PictureLabelLayout extends Layout {
	Point iExtent, tExtent; // the cached sizes
	
	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
	     Control [] children = composite.getChildren();
	     if (flushCache || iExtent == null || tExtent == null) {
	         iExtent = children[0].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
	         tExtent = children[1].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
	     }
	     int width = iExtent.x + 5 + tExtent.x;
	     int height = Math.max(iExtent.y, tExtent.y);
	     return new Point(width + 2, height + 2);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		Control [] children = composite.getChildren();

	     if (flushCache || iExtent == null || tExtent == null) {
	         iExtent = children[0].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
	         tExtent = children[1].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
	     }

	     children[0].setBounds(1, 1, iExtent.x, iExtent.y);
	     children[1].setBounds(iExtent.x + 5, 1, tExtent.x, tExtent.y);

	}

}
