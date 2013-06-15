package tests;

import static org.junit.Assert.*;

import org.eclipse.swt.SWT;
import org.junit.Test;

import staruml.ClassElement;

public class ClassElementTest {

	@Test
	public void testToXMLString() {
		int x = 50;
		int y = 70;
		int width = 100;
		int height = 160;
		int ID = 1;
		
		ClassElement firstClassElement = new ClassElement(null, SWT.NONE, x, y, width, height, ID);
		ClassElement secondClassElement = new ClassElement(null, SWT.NONE, firstClassElement.toXMLString());
		
		System.out.println(firstClassElement.toXMLString());
		
		//assertEquals(x, secondClassElement.getX());
	}

}
