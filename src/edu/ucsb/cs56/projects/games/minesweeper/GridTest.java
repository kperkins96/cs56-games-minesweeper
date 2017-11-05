package edu.ucsb.cs56.projects.games.minesweeper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/** Test class for Grid

 @author David Acevedo
 @version 2015/03/04 for lab07, cs56, W15
 @see Grid

 */

public class GridTest {

	/**
	 * Test case for setZero method of the Grid class
	 *
	 * @see Grid#setZero()
	 */

	@Test
	public void test_setZero() {
		boolean correct = true;
		Grid test = new Grid(0);
		test.setZero();
		int s = test.getSize();
		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				if (test.getG()[i][j] != '0') {
					correct = false;
				}
			}
		}
		assertEquals(true, correct);
	}

	/**
	 * Test case for blankToMine method of the Grid class
	 *
	 * @see Grid#blankToMine()
	 */

	@Test
	public void test_blankToMine_Easy() {
		int count = 0;
		Grid test = new Grid(0);
		int s = test.getSize();
		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				if (test.getG()[i][j] == 'X') {
					count++;
				}
			}
		}
		assertEquals(10, count);
	}

	@Test
	public void test_blankToMine_Medium() {
		int count = 0;
		Grid test = new Grid(1);
		int s = test.getSize();
		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				if (test.getG()[i][j] == 'X') {
					count++;
				}
			}
		}
		assertEquals(30, count);
	}

	@Test
	public void test_blankToMine_Hard() {
		int count = 0;
		Grid test = new Grid(2);
		int s = test.getSize();
		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				if (test.getG()[i][j] == 'X') {
					count++;
				}
			}
		}
		assertEquals(60, count);
	}


	/**
	 * Test case for toString method of the Grid class
	 *
	 * @see Grid#toString()
	 */

	@Test
	public void test_toString() {

		//not going to implement tests, this version is obsolete
	}


	/**
	 * Test case for isOpen method of the Grid class
	 *
	 * @see Grid#isOpen
	 */

	@Test
	public void test_isOpen1() {
		Grid g1 = new Grid();
		assertEquals(false, g1.isOpen(1));
	}


	/**
	 * Test case for setZero method of the Grid class
	 *
	 * @see Grid#isOpen()
	 */

	@Test
	public void test_isOpen2() {
		Grid g1 = new Grid();
		assertEquals(false, g1.isOpen(1));
	}

	/**
	 * Test case for isFlag method of the Grid class
	 *
	 * @see Grid#isFlag()
	 */

	@Test
	public void test_isFlag1() {
		Grid g1 = new Grid();
		assertEquals(false, g1.isOpen(99));
	}

	/**
	 * Test case for isFlag method of the Grid class
	 *
	 * @see Grid#isFlag()
	 */

	@Test
	public void test_isFlag2() {
		Grid g1 = new Grid();
		assertEquals(false, g1.isOpen(99));
	}

	/**
	 * Test case for flagBox method of the Grid class
	 *
	 * @see Grid#flagBox()
	 */

	@Test
	public void test_flagBox() {
		boolean correct = false;
		Grid test = new Grid();
		test.flagBox(6);
		test.flagBox(12);
		test.flagBox(27);
		if (test.isFlag(0, 6) && test.isFlag(1, 2) && test.isFlag(2, 7)) {
			correct = true;
		}
		assertEquals(true, correct);
	}

	/**
	 * Test case for deflagBox method of the Grid class
	 *
	 * @see Grid#deflagBox()
	 */

	@Test
	public void test_deflagBox() {
		boolean correct = false;
		Grid test = new Grid();
		test.flagBox(6);
		test.flagBox(12);
		test.flagBox(27);
		test.deflagBox(6);
		test.deflagBox(12);
		test.deflagBox(27);
		if (!test.isFlag(0, 6) && !test.isFlag(1, 2) && !test.isFlag(2, 7)) {
			correct = true;
		}
		assertEquals(true, correct);
	}

	/**
	 * Test case for gameStatus method of the Grid class
	 *
	 * @see Grid#gameStatus()
	 */
	@Test
	public void test_gameStatus() {
		Grid g1 = new Grid();
		assertEquals(0, g1.gameStatus(0));
	}
}
