package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/** Makes the game on the terminal functional
 @author David Acevedo
 @author Ryan Wiener
 @see Grid
 */

public class TextGame {

	private static Scanner sc;
	private static Grid game;
	private static Constants.ApplicationState state;
	private static ArrayList<String> relations;

	/**
	 * Handles switch statement that controls flow of game based off its current state
	 * @param args arguments passed into program call
	 */
	public static void main(String [] args) {
		// set up input handler
		sc = new Scanner(System.in);
		// initialize database connection
        DBConnector.init();
        // column labels
		initRelations();
		// set intial state to main menu
		state = Constants.ApplicationState.MAINMENU;
		refreshConsole();
		System.out.println("Welcome to the text version of MineSweeper!");
		// continue running until user requests to quit
		while (true) {
			switch (state) {
				case MAINMENU:
					printMainMenu();
					int menuResponse = getMenuResponse();
					refreshConsole();
					handleMainMenuResponse(menuResponse);
					break;
				case GAME:
					playGame();
					handleEndGameState();
					// go back to main menu
					state = Constants.ApplicationState.MAINMENU;
					break;
				case LEADERBOARD:
					// ask which leaderboard the user would like to see (easy, medium or hard)
					printLeaderboardPrompt();
					int leaderboardResponse = getLeaderboardResponse();
					refreshConsole();
					handleLeaderboardResponse(leaderboardResponse);
					break;
				default:
					// code should never reach this line just here for good practice
					System.exit(0);
					break;
			}
		}
	}

	/**
	 * initialize the list of column labels for leaderboard display
	 * have relations as static member since it should never change during runtime
	 */
	private static void initRelations() {
		relations = new ArrayList<>(4);
		relations.add("place");
		relations.add("name");
		relations.add("score");
		relations.add("attime");
	}

	/**
	 * print main menu options
	 */
	private static void printMainMenu() {
		System.out.println("What would you like to do?");
		System.out.println("1) To start a new game");
		System.out.println("2) To load your last game");
		System.out.println("3) To display the help screen");
		System.out.println("4) To display the leaderboards");
		System.out.println("5) To quit");
	}

	/**
	 * Get valid user input after printing main menu
	 * @return user input
	 */
	private static int getMenuResponse() {
		int menuResponse = getIntInput();
		// make sure menu response is valid
		while (menuResponse < 1 || menuResponse > 5) {
			refreshConsole();
			printInvalidInput();
			printMainMenu();
			menuResponse = getIntInput();
		}
		return menuResponse;
	}

	/**
	 * Handle user input from main menu prompt and act accordingly
	 * @param menuResponse user input in response to main menu
	 * menuResponse == 1 -> start new game
	 * menuResponse == 2 -> attempt to load new game, but if no previous save print error
	 * menuResponse == 3 -> print help menu
	 * menuResponse == 4 -> go to leaderboard menu
	 * menuResponse == 5 -> quit the game
	 */
	private static void handleMainMenuResponse(int menuResponse) {
		switch (menuResponse) {
			case 1:
				// start new game
				setUpGameDifficulty();
				refreshConsole();
				break;
			case 2:
				try {
					// attempt to load the previous game
					game = Grid.loadGame();
					state = Constants.ApplicationState.GAME;
				} catch (IOException | ClassNotFoundException e) {
					System.out.println(Constants.ANSI_RED + "There was no save file to load from" + Constants.ANSI_RESET);
				}
               break;
			case 3:
			    // print help menu
				printHelp();
				break;
			case 4:
				// change state to leaderboard menu
				state = Constants.ApplicationState.LEADERBOARD;
				break;
			case 5:
				// quit
				System.exit(0);
				break;
			default:
				// will never get here but put just as good practice
				printInvalidInput();
				break;
		}
	}

	/**
	 * Print help menu for text game
	 */
	private static void printHelp() {
		System.out.println("How to Play:");
		System.out.println("To reveal a spot enter 1 to search a box and then enter the space's row and column.");
		System.out.println("If there is no mine, there is a number shown. This number shows how many mines are touching this square, including diagonals.");
		System.out.println("If you know there is a mine in a spot, flag it. F symbolizes flag, letting you know there is a flag there");
		System.out.println("To flag a spot enter 2 to set a flag and then enter the space's row and column.");
		System.out.println("If you open all spaces without mines and flag all spaces with mines, you win!");
		System.out.println("However, if you trigger a mine, you lose.");
		System.out.println("Have fun!");
	}

	/**
	 * Ask user for what difficulty they would like to play for their new game
	 * and set up that game accordingly
	 */
	private static void setUpGameDifficulty() {
		printDifficultyPrompt();
		int difficulty = getIntInput();
		// make sure user input is valid
		while (difficulty < 1 || difficulty > 4) {
			refreshConsole();
			printInvalidInput();
			printDifficultyPrompt();
			difficulty = getIntInput();
		}
		if (difficulty == 4) {
			state = Constants.ApplicationState.MAINMENU;
		} else {
			game = new Grid(Constants.Difficulty.values()[difficulty]);
			state = Constants.ApplicationState.GAME;
		}
	}

	/**
	 * Ask user what difficulty they would like to play
	 */
	private static void printDifficultyPrompt() {
		System.out.println("Please enter what difficulty you would like to play");
		System.out.println("1) Easy");
		System.out.println("2) Medium");
		System.out.println("3) Hard");
		System.out.println("4) Go back to the main menu");
	}

	/**
	 * Have the user play the current game until they win, lose or request to leave
	 */
	private static void playGame() {
		// play game until done or user requests to leave
		while (game.getGameState() == Constants.GameState.PLAYING) {
			// print the game through the toString() in Grid.java
			System.out.println(game);
			printMoveOptions();
			int move;
			move = getMoveResponse();
			if (move < 4) {
				// if the user still wants to play the current game
				System.out.println("Enter the number of the row and column which box you would like to select (5 7 would be row 5 column 7)");
				int row, col;
				try {
					row = sc.nextInt();
				} catch (InputMismatchException e) {
					row = -1;
				}
				col = getIntInput();
				refreshConsole();
				handleMove(move, row, col);
			} else if (move == 4) {
				// save and go to main menu
				game.save();
				refreshConsole();
				break;
			} else if (move == 5) {
				// start new game
				game = new Grid(game.getDifficulty());
				refreshConsole();
			} else {
				// save and quit
				game.save();
				System.exit(0);
			}
		}
	}

	/**
	 * Ask user what they would like to do when starting a new move
	 */
	private static void printMoveOptions() {
		System.out.println("What would you like to do?");
		System.out.println("1) To search a spot");
		System.out.println("2) To place a flag");
		System.out.println("3) To deflag a spot");
		System.out.println("4) To go back to main menu");
		System.out.println("5) To reset the game");
		System.out.println("6) To quit");
	}

	/**
	 * get valid user input after asking for what move they want to do
	 * @return user input
	 */
	private static int getMoveResponse() {
		int move = getIntInput();
		// make sure move is valid
		while (move < 1 || move > 6) {
			refreshConsole();
			printInvalidInput();
			System.out.println(game);
			printMoveOptions();
			move = getIntInput();
		}
		return move;
	}

	/**
	 * Handle user input during a game to make a move
	 * @param move int value indicating type of move that user requested to do
	 * move == 1 -> search box
	 * move == 2 -> flag box
	 * move == 3 -> deflag box
	 * @param row int value indicating row of box to operate the move on (0 based indecies)
	 * @param row int value indicating column of box to operate the move on (0 based indecies)
	 */
	private static void handleMove(int move, int row, int col) {
		// make sure that user entered a valid row and column
		if (row * game.getSize() + col >= 0 && row * game.getSize() + col < game.getSize() * game.getSize()) {
			switch (move) {
				case 1:
					game.searchBox(row, col);
					break;
				case 2:
					game.flagBox(row, col);
					break;
				case 3:
					game.deflagBox(row, col);
					break;
				default:
					printInvalidInput();
					break;
			}
		} else {
			printInvalidInput();
		}
	}

