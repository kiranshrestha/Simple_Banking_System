package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = String.format("jdbc:sqlite:%s", args[1]);
        Scanner s = new Scanner(System.in);

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                DatabaseHelper databaseHelper = new DatabaseHelper(con);
                databaseHelper.createTableIfNotExists();
                CardManager cardManager = new CardManager(s, databaseHelper);
                cardManager.showMenu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}