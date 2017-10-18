package edu.ucsb.cs56.projects.games.minesweeper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/** Test class for GUI components

 @author Johnny Zhang
 @version 2016/17/11 for lab07, cs56, F15
 @see Grid

 */

public class GUITest{

	@Test
	public void test_dimensions() {
		MineGUI test = new MineGUI();
		test.newGame();
		assertEquals(650, test.frame.getWidth());
		assertEquals(600, test.frame.getHeight());
	}

	@Test
	public void test_clickbait() {
		MineGUI test = new MineGUI();
		test.newGame();
		// test.easyGame.doClick();
		// assertEquals(10,test.grid);
	}
}