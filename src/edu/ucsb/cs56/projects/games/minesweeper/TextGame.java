package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.IOException;
import java.util.Scanner;

/** Makes the game on the terminal functional
 @author Unknown
 @author David Acevedo
 @version 2015/03/04 for lab07, cs56, W15
 @see Grid
 */

public class TextGame {

	private static Scanner sc;
	private static Grid game;
	private static Constants.ApplicationState state;

	public static void main(String [] args) {
		sc = new Scanner(System.in);
		state = Constants.ApplicationState.MAINMENU;
		refreshConsole();
		System.out.println("Welcome to the text version of MineSweeper!");
		while (true) {
			switch (state) {
				case MAINMENU:
					printMainMenu();
					int menuResponse = getIntInput();
					while (menuResponse < 1 || menuResponse > 2) {
						refreshConsole();
						switch (menuResponse) {
							case 3:
								printHelp();
								break;
							case 4:
								System.exit(0);
								break;
							default:
								printInvalidInput();
								break;
						}
						printMainMenu();
						menuResponse = sc.nextInt();
						sc.nextLine();
					}
					if (menuResponse == 1) {
						setUpGameDifficulty();
					} else {
						try {
							game = Grid.loadGame();
						} catch (IOException | ClassNotFoundException e) {
							setUpGameDifficulty();
						}
					}
					boolean done = false;
				while (!done) {
					refreshConsole();
					System.out.println(game); // g.toString() implicitly invoked
					printMoveOptions();
					int move = sc.nextInt();
					// clears input
					sc.nextLine();
					while (move < 1 || move > 6) {
						refreshConsole();
						printInvalidInput();
						System.out.println(game);
						printMoveOptions();
						move = sc.nextInt();
						sc.nextLine();
					}
					System.out.println("Enter the number of the row and column which box you would like to select (5 7 would be row 5 column 7)");
					int row = sc.nextInt();
					int col = sc.nextInt();
					// clears input
					sc.nextLine();
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
					if (game.getGameState() != Constants.GameState.PLAYING) {
						done = true;
					}
				}
			}
		}
		refreshConsole();
		System.out.println(game); // g.toString() implicitly invoked
		if (game.getGameState() == Constants.GameState.LOST) {
			System.out.println("You lose!");
		} else {
			System.out.println("You win!!!");
		}
	}

	private static void printMainMenu() {
		System.out.println("What would you like to do?");
		System.out.println("1) To start a new game");
		System.out.println("2) To load your last game");
		System.out.println("3) To display the help screen");
		System.out.println("4) To quit");
	}

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

	private static void printInvalidInput() {
		System.out.println(Constants.ANSI_RED + "Please select a valid input" + Constants.ANSI_RESET);
	}

	private static void printDifficultyPrompt() {
		System.out.println("Please enter what difficulty you would like to play");
		System.out.println("1) Easy");
		System.out.println("2) Medium");
		System.out.println("3) Hard");
	}

	private static void printMoveOptions() {
		System.out.println("What would you like to do?");
		System.out.println("1) To search a spot");
		System.out.println("2) To place a flag");
		System.out.println("3) To deflag a spot");
		System.out.println("4) To go back to main menu");
		System.out.println("5) To reset the game");
		System.out.println("6) To quit");
	}

	private static void setUpGameDifficulty() {
		refreshConsole();
		printDifficultyPrompt();
		int difficulty = sc.nextInt();
		sc.nextLine();
		while (difficulty < 1 || difficulty > 3) {
			refreshConsole();
			printDifficultyPrompt();
			difficulty = sc.nextInt();
			// clears input
			sc.nextLine();
		}
		game = new Grid(Constants.Difficulty.values()[difficulty]);
	}

	private static int getIntInput() {
		int temp = sc.nextInt();
		sc.nextLine();
		return temp;
	}

	private static void refreshConsole() {
	    //prints new lines to move previous grid off screen
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
	}
}
