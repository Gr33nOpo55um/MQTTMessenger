package ch.silas.backup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Silas Stegmueller on 22.06.15.
 */
public class SQLChat {

    Statement stmtCreate = null;
    Connection connection = null;

    public void dbConnect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }


    public void dbDisconnect() throws SQLException {

        this.connection.close();
        System.out.println("DB Connection closed");


    }


    public void dbWriter(String sender, String message) {


        //Some tricky substrings, thanks codingbat ^^
        String date, text;

        System.out.println(message.substring(6, 31));

        text = message.substring(51, message.length() - 1);

        date = message.substring(8, 25).replace('T', ' ');
        //Date: 2015-06-23T15:14:07.518Z;Sender: Silas;Text: test
        // to 15-06-23 15:22:06


        System.out.print(this.connection);
        try {
            stmtCreate = this.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // String sqlInsertMsg = "INSERT INTO mqttChat (SENDER, MESSAGE) VALUES (" + sender + "," + message + ");";

        try {
            stmtCreate.executeUpdate("INSERT INTO mqttChat (CREATION_TS, SENDER, MESSAGE) VALUES ( " + date + "," + sender + ", " + message + "  );");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmtCreate.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.connection.commit(); //not necessary because of auto commit
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Operation done successfully");
    }
}
