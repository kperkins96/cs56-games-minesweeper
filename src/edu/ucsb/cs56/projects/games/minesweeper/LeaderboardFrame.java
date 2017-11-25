package edu.ucsb.cs56.projects.games.minesweeper;

import javax.swing.JTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import javax.swing.JToggleButton;
import javafx.scene.control.ToggleGroup;
/**
 * Created by ryanwiener on 11/15/17.
 */

public class LeaderboardFrame extends JFrame {

    private JButton backBtn;
    private JLabel title;
    private JTextArea highScoreList;
    private JScrollPane scroller;
    private JRadioButton easyBtn;
    private JRadioButton mediumBtn;
    private JRadioButton hardBtn;
    private JTable highScoreTable;
    final ButtonGroup group = new ButtonGroup();

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

        JRadioButton easyBtn = new JRadioButton("Easy");
        group.add(easyBtn);
        easyBtn.setSelected(true);
        JRadioButton mediumBtn = new JRadioButton("Medium");
        group.add(mediumBtn);
        JRadioButton hardBtn = new JRadioButton("Hard");
        group.add(hardBtn);

        String[] columnNames = {"Place", "Name", "Score", "At Time"};
        highScoreTable = new JTable(getHighScores(1), columnNames);
        //tableModel = new DefaultTableModel();
        ItemListener itemListener = new ItemListener() {
            String lastSelected;
            public void itemStateChanged(ItemEvent itemEvent) {
                AbstractButton aButton = (AbstractButton)itemEvent.getSource();
                int state = itemEvent.getStateChange();
                String label = aButton.getText();
                String msgStart;
                if (state == ItemEvent.SELECTED) {
                    if (label.equals("Easy")) {
                        highScoreTable = new JTable(getHighScores(1), columnNames);
                    }
                    else if (label.equals("Medium")) {
                        highScoreTable = new JTable(getHighScores(2), columnNames);
                    }
                    else {
                        highScoreTable = new JTable(getHighScores(3), columnNames);
                    }
                }
            }
        };

        easyBtn.addItemListener(itemListener);
        mediumBtn.addItemListener(itemListener);
        hardBtn.addItemListener(itemListener);

        scroller = new JScrollPane(highScoreTable);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroller.setViewportView(highScoreTable);

        menu.add(backBtn);
        menu.add(title);
        menu.add(togglePanel);
        togglePanel.add(easyBtn);
        togglePanel.add(mediumBtn);
        togglePanel.add(hardBtn);
        menu.add(highScoreTable.getTableHeader());
        menu.add(highScoreTable);
        menu.add(scroller);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public Object[][] getHighScores(int difficulty) {
        ArrayList<Map<String, String>> highScores;
        if(difficulty == 1){
            highScores = DBConnector.getTopTenEasy();
        }
        else if(difficulty == 2){
            highScores = DBConnector.getTopTenMedium();
        }
        else { //hard difficulty
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
