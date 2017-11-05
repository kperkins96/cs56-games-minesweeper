package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.Serializable;
import java.util.LinkedList;

/** The Grid class is the foundation for minesweeper, applies mine locations, checks if something is open,
 makes flags functional, etc.
 @author Caleb Nelson
 @author David Acevedo
 @version 2015/03/04 for lab07, cs56, W15


 @author Isaiah Egan
 @author Austin Hwang
 @author Sai Srimat
 @version July 2016 for Legacy Code, cs56, M16

 */
public class Grid implements Serializable{

	private final int EASY_SIZE = 10;
	private final int MED_SIZE = 15;
	private final int HARD_SIZE = 20;

	public String saveTime;
	private GridComponent[][] grid;
	/*
	// instance variables
	private int size;
	private char[][] grid;
	private char[][] map;
	*/

	/**
	 * Default constructor for objects of class GUIGrid
	 */

	public Grid() {
		this(0);
		/*
		saveTime = new String("0");
		grid = new GridComponent[EASY_SIZE][EASY_SIZE];
		setZero();
		for (int i = 0; i < grid.length; i++) {
			setMine();
		}
		*/
	}

	public Grid(int difficulty) {
		saveTime = new String("0");
		switch (difficulty) {
			case -1: //for known grid testing
				grid = new GridComponent[4][4];
				break;
			case 0:
				grid = new GridComponent[EASY_SIZE][EASY_SIZE];
				break;
			case 1:
				grid = new GridComponent[MED_SIZE][MED_SIZE];
				break;
			case 2:
				grid = new GridComponent[HARD_SIZE][HARD_SIZE];
				break;
			default:
				throw new IllegalArgumentException("Difficulty needs to be an integer between 0 and 2 inclusive.");
		}
		setZero();
		for (int i = 0; i < (difficulty + 1) * grid.length; i++) {
			setMine();
		}
	}

	/**
	 *	Getter for size
	 */
	public int getSize() {
		return grid.length;
	}