	/**
	 * Handle what to do if a user won or lost after they finished playing the game
	 */
	private static void handleEndGameState() {
		// if user won or lost the game
		if (game.getGameState() != Constants.GameState.PLAYING) {
			refreshConsole();
			System.out.println(game);
			if (game.getGameState() == Constants.GameState.LOST) {
				System.out.println("You lose");
			} else {
				System.out.println("You won in " + game.getGameTime() + " seconds!!!");
				// ask if they want their name in the leaderboard
				printLeaderboardQuestion();
				int leaderboardResponse = getIntInput();
				// make sure leaderboard response is valid
				while (leaderboardResponse < 1 || leaderboardResponse > 2) {
					refreshConsole();
					printInvalidInput();
					printLeaderboardQuestion();
					leaderboardResponse = getIntInput();
				}
				if (leaderboardResponse == 1) {
					System.out.println("Please enter your name");
					String name = sc.nextLine();
					// enter name, score and difficulty into the leaderboard
					DBConnector.addScore(name, game.getGameTime(), game.getDifficulty().ordinal());
				}
				refreshConsole();
			}
		}
	}

	/**
	 * Prompt user whether they want their name on the leaderboard after they have won
	 */
	private static void printLeaderboardQuestion() {
		System.out.println("Would you like to enter your name into the leaderboard?");
		System.out.println("1) Yes");
		System.out.println("2) No");
	}

	/**
	 * Ask user what leaderboard they would like to view
	 */
	private static void printLeaderboardPrompt() {
		System.out.println("Which leaderboard would you like to view?");
		System.out.println("1) Easy");
		System.out.println("2) Medium");
		System.out.println("3) Hard");
		System.out.println("4) Go back to the main menu");
	}

	/**
	 * get Valid input from user for leaderboard menu
	 * @return user input
	 */
	private static int getLeaderboardResponse() {
		int leaderboardResponse = getIntInput();
		// make sure input is valid from user
		while (leaderboardResponse < 1 || leaderboardResponse > 4) {
			refreshConsole();
			printInvalidInput();
			printLeaderboardQuestion();
			leaderboardResponse = getIntInput();
		}
		return leaderboardResponse;
	}

	/**
	 * Handle user input from leaderboard menu
	 * @param leaderboardResponse user input after printing leaderboard menu
	 * leaderboardResponse == 1 -> print easy leaderboard
	 * leaderboardResponse == 2 -> print medium leaderboard
	 * leaderboardResponse == 3 -> print hard leaderboard
	 * leaderboardResponse == 4 -> go back to main menu
	 */
	private static void handleLeaderboardResponse(int leaderboardResponse) {
		switch (leaderboardResponse) {
			case 1:
				// print easy leaderboard
				System.out.println("Easy leaderboard:");
				printTable(DBConnector.getTopTenEasy());
				System.out.println();
				break;
			case 2:
				// print medium leaderboard
				System.out.println("Medium leaderboard:");
				printTable(DBConnector.getTopTenMedium());
				System.out.println();
				break;
			case 3:
				// print hard leaderboard
				System.out.println("Hard leaderboard:");
				printTable(DBConnector.getTopTenHard());
				System.out.println();
	           break;
			case 4:
				// go back to main menu
				state = Constants.ApplicationState.MAINMENU;
				break;
			default:
				break;
		}
	}

