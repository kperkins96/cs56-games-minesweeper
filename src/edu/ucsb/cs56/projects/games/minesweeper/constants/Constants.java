package edu.ucsb.cs56.projects.games.minesweeper.constants;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Supplies universal enums and finals for classes to share between each other
 * @author Ryan Wiener
 */

public class Constants {

	private Constants() {}

	/**
	 * The game state of the grid
	 */
	public enum GameState {
		PLAYING,
		LOST,
		WON
	}

	/**
	 * The current state of the application for the text game
	 */
	public enum ApplicationState {
		MAINMENU,
		GAME,
		LEADERBOARD
	}

	/**
	 * The difficulty of the game
	 */
	public enum Difficulty {
		TEST,
		EASY,
		MEDIUM,
		HARD,
		LOAD,
	}

	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_RESET = "\u001B[0m";

	private static final Map<Difficulty, Integer> gridSizes = new HashMap<Difficulty, Integer>();

	/**
	 * links the difficulty enum to grid size
	 * @param difficulty Difficulty enum member that you want the grid size of
	 * @return size of grid for the passed in difficulty
	 */
	public static int getGridSize(Difficulty difficulty) {
		if (gridSizes.size() == 0) {
			gridSizes.put(Difficulty.TEST, 4);
			gridSizes.put(Difficulty.EASY, 10);
			gridSizes.put(Difficulty.MEDIUM, 15);
			gridSizes.put(Difficulty.HARD, 20);
		}
		return gridSizes.get(difficulty);
	}

	/**
	 * disable error outputs from DBConnector if not able to connect to the database
	 */
	public static void disableErrorOutput() {
		/*		System.setErr(new PrintStream(new OutputStream() {
			@Override
			public void write(int i) throws IOException {

			}
			})); */
	}
}
