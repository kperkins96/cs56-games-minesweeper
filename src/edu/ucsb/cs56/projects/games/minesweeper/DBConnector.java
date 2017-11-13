package edu.ucsb.cs56.projects.games.minesweeper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by ryanwiener on 11/12/17.
 */

public class DBConnector {

    private static Connection connection;

    public static void main(String[] args) {
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
        } catch (SQLException e) {
            System.out.println("Noooooooo");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Connected!");
    }
}
