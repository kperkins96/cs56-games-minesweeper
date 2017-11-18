package edu.ucsb.cs56.projects.games.minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Image;
import javax.imageio.ImageIO; 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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

/**
 * Created by ryanwiener on 11/3/17.
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

	GameFrame(Constants.Difficulty difficulty) throws IOException, ClassNotFoundException {
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
	}

	public int getGridButtonX(int i, int j) {
		return grid.getX() + buttons[i][j].getX() + 10;
	}

	public int getGridButtonY(int i, int j) {
		return grid.getY() + buttons[i][j].getY();
	}

	public void saveHighest(String name, int time) {
		DBConnector.addScore(name, time, game.getDifficulty().ordinal());
	}

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
		mainMenu.addActionListener((ActionEvent e) -> { MineGUI.goToMainMenu(); });
		inGameHelp.addActionListener((ActionEvent e) -> { MineGUI.setHelpScreenVisible(true); });
		quitMine.addActionListener((ActionEvent e) -> { MineGUI.quitPrompt(); });
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

	public int getRefreshX() {
		return refresh.getX();
	}

	public int getRefreshY() {
		return refresh.getY();
	}
	
	public int getFlagBtnX() {
		return flagBtn.getX();
	}
	
	public int getFlagBtnY() {
		return flagBtn.getY();
	}

	public int getMainMenuX() {
		return mainMenu.getX();
	}

	public int getMainMenuY() {
		return mainMenu.getY();
	}

	public int getHelpX() {
		return inGameHelp.getX();
	}

	public int getHelpY() {
		return inGameHelp.getY();
	}

	public void flag() {
		flagBtn.setSelected(!flagBtn.isSelected());
	}

	public void playSound(String dir) {
		if (dir != null) {
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(dir).getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
	}

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
						ImageIcon icon = new ImageIcon("resources/images/mine.jpg");
						Image img = icon.getImage();
						//resize icon to fit button
						Image newImg = img.getScaledInstance(grid.getWidth() / game.getSize() - 10, grid.getHeight() / game.getSize() - 10,  java.awt.Image.SCALE_DEFAULT) ;
						icon = new ImageIcon(newImg);
						buttons[i][j].setIcon(icon);
					} else {
						if (game.getCell(i, j) == '0') {
							buttons[i][j].setForeground(ZERO);
						} else {
							buttons[i][j].setForeground(NUMBER);
						}
						buttons[i][j].setText(Character.toString(game.getCell(i, j)));
					}
				} else if (game.isFlag(i, j)) {
					ImageIcon icon = new ImageIcon("resources/images/flag.png");
					Image img = icon.getImage();
					//resize icon to fit button
					Image newImg = img.getScaledInstance(grid.getWidth() / game.getSize() - 10, grid.getHeight() / game.getSize() - 10,  java.awt.Image.SCALE_DEFAULT) ;
					icon = new ImageIcon(newImg);
					buttons[i][j].setIcon(icon);
				} else {
					buttons[i][j].setIcon(null);
					buttons[i][j].setText("");
				}
			}
		}
	}

	public Grid getGrid(){
		return game;
	}

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

		public ButtonListener(int i, int j) {
			super();
			row = i;
			col = j;
		}

		/**
		 Places player's symbol on button, checks for a winner or tie
		 @param event when a button is clicked
		 */
		public void mouseReleased(MouseEvent event) {
			String soundName = null;
			if (game.getGameState() == Constants.GameState.PLAYING) {
				if(event.getButton() == MouseEvent.BUTTON1 && !game.isFlag(row, col) && !game.isOpen(row, col) && !flagBtn.isSelected()){
					//if you left click and the button is available (not a flag and not already opened)
					char result = game.searchBox(row, col);
					if (result == 'X') {
						soundName = "resources/sounds/explosion.wav";
						// will update gui when lost
					} else {
						soundName = "resources/sounds/clicked.wav";
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
					soundName = "resources/sounds/userError.wav";
				} else if (event.getButton() == MouseEvent.BUTTON3 || flagBtn.isSelected()) {
					// If you right click or have flag button selected
					if (game.isFlag(row, col)) {
						game.deflagBox(row, col);
						buttons[row][col].setIcon(null);
					} else if (!game.isOpen(row, col)) {
						soundName = "resources/sounds/place_flag.wav";
						game.flagBox(row, col);
						ImageIcon icon = new ImageIcon("resources/images/flag.png");
						Image img = icon.getImage();
						//resize icon to fit button
						Image newImg = img.getScaledInstance(grid.getWidth() / game.getSize() - 10, grid.getHeight() / game.getSize() - 10,  java.awt.Image.SCALE_DEFAULT) ;
						icon = new ImageIcon(newImg);
						buttons[row][col].setIcon(icon);
					} else {
						soundName = "resources/sounds/userError.wav";
					}
				} else if (event.getButton() == MouseEvent.BUTTON1 && game.isOpen(row, col)){
					soundName = "resources/sounds/userError.wav";
				} else if (event.getButton() == MouseEvent.BUTTON2) {
					if (game.searchSurrounding(row, col)) {
						soundName = "resources/sounds/clicked.wav";
						refresh();
					} else {
						soundName = "resources/sounds/userError.wav";
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
					String user = JOptionPane.showInputDialog(null, "You win! Enter your name for the leaderboard.", "Victory!", JOptionPane.QUESTION_MESSAGE);
					if (user != null) {
						int response = JOptionPane.showOptionDialog(null, "You win! Press 'Reset Game' to start a new game.", "Victory!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
						if (response == JOptionPane.YES_OPTION) {
							saveHighest(user, game.getGameTime());
							MineGUI.goToMainMenu();
						} else if (response == JOptionPane.INFORMATION_MESSAGE) {
							saveHighest(user, game.getGameTime());
							resetGame();
						} else {
							//do nothing
						}
					}
				}
			}
		}
	} // class ButtonListener
}