	/**
	 * Sets all cells in the grid to zero.
	 */
	public void setZero() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = new GridComponent();
			}
		}
	}

	/**
	 * Finds a random Empty cell and makes it a Mine
	 */
	public void setMine() {
		int spotX = (int) (grid.length * grid.length * Math.random());
		int a = spotX / grid.length;
		int b = spotX % grid.length;
		if (grid[a][b].makeMine()) {
			for (int i = a - 1; i <= a + 1; i++) {
				for (int j = b - 1; j <= b + 1; j++) {
					if (i >= 0 && i < grid.length && j >= 0 && j < grid.length) {
						grid[i][j].iterate();
					}
				}
			}
		} else {
			setMine();
		}
	}

	/**
	 * Prints out the map in a table
	 */
	public String toString() {
		final String borders = " ---------------------";
		final String line = "|";

		String game = "";

		game += "\n  ";
		for (int i = 0; i < grid.length; i++) {
			game += i;
			game += " ";
		}
		game += "\n";
		game += borders;
		game += "\n";
		for (int i = 0; i < grid.length; i++) {
			game += i + line;
			for (int j = 0; j < grid.length; j++) {
				game += grid[i][j];
				game += line;
			}
			game += "\n";
		}
		game += borders;

		return game;
	}

	/**
	 * Checks a cell to see if it has been opened
	 */
	public boolean isOpen(int i) throws IllegalArgumentException {
	    return isOpen(i / grid.length, i % grid.length);
	}

	/**
	 * Checks a cell to see if it has been opened
	 */
	public boolean isOpen(int i, int j) throws IllegalArgumentException {
		if (i >= 0 && i < grid.length && j >= 0 && j < grid.length) {
		    return grid[i][j].getIsOpen();
		} else {
			throw new IllegalArgumentException("I don't know where this exists :(");
		}
	}

	/**
	 * Checks a cell to see if there is a grid underneath
	 */
	public boolean isMine(int i) throws IllegalArgumentException {
	    return isMine(i / grid.length, i % grid.length);
	}

	/**
	 * Checks a cell to see if there is a grid underneath
	 */
	public boolean isMine(int i, int j) throws IllegalArgumentException {
		if (i >= 0 && i < grid.length && j >= 0 && j < grid.length) {
		    return grid[i][j].getIsMine();
		} else {
			throw new IllegalArgumentException("I don't know where this exists :(");
		}
	}

	/**
	 * Check to see if a user placed a flag on that cell
	 */
	public boolean isFlag(int i) throws IllegalArgumentException {
	    return isFlag(i / grid.length, i % grid.length);
	}

	/**
	 * Check to see if a user placed a flag on that cell
	 */
	public boolean isFlag(int i, int j) throws IllegalArgumentException {
		if (i >= 0 && i < grid.length && j >= 0 && j < grid.length) {
		    return grid[i][j].getIsFlagged();
		} else {
			throw new IllegalArgumentException("I don't know where this exists :(");
		}
	}

	/**
	 * Opens the cell and returns what will be placed there
	 */
	public char searchBox(int box) {
	    return searchBox(box / grid.length, box % grid.length);
	}

	/**
	 * Opens the cell and returns what will be placed there
	 */
	public char searchBox(int i, int j) {
		char spot = 'e';
		if (i >= 0 && i < grid.length && j >= 0 && j < grid.length) {
			// set variable to an object in the grid
			if (grid[i][j].getIsFlagged()) {
				System.out.println("You cannot search a flagged box!");
			} else if (grid[i][j].getIsOpen()) {
				System.out.println("You cannot search an opened box!");
			} else {
				spot = grid[i][j].getSymbol();
				grid[i][j].open();
				if (grid[i][j].getSymbol() == '0') {
					findAllZeros(i, j);
				}
			}
		}
		return spot;
	}

	/**
	 * Places a flag on the cell
	 */
	public void flagBox(int box) {
	    flagBox(box / grid.length, box % grid.length);
	}

	/**
	 * Places a flag on the cell
	 */
	public void flagBox(int i, int j) {
		if (grid[i][j].getIsFlagged()) {
			System.out.println("This box is already flagged!");
		} else if (grid[i][j].getIsFlagged()) {
			System.out.println("You cannot put a flag on an opened box!");
		} else {
			// TODO: places 'F' only after a left click on a nonflag occurs?
			grid[i][j].setFlagged(true);
		}
	}

	/**
	 * Removes a flag on a cell that has one
	 */
	public void deflagBox(int box) {
	    deflagBox(box / grid.length, box % grid.length);
	}

	/**
	 * Removes a flag on a cell that has one
	 */
	public void deflagBox(int i, int j) {
		if (!grid[i][j].getIsFlagged()) {
			System.out.println("That box does not have a flag on it!");
		} else {
			grid[i][j].setFlagged(false);
		}
	}

	/**
	 * Looks for surrounding numbers near the cell and opens them, repeats when find another zero
	 */
	public void findAllZeros(int row, int col) { //TODO: throw exception
        LinkedList<Integer> bfs = new LinkedList<>();
        if (grid[row][col].getSymbol() == '0') {
			bfs.add(row * grid.length + col);
			while (!bfs.isEmpty()) {
				row = bfs.peekFirst() / grid.length;
				col = bfs.pollFirst() % grid.length;
				if (grid[row][col].getSymbol() == '0') {
					for (int i = row - 1; i <= row + 1; i++) {
						for (int j = col - 1; j <= col + 1; j++) {
							if (i >= 0 && i < grid.length && j >= 0 && j < grid.length) {
								if (grid[i][j].getSymbol() == '0' && !grid[i][j].getIsOpen()) {
									bfs.add(i * grid.length + j);
								}
								grid[i][j].open();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Updates the state of the game
	 */
	public int gameStatus(int stat) {
		if (stat == 0) { // runs only if player hasn't hit a mine
			int correctBoxes = 0;
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid.length; j++) {
					if (grid[i][j].getIsOpen() && grid[i][j].getIsMine()) {
						stat = -1;
						break;
					} else if ((grid[i][j].getIsMine() && grid[i][j].getIsFlagged()) || grid[i][j].getIsOpen()) {
						correctBoxes++; //the map has the correct move for that cell
					}
				}
			}
			if(stat == 0 && correctBoxes == grid.length * grid.length) { //all correct moves have been made
				stat = 1;
			}
		}
		return stat;
	}

	/**
	 * Finds the current condition of a cell
	 */
	public char getCell (int cell) {
		return grid[cell / grid.length][cell % grid.length].getSymbol();
	}

	/**
	 * Finds the current condition of a cell
	 */
	public char getCell (int i, int j) {
		return grid[i][j].getSymbol();
	}

	char[][] getG() {
		char[][] g = new char[grid.length][grid.length];
		for (int i = 0; i < g.length; i++) {
			for (int j = 0; j < g.length; j++) {
				g[i][j] = grid[i][j].getSymbol();
			}
		}
		return g;
	}
}
