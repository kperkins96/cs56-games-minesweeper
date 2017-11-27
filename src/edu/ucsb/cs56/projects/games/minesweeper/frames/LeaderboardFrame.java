package edu.ucsb.cs56.projects.games.minesweeper.frames;

import javax.swing.JTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JRadioButton;

import javax.swing.table.TableColumn;

import edu.ucsb.cs56.projects.games.minesweeper.database.DBConnector;
import javafx.scene.control.ToggleGroup;

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

	public LeaderboardFrame() {
		super();
		setSize(650, 600);
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
		highScoreTable = new JTable(getHighScores(1), columnNames);
		highScoreTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setUpColumnWidths();

		ItemListener itemListener = (ItemEvent itemEvent) -> {
			AbstractButton aButton = (AbstractButton)itemEvent.getSource();
			int state = itemEvent.getStateChange();
			String label = aButton.getText();
			if (state == ItemEvent.SELECTED) {
				if (label.equals("Easy")) {
					highScoreTable.setModel(new DefaultTableModel(getHighScores(1), columnNames));
				} else if (label.equals("Medium")) {
					highScoreTable.setModel(new DefaultTableModel(getHighScores(2), columnNames));
				} else {
					highScoreTable.setModel(new DefaultTableModel(getHighScores(3), columnNames));
				}
				setUpColumnWidths();
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

	public Object[][] getHighScores(int difficulty) {
		ArrayList<Map<String, String>> highScores;
		if (difficulty == 1) {
			highScores = DBConnector.getTopTenEasy();
		} else if (difficulty == 2) {
			highScores = DBConnector.getTopTenMedium();
		} else { //hard difficulty
			highScores = DBConnector.getTopTenHard();
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

	public int getBackX() {
		return backBtn.getX();
	}

	public int getBackY() {
		return backBtn.getY();
	}
}
