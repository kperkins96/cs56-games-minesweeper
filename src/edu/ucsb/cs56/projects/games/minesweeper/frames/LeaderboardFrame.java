package edu.ucsb.cs56.projects.games.minesweeper.frames;

import javax.swing.JTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JRadioButton;

import javax.swing.table.TableColumn;

import edu.ucsb.cs56.projects.games.minesweeper.constants.Constants;
import edu.ucsb.cs56.projects.games.minesweeper.database.DBConnector;

/**
 * Displays the leaderboard in a table using the javax.swing package
 * @author Kate Perkins
 * @author Ryan Wiener
 */

public class LeaderboardFrame extends JFrame {

	private JButton backBtn;
	private JLabel title;
	private JScrollPane scroller;
	private JTable highScoreTable;
	private String[] columnNames;
	private ButtonGroup group;
	private Constants.Difficulty difficulty;

	public LeaderboardFrame() {
		super();
		setSize(650, 600);
		difficulty = Constants.Difficulty.EASY;
		Container menu = getContentPane();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

		backBtn = new JButton("Back");
		backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		title = new JLabel("Leaderboard");
		title.setFont(new Font("Serif", Font.PLAIN, 20));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel togglePanel = new JPanel(new FlowLayout());
		group = new ButtonGroup();
		JRadioButton easyBtn = new JRadioButton("Easy");
		group.add(easyBtn);
		easyBtn.setSelected(true);
		JRadioButton mediumBtn = new JRadioButton("Medium");
		group.add(mediumBtn);
		JRadioButton hardBtn = new JRadioButton("Hard");
		group.add(hardBtn);

		columnNames = new String[4];
		columnNames[0] = "Place";
		columnNames[1] = "Name";
		columnNames[2] = "Score";
		columnNames[3] = "At Time";
		highScoreTable = new JTable(getHighScores(Constants.Difficulty.EASY), columnNames);
		highScoreTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setUpColumnWidths();

		ItemListener itemListener = (ItemEvent itemEvent) -> {
			AbstractButton aButton = (AbstractButton)itemEvent.getSource();
			int state = itemEvent.getStateChange();
			String label = aButton.getText();
			if (state == ItemEvent.SELECTED) {
				difficulty = Constants.Difficulty.valueOf(label.toUpperCase());
				refresh();
			}
		};

		easyBtn.addItemListener(itemListener);
		mediumBtn.addItemListener(itemListener);
		hardBtn.addItemListener(itemListener);

		scroller = new JScrollPane(highScoreTable);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		menu.add(backBtn);
		menu.add(title);
		togglePanel.add(easyBtn);
		togglePanel.add(mediumBtn);
		togglePanel.add(hardBtn);
		menu.add(togglePanel);
		menu.add(scroller);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public void setUpColumnWidths() {
		TableColumn column = highScoreTable.getColumn(columnNames[0]);
		column.setMaxWidth(40);
		column.setMinWidth(40);
		column = highScoreTable.getColumn(columnNames[1]);
		column.setMinWidth(100);
		//column.setMaxWidth(400);
		column = highScoreTable.getColumn(columnNames[2]);
		column.setMaxWidth(50);
		column.setMinWidth(40);
		column = highScoreTable.getColumn(columnNames[3]);
		column.setMinWidth(250);
	}

	public Object[][] getHighScores(Constants.Difficulty diff) {
		ArrayList<Map<String, String>> highScores;
		switch (diff) {
			case EASY:
				highScores = DBConnector.getTopTenEasy();
				break;
			case MEDIUM:
				highScores = DBConnector.getTopTenMedium();
				break;
			case HARD:
				highScores = DBConnector.getTopTenHard();
				break;
			default:
				highScores = new ArrayList<Map<String, String>>();
				break;
		}
		Object [][] data = new Object[highScores.size()][4];
		int i = 0;
		for (Map<String, String> row : highScores) {
			data[i][0] = row.get("place");
			data[i][1] = row.get("name");
			data[i][2] = row.get("score");
			data[i][3] = row.get("attime");
			i++;
		}
		return data;
	}

	public void refresh() {
		switch (difficulty) {
			case EASY:
				highScoreTable.setModel(new DefaultTableModel(getHighScores(Constants.Difficulty.EASY), columnNames));
				break;
			case MEDIUM:
				highScoreTable.setModel(new DefaultTableModel(getHighScores(Constants.Difficulty.MEDIUM), columnNames));
				break;
			case HARD:
				highScoreTable.setModel(new DefaultTableModel(getHighScores(Constants.Difficulty.HARD), columnNames));
				break;
			default:
				break;
		}
		setUpColumnWidths();
	}

	public int getBackX() {
		return backBtn.getX();
	}

	public int getBackY() {
		return backBtn.getY();
	}
}
