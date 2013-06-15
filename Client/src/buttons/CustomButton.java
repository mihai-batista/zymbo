package buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class CustomButton extends Canvas {
    private int mouse = 0;
    private boolean hit = false;
    private String title = "";
    private CustomButton button;

    public CustomButton(Composite parent, int style) {
        super(parent, style);
        button = this;
        
        this.setLayout(null);
        

        this.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                switch (mouse) {
                case 0:
                    // Default state
                   
                    e.gc.setBackground(new Color(null, 200, 0 , 0));
                    int[] points = {button.getBounds().x, button.getBounds().y, 
                    		button.getBounds().x + button.getBounds().width, button.getBounds().y,
                    		button.getBounds().x + button.getBounds().width, button.getBounds().y + button.getBounds().height, 
                    		button.getBounds().x, button.getBounds().y + button.getBounds().height};
                    e.gc.drawPolygon(points);
                    e.gc.fillPolygon(points);
                    
                    e.gc.drawString("Normal", 5, 5);
                    break;
                case 1:
                    // Mouse over
                    e.gc.drawString("Mouse over", 5, 5);
                    break;
                case 2:
                    // Mouse down
                    e.gc.drawString("Hit", 5, 5);
                    break;
                }
            }
        });
        
        this.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
                if (!hit)
                    return;
                mouse = 2;
                if (e.x < 0 || e.y < 0 || e.x > getBounds().width
                        || e.y > getBounds().height) {
                    mouse = 0;
                }
                redraw();
            }
        });
        this.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseEnter(MouseEvent e) {
                mouse = 1;
                redraw();
            }

            public void mouseExit(MouseEvent e) {
                mouse = 0;
                redraw();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                hit = true;
                mouse = 2;
                redraw();
            }

            public void mouseUp(MouseEvent e) {
                hit = false;
                mouse = 1;
                if (e.x < 0 || e.y < 0 || e.x > getBounds().width
                        || e.y > getBounds().height) {
                    mouse = 0;
                }
                redraw();
                if (mouse == 1)
                    notifyListeners(SWT.Selection, new Event());
            }
        });
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == '\r' || e.character == ' ') {
                    Event event = new Event();
                    notifyListeners(SWT.Selection, event);
                }
            }
        });
        
    
    }
    
    public void setTitle(String iTitle) {
    	title = iTitle;
    }

}

