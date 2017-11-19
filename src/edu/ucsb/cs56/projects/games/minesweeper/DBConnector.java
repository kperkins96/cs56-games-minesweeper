package edu.ucsb.cs56.projects.games.minesweeper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ryanwiener on 11/12/17.
 */

public class DBConnector {

	private static Connection connection;
	private static PreparedStatement insertionStatement;
	private static PreparedStatement queryStatement;

	public static void init() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String host = "jdbc:postgresql://ec2-23-21-197-231.compute-1.amazonaws.com:5432/d5pb4fr0mh116p";
		String username = "iynzxgmnmkikbp";
		String password = "4fdb5c230919c81b2dc2daa12a3dd420c602a9154e3bcf2bf0b2d3d62be2f42c";
		Properties properties = new Properties();
		properties.setProperty("user", username);
		properties.setProperty("password", password);
		properties.setProperty("ssl", "true");
		properties.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
		try {
			connection = DriverManager.getConnection(host, properties);
			insertionStatement = connection.prepareStatement("INSERT INTO scores (name, score, difficulty) VALUES (?, ?, ?);");
			queryStatement = connection.prepareStatement("SELECT * FROM scores WHERE difficulty = ? ORDER BY score ASC LIMIT 10;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean addScore(String name, int score, int difficulty) {
	    try {
			insertionStatement.setString(1, name);
            insertionStatement.setInt(2, score);
			insertionStatement.setInt(3, difficulty);
			int rowCount = insertionStatement.executeUpdate();
			insertionStatement.clearParameters();
			if (rowCount > 0) {
				return true;
			}
		} catch (SQLException e) {
	    	e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<Map<String, String>> getTopTenEasy() {
		ArrayList<Map<String, String>> data = new ArrayList<>(10);
	    try {
			queryStatement.setInt(1, 1);
			ResultSet result = queryStatement.executeQuery();
			for (int i = 0; i < 10 && result.next(); i++) {
				Map<String, String> row = new HashMap<>();
				row.put("name", result.getString("name"));
				row.put("score", result.getString("score"));
				row.put("difficulty", result.getString("difficulty"));
				Date date = result.getDate("attime");
				row.put("attime", date.toString());
				data.add(row);
			}
		} catch (SQLException e) {
	    	e.printStackTrace();
		}
		return data;
	}

	public static ArrayList<Map<String, String>> getTopTenMedium() {
		ArrayList<Map<String, String>> data = new ArrayList<>(10);
		try {
			queryStatement.setInt(1, 2);
			ResultSet result = queryStatement.executeQuery();
			for (int i = 0; i < 10 && result.next(); i++) {
				Map<String, String> row = new HashMap<>();
				row.put("name", result.getString("name"));
				row.put("score", result.getString("score"));
				row.put("difficulty", result.getString("difficulty"));
				row.put("attime", result.getString("attime"));
				data.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static ArrayList<Map<String, String>> getTopTenHard() {
		ArrayList<Map<String, String>> data = new ArrayList<>(10);
		try {
			queryStatement.setInt(1, 3);
			ResultSet result = queryStatement.executeQuery();
			for (int i = 0; i < 10 && result.next(); i++) {
				Map<String, String> row = new HashMap<>();
				row.put("name", result.getString("name"));
				row.put("score", result.getString("score"));
				row.put("difficulty", result.getString("difficulty"));
				row.put("attime", result.getString("attime"));
				data.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
}
