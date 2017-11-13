package edu.ucsb.cs56.projects.games.minesweeper;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

/**
 * Created by ryanwiener on 11/3/17.
 */

public class MainMenu extends JFrame {

	private JButton quitMine;
	private JButton easyGame;
	private JButton medGame;
	private JButton hardGame;
	private JButton load; //loads game
	private JButton help;    //Main Menu Help Button
	private JScrollPane scroller;
	private JLabel highScore; // this label status displays the local high score.
	private JLabel highScoreList;

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
		highScore = new JLabel("Leaderboards: "); // added another JLabel
		highScoreList = new JLabel(getHighScores());
		scroller = new JScrollPane(highScoreList);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		easyGame.addActionListener((ActionEvent e) -> {
			if (MineGUI.overwriteSavePrompt()) {
				MineGUI.newGame(Grid.Difficulty.EASY);
			}
		});
		medGame.addActionListener((ActionEvent e) -> {
			if (MineGUI.overwriteSavePrompt()) {
				MineGUI.newGame(Grid.Difficulty.MEDIUM);
			}
		});
		hardGame.addActionListener((ActionEvent e) -> {
			if (MineGUI.overwriteSavePrompt()) {
				MineGUI.newGame(Grid.Difficulty.HARD);
			}
		});
		help.addActionListener((ActionEvent e) -> { MineGUI.setHelpScreenVisible(true); });
		load.addActionListener((ActionEvent e) -> { MineGUI.newGame(Grid.Difficulty.LOAD); });
		quitMine.addActionListener((ActionEvent e) -> { MineGUI.quitPrompt(); });
		menu.add(easyGame);
		menu.add(medGame);
		menu.add(hardGame);
		menu.add(load);
		menu.add(help);
		menu.add(quitMine);
		menu.add(highScore); // add new highScore feature to frame.
		menu.add(scroller);
		setVisible(true);
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

	public void refreshHighScoreChart() {
		highScoreList.setText(getHighScores());
	}

	public String getHighScores() {
		ArrayList<Map<String, String>> highScores = DBConnector.getTopTenEasy();
		String display = "";
		for (Map<String, String> row : highScores) {
			display += row.get("name") + " ";
			display += row.get("score") + " ";
			display += Grid.Difficulty.values()[Integer.parseInt(row.get("difficulty"))] + " ";
			display += row.get("attime") + " ";
		}
		return  display;
	}
}
