package edu.ucsb.cs56.projects.games.minesweeper.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;

import edu.ucsb.cs56.projects.games.minesweeper.constants.Constants;
import edu.ucsb.cs56.projects.games.minesweeper.database.DBConnector;
import edu.ucsb.cs56.projects.games.minesweeper.gamelogic.Grid;
import edu.ucsb.cs56.projects.games.minesweeper.gui.MineGUI;

/** The window that displays the game in the GUI
 * @author Ryan Wiener
 * @author Kate Perkins
 */

public class GameFrame extends JFrame {

	private static final Color ZERO = new Color(0, 0, 128);
	private static final Color NUMBER = new Color(0, 100, 0);
	private Grid game;
	private JButton[][] buttons;
	private JTextField timeDisplay;
	private JButton refresh;
	private JButton mainMenu;
	private JButton quitMine;
	private JButton inGameHelp;
	private JButton flagBtn;
	private JPanel grid;

	/**
	 * Constructs game from the given difficulty
	 * @param difficulty difficulty of the game to be played
	 * @throws IOException if loading fails
	 * @throws ClassNotFoundException if loading fails
	 */
	public GameFrame(Constants.Difficulty difficulty) throws IOException, ClassNotFoundException {
		super(); // is this line necessary?  what does it do?
		setSize(650, 600);
		if (difficulty == Constants.Difficulty.LOAD) {
			game = Grid.loadGame();
		} else {
			game = new Grid(difficulty);  // the Interface game
		}
		JToolBar toolbar = new JToolBar("In-game toolbar");
		createToolbar(toolbar);
		getContentPane().add(toolbar, BorderLayout.NORTH); //puts the game toolbar at the top of the screen
		grid = new JPanel();
		grid.setLayout(new GridLayout(game.getSize() ,0));
		buttons = new JButton[game.getSize()][game.getSize()];
		for (int i = 0; i < game.getSize(); i++) {
			for (int j = 0; j < game.getSize(); j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].addMouseListener(new ButtonListener(i, j));
				buttons[i][j].setFont(new Font("sansserif", Font.BOLD, 10));
				buttons[i][j].setIcon(null);
				grid.add(buttons[i][j]);
			}
		}
		if (difficulty == Constants.Difficulty.LOAD) {
			refresh();
		}
		getContentPane().add(grid);
		getContentPane().addComponentListener(new SizeListener());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Get x coordinate of a given grid button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @param i row of grid button
	 * @param j column of grid button
	 * @return x coordinate of grid button
	 */
	public int getGridButtonX(int i, int j) {
		return grid.getX() + buttons[i][j].getX() + 10;
	}

	/**
	 * Get y coordinate of a given grid button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @param i row of grid button
	 * @param j column of grid button
	 * @return y coordinate of grid button
	 */
	public int getGridButtonY(int i, int j) {
		return grid.getY() + buttons[i][j].getY();
	}

	/**
	 * helper function for adding scores to database
	 * @param name name of user
	 * @param time how long it took the user to win
	 */
	public void saveHighest(String name, int time) {
		DBConnector.addScore(name, time, game.getDifficulty().ordinal());
	}

