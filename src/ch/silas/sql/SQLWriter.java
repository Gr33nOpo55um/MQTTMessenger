package ch.silas.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Silas Stegmueller on 10.05.15.
 */
public class SQLWriter {


    public void dbConnect(Connection c) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }


    public void dbDisconnect(Connection c) throws SQLException {

        c.close();
        System.out.println("DB Connection closed");


    }


    public void dbWriter(String topic, List<String> cachedMessages, Connection c) {
        Statement stmtCreate = null;
        try {
            stmtCreate = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlCreateTopic = "CREATE TABLE IF NOT EXISTS " + topic +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL," +
                " CREATION_TS  TIMESTAMP DEFAULT CURRENT_TIMESTAMP   NOT NULL, " +
                " MESSAGE            TEXT     NOT NULL) ";

        try {
            stmtCreate.executeUpdate(sqlCreateTopic);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmtCreate = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlInsertMsg = "INSERT INTO " + topic + "  (MESSAGE)" +
                "VALUES ('" + cachedMessages + "');";
        try {
            stmtCreate.executeUpdate(sqlInsertMsg);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmtCreate.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //c.commit(); //not necessary because of auto commit

        System.out.println("Operation done successfully");
    }
}





