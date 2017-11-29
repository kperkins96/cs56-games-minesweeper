package edu.ucsb.cs56.projects.games.minesweeper.database;

import edu.ucsb.cs56.projects.games.minesweeper.constants.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.net.UnknownHostException;

/**
 * Supplies an abstraction of connecting to the leaderboard stored on a remote database
 * @author Ryan Wiener
 */

public class DBConnector {

	private static Connection connection;
	private static PreparedStatement insertionStatement;
	private static PreparedStatement queryStatement;
	

	
	/**
	 * Initialize connection to database
	 * Should be called at the beginning of program execution
	 */
	public static void init() {

       		
		Map<String, String> env = System.getenv();
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String host = env.get("DB_HOST");
		String username = env.get("DB_USER"); 
		String password = env.get("DB_PASS");
		if (host == null || username == null || password == null) {
			System.err.println("Fatal error: You must define environment variables DB_HOST, DB_USER, and DB_PASS");
			System.exit(1);
		}
		Properties properties = new Properties();
		properties.setProperty("user", username);
		properties.setProperty("password", password);
		properties.setProperty("ssl", "true");
		properties.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");

		Constants.disableErrorOutput();

		try {
			connection = DriverManager.getConnection(host, properties);
			insertionStatement = connection.prepareStatement("INSERT INTO scores (name, score, difficulty) VALUES (?, ?, ?);");
			queryStatement = connection.prepareStatement("SELECT * FROM scores WHERE difficulty = ? ORDER BY score ASC LIMIT 10;");
		} catch (SQLException e) {
			System.err.println("Caught SQLException " + e); 
			connection = null;
		}
		catch (Exception e) {
			System.err.println("Caught exception " + e);							  
			connection = null;
		}
		Constants.reenableErrorOutput();
		
	}

	/**
	 * Check if database connection is working
	 * @return a boolean value indicating whether the connection to the database is working
	 */
	public static boolean isConnected() {
		if (connection == null) {
			init();
		}
		return connection != null;
	}

	/**
	 * Adds a high score to the database
	 * @param name name of the user with the score
	 * @param score time it took the user to win
	 * @param difficulty an integer representation of the difficulty the user beat
	 * difficulty corresponds with the Difficulty enum .ordinal() method
	 * @return boolean indicating whether score addition was successful or not
	 */
	public static boolean addScore(String name, int score, int difficulty) {
		// if not connected try to connect
		if (connection == null) {
			init();
		}
		if (connection != null) {
			try {
				insertionStatement.setString(1, name);
				insertionStatement.setInt(2, score);
				insertionStatement.setInt(3, difficulty);
				int rowCount = insertionStatement.executeUpdate();
				if (rowCount > 0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Gets top ten easy difficulty leaders
	 * @return an ArrayList of Maps of String to Strings so they can be efficiently displayed in a table
	 * Map has keys "place", "name", "score" and "attime"
	 */
	public static ArrayList<Map<String, String>> getTopTenEasy() {
		return getTopTenLeaders(1);
	}

	/**
	 * Gets top ten medium difficulty leaders
	 * @return an ArrayList of Maps of String to Strings so they can be efficiently displayed in a table
	 * Map has keys "place", "name", "score" and "attime"
	 */
	public static ArrayList<Map<String, String>> getTopTenMedium() {
		return getTopTenLeaders(2);
	}

	/**
	 * Gets top ten hard difficulty leaders
	 * @return an ArrayList of Maps of String to Strings so they can be efficiently displayed in a table
	 * Map has keys "place", "name", "score" and "attime"
	 */
	public static ArrayList<Map<String, String>> getTopTenHard() {
		return getTopTenLeaders(3);
	}

	/**
	 * Gets top ten leaders for given difficulty
	 * @param difficulty an integer representation of the difficulty the user beat
	 * difficulty corresponds with the Difficulty enum .ordinal() method
	 * @return an ArrayList of Maps of String to Strings so they can be efficiently displayed in a table
	 * Map has keys "place", "name", "score" and "attime"
	 */
	private static ArrayList<Map<String, String>> getTopTenLeaders(int difficulty) {
		ArrayList<Map<String, String>> data = new ArrayList<>(10);
		// if not connected try to connect
		if (connection == null) {
			init();
		}
		if (connection != null) {
			try {
				queryStatement.setInt(1, difficulty);
				ResultSet result = queryStatement.executeQuery();
				for (int i = 0; i < 10 && result.next(); i++) {
					Map<String, String> row = new HashMap<>();
					row.put("place", Integer.toString(i + 1));
					row.put("name", result.getString("name"));
					row.put("score", result.getString("score"));
					Date date = result.getTimestamp("attime");
					// format date nicely
					DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.getDefault());
					int offset = TimeZone.getDefault().getOffset(date.getTime());
					date.setTime(date.getTime() + offset);
					dateFormat.setTimeZone(TimeZone.getDefault());
					row.put("attime", dateFormat.format(date));
					data.add(row);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Map<String, String> notConnectedMesage = new HashMap<>();
			notConnectedMesage.put("place", "Not");
			notConnectedMesage.put("name", "Connected");
			notConnectedMesage.put("score", "To");
			notConnectedMesage.put("attime", "Database");
			data.add(notConnectedMesage);
		}
		return data;
	}
}