	/**
	 * Prints the given ArrayList of Maps into a table identical to what would be returned from a sql query
	 * @param leaders ArrayList<Map<String, String>> of leaders stats
	 * leaders must be at least of size 1 and must have each index contain a map with
	 * keys "place", "name", "score" and "attime"
	 * Will do nothing if leaders has size 0 or not the valid keys for every index
	 */
	private static void printTable(ArrayList<Map<String, String>> leaders) {
		// make sure leaders isn't empty
		if (leaders.size() > 0) {
			// check to make sure that the map has valid keys
			for (int i = 0; i < leaders.size(); i++) {
				if (leaders.get(i).size() != relations.size()) {
					return;
				}
				Set<String> keys = leaders.get(i).keySet();
				for (int j = 0; j < relations.size(); j++) {
					if (!keys.contains(relations.get(j))) {
						return;
					}
				}
			}
			ArrayList<Integer> spaces = findMaxLengthWords(leaders);
			// print column labels with appropriate spaces in order to center it (leaning right if needed)
			for (int i = 0; i < relations.size(); i++) {
				printTermWithSpacing(relations.get(i), spaces.get(i));
				if (i != relations.size() - 1) {
					System.out.print("|");
				}
			}
			System.out.println();
			printColumnLabelUnderline(spaces);
			// print entries with appropriate spaces in order to center it (leaning right if needed)
			for (int i = 0; i < leaders.size(); i++) {
				for (int j = 0; j < leaders.get(i).size(); j++) {
					if (j != 0) {
						System.out.print("|");
					}
					printTermWithSpacing(leaders.get(i).get(relations.get(j)), spaces.get(j));
				}
				System.out.println();
			}
		}
	}

	/**
	 * find max leangth of the words for each given key
	 * @param leaders ArrayList<Map<String, String>> of leader statistic must have size greater than 0
	 * Each index must contain a map with 4 key value pairs that all represent one leader
	 * The 4 keys must be "place", "name", "score" and "attime"
	 * @return an array list containing the longest length strings for each column including the column label
	 * Example: returnedArrayList.get(0) == longest length string in place column (since place is 0 indexed column)
	 */
	private static ArrayList<Integer> findMaxLengthWords(ArrayList<Map<String, String>> leaders) {
		// find max length of any given entry (including column labels)
		ArrayList<Integer> spaces = new ArrayList<Integer>(relations.size());
		for (int i = 0; i < relations.size(); i++) {
			spaces.add(relations.get(i).length());
		}
		for (int i = 0; i < leaders.get(0).size(); i++) {
			for (int j = 0; j < leaders.size(); j++) {
				if (spaces.get(i) < leaders.get(j).get(relations.get(i)).length()) {
					spaces.set(i, leaders.get(j).get(relations.get(i)).length());
				}
			}
		}
		return spaces;
	}

	/**
	 * prints term with spacing such that it's centered in its column (leaning right if needed)
	 * @param term string to be printed
	 * @param spacing the longest length word in the same column as term
	 */
	private static void printTermWithSpacing(String term, int spacing) {
		// print spaces to left of label (at least 1)
		printSpaces((spacing + 3 - term.length()) / 2);
		// print column label
		System.out.print(term);
		// print spaces to right of label (at least 1)
		printSpaces((spacing + 2 - term.length()) / 2);

	}

	/**
	 * print numSpaces on the same line
	 * @param numSpaces the number os spaces to print
	 */
	private static void printSpaces(int numSpaces) {
		for (int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
	}

	/**
	 * Prints an underline underneath the column labels
	 * @param spaces the array of longest lengths for each column
	 */
	private static void printColumnLabelUnderline(ArrayList<Integer> spaces) {
		// print line underneath column labels
		for (int i = 0; i < spaces.size(); i++) {
			for (int j = 0; j < spaces.get(i) + 2; j++) {
				System.out.print("-");
			}
			if (i != spaces.size() - 1) {
				System.out.print("+");
			}
		}
		System.out.println();
	}

	/**
	 * Input in form of an int and then clears input stream
	 * @return user input or -1 if scanner.nextInt() throws an exception
	 */
	private static int getIntInput() {
		int temp;
		try {
			temp = sc.nextInt();
		} catch (InputMismatchException e) {
			temp = -1;
		}
		sc.nextLine();
		return temp;
	}

	/**
	 * Alert user that their previous input was invalid in red text
	 */
	private static void printInvalidInput() {
		System.out.println(Constants.ANSI_RED + "Please select a valid input" + Constants.ANSI_RESET);
	}

	/**
	 * Clears screen to make next print at the top of the shell
	 */
	private static void refreshConsole() {
		try {
			if (System.getProperty("os.name").contains("Windows")) {
			    //Windows
				Runtime.getRuntime().exec("cls");
			} else {
			    //UNIX
				System.out.print("\033[H\033[2J");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Print new line because ant acts weird
		System.out.println();
	}
}
