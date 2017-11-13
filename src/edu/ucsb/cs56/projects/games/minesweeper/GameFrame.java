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
	private String globalTE;
	private JTextField timeDisplay;
	private Timer timer;
	private JButton refresh;
	private JButton mainMenu;
	private JButton quitMine;
	private JButton inGameHelp;
	private JButton flagBtn;
	private JPanel grid;

	GameFrame(Grid.Difficulty difficulty) {
		super(); // is this line necessary?  what does it do?
		setSize(650, 600);
		if (difficulty == Grid.Difficulty.LOAD) {
			load();
		} else {
			game = new Grid(difficulty);  // the Interface game
		}
		JToolBar toolbar = new JToolBar("In-game toolbar");
		createToolbar(toolbar);
		getContentPane().add(toolbar, BorderLayout.NORTH); //puts the game toolbar at the top of the screen
		globalTE = "0";
		timer = new Timer();
		timer.schedule(new Clock(), 0, 1000);
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
		if (difficulty == Grid.Difficulty.LOAD) {
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

	public void load() {
		System.out.println("Loading...");
		try {
			FileInputStream fileStream = new FileInputStream("MyGame.ser");
			ObjectInputStream os = new ObjectInputStream(fileStream);
			Object one;
			try {
				one = os.readObject();
				game = (Grid) one;
				globalTE = game.saveTime;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void save() {
		System.out.println("Saving...");
		game.saveTime = globalTE;
		try {
			FileOutputStream fileStream = new FileOutputStream("MyGame.ser");
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(game);
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	//int lowestTime=1000;
	public void saveHighest(String name, String time) { //will throw an exception if highscore.txt doesn't exist, but will create one when you win the game.
		// write to a file if the game is won, include timer for highest score.
		System.out.println("Saving high score only if you WIN game");
		try (FileWriter fw = new FileWriter("HighScore.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) { // variables in parenthesis will automatically close if try block fails
			out.println(name + " finished " + game.getDifficulty().toString() + " difficulty in " + time + " seconds!");
		} catch (IOException e) {
			e.printStackTrace();
			//exception handling left as an exercise for the reader
		}
	}

	public void createToolbar(JToolBar toolbar) {
		//make buttons
		refresh = new JButton("Reset Game");
		mainMenu = new JButton("Main Menu");
		quitMine = new JButton("Quit Minesweeper");
		inGameHelp = new JButton("Help");
		flagBtn = new JButton("Flag"); //new ImageIcon("resource/images/flag.png"));
		Clock gClock = new Clock();
		timeDisplay = new JTextField(globalTE);
		timeDisplay.setColumns(4);
		timeDisplay.setEditable(false);
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
		stopTimer();
		MineGUI.newGame(game.getDifficulty());
	}

	public void stopTimer() {
		timer.cancel();
		timer.purge();
	}

	public class Clock extends TimerTask {
		private long currClock;
		private long endClock;
		private long elapse;
		private final long nano;
		private long sec;
		private String leftOver;

		public Clock(){
			nano = 1000000000;
			leftOver = globalTE;
			globalTE = "0";
			currClock = System.nanoTime();
		}

		private void updateTE(){
			endClock = System.nanoTime();
			elapse = endClock - currClock;
			sec = Math.floorDiv(elapse, nano);
			globalTE = String.valueOf(sec + Long.parseLong(leftOver));
		}

		public void pauseClock(){
			game.saveTime = globalTE;
		}

		public void run() {
			this.updateTE();
			timeDisplay.setText(globalTE);
			timeDisplay.repaint();
		}
	} // class Clock

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
			/*
			int size = buttons[0][0].getSize().height / 2;
			if (buttons[0][0].getSize().height / 2 > buttons[0][0].getSize().width / 4) {
				size = buttons[0][0].getSize().width / 4;
			}
			for (int i = 0; i < game.getSize(); i++) {
				for (int j = 0; j < game.getSize(); j++) {
					if (game.isOpen(i, j) || game.isFlag(i, j)) {
						if (game.isFlag(i, j) || game.isMine(i, j)) {
							Image img = ((ImageIcon) buttons[i][j].getIcon()).getImage();
							//resize icon to fit button
							Image newImg = img.getScaledInstance(grid.getWidth() / game.getSize() - 10, grid.getHeight() / game.getSize() - 10, java.awt.Image.SCALE_DEFAULT);
							buttons[i][j].setIcon(new ImageIcon((newImg)));
						} else {
							buttons[i][j].setFont(new Font("sansserif", Font.BOLD, size));
						}
					}
				}
			}
			*/
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
			if (game.getGameState() == Grid.GameState.PLAYING) {
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
				if (game.getGameState() == Grid.GameState.LOST) {
					// display mines
					refresh();
					stopTimer();
					int response = JOptionPane.showOptionDialog(null, "You lose! Press 'Reset Game' to start a new game.", "Defeat!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
					if (response == JOptionPane.YES_OPTION) {
						MineGUI.goToMainMenu();
					} else {
						resetGame();
					}
				} else if (game.getGameState() == Grid.GameState.WON) {
					soundName = "resources/sounds/win.wav";
					playSound(soundName);
					stopTimer();
					String user = JOptionPane.showInputDialog(null, "You win! Enter your name for the leaderboard.", "Victory!", JOptionPane.QUESTION_MESSAGE);
					if (user != null) {
						int response = JOptionPane.showOptionDialog(null, "You win! Press 'Reset Game' to start a new game.", "Victory!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
						if (response == JOptionPane.YES_OPTION) {
							saveHighest(user, globalTE);
							MineGUI.goToMainMenu();
						} else if (response == JOptionPane.INFORMATION_MESSAGE) {
							saveHighest(user, globalTE);
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
