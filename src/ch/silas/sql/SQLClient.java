package ch.silas.sql;

import java.sql.*;

/**
 * Created by Silas Stegmueller on 23.06.15.
 */
public class SQLClient {


    Connection c = null;
    Statement statement = null;


    public void dbConnect() {


        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");


    }

    public void dbWriter(String writeStatement) {

        try {
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.executeUpdate(writeStatement);
        } catch (SQLException e) {
            try {
                statement.close();
            } catch (SQLException e1) {

            }

            try {
                c.commit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }


            try {
                c.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("Records created successfully");

        }

    }


    public void dbDisconnect() {

        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void readChat() throws SQLException {
        String sqlResult = "";
        ResultSet rs;
        String sqlQuery = "select creation_ts, sender, message from mqttChat;";

        rs = statement.executeQuery(String.valueOf(sqlQuery));


        while (rs.next()) {

            Date date = rs.getDate("creation_ts");
            String sender = rs.getString("sender");
            String message = rs.getString("message");
            System.out.println("Date: = " + date);
            System.out.println("Sender = " + sender);
            System.out.println("Message = " + message);
            System.out.println();
        }
        rs.close();

        statement.close();
        c.close();

        System.out.println("Operation done successfully");


    }
}
