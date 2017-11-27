package edu.ucsb.cs56.projects.games.minesweeper.tests;

import org.junit.Test;

import edu.ucsb.cs56.projects.games.minesweeper.constants.Constants;
import edu.ucsb.cs56.projects.games.minesweeper.gamelogic.Grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for Grid
 * @author David Acevedo
 * @version 2015/03/04 for lab07, cs56, W15
 * @see Grid
 */

public class GridTest {

	/**
	 * Test case for setZero method of the Grid class
	 * @see Grid#setZero()
	 */
	@Test
	public void test_setZero() {
		boolean correct = true;
		Grid test = new Grid();
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
	 * Test case default constructor of the Grid class
	 * @see Grid#Grid()
	 */
	@Test
	public void test_Grid_Easy() {
		int count = 0;
		Grid test = new Grid();
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

	/**
	 * Test medium difficulty constructor of Grid class
	 * @see Grid#Grid(Constants.Difficulty)
	 */
	@Test
	public void test_Grid_Medium() {
		int count = 0;
		Grid test = new Grid(Constants.Difficulty.MEDIUM);
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

	/**
	 * Test medium difficulty constructor of Grid class
	 * @see Grid#Grid(Constants.Difficulty)
	 */
	@Test
	public void test_Grid_Hard() {
		int count = 0;
		Grid test = new Grid(Constants.Difficulty.HARD);
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
	 * Test case for isOpen method of the Grid class
	 * @see Grid#isOpen
	 */
	@Test
	public void test_isOpen1() {
		Grid g1 = new Grid();
		assertEquals(false, g1.isOpen(0, 1));
	}


	/**
	 * Test case for isOpen method of the Grid class
	 * @see Grid#isOpen(int, int)
	 */
	@Test
	public void test_isOpen2() {
		Grid g1 = new Grid();
		assertEquals(false, g1.isOpen(0, 1));
	}

	/**
	 * Test case for isFlag method of the Grid class
	 * @see Grid#isFlag(int, int)
	 */
	@Test
	public void test_isFlag1() {
		Grid g1 = new Grid();
		assertEquals(false, g1.isFlag(9, 9));
	}

	/**
	 * Test case for isFlag method of the Grid class
	 * @see Grid#isFlag(int, int)
	 */
	@Test
	public void test_isFlag2() {
		Grid g1 = new Grid();
		assertEquals(false, g1.isFlag(9, 9));
	}

	/**
	 * Test case for flagBox method of the Grid class
	 * @see Grid#flagBox(int, int)
	 */
	@Test
	public void test_flagBox() {
		boolean correct = false;
		Grid test = new Grid();
		test.flagBox(0, 6);
		test.flagBox(1, 2);
		test.flagBox(2, 7);
		if (test.isFlag(0, 6) && test.isFlag(1, 2) && test.isFlag(2, 7)) {
			correct = true;
		}
		assertEquals(true, correct);
	}

	/**
	 * Test case for deflagBox method of the Grid class
	 * @see Grid#deflagBox(int, int)
	 */
	@Test
	public void test_deflagBox() {
		boolean correct = false;
		Grid test = new Grid();
		test.flagBox(0, 6);
		test.flagBox(1, 2);
		test.flagBox(2, 7);
		test.deflagBox(0, 6);
		test.deflagBox(1, 2);
		test.deflagBox(2, 7);
		if (!test.isFlag(0, 6) && !test.isFlag(1, 2) && !test.isFlag(2, 7)) {
			correct = true;
		}
		assertEquals(true, correct);
	}

	/**
	 * Test case for gameStatus method of the Grid class
	 * @see Grid#getGameState()
	 */
	@Test
	public void test_getGameState() {
		Grid g1 = new Grid();
		assertEquals(Constants.GameState.PLAYING, g1.getGameState());
	}
}
