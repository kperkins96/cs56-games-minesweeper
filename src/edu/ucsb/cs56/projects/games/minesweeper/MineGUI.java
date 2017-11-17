package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	private static HelpScreen helpScreen;

	public static void main (String[] args) {
		DBConnector.init();
		System.out.println(DBConnector.getTopTenEasy());
		mainMenu = new MainMenu();
		helpScreen = new HelpScreen();
	}

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

	public static void goToMainMenu() {
		mainMenu.refreshHighScoreChart();
		if (gameFrame != null) {
			gameFrame.save();
			gameFrame.dispose();
			gameFrame = null;
		}
		helpScreen.setVisible(false);
		mainMenu.setVisible(true);
	}

	public static void setHelpScreenVisible(boolean visible) {
		helpScreen.setVisible(visible);
	}

	public static void quitPrompt() {
		JFrame currFrame = getCurrentFrame();
		int response = JOptionPane.showConfirmDialog(currFrame, "Are you sure you want to quit the game?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			if (currFrame instanceof GameFrame) {
				((GameFrame) currFrame).save();
			}
			System.out.println("Closing...");
			System.exit(0);
		} else {
			currFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}

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

	public static JFrame getCurrentFrame() {
		if (helpScreen.isVisible()) {
			return helpScreen;
		} else if (gameFrame != null && gameFrame.isVisible()) {
			return gameFrame;
		} else {
			return mainMenu;
		}
	}
}
