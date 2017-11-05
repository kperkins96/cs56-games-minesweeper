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

	private JButton mainMenu;
	private JButton quitMine;
	private JButton inGameHelp;
	private JButton refresh;
	private JButton easyGame;
	private JButton medGame;
	private JButton hardGame;
	private JButton load; //loads game
	private JButton help;    //Main Menu Help Button
	private JButton save;
	public JFrame frame;    //The frame is where all the good stuff is displayed e.g. Everything
	private JPanel menu;    //Menu Panel, initial panel at initial creation of the game e.g. Main Menu
	private JPanel game;    //Game Panel, where the game is played
	private boolean inUse; //if game is started and in use
	private JTextField Time;
	private int timeTBPos;
	private Timer timer;
	public String globalTE;
	private JScrollPane scroller;
	private JLabel highScore; // this label status displays the local high score.
	private JLabel highScoreList;
	public String User;
	private int count;

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
		addActionListener(easyGame, "New Easy Game");
		addActionListener(medGame, "New Medium Game");
		addActionListener(hardGame, "New Hard Game");
		addActionListener(help, "Help");
		addActionListener(load, "Load");
		addActionListener(quitMine, "Quit Minesweeper");
		menu.add(easyGame);
		menu.add(medGame);
		menu.add(hardGame);
		menu.add(load);
		menu.add(help);
		menu.add(quitMine);
		menu.add(highScore); // add new highScore feature to frame.
		menu.add(scroller);
		boolean inUse = false;
    }

	public void addActionListener(JButton button, String action) {
		if (action == "New Easy Game") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (overwriteSavePrompt()) {
						MineGUI.newGame(0);
					}
				}
			});
		} else if (action == "New Medium Game") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (overwriteSavePrompt()) {
						MineGUI.newGame(1);
					}
				}
			});
		} else if (action == "New Hard Game") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (overwriteSavePrompt()) {
						MineGUI.newGame(2);
					}
				}
			});
		} else if (action == "Quit Minesweeper") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					quitPrompt();
				}
			});
		} else if (action == "Help") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HelpScreen helpScreen = new HelpScreen();
					setVisible(false);
				}
			});
		} else if (action == "Load") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
                    MineGUI.newGame(-2);
				}
			});
		}
	}

	public void refreshHighScoreChart() {
		highScoreList.setText(getHighScores());
	}

	public String getHighScores() {
		int num = count;
		//System.out.println("Loading high scores onto mainframe");
		String score = "<html>";
		String line = "";
		try {
			File myFile = new File("HighScore.txt");
			FileReader filereader = new FileReader(myFile);
			BufferedReader reader = new BufferedReader(filereader);
			while ((line = reader.readLine()) != null) {
				//while(num > -1) {
				score += line + "<br>";
				//    num--;
				//}
			}
			reader.close();
			score += "</html>";
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// sort scores by smallest time
		return score;
	}

	public void quitPrompt() {
		int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit the game?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.out.println("Closing...");
			System.exit(0);
		} else {
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}

	public boolean overwriteSavePrompt() {
		int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to do this? This will delete previous save data", "Overwriting Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			return true;
		} else {
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			return false;
		}
	}
}
