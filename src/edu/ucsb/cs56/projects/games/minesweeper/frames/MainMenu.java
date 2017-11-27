package edu.ucsb.cs56.projects.games.minesweeper.frames;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import edu.ucsb.cs56.projects.games.minesweeper.constants.Constants;
import edu.ucsb.cs56.projects.games.minesweeper.gui.MineGUI;

/**
 * Main Menu JFrame
 * @author Ryan Wiener
 */

public class MainMenu extends JFrame {

	private JButton quitMine;
	private JButton easyGame;
	private JButton medGame;
	private JButton hardGame;
	private JButton load; //loads game
	private JButton help;    //Main Menu Help Button
	private JButton highScore; // this label status displays the local high score.

	/**
	 * Default Constructor for main menu
	 * @throws HeadlessException if no display
	 */
	public MainMenu() throws HeadlessException {
		super();
		setSize(650, 600);
		Container menu = getContentPane();
		menu.setLayout(new GridLayout(4, 0)); //our 2 section grid layout for our main menu
		quitMine = new JButton("Quit Minesweeper");
		easyGame = new JButton("New Easy Game");
		medGame = new JButton("New Medium Game");
		hardGame = new JButton("New Hard Game");
		help = new JButton("Help");
		load = new JButton("Load Last Game");
		highScore = new JButton("Leaderboards");
		easyGame.addActionListener((ActionEvent e) -> {
			if (MineGUI.overwriteSavePrompt()) {
				MineGUI.newGame(Constants.Difficulty.EASY);
			}
		});
		medGame.addActionListener((ActionEvent e) -> {
			if (MineGUI.overwriteSavePrompt()) {
				MineGUI.newGame(Constants.Difficulty.MEDIUM);
			}
		});
		hardGame.addActionListener((ActionEvent e) -> {
			if (MineGUI.overwriteSavePrompt()) {
				MineGUI.newGame(Constants.Difficulty.HARD);
			}
		});
		help.addActionListener((ActionEvent e) -> { MineGUI.setHelpScreenVisible(true); });
		load.addActionListener((ActionEvent e) -> { MineGUI.newGame(Constants.Difficulty.LOAD); });
		quitMine.addActionListener((ActionEvent e) -> { MineGUI.quitPrompt(); });
		highScore.addActionListener((ActionEvent e) -> {MineGUI.setLeaderboardVisible(true); });
		menu.add(easyGame);
		menu.add(medGame);
		menu.add(hardGame);
		menu.add(load);
		menu.add(help);
		menu.add(quitMine);
		menu.add(highScore); // add new highScore feature to frame.
		setVisible(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public int getEasyGameX() {
		return easyGame.getX();
	}

	public int getEasyGameY() {
		return easyGame.getY();
	}

	public int getMedGameX() {
		return medGame.getX();
	}

	public int getMedGameY() {
		return medGame.getY();
	}

	public int getHardGameX() {
		return hardGame.getX();
	}

	public int getHardGameY() {
		return hardGame.getY();
	}

	public int getLoadGameX() {
		return load.getX();
	}

	public int getLoadGameY() {
		return load.getY();
	}

	public int getHelpX() {
		return help.getX();
	}

	public int getHelpY() {
		return help.getY();
	}

	public int getLeaderBoardX() { return highScore.getX(); }

	public int getLeaderBoardY() { return highScore.getY(); }

}
