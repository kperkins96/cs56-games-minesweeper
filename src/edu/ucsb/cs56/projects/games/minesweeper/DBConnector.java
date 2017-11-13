package edu.ucsb.cs56.projects.games.minesweeper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ryanwiener on 11/12/17.
 */

public class DBConnector {

    private static Connection connection;

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
            e.printStackTrace();
            return;
        }
        String host = "jdbc:postgresql://ec2-23-21-197-231.compute-1.amazonaws.com:5432/d5pb4fr0mh116p";
        String username = "iynzxgmnmkikbp";
        String password = "4fdb5c230919c81b2dc2daa12a3dd420c602a9154e3bcf2bf0b2d3d62be2f42c";
        try {
            connection = DriverManager.getConnection(host, username, password);
            System.out.println(connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
