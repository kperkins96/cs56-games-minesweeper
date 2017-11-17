package edu.ucsb.cs56.projects.games.minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryanwiener on 11/16/17.
 */

public class Constants {

    private Constants() {}

    public enum GameState {
    	PLAYING,
		LOST,
		WON
	}

	public enum ApplicationState {
    	MAINMENU,
		GAME,
		LEADERBOARD
	}

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

	public static int getGridSize(Difficulty difficulty) {
		if (gridSizes.size() == 0) {
			gridSizes.put(Difficulty.TEST, 4);
			gridSizes.put(Difficulty.EASY, 10);
			gridSizes.put(Difficulty.MEDIUM, 15);
			gridSizes.put(Difficulty.HARD, 20);
		}
		return gridSizes.get(difficulty);
	}
}
