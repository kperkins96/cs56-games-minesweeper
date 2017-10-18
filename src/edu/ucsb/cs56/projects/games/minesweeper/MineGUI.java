package edu.ucsb.cs56.projects.games.minesweeper;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Math;
import java.util.TimerTask;
import java.util.Timer;
import java.lang.Object;
import java.lang.Long;
import java.io.*;
import java.util.ArrayList;

import java.io.*;
import java.util.ArrayList;


import javax.swing.*;

/** MineGUI.java is a base that calls all GUI objects and handles tasks
    such as pausing the game, creating the GUI, making the escape key functional,
	and allowing for a new game.

     @author David Acevedo
     @version 2015/03/04 for lab07, cs56, W15
	 @see MineGUI
*/


public class MineGUI {

    private JToolBar toolbar;
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
    private Clock gClock;
    private JTextField Time;
    private int timeTBPos;
    private Timer timer;
    public String globalTE;

    private JScrollPane scroller;
    private JLabel highScore; // this label status displays the local high score.
    private JTextArea highScoreList;
    private JTextField Username;
    public String User;

    private int count;

    public MineComponent mc; //MineComponent is the actual layout of the game, and what makes the game function
    private JLabel status;        //the game status label that is displayed during the game


    /**
     no-arg constructor which creates the GUI of the Main Menu for Minesweeper
     This menu includes a start and help button
     */

    public MineGUI() {
        globalTE = new String("0");
        frame = new JFrame();
        count = 1;
        User = "";
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit the game?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    save();
                    System.exit(0);
                } else {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        createMainMenu();
        frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        frame.setSize(650, 600);
        frame.setVisible(true);
        frame.setName("testframe");
    }

    /**
     * Starts a new Minesweeper game from the main menu
     */

    //TODO: should we delete this? Looks useless
    public void newGame() {
        globalTE = "0";
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        game = new JPanel(new BorderLayout());            //our game panel e.g. where everything will be put in for this display
        Grid grid = new Grid(true);
        mc = new MineComponent(grid, this);    //creates our game interface
        frame.setSize((65 * mc.getGrid().getSize() > screenSize.width ? screenSize.width : 65 * mc.getGrid().getSize()), (60 * mc.getGrid().getSize() > screenSize.height - 40 ? screenSize.height - 40 : 60 * mc.getGrid().getSize()));
        JToolBar toolbar = new JToolBar("In-game toolbar");
        createToolbar(toolbar);
        game.add(mc);                            //puts the game in the jPanel
        game.add(toolbar, BorderLayout.NORTH);    //puts the game toolbar at the top of the screen
        menu.setVisible(false);                    //puts the menu away
        frame.getContentPane().add(game);
        inUse = true;
        timer = new Timer();
        timer.schedule(new Clock(), 0, 1000);
    }

    public void newGame(int difficulty) {
        globalTE = "0";
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        game = new JPanel(new BorderLayout());      //our game panel e.g. where everything will be put in for this display
        Grid grid = new Grid(true, difficulty);
        mc = new MineComponent(grid, this);         //creates our game interface
        frame.setSize((65 * mc.getGrid().getSize() > screenSize.width ? screenSize.width : 65 * mc.getGrid().getSize()), (60 * mc.getGrid().getSize() > screenSize.height - 30 ? screenSize.height - 30 : 60 * mc.getGrid().getSize()));
        JToolBar toolbar = new JToolBar("In-game toolbar");
        createToolbar(toolbar);
        game.add(mc);               //puts the game in the jPanel
        game.add(toolbar, BorderLayout.NORTH);    //puts the game toolbar label at the top of the screen
        menu.setVisible(false);                    //puts the menu away
        frame.getContentPane().add(game);
        inUse = true;
        timer = new Timer();
        timer.schedule(new Clock(), 0, 1000);
    }

    /**
     * Creates the main menu, the menu when you launch the application
     */
    public void createMainMenu() {
        frame.setSize(650, 600);
        menu = new JPanel(new GridLayout(4, 0));        //our 2 section grid layout for our main menu
        quitMine = new JButton("Quit Minesweeper");
        easyGame = new JButton("New Easy Game");
        medGame = new JButton("New Medium Game");
        hardGame = new JButton("New Hard Game");
        help = new JButton("Help");
        load = new JButton("Load Last Game");
        Username = new JTextField("Name: ");
        highScore = new JLabel("Leaderboards: "); // added another JLabel
        highScoreList = new JTextArea(10, 20);
        scroller = new JScrollPane(highScoreList);
        highScoreList.setLineWrap(true);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        highScoreList.append(getHighScores());
        addActionListener(Username);
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
        //menu.add(highScoreList);
        menu.add(scroller);
        menu.add(Username);
        frame.getContentPane().add(menu);
        inUse = false;
    }

