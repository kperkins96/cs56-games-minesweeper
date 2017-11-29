package edu.ucsb.cs56.projects.games.minesweeper.tests;
import edu.ucsb.cs56.projects.games.minesweeper.constants.Constants;


public class ConstantsTest {

	public static void main(String [] args) {

		System.err.println("Hello");
		
		Constants.disableErrorOutput();
		System.err.println("You should not see this");

		Constants.reenableErrorOutput();
		System.err.println("Goodbye");

	}

}

