package edu.ucsb.cs56.projects.games.minesweeper;

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
		boolean done = false;
		int status = 0;

		// TicTacToeGrid implements TicTacToeGame
		Grid g = new Grid(false);

		while (!done) {
		   System.out.println(g); // g.toString() implicitly invoked
		   System.out.println("What would you like to do?");
		   System.out.println("Enter 1 to Search  a spot, Enter 2 to place a flag, Enter 3 to Deflag a spot");
		   String line = sc.nextLine();

		   int num = 0;
		   int num2 = 0;
		   try {
		       num = Integer.parseInt(line);
		       if (num == 1 || num == 2 || num == 3) {
				   System.out.println("Enter the number of the row and column which box you would like to select");
				   String line2  = sc.nextLine();
				   num2 = Integer.parseInt(line2);
				   if (num2 >= 0 && num2 <= 99) {
						switch (num) {
							case 1:
							    g.searchBox(num2);
							    break;
							case 2:
							    g.flagBox(num2);
							    break;
							case 3:
							    g.deflagBox(num2);
							    break;
						}
				   } else {
					   System.out.println("Please try again. \n");
				   }
			   } else {
				   System.out.println("Please try again. \n");
			   }
		   } catch (NumberFormatException nfe) {
		       System.out.println("Please try again. \n");
		   }
		   status = g.gameStatus(status);
		   System.out.println(status);
		   if (status != 0) {
			   done = true;
		   }
		}
		System.out.println(g); // g.toString() implicitly invoked
		if (status == -1) {
			System.out.println("You lose!!!!");
		} else {
		    System.out.println(status);
		    System.out.println("You win!!");
		}
    }
}
