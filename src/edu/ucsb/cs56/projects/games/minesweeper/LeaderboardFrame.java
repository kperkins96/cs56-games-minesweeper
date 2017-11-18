package edu.ucsb.cs56.projects.games.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;

import javax.swing.JToggleButton;
import javafx.scene.control.ToggleGroup;
/**
 * Created by ryanwiener on 11/15/17.
 */

public class LeaderboardFrame extends JFrame {

    private JButton backBtn;
    private JLabel title;
    private JTable table;
    private JToggleButton easyToggle;
    private JToggleButton mediumToggle;
    private JToggleButton hardToggle;
    private ToggleGroup difficultyToggle;



    public LeaderboardFrame() {
        super();
        setSize(650, 600);
        Container menu = getContentPane();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
       // menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        //setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

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

        JPanel togglePanel = new JPanel(new FlowLayout())

        easyToggle = new JToggleButton("Easy");
        easyToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumToggle = new JToggleButton("Medium");
        mediumToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        hardToggle = new JToggleButton("Hard");
        hardToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyToggle = new ToggleGroup();
        //easyToggle.setToggleGroup(difficultyToggle);

        Object rowData[][] = { { "Row1-Column1", "Row1-Column2", "Row1-Column3" },
                { "Row2-Column1", "Row2-Column2", "Row2-Column3" } };
        Object columnNames[] = { "Column One", "Column Two", "Column Three" };
        JTable table = new JTable(rowData, columnNames);

        menu.add(backBtn);
        menu.add(title);
        menu.add(easyToggle);
        menu.add(mediumToggle);
        menu.add(hardToggle);
        //menu.add(difficultyToggle);




    }

    public String getHighScores() {
        ArrayList<Map<String, String>> highScores = DBConnector.getTopTenEasy();
        String display = "";
        for (Map<String, String> row : highScores) {
            display += row.get("name") + " ";
            display += row.get("score") + " ";
            display += Grid.Difficulty.values()[Integer.parseInt(row.get("difficulty"))] + " ";
            display += row.get("attime") + " ";
            display += '\n';
        }
        return  display;
    }

    public int getBackX() {
        return backBtn.getX();
    }

    public int getBackY() {
        return backBtn.getY();
    }
}
