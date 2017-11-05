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
    private int status;
    private JButton[][] buttons;
	private String globalTE;
	private JTextField timeDisplay;
	private Timer timer;

	GameFrame(int difficulty) {
		super(); // is this line necessary?  what does it do?
		setSize(650, 600);
		status = 0;
        if (difficulty == -2) {
			load();
		} else {
			game = new Grid(difficulty);  // the Interface game
		}
		buttons = new JButton[game.getSize()][game.getSize()];
		JToolBar toolbar = new JToolBar("In-game toolbar");
		createToolbar(toolbar);
		getContentPane().add(toolbar, BorderLayout.NORTH); //puts the game toolbar at the top of the screen
		globalTE = "0";
		timer = new Timer();
		timer.schedule(new Clock(), 0, 1000);
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(game.getSize() ,0));
		for (int i = 0; i < game.getSize(); i++) {
			for (int j = 0; j < game.getSize(); j++) {
				String label = String.format("%d", i * game.getSize() + j);
				JButton jb = new JButton(label);
				buttons[i][j] = jb;
				jb.addMouseListener(new ButtonListener(i * game.getSize() + j));
				jb.setFont(new Font("sansserif", Font.BOLD, 10));
				jb.setText("");
				jb.setIcon(null);
				jb.addComponentListener(new SizeListener());
				grid.add(jb);
			}
		}
		if (difficulty == -2) {
			refresh();
		}
		getContentPane().add(grid);
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
	public void saveHighest(String name, String time, int difficulty) { //will throw an exception if highscore.txt doesn't exist, but will create one when you win the game.
		int t = Integer.parseInt(time);
		String line="";
		/*
		//initialize count first.
		try {
			File myFile = new File("HighScore.txt");
			FileReader filereader = new FileReader(myFile);
			BufferedReader reader = new BufferedReader(filereader);
			while ((line = reader.readLine()) != null) {
				int c = Integer.parseInt(line);
				if (c % 2 == 1 && c < 50) { //every odd entry
					//count = Integer.parseInt(line); //UPDATES COUNT
					count++;
				}
				reader.close();
			}
		}catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		// write to a file if the game is won, include timer for highest score.
		System.out.println("Saving high score only if you WIN game");
		//if (t < lowestTime) {
		try (FileWriter fw = new FileWriter("HighScore.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) { // variables in parenthesis will automatically close if try block fails
			//String countString = Integer.toString(count);
			// out.println(countString);
			out.println(name + " finished " + difToString(difficulty) + " difficulty in " + time + " seconds!");
			//lowestTime = t;
			//count++;
		} catch (IOException e) {
			//exception handling left as an exercise for the reader
		}
	}


	public void createToolbar(JToolBar toolbar) {
		//make buttons
		JButton refresh = new JButton("Reset Game");
		JButton mainMenu = new JButton("Main Menu");
		JButton quitMine = new JButton("Quit Minesweeper");
		JButton inGameHelp = new JButton("Help");
		Clock gClock = new Clock();
		timeDisplay = new JTextField(globalTE);
		timeDisplay.setColumns(4);
		timeDisplay.setEditable(false);
		addActionListener(refresh, "Reset Game");
		addActionListener(mainMenu, "Main Menu");
		addActionListener(inGameHelp, "Help");
		addActionListener(quitMine, "Quit Minesweeper");
		toolbar.add(mainMenu);
		toolbar.add(refresh);
		toolbar.add(inGameHelp);
		toolbar.add(quitMine);
		toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		toolbar.add(timeDisplay);
		toolbar.setFloatable(false);
	}

	public void addActionListener(JButton button, String action) {
		if (action == "Main Menu") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MineGUI.goToMainMenu();
				}
			});
		} else if (action == "Quit Minesweeper") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					quitPrompt();
				}
			});
		} else if (action == "Reset Game") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (overwriteSavePrompt()) {
						resetGame();
					}
				}
			});
		} else if (action == "Help") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HelpScreen helpScreen = new HelpScreen();
				}
			});
		} else if (action == "Load") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					load();
				}
			});
		} else if (action == "Save") {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					save();
				}
			});
		}
	}

	public void quitPrompt() {
		int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit the game?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			save();
			System.out.println("Closing...");
			System.exit(0);
		} else {
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}

	public boolean overwriteSavePrompt() {
		int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to do this? This will delete previous save data", "Overwriting Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			return true;
		} else {
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			return false;
		}
	}

	public void playSound(String dir){
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(dir).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void refresh() {
		for (int i = 0; i < game.getSize(); i++) {
			for (int j = 0; j < game.getSize(); j++) {
				JButton jb = buttons[i][j];
				if (game.getCell(i * game.getSize() + j) != '?') {
					int fontSize = jb.getSize().height / 2;
					if (jb.getSize().height / 2 > jb.getSize().width / 4) {
						fontSize = jb.getSize().width / 4;
					}
					jb.setFont(new Font("sansserif", Font.BOLD, fontSize));
					if (game.getCell(i * game.getSize() + j) == 48) {
						jb.setForeground(ZERO);
					} else if (game.getCell(i * game.getSize() + j) == 70) {
						jb.setForeground(Color.RED);
					} else if (game.getCell(i * game.getSize() + j) == 88) {
						jb.setForeground(Color.BLACK);
					} else {
						jb.setForeground(NUMBER);
					}
					jb.setText(Character.toString(game.getCell(i * game.getSize() + j)));
				}
			}
		}
	}

	void exposeMines() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons.length; j++) {
				if (game.isMine(i * buttons.length + j)) {
					ImageIcon mineImage = new ImageIcon("resources/images/mine.jpg")  ;

					Image img = mineImage.getImage() ;  
			        Image newimg = img.getScaledInstance( 2 * game.getSize(), 2 *game.getSize(),  java.awt.Image.SCALE_DEFAULT ) ;  
					mineImage = new ImageIcon( newimg );
					buttons[i][j].setIcon(mineImage);
					
					buttons[i][j].setFont(new Font("sansserif", Font.PLAIN, 1));
					buttons[i][j].setText("X");
					buttons[i][j].setForeground(new Color(255, 255, 255, 0));
				}
			}
		}
	}

	int getStatus(){
		return status;
	}

	Grid getGrid(){
		return game;
	}

	public String difToString(int difficulty){
		if (difficulty == 10) {
			return "Easy";
		} else if (difficulty == 15) {
			return "Medium";
		} else if (difficulty == 20) {
			return "Hard";
		}
		return "";
	}

	public void resetGame() {
		stopTimer();
		int diff = game.getSize();
		if (diff == 10) {
			MineGUI.newGame(0);
		} else if (diff == 15) {
			MineGUI.newGame(1);
		} else if (diff == 20) {
			MineGUI.newGame(2);
		}
	}

	public void stopTimer() {
		timer.cancel();
		timer.purge();
	}

	public class Clock extends TimerTask {
		private long currClock;
		private long pClock;
		private long endClock;
		private long elapse;
		private final long nano;
		private long sec;
		private long resClock;
		private String timeElapsed;
		private String leftOver;

		public Clock(){
			nano = 1000000000;
			leftOver = globalTE;
			globalTE = "0";
			currClock = System.nanoTime();
			pClock = 0;
		}

		public void updateTE(){
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
			int size = e.getComponent().getSize().height / 2;
			if (e.getComponent().getSize().height / 2 > e.getComponent().getSize().width / 4) {
				size = e.getComponent().getSize().width / 4;
			}
			e.getComponent().setFont(new Font("sansserif", Font.BOLD, size));
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

		private int num;

		public ButtonListener(int i) {
			super();
			this.num = i;
		}

		/**
		 Places player's symbol on button, checks for a winner or tie
		 @param event when a button is clicked
		 */
		public void mouseReleased(MouseEvent event) {
			Clip clip;
			String soundName;
			AudioInputStream audioInputStream;
			if (game.gameStatus(status) == 0) {
				//if you left click and the button is available (not a flag and not already opened)
				if(event.getButton() == MouseEvent.BUTTON1 && !game.isFlag(num) && !game.isOpen(num)){
					char box = game.searchBox(num);
					if (box == 'X') {
						soundName = "resources/sounds/explosion.wav";
					} else {
						soundName = "resources/sounds/clicked.wav";
					}
					playSound(soundName);
					refresh();
					status = game.gameStatus(status);
					if (status == -1) {
						// TODO: display mines
						exposeMines();
						stopTimer();
						int response = JOptionPane.showOptionDialog(null, "You lose! Press 'Reset Game' to start a new game.", "Defeat!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
						if (response == JOptionPane.YES_OPTION) {
							MineGUI.goToMainMenu();
						} else {
							resetGame();
						}
					} else if (status == 1) {
						soundName= "resources/sounds/win.wav";
						playSound(soundName);
						stopTimer();
						String user = JOptionPane.showInputDialog(null, "You win! Enter your name for the leaderboard.", "Victory!", JOptionPane.QUESTION_MESSAGE);
						int response = JOptionPane.showOptionDialog(null, "You win! Press 'Reset Game' to start a new game.", "Victory!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
						if (response == JOptionPane.YES_OPTION) {
							saveHighest(user, globalTE, game.getSize());
							MineGUI.goToMainMenu();
						} else if (response == JOptionPane.INFORMATION_MESSAGE) {
							saveHighest(user, globalTE, game.getSize());
							resetGame();
						} else {
							//do nothing
						}
					}
				} else if (event.getButton() == MouseEvent.BUTTON1 && (game.isFlag(num) | game.isOpen(num))) {
					// If you left click and the button is a flag or has been opened
					game.searchBox(num);
					soundName = "resources/sounds/userError.wav";
					playSound(soundName);
				} else if (event.getButton() == MouseEvent.BUTTON3) {
					// If you right click
					if (game.isFlag(num)) {
						game.deflagBox(num);
						JButton jb = buttons[num / game.getSize()][num % game.getSize()];
						//jb.setFont(new Font("sansserif",Font.BOLD,12));
						jb.setForeground(Color.BLACK);
						jb.setText("");
					} else if (!game.isOpen(num)) {
						soundName = "resources/sounds/place_flag.wav";
						playSound(soundName);
						game.flagBox(num);
						JButton jb = buttons[num / game.getSize()][num % game.getSize()];
						jb.setFont(new Font("sansserif", Font.PLAIN,1));
						//jb.setForeground(Color.RED);
						
						
						ImageIcon flagImage = new ImageIcon("resources/images/flag.png");
						Image img = flagImage.getImage() ;
						//resize icon to fit button
				        Image newimg = img.getScaledInstance( 2 * game.getSize(), 2 *game.getSize(),  java.awt.Image.SCALE_DEFAULT ) ;  
						
				        flagImage = new ImageIcon( newimg );
						jb.setIcon(flagImage);
						jb.setText("F");
						jb.setForeground(new Color(255, 255, 255, 0));

					} else {
						game.flagBox(num);
						soundName = "resources/sounds/userError.wav";
						playSound(soundName);
					}
					int status = game.gameStatus(0);
					if (status == 1) {
						soundName= "resources/sounds/win.wav";
						playSound(soundName);
						exposeMines();
						stopTimer();
						String user = JOptionPane.showInputDialog(null, "You win! Enter your name for the leaderboard.", "Victory!", JOptionPane.QUESTION_MESSAGE);
						int response = JOptionPane.showOptionDialog(null, "You win! Press 'Reset Game' to start a new game.", "Victory!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
						if (response == JOptionPane.YES_OPTION) {
							saveHighest(user, globalTE, game.getSize());
							MineGUI.goToMainMenu();
						} else if (response == JOptionPane.INFORMATION_MESSAGE) {
							saveHighest(user, globalTE, game.getSize());
							resetGame();
						} else {
							// do nothing
						}
					}
				} else if (event.getButton() == MouseEvent.BUTTON1 && game.isOpen(num)){
					soundName = "resources/sounds/userError.wav";
					playSound(soundName);
				}
			}
		}
	} // class ButtonListener
}
