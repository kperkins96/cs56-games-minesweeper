package edu.ucsb.cs56.projects.games.minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * HelpScreen.java is a JFrame that displays help messages and a button to return to the StartMenu
 * @author Julian Gee
 * @version 2015/03/04 for lab07, cs56, W15
 *
 * @author Ryan Wiener
 */

public class HelpScreen extends JFrame {

	private JButton backButton;
	private JTextArea helpText;

	/**
	 * Default constructor for help screen
	 */
	public HelpScreen() {
		setSize(650, 600);
		Container screen = getContentPane();
		screen.setLayout(new GridLayout(2, 0));
		helpText = new JTextArea("How to Play:\n1. Click on a space to reveal whether there is a mine or not.\n2. If there is no mine, there is a number shown. This number shows how many mines are touching this\n      square, including diagonals.\n3. If you know there is a mine in a spot, right click it. F symbolizes flag, letting you know there is a mine\n      there.\n4. The goal is to locate all the mines on the board.\n5. If you successfully locate all the mines, you win! If you trigger a mine, you lose.\n6. Have fun!");
		helpText.setEditable(false);
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		screen.add(helpText);
		screen.add(backButton);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	/**
	 * get x coordinate of back button for robot testing
	 * @return x coordinate of help screen back button
	 */
	public int getBackX() {
		return backButton.getX();
	}

	/**
	 * get y coordinate of back button for robot testing
	 * @return y coordinate of help screen back button
	 */
	public int getBackY() {
		return backButton.getY();
	}
}
