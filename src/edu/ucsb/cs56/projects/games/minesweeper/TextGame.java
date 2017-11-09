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

	public static void main(String [] args) {
		Scanner sc = new Scanner(System.in);
		refreshConsole();
		System.out.println("Welcome to the text version of MineSweeper!");
		System.out.println("Please select a difficulty");
		System.out.println("Enter 1 for easy, Enter 2 for medium, Enter 3 for hard");
		int difficulty = sc.nextInt();
		// clears input
		sc.nextLine();
		Grid game = null;
		boolean done = false;
		while (!done) {
			done = true;
			switch (difficulty) {
				case 1:
					game = new Grid(Grid.Difficulty.EASY);
					break;
				case 2:
					game = new Grid(Grid.Difficulty.MEDIUM);
					break;
				case 3:
					game = new Grid(Grid.Difficulty.HARD);
					break;
				default:
					done = false;
					System.out.println("Please select a valid difficulty");
					System.out.println("Enter 1 for easy, Enter 2 for medium, Enter 3 for hard");
					break;
			}
		}
		//reset done for game play
		done = false;
		while (!done) {
			refreshConsole();
			System.out.println(game); // g.toString() implicitly invoked
			System.out.println("What would you like to do?");
			System.out.println("Enter 1 to search a spot, Enter 2 to place a flag, Enter 3 to deflag a spot");
			int move = sc.nextInt();
			// clears input
			sc.nextLine();
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
						System.out.println("Please try again. \n");
						break;
				}
			} else {
				System.out.println("Please try again. \n");
			}
			if (game.getGameState() != Grid.GameState.PLAYING) {
				done = true;
			}
		}
		refreshConsole();
		System.out.println(game); // g.toString() implicitly invoked
		if (game.getGameState() == Grid.GameState.LOST) {
			System.out.println("You lose!");
		} else {
			System.out.println("You win!!!");
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
	}
}
