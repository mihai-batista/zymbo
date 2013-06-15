package staruml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;

public class WhiteboardHelpers {
	
	public static String restoreSessionFromFile(String filename) {
		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(filename));
			try {
		      while (scanner.hasNextLine()){
		    	  text.append(scanner.nextLine() + NL);
		      }
			} finally{
				scanner.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return text.toString();		  
	}
	
	public static String extractName(String address) {
		int slashPosition = address.indexOf("/");
		return address.substring(slashPosition + 1, address.length());
	}
	
	public static void drawAggregation(PaintEvent e, Color foreground, Color background, int[] points) {
		e.gc.setForeground(foreground);
		e.gc.setBackground(background);
		e.gc.fillPolygon(points);
		e.gc.drawPolygon(points);
	}
}
