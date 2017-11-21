package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * MineGUI.java is a base that calls all GUI objects and handles tasks
 * such as pausing the game, creating the GUI, making the escape key functional,
 * and allowing for a new game.
 *
 * @author David Acevedo
 * @version 2015/03/04 for lab07, cs56, W15
 *
 * @author Ryan Wiener
 */

public class MineGUI {

    private static MainMenu mainMenu;
	private static GameFrame gameFrame;
	private static HelpScreen helpScreen;
	private static LeaderboardFrame leaderboardFrame;

	/**
	 * main function called upon start up
	 * @param args args passed into program call
	 */
	public static void main (String[] args) {
		DBConnector.init();
		System.out.println(DBConnector.getTopTenEasy());
		mainMenu = new MainMenu();
		helpScreen = new HelpScreen();
		leaderboardFrame = new LeaderboardFrame();
	}

	/**
	 * creates a new game and displays the game frame
	 * @param difficulty the difficulty of the game to be played
	 */
	public static void newGame(Constants.Difficulty difficulty) {
        if (gameFrame != null) {
			gameFrame.dispose();
		}
		try {
			gameFrame = new GameFrame(difficulty);
			mainMenu.setVisible(false);
			gameFrame.setVisible(true);
		} catch (IOException | ClassNotFoundException e) {
        	JOptionPane.showMessageDialog(null, "There is no previous game to load", "No previous game", JOptionPane.DEFAULT_OPTION);
		}
	}

	/**
	 * return to main menu from either the help screen or game frame
	 */
	public static void goToMainMenu() {
		mainMenu.refreshHighScoreChart();
		if (gameFrame != null) {
			gameFrame.getGrid().save();
			gameFrame.dispose();
			gameFrame = null;
		}
		helpScreen.setVisible(false);
		mainMenu.setVisible(true);
	}

	/**
	 * set visibility of help screen
	 * @param visible boolean indicating whether help screen should be visible or not
	 */
	public static void setHelpScreenVisible(boolean visible) {
		helpScreen.setVisible(visible);
	}
	public static void setLeaderboardVisible(boolean visible) { leaderboardFrame.setVisible(visible); }

	/**
	 * Display prompt asking user to confirm that they want to quit
	 */
	public static void quitPrompt() {
		JFrame currFrame = getCurrentFrame();
		int response = JOptionPane.showConfirmDialog(currFrame, "Are you sure you want to quit the game?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			if (currFrame instanceof GameFrame) {
				((GameFrame) currFrame).getGrid().save();
			}
			System.out.println("Closing...");
			System.exit(0);
		} else {
			currFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}

	/**
	 * Display prompt asking user whether they want to overwrite their previous game with a new one
	 * @return boolean indicating true if user wants to overwrite and false otherwise
	 */
	public static boolean overwriteSavePrompt() {
		JFrame currFrame = getCurrentFrame();
		int response = JOptionPane.showConfirmDialog(currFrame, "Are you sure you want to do this? This will delete previous save data", "Overwriting Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			return true;
		} else {
			currFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			return false;
		}
	}

	/**
	 * get current frame that the user is looking at
	 * @return the current fram that the user has open (if help screen is open it will always be returned even if not selected
	 */
	public static JFrame getCurrentFrame() {
		if (helpScreen.isVisible()) {
			return helpScreen;
		} else if(leaderboardFrame.isVisible()) {
			return leaderboardFrame;
		} else if (gameFrame != null && gameFrame.isVisible()) {
			return gameFrame;
		} else {
			return mainMenu;
		}
	}
}
