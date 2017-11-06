package edu.ucsb.cs56.projects.games.minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ryanwiener on 11/6/17.
 */

public final class ActionListenerFactory {

	public enum Purpose {
		MAIN_MENU,
		QUIT,
		RESET,
		HELP,
		LOAD,
		SAVE,
		FLAG,
		EASY_GAME,
		MED_GAME,
		HARD_GAME
	}

	private ActionListenerFactory() {}

	public static ActionListener buildActionListener(Purpose p) {
		switch (p) {
			case MAIN_MENU:
				return (ActionEvent e) -> { MineGUI.goToMainMenu(); };
			case QUIT:
				return (ActionEvent e) -> { MineGUI.quitPrompt(); };
			case RESET:
			    return (ActionEvent e) -> {
					if (MineGUI.getCurrentFrame() instanceof GameFrame) {
						((GameFrame) MineGUI.getCurrentFrame()).resetGame();
					}
				};
			case HELP:
			    return (ActionEvent e) -> { MineGUI.setHelpScreenVisible(true); };
			case LOAD:
				return (ActionEvent e) -> { MineGUI.newGame(-2); };
			case SAVE:
				return (ActionEvent e) -> {
					if (MineGUI.getCurrentFrame() instanceof GameFrame) {
						((GameFrame) MineGUI.getCurrentFrame()).save();
					}
				};
			case FLAG:
				return (ActionEvent e) -> {
					if (MineGUI.getCurrentFrame() instanceof GameFrame) {
						((GameFrame) MineGUI.getCurrentFrame()).flag();
					}
				};
			case EASY_GAME:
				return (ActionEvent e) -> {
					if (MineGUI.overwriteSavePrompt()) {
						MineGUI.newGame(0);
					}
				};
			case MED_GAME:
				return (ActionEvent e) -> {
					if (MineGUI.overwriteSavePrompt()) {
						MineGUI.newGame(1);
					}
				};
			case HARD_GAME:
				return (ActionEvent e) -> {
					if (MineGUI.overwriteSavePrompt()) {
						MineGUI.newGame(2);
					}
				};
			default:
				return (ActionEvent e) -> {};
		}
	}
}
