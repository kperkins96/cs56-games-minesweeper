package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

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

	private int gameTime;
	private transient Timer timer;
	private GridComponent[][] grid;
	private Constants.GameState gameState;
	private Constants.Difficulty difficulty;
	private int correctMoves;

	/**
	 * Default constructor for objects of class GUIGrid
	 */

	public Grid() { this(Constants.Difficulty.EASY); }

	public Grid(Constants.Difficulty difficulty) {
		gameState = Constants.GameState.PLAYING;
		this.difficulty = difficulty;
		correctMoves = 0;
		grid = new GridComponent[Constants.getGridSize(difficulty)][Constants.getGridSize(difficulty)];
		setZero();
		if (difficulty == Constants.Difficulty.TEST) {
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
		startTimer();
	}

	public void deleteSave() {
		File file = new File("MyGame.ser");
		file.delete();
	}

	public void endGame() {
		stopTimer();
		deleteSave();
	}

	public void startTimer() {
		timer = new Timer();
		timer.schedule(new Clock(), 0, 1);
	}

	public void stopTimer() {
		timer.cancel();
		timer.purge();
	}

	public int getGameTime() {
		return gameTime;
	}

	/**
	 *	Getter for size
	 */
	public int getSize() {
		return grid.length;
	}

	public Constants.Difficulty getDifficulty() {
		return difficulty;
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
					game += Constants.ANSI_RED + grid[i][j] + Constants.ANSI_RESET;
				} else if (grid[i][j].getIsOpen()) {
					if (grid[i][j].getIsMine()) {
						game += Constants.ANSI_RED + grid[i][j] + Constants.ANSI_RESET;
					} else {
						game += Constants.ANSI_BLUE + grid[i][j] + Constants.ANSI_RESET;
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
					gameState = Constants.GameState.LOST;
					endGame();
					exposeMines();
				} else {
					correctMoves++;
					if (correctMoves >= grid.length * grid.length) {
						gameState = Constants.GameState.WON;
						endGame();
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
					gameState = Constants.GameState.WON;
                    endGame();
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
									gameState = Constants.GameState.WON;
                                    endGame();
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
	public Constants.GameState getGameState() {
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
	
	boolean searchSurrounding(int row, int col) {
		int numFlags = 0;
		for(int i = row - 1; i <= row + 1; i++) {
			for(int j = col - 1; j <= col + 1; j++) {
				if ((i >= 0 && i < grid.length) && (j >= 0 && j < grid.length )) {
					if(grid[i][j].getIsFlagged())
						numFlags++;
				}
			}
		}
		if(Integer.toString(numFlags).equals(Character.toString(grid[row][col].getSymbol())) && !grid[row][col].getIsFlagged()) {
			for(int i = row - 1; i <= row + 1; i++) {
				for(int j = col - 1; j <= col + 1; j++) {
					if ((i >= 0 && i < grid.length) && (j >= 0 && j < grid.length )) {
						grid[i][j].open();
					}
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	public static Grid loadGame() throws IOException, ClassNotFoundException {
		FileInputStream fileStream = new FileInputStream("MyGame.ser");
		ObjectInputStream os = new ObjectInputStream(fileStream);
		Object one;
		one = os.readObject();
		os.close();
		Grid g = (Grid) one;
		g.startTimer();
		return g;
	}

	public void save() {
		try {
			FileOutputStream fileStream = new FileOutputStream("MyGame.ser");
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(this);
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public class Clock extends TimerTask {
		private long currClock;
		private long endClock;
		private long elapse;
		private final int NANO = 1000000000;
		private long sec;
		private int leftOver;

		public Clock(){
			leftOver = gameTime;
			gameTime = 0;
			currClock = System.nanoTime();
		}

		public void run(){
			endClock = System.nanoTime();
			elapse = endClock - currClock;
			sec = Math.floorDiv(elapse, NANO);
			gameTime = (int)sec + leftOver;
		}
	} // class Clock
}
