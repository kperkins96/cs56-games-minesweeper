package edu.ucsb.cs56.projects.games.minesweeper;

import javax.swing.JFrame;

/** MineGUI.java is a base that calls all GUI objects and handles tasks
 such as pausing the game, creating the GUI, making the escape key functional,
 and allowing for a new game.

 @author David Acevedo
 @version 2015/03/04 for lab07, cs56, W15
 @see MineGUI
 */


public class MineGUI {

    private static MainMenu mainMenu;
	private static GameFrame gameFrame;

	public static void main (String[] args) {
		mainMenu = new MainMenu();
		mainMenu.setVisible(true);
	}

	public static void newGame(int difficulty) {
        mainMenu.setVisible(false);
        if (gameFrame != null) {
			gameFrame.dispose();
		}
		gameFrame = new GameFrame(difficulty);
		gameFrame.setVisible(true);
	}

	public static void goToMainMenu() {
		mainMenu.refreshHighScoreChart();
		if (gameFrame != null) {
			gameFrame.save();
			gameFrame.dispose();
			gameFrame = null;
		}
		mainMenu.setVisible(true);
	}

	public static void helpBack() {
		if (gameFrame != null) {
			gameFrame.setVisible(true);
		} else {
			mainMenu.setVisible(true);
		}
	}

	/* static getter for JUnit testing
	 */
	public static JFrame getCurrentFrame() {
		if (mainMenu.isVisible()) {
			return mainMenu;
		} else if (gameFrame != null && gameFrame.isVisible()) {
			return gameFrame;
		} else {
			return new HelpScreen();
		}
	}
}