    public void addActionListener(JTextField text) {
        text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                User = text.getText();
                System.out.println("User is set to: " + User);
            }
        });
    }

    public void createToolbar(JToolBar toolbar) {
        //make buttons
        refresh = new JButton("Reset Game");
        mainMenu = new JButton("Main Menu");
        quitMine = new JButton("Quit Minesweeper");
        inGameHelp = new JButton("Help");
        gClock = new Clock();
        Time = new JTextField(globalTE);
        Time.setColumns(4);
        Time.setEditable(false);
        addActionListener(refresh, "Reset Game");
        addActionListener(mainMenu, "Main Menu");
        addActionListener(inGameHelp, "Help");
        addActionListener(quitMine, "Quit Minesweeper");
        toolbar.add(mainMenu);
        toolbar.add(refresh);
        toolbar.add(inGameHelp);
        toolbar.add(quitMine);
        toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        toolbar.add(Time);
        toolbar.setFloatable(false);
    }

    /**
     * Sets a message on the status bar on the top of the GUI
     *
     * @param status - message you wish the status bar to display
     */
    public void setLabel(String status) {
        this.status.setText(status);
    }

    /**
     * Creates a specified task for the buttons
     *
     * @param button - The JButton that you want to assign a task to
     * @param action - the action you would like to give the button
     */
    public void addActionListener(JButton button, String action) {
        if (action == "New Easy Game") {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (overwriteSavePrompt(frame)) {
                        newGame(0);
                    }
                }
            });
        } else if (action == "New Medium Game") {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (overwriteSavePrompt(frame)) {
                        newGame(1);
                    }
                }
            });
        } else if (action == "New Hard Game") {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (overwriteSavePrompt(frame)) {
                        newGame(2);
                    }
                }
            });
        } else if (action == "Main Menu") {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    goToMainMenu();
                }
            });

        } else if (action == "Quit Minesweeper") {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    quitPrompt(frame);
                }
            });
        } else if (action == "Reset Game") {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //if (grid.gameStatus(0) == 0){
                    if (overwriteSavePrompt(frame)) {
                        resetGame();
                    }
                }
            });
        } else if (action == "Help") {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (inUse == false) {
                        HelpScreen helpScreen = new HelpScreen(frame, menu);
                    } else {
                        HelpScreen helpScreen = new HelpScreen(frame, game);
                    }
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

    public void quitPrompt(JFrame frame) {
        int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit the game?", "Quit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            save();
            System.out.println("Closing...");
            System.exit(0);
        } else {
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    public boolean overwriteSavePrompt(JFrame frame) {
        int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to do this? This will delete previous save data", "Overwriting Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            return true;
        } else {
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            return false;
        }
    }

    public void load() {
        System.out.println("Loading...");
        try {
            FileInputStream fileStream = new FileInputStream("MyGame.ser");
            ObjectInputStream os = new ObjectInputStream(fileStream);
            Object one;
            try {
                one = os.readObject();
                Grid grid = (Grid) one;
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                game = new JPanel(new BorderLayout());            //our game panel e.g. where everything will be put in for this display
                mc = new MineComponent(grid, this);//creates our game interface
                int gridSize = mc.getGrid().getSize();
                frame.setSize((65 * gridSize > screenSize.width ? screenSize.width : 65 * gridSize), (60 * gridSize > screenSize.height - 30 ? screenSize.height - 30 : 60 * gridSize));
                game.add(mc);                            //puts the game in the jPanel
                JToolBar toolbar = new JToolBar("In-game toolbar");
                createToolbar(toolbar);
                game.add(toolbar, BorderLayout.NORTH);    //puts the game toolbar at the top of the screen
                menu.setVisible(false);                    //puts the menu away
                frame.getContentPane().add(game);
                mc.refresh();
                inUse = true;
                globalTE = grid.saveTime;
                timer = new Timer();
                timer.schedule(new Clock(), 0, 1000);
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

    public void displayMines() {
        Grid g = mc.getGrid();
        g.getG()[1][1]  = 'W';
        game.add(mc);
        frame.getContentPane().add(game);
        mc.refresh();
    }


	public void save(){
        if (inUse) {
            if (mc != null) {
                System.out.println("Saving...");
                mc.getGrid().saveTime = globalTE;
                try {
                    FileOutputStream fileStream = new FileOutputStream("MyGame.ser");
                    ObjectOutputStream os = new ObjectOutputStream(fileStream);
                    os.writeObject(mc.getGrid());
                    os.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                System.exit(0);
            }
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
            String countString = Integer.toString(count);
            // out.println(countString);
            out.println(name + " finished " + difToString(difficulty) + " difficulty in " + time + " seconds!");
            //lowestTime = t;
            //count++;
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public String getHighScores() {
        int num = count;
        System.out.println("Loading high scores onto mainframe");
        String score = "";
        String line = "";
        try {
            File myFile = new File("HighScore.txt");
            FileReader filereader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(filereader);
            while ((line = reader.readLine()) != null) {
                //while(num > -1) {
                score += line + "\n";
                //    num--;
                //}
            }
            reader.close();
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // sort scores by smallest time
        return score;
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
        menu.setVisible(false);
        refreshFrame(frame);
        int diff = mc.getGrid().getSize();
        if (diff == 10) {
            newGame(0);
        } else if (diff == 15) {
            newGame(1);
        } else if (diff == 20) {
            newGame(2);
        }
    }

    public void stopTimer() {
        timer.cancel();
        timer.purge();
    }

    public void goToMainMenu() {
        stopTimer();
        Grid g = mc.getGrid();
        int stat = mc.getStatus();
        if (g.gameStatus(stat) == 0) {
            save();
        }
        game.setVisible(false);
        inUse = false;
        refreshFrame(frame);
        createMainMenu();
        gClock.pauseClock();
        menu.setVisible(true);
    }

    public static void main (String[] args) {
	MineGUI frame = new MineGUI();
    }
    
    public void refreshFrame(JFrame frame){
        frame.getContentPane().removeAll();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public class Clock extends TimerTask{
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
            mc.getGrid().saveTime = globalTE;
        }
        
        public void run() {
            this.updateTE();
            Time.setText(globalTE);
            Time.repaint();
        }
    } // class Clock
} // class MineGUI