	/**
	 * Initializes toolbar at the top of the screen
	 * @param toolbar toolbar to be initialized
	 */
	public void createToolbar(JToolBar toolbar) {
		//make buttons
		refresh = new JButton("Reset Game");
		mainMenu = new JButton("Main Menu");
		quitMine = new JButton("Quit Minesweeper");
		inGameHelp = new JButton("Help");
		flagBtn = new JButton("Flag"); //new ImageIcon("resource/images/flag.png"));
		timeDisplay = new JTextField(game.getGameTime());
		timeDisplay.setColumns(4);
		timeDisplay.setEditable(false);
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				timeDisplay.setText(Integer.toString(game.getGameTime()));
			}
		}, 0, 1);
		refresh.addActionListener((ActionEvent e) -> {
			if (MineGUI.overwriteSavePrompt()) {
				resetGame();
			}
		});
		mainMenu.addActionListener((ActionEvent e) -> {
			game.save();
			MineGUI.goToMainMenu();
		});
		inGameHelp.addActionListener((ActionEvent e) -> { MineGUI.setHelpScreenVisible(true); });
		quitMine.addActionListener((ActionEvent e) -> {
			game.save();
			MineGUI.quitPrompt();
		});
		flagBtn.addActionListener((ActionEvent e) -> { flag(); });
		toolbar.add(flagBtn);
		toolbar.add(mainMenu);
		toolbar.add(refresh);
		toolbar.add(inGameHelp);
		toolbar.add(quitMine);
		toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		toolbar.add(timeDisplay);
		toolbar.setFloatable(false);
	}

	/**
	 * Get x coordinate of a given refresh button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @return x coordinate of refresh button
	 */
	public int getRefreshX() {
		return refresh.getX();
	}

	/**
	 * Get y coordinate of a given refresh button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @return y coordinate of refresh button
	 */
	public int getRefreshY() {
		return refresh.getY();
	}

	/**
	 * Get x coordinate of a given flag button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @return x coordinate of flag button
	 */
	public int getFlagBtnX() {
		return flagBtn.getX();
	}

	/**
	 * Get y coordinate of a given flag button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @return y coordinate of flag button
	 */
	public int getFlagBtnY() {
		return flagBtn.getY();
	}

	/**
	 * Get x coordinate of a given main menu button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @return x coordinate of main menu button
	 */
	public int getMainMenuX() {
		return mainMenu.getX();
	}

	/**
	 * Get y coordinate of a given main menu button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @return y coordinate of main menu button
	 */
	public int getMainMenuY() {
		return mainMenu.getY();
	}

	/**
	 * Get x coordinate of a given help button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @return x coordinate of help button
	 */
	public int getHelpX() {
		return inGameHelp.getX();
	}

	/**
	 * Get y coordinate of a given help button
	 * used for GUITest to make Robot class move mouse button to correct coordinates
	 * @return y coordinate of help button
	 */
	public int getHelpY() {
		return inGameHelp.getY();
	}

	/**
	 * switch flag buttons selected property
	 * if flag button is selected then left clicking flags boxes rather than opening them
	 */
	public void flag() {
		flagBtn.setSelected(!flagBtn.isSelected());
	}

	/**
	 * plays a sound from the resources
	 * @param dir name of the sound file to be played
	 */
	public void playSound(String dir) {
		if (dir != null) {
			try {
				File resource = new File("resources" + dir);
				AudioInputStream audioInputStream;
				if (resource.exists()) {
					audioInputStream = AudioSystem.getAudioInputStream(resource.getAbsoluteFile());
				} else {
					audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(dir));
				}
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ImageIcon getImageIcon(String resource) {
	    File local = new File("resources/" + resource);
	    ImageIcon icon;
	    if (local.exists()) {
			icon = new ImageIcon(local.getPath());
		} else {
	    	icon = new ImageIcon(getClass().getResource(resource));
		}
		Image img = icon.getImage();
		//resize icon to fit button
		Image newImg = img.getScaledInstance(grid.getWidth() / game.getSize() - 10, grid.getHeight() / game.getSize() - 10,  java.awt.Image.SCALE_DEFAULT) ;
		return new ImageIcon(newImg);
	}

	/**
	 * Refreshed the grid to coincide with the game
	 */
	public void refresh() {
		int fontSize = buttons[0][0].getSize().height / 2;
		if (buttons[0][0].getSize().height / 2 > buttons[0][0].getSize().width / 4) {
			fontSize = buttons[0][0].getSize().width / 4;
		}
		for (int i = 0; i < game.getSize(); i++) {
			for (int j = 0; j < game.getSize(); j++) {
				buttons[i][j].setFont(new Font("sansserif", Font.BOLD, fontSize));
				if (game.isOpen(i, j)) {
					if (game.isMine(i, j)) {
						buttons[i][j].setIcon(getImageIcon("/images/mine.jpg"));
					} else {
						if (game.getCell(i, j) == '0') {
							buttons[i][j].setForeground(ZERO);
						} else {
							buttons[i][j].setForeground(NUMBER);
						}
						buttons[i][j].setText(Character.toString(game.getCell(i, j)));
					}
				} else if (game.isFlag(i, j)) {
					buttons[i][j].setIcon(getImageIcon("/images/flag.png"));
				} else {
					buttons[i][j].setIcon(null);
					buttons[i][j].setText("");
				}
			}
		}
	}

	/**
	 * Get the Grid object (the game itself)
	 * @return the underlying Grid object (the game)
	 */
	public Grid getGrid(){
		return game;
	}

	/**
	 * Reset the game with the same difficulty
	 */
	public void resetGame() {
		MineGUI.newGame(game.getDifficulty());
	}

	/**
	 * inner class, reponds to resizing of component to resize font
	 */
	class SizeListener implements ComponentListener {

		@Override
		public void componentHidden(ComponentEvent e) {
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}

		/**
		 * refresh the screen when resizing the frame
		 * @param e the ComponentEvent object (not used)
		 */
		@Override
		public void componentResized(ComponentEvent e) {
			refresh();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
		}
	} // class SizeListener

	/**
	 * Inner Class, responds to the event source.
	 */
	class ButtonListener extends MouseAdapter {

		private int row;
		private int col;

		/**
		 * Constructs ButtonListener
		 * @param i row value of the ButtonListener
		 * @param j column value of the ButtonListener
		 */
		public ButtonListener(int i, int j) {
			super();
			row = i;
			col = j;
		}

		/**
		 * Places player's symbol on button, checks for a winner or tie
		 * @param event when a button is clicked
		 */
		public void mouseReleased(MouseEvent event) {
			String soundName = null;
			if (game.getGameState() == Constants.GameState.PLAYING) {
				if(event.getButton() == MouseEvent.BUTTON1 && !game.isFlag(row, col) && !game.isOpen(row, col) && !flagBtn.isSelected()){
					//if you left click and the button is available (not a flag and not already opened)
					char result = game.searchBox(row, col);
					if (result == 'X') {
						soundName = "/sounds/explosion.wav";
						// will update gui when lost
					} else {
						soundName = "/sounds/clicked.wav";
						if (result == '0') {
							// need to update all cells since they opened up
							refresh();
						} else {
							// only need to update the current cell
							buttons[row][col].setText(Character.toString(game.getCell(row, col)));
							buttons[row][col].setForeground(NUMBER);
						}
					}
				} else if (event.getButton() == MouseEvent.BUTTON1 && (game.isFlag(row, col) | game.isOpen(row, col)) && !flagBtn.isSelected()) {
					// If you left click and the button is a flag or has been opened
					soundName = "/sounds/userError.wav";
				} else if (event.getButton() == MouseEvent.BUTTON3 || flagBtn.isSelected()) {
					// If you right click or have flag button selected
					if (game.isFlag(row, col)) {
						game.deflagBox(row, col);
						buttons[row][col].setIcon(null);
					} else if (!game.isOpen(row, col)) {
						soundName = "/sounds/place_flag.wav";
						game.flagBox(row, col);
						buttons[row][col].setIcon(getImageIcon("/images/flag.png"));
					} else {
						soundName = "/sounds/userError.wav";
					}
				} else if (event.getButton() == MouseEvent.BUTTON1 && game.isOpen(row, col)){
					soundName = "/sounds/userError.wav";
				} else if (event.getButton() == MouseEvent.BUTTON2) {
					if (game.searchSurrounding(row, col)) {
						soundName = "/sounds/clicked.wav";
						refresh();
					} else {
						soundName = "/sounds/userError.wav";
					}
				}
				playSound(soundName);
				if (game.getGameState() == Constants.GameState.LOST) {
					// display mines
					refresh();
					int response = JOptionPane.showOptionDialog(null, "You lose! Press 'Reset Game' to start a new game.", "Defeat!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
					if (response == JOptionPane.YES_OPTION) {
						MineGUI.goToMainMenu();
					} else {
						resetGame();
					}
				} else if (game.getGameState() == Constants.GameState.WON) {
					soundName = "resources/sounds/win.wav";
					playSound(soundName);
					if (DBConnector.isConnected()) {
						String user = JOptionPane.showInputDialog(null, "You win! Enter your name for the leaderboard.", "Victory!", JOptionPane.QUESTION_MESSAGE);
						if (user != null) {
							saveHighest(user, game.getGameTime());
						}
					}
					int response = JOptionPane.showOptionDialog(null, "You win! Press 'Reset Game' to start a new game.", "Victory!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
					if (response == JOptionPane.YES_OPTION) {
						MineGUI.goToMainMenu();
					} else if (response == JOptionPane.INFORMATION_MESSAGE) {
						resetGame();
					} else {
						//do nothing
					}
				}
			}
		}
	} // class ButtonListener
}
