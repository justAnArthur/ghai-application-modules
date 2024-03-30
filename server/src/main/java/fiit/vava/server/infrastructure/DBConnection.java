package fiit.vava.server.infrastructure;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

//    private static final String URL = "jdbc:postgresql://localhost:5432/db";
    private static final String URL = Dotenv.load().get("DATABASE_URL");

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL);
        } catch (final SQLException e) {
            // Log the error or throw a custom exception
            throw new SQLException("Failed to connect to the database", e);
        }
    }
}
