package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
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
        DBConnector.init();
		state = Constants.ApplicationState.MAINMENU;
		refreshConsole();
		System.out.println("Welcome to the text version of MineSweeper!");
		while (true) {
			switch (state) {
				case MAINMENU:
					printMainMenu();
					int menuResponse = getIntInput();
					while (menuResponse < 1 || menuResponse > 5) {
						refreshConsole();
						printInvalidInput();
						printMainMenu();
						menuResponse = getIntInput();
					}
					refreshConsole();
					switch (menuResponse) {
						case 1:
							setUpGameDifficulty();
							refreshConsole();
							break;
						case 2:
							try {
								game = Grid.loadGame();
								state = Constants.ApplicationState.GAME;
							} catch (IOException | ClassNotFoundException e) {
								System.out.println(Constants.ANSI_RED + "There was no save file to load from" + Constants.ANSI_RESET);
							}
                            break;
						case 3:
							printHelp();
							break;
						case 4:
							state = Constants.ApplicationState.LEADERBOARD;
							break;
						case 5:
							System.exit(0);
							break;
						default:
							printInvalidInput();
							break;
					}
					break;
				case GAME:
					boolean done = false;
					while (!done) {
						System.out.println(game); // g.toString() implicitly invoked
						printMoveOptions();
						int move = getIntInput();
						while (move < 1 || move > 6) {
							refreshConsole();
							printInvalidInput();
							System.out.println(game);
							printMoveOptions();
							move = getIntInput();
						}
						if (move < 4) {
							System.out.println("Enter the number of the row and column which box you would like to select (5 7 would be row 5 column 7)");
							int row = sc.nextInt();
							int col = getIntInput();
                            refreshConsole();
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
						} else if (move == 4) {
							game.save();
							refreshConsole();
                            break;
						} else if (move == 5) {
							game = new Grid(game.getDifficulty());
							refreshConsole();
						} else {
							game.save();
							System.exit(0);
						}
					}
					if (done) {
						refreshConsole();
						System.out.println(game); // game.toString() implicitly invoked
						if (game.getGameState() == Constants.GameState.LOST) {
							System.out.println("You lose");
						} else {
							System.out.println("You won in " + game.getGameTime() + " seconds!!!");
							printLeaderboardQuestion();
							int leaderboardResponse = getIntInput();
							while (leaderboardResponse < 1 || leaderboardResponse > 2) {
								refreshConsole();
								printInvalidInput();
								printLeaderboardQuestion();
								leaderboardResponse = getIntInput();
							}
							if (leaderboardResponse == 1) {
								System.out.println("Please enter your name");
								String name = sc.nextLine();
								DBConnector.addScore(name, game.getGameTime(), game.getDifficulty().ordinal());
							}
							refreshConsole();
						}
					}
					state = Constants.ApplicationState.MAINMENU;
					break;
				case LEADERBOARD:
					printLeaderboardPrompt();
					int leaderboardResponse = getIntInput();
					while (leaderboardResponse < 1 || leaderboardResponse > 4) {
						refreshConsole();
						printInvalidInput();
						printLeaderboardQuestion();
						leaderboardResponse = getIntInput();
					}
					refreshConsole();
					switch (leaderboardResponse) {
						case 1:
							System.out.println("Easy leaderboard:");
							printTable(DBConnector.getTopTenEasy());
							System.out.println();
							break;
						case 2:
							System.out.println("Medium leaderboard:");
							printTable(DBConnector.getTopTenMedium());
							System.out.println();
							break;
						case 3:
							System.out.println("Hard leaderboard:");
							printTable(DBConnector.getTopTenHard());
							System.out.println();
                            break;
						case 4:
							state = Constants.ApplicationState.MAINMENU;
							break;
						default:
							break;
					}
					break;
				default:
					System.exit(0);
					break;
			}
		}

	}

	private static void printMainMenu() {
		System.out.println("What would you like to do?");
		System.out.println("1) To start a new game");
		System.out.println("2) To load your last game");
		System.out.println("3) To display the help screen");
		System.out.println("4) To display the leaderboards");
		System.out.println("5) To quit");
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
		System.out.println("4) Go back to the main menu");
	}

	private static void printLeaderboardPrompt() {
		System.out.println("Which leaderboard would you like to view?");
		System.out.println("1) Easy");
		System.out.println("2) Medium");
		System.out.println("3) Hard");
		System.out.println("4) Go back to the main menu");
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
		printDifficultyPrompt();
		int difficulty = getIntInput();
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

	private static void printLeaderboardQuestion() {
		System.out.println("Would you like to enter your name into the leaderboard?");
		System.out.println("1) Yes");
		System.out.println("2) No");
	}

	private static int getIntInput() {
		int temp = sc.nextInt();
		sc.nextLine();
		return temp;
	}

	private static void printTable(ArrayList<Map<String, String>> leaders) {
		if (leaders.size() > 0) {
			ArrayList<String> relations = new ArrayList<String>(leaders.get(0).size());
			relations.add("place");
			relations.add("name");
			relations.add("score");
			relations.add("attime");
			ArrayList<Integer> spaces = new ArrayList<Integer>(leaders.get(0).size());
			for (int i = 0; i < leaders.get(0).size(); i++) {
				spaces.add(relations.get(i).length());
			}
			for (int i = 0; i < leaders.get(0).size(); i++) {
				for (int j = 0; j < leaders.size(); j++) {
					if (relations.get(i).equals("place")) {
						if (spaces.get(i) < Integer.toString(i).length()) {
							spaces.set(i, leaders.get(j).get(relations.get(i)).length());
						}
					} else if (spaces.get(i) < leaders.get(j).get(relations.get(i)).length()) {
						spaces.set(i, leaders.get(j).get(relations.get(i)).length());
					}
				}
			}
			String borders = "";
			for (int i = 0; i < spaces.size(); i++) {
				for (int j = 0; j < spaces.get(i) + 2; j++) {
					borders += "-";
				}
				if (i != spaces.size() - 1) {
					borders += "+";
				}
			}
			for (int i = 0; i < relations.size(); i++) {
				for (int j = 0; j < (spaces.get(i) + 3 - relations.get(i).length()) / 2; j++) {
					System.out.print(" ");
				}
				System.out.print(relations.get(i));
				for (int j = 0; j < (spaces.get(i) + 2 - relations.get(i).length()) / 2; j++) {
					System.out.print(" ");
				}
				if (i != relations.size() - 1) {
					System.out.print("|");
				}
			}
			System.out.println();
			System.out.println(borders);
			for (int i = 0; i < leaders.size(); i++) {
				//System.out.println(borders);
				for (int k = 0; k < (spaces.get(0) + 3 - Integer.toString(i).length()) / 2; k++) {
					System.out.print(" ");
				}
				System.out.print(i + 1);
				for (int k = 0; k < (spaces.get(0) + 2 - Integer.toString(i).length()) / 2; k++) {
					System.out.print(" ");
				}
				for (int j = 1; j < leaders.get(i).size(); j++) {
					System.out.print("|");
				   	for (int k = 0; k < (spaces.get(j) + 3 - leaders.get(i).get(relations.get(j)).length()) / 2; k++) {
						System.out.print(" ");
					}
					System.out.print(leaders.get(i).get(relations.get(j)));
					for (int k = 0; k < (spaces.get(j) + 2 - leaders.get(i).get(relations.get(j)).length()) / 2; k++) {
						System.out.print(" ");
					}
				}
				System.out.println();
			}
		}
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
		//Print new line because ant acts weird
		System.out.println();
	}
}
