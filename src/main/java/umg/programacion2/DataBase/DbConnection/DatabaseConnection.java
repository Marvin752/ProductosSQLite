package umg.programacion2.DataBase.DbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:C:\\SQLite\\sqlite-tools-win-x64-3460100\\db_telebot"; // Cambia el nombre del archivo según sea necesario

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}

