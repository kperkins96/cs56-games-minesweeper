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
		for (int i =0; i< s; i++){
			for (int j=0; j<s; j++)
			{
				if (test.getG()[i][j] == 'X')
					count++;
			}
		}
		assertEquals(10, count);
	}

	@Test
	public void test_blankToMine_Medium(){
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
	 * Test case for insertNums method of the Grid class
	 *
	 * @see Grid#insertNums()
	 */

	@Test
	public void test_insertNums() {
		boolean correct = true;

		Grid t = new Grid(-1);
		Grid k = new Grid(-1);
		//make a entirely known array and an array with known bombs
		char[][] test = new char[4][4];
		char[][] known = new char[4][4];
		known[0][0] = '0';
		known[0][1] = '1';
		known[0][2] = '1';
		known[0][3] = '1';
		known[1][0] = '1';
		known[1][1] = '2';
		known[1][3] = '1';
		known[2][0] = '2';
		known[2][2] = '3';
		known[2][3] = '2';
		known[3][1] = '3';
		known[3][3] = '1';
		test[0][0] = '0';
		test[0][1] = '0';
		test[0][2] = '0';
		test[0][3] = '0';
		test[1][0] = '0';
		test[1][1] = '0';
		test[1][3] = '0';
		test[2][0] = '0';
		test[2][2] = '0';
		test[2][3] = '0';
		test[3][1] = '0';
		test[3][3] = '0';
		test[1][2] = known[1][2] = test[2][1] = known[2][1] = test[3][2] = known[3][2] = test[3][0] = known[3][0] = 'X';
		//assign grid arrays to hard coded arrays
		t.setGrid(test);
		k.setGrid(known);

		t.insertNums();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (t.getG()[i][j] != k.getG()[i][j]) {
					correct = false;
				}
			}
		}

		assertEquals(true, correct);
	}

	/**
	 * Test case for mapMaker method of the Grid class
	 *
	 * @see Grid#mapMaker()
	 */

	@Test
	public void test_mapMaker() {
		int count = 0;
		Grid test = new Grid();
		int s = test.getSize();
		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				if (test.getM()[i][j] == '?') {
					count++;
				}
			}
		}

		assertEquals(100, count);
	}

	/**
	 * Test case for toString method of the Grid class
	 *
	 * @see Grid#toString()
	 */

	@Test
	public void test_toString(){

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
	 * Test case for isMine method of the Grid class
	 *
	 * @see Grid#isMine()
	 */

	@Test
	public void test_isMine() {
		boolean correct = true;
		Grid k = new Grid(-1);
		//make a entirely known array
		char[][] known = new char[4][4];
		known[0][0] = '0';
		known[0][1] = '1';
		known[0][2] = '1';
		known[0][3] = 'X';
		known[1][0] = '1';
		known[1][1] = '2';
		known[1][3] = '1';
		known[2][0] = 'X';
		known[2][2] = 'X';
		known[2][3] = '2';
		known[3][1] = '3';
		known[3][3] = '1';
		known[1][2] = known[2][1] = known[3][2] = known[3][0] = '0';
		//assign grid arrays to hard coded arrays
		k.setGrid(known);

		if (!k.isMine(8) || !k.isMine(10) || !k.isMine(3)) {
			correct = false;
		}
		assertEquals(true, correct);

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
	 * Test case for searchBox method of the Grid class
	 *
	 * @see Grid#searchBox()
	 */

	@Test
	public void test_searchBox() {
		boolean correct = true;
		Grid k = new Grid(-1);
		//make a entirely known array
		char[][] known = new char[4][4];
		known[0][0] = '0';
		known[0][1] = '1';
		known[0][2] = '1';
		known[0][3] = '2';
		known[1][0] = '1';
		known[1][1] = '2';
		known[1][3] = '1';
		known[2][0] = 'X';
		known[2][2] = '1';
		known[2][3] = '2';
		known[3][1] = '3';
		known[3][3] = '1';
		known[1][2] = known[2][1] = known[3][2] = known[3][0] = '0';
		//assign grid arrays to hard coded arrays
		k.setGrid(known);
		if (k.searchBox(8) != 'X' || k.searchBox(10) != '1' || k.searchBox(0) != '0' || k.searchBox(3) != '2') {
			correct = false;
		}
		assertEquals(true, correct);
	}

	/**
	 * Test case for flagBox method of the Grid class
	 *
	 * @see Grid#flagBox()
	 */

	@Test
	public void test_flagBox() {
		boolean correct = true;
		Grid test = new Grid();
		test.flagBox(6);
		test.flagBox(12);
		test.flagBox(27);
		if (test.getM()[0][6] != 'F' || test.getM()[1][2] != 'F' || test.getM()[2][7] != 'F') {
			correct = false;
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
		boolean correct = true;
		Grid test = new Grid();
		test.flagBox(6);
		test.flagBox(12);
		test.flagBox(27);
		test.deflagBox(6);
		test.deflagBox(12);
		test.deflagBox(27);
		if (test.getM()[0][6] != '?' || test.getM()[1][2] != '?' || test.getM()[2][7] != '?') {
			correct = false;
		}
		assertEquals(true, correct);
	}

	/**
	 * Test case for findAllZeros method of the Grid class
	 *
	 * @see Grid#findAllZeros()
	 */

	@Test
	public void test_findAllZeros() {
		boolean correct = true;
		Grid k = new Grid(-1);
		//make a entirely known array
		char[][] known = new char[4][4];
		known[0][0] = '0';
		known[0][1] = '1';
		known[0][2] = '1';
		known[0][3] = '2';
		known[1][0] = '1';
		known[1][1] = '2';
		known[1][3] = '1';
		known[2][0] = 'X';
		known[2][2] = '1';
		known[2][3] = '2';
		known[3][1] = '3';
		known[3][3] = '1';
		known[1][2] = known[2][1] = known[3][2] = known[3][0] = '0';
		//assign grid arrays to hard coded arrays
		k.setGrid(known);
		k.findAllZeros(0, 0);
		if (k.getM()[0][0] != '0' || k.getM()[1][0] != '1' || k.getM()[1][1] != '2' || k.getM()[0][1] != '1') {
			correct = false;
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

	/**
	 * Test case for getCell method of the Grid class
	 *
	 * @see Grid#getCell()
	 */

	@Test
	public void test_getCell() {
		boolean correct = true;
		Grid k = new Grid(-1);
		//make a entirely known array
		char[][] known = new char[4][4];
		known[0][0] = '0';
		known[0][1] = '1';
		known[0][2] = '1';
		known[0][3] = '2';
		known[1][0] = '1';
		known[1][1] = '2';
		known[1][3] = '1';
		known[2][0] = 'X';
		known[2][2] = '1';
		known[2][3] = '2';
		known[3][1] = '3';
		known[3][3] = '1';
		known[1][2] = known[2][1] = known[3][2] = known[3][0] = '0';
		//assign grid arrays to hard coded arrays
		k.setGrid(known);
		k.findAllZeros(0, 0);
		k.flagBox(3);
		if (k.getCell(0) != '0' || k.getCell(3) != 'F' || k.getCell(13) != '?') {
			correct = false;
		}
		assertEquals(true, correct);
	}
}
