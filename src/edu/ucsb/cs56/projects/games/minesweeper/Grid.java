package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PrimitiveIterator;
import java.util.Queue;

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

    public enum GameState {
    	PLAYING,
		LOST,
		WON
	}

	public enum Difficulty {
		TEST(4),
    	EASY(10),
		MEDIUM(15),
		HARD(20),
		LOAD(-1);

		private final int value;

		private Difficulty(final int val) {
			value = val;
		}

		public final int getValue() {
			return value;
		}

		public static final Difficulty getDifficultyFromSize(int size) {
			switch (size) {
				case 4:
					return TEST;
				case 10:
					return EASY;
				case 15:
					return MEDIUM;
				case 20:
					return HARD;
				default:
					return EASY;
			}
		}
	}

	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_RESET = "\u001B[0m";

	public int saveTime;
	private GridComponent[][] grid;
	private GameState gameState;
	private int correctMoves;

	/**
	 * Default constructor for objects of class GUIGrid
	 */

	public Grid() { this(Difficulty.EASY); }

	public Grid(Difficulty difficulty) {
		saveTime = 0;
		gameState = GameState.PLAYING;
		correctMoves = 0;
		grid = new GridComponent[difficulty.getValue()][difficulty.getValue()];
		setZero();
		if (difficulty == Difficulty.TEST) {
			grid[3][3].makeMine();
			for (int i = 2; i <= 3; i++) {
				for (int j = 2; j <= 3; j++) {
					grid[i][j].iterate();
				}
			}
		}
		for (int i = 0; i < difficulty.ordinal() * grid.length; i++) {
			setMine();
		}
	}

	/**
	 *	Getter for size
	 */
	public int getSize() {
		return grid.length;
	}

	public Difficulty getDifficulty() {
		return Difficulty.getDifficultyFromSize(grid.length);
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
	@Override
	public String toString() {
		String borders = " ";
		final String line = "|";
		String preSpace = "";
		String game = "";
		for (int i = 1; i < Integer.toString(grid.length).length(); i++) {
			preSpace += " ";
		}
		game += "\n";
		for (int i = 0; i <= Integer.toString(grid.length).length(); i++) {
			game += " ";
			borders += " ";
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = Integer.toString(i).length(); j <= Integer.toString(grid.length).length(); j++) {
				game += " ";
				borders += "-";
			}
			game += i;
			for (int j = 0; j < Integer.toString(i).length(); j++) {
				borders += "-";
			}
		}
		game += "\n";
		game += borders;
		game += "\n";
		for (int i = 0; i < grid.length; i++) {
			for (int j = Integer.toString(i).length(); j < Integer.toString(grid.length).length(); j++) {
				game += " ";
			}
			game += i + line;
			game += " ";
			for (int j = 0; j < grid.length; j++) {
				game += preSpace;
				if (grid[i][j].getIsFlagged()) {
					game += ANSI_RED + grid[i][j] + ANSI_RESET;
				} else if (grid[i][j].getIsOpen()) {
					if (grid[i][j].getIsMine()) {
						game += ANSI_RED + grid[i][j] + ANSI_RESET;
					} else {
						game += ANSI_BLUE + grid[i][j] + ANSI_RESET;
					}
				} else {
					game += grid[i][j];
				}
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
				if (grid[i][j].getIsMine()) {
					gameState = GameState.LOST;
					exposeMines();
				} else {
					correctMoves++;
					if (correctMoves >= grid.length * grid.length) {
						gameState = GameState.WON;
					} else if (grid[i][j].getSymbol() == '0') {
						findAllZeros(i, j);
					}
				}
			}
		}
		return spot;
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
			if (grid[i][j].getIsMine()) {
				correctMoves++;
				if (correctMoves >= grid.length * grid.length) { 
					gameState = GameState.WON;
				}
			}
		}
	}

	/**
	 * Removes a flag on a cell that has one
	 */
	public void deflagBox(int i, int j) {
		if (!grid[i][j].getIsFlagged()) {
			System.out.println("That box does not have a flag on it!");
		} else {
			grid[i][j].setFlagged(false);
			if (grid[i][j].getIsMine()) {
				correctMoves--;
			}
		}
	}

	/**
	 * Looks for surrounding numbers near the cell and opens them, repeats when find another zero
	 */
	public void findAllZeros(int row, int col) { //TODO: throw exception
        Queue<Integer> bfs = new LinkedList<Integer>();
        if (grid[row][col].getSymbol() == '0') {
			bfs.add(row * grid.length + col);
			while (!bfs.isEmpty()) {
				row = bfs.peek() / grid.length;
				col = bfs.poll() % grid.length;
				if (grid[row][col].getSymbol() == '0') {
					for (int i = row - 1; i <= row + 1; i++) {
						for (int j = col - 1; j <= col + 1; j++) {
							if (i >= 0 && i < grid.length && j >= 0 && j < grid.length && !grid[i][j].getIsFlagged() && !grid[i][j].getIsMine() && !grid[i][j].getIsOpen()) {
								correctMoves++;
								if (correctMoves >= grid.length * grid.length) {
									gameState = GameState.WON;
								}
								if (grid[i][j].getSymbol() == '0') {
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

	private void exposeMines() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				if (grid[i][j].getIsMine()) {
					grid[i][j].open();
				}
			}
		}
	}

	/**
	 * Updates the state of the game
	 */
	public GameState getGameState() {
	    return gameState;
    }

	/**
	 * Finds the current condition of a cell
	 */
	public char getCell(int i, int j) {
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
