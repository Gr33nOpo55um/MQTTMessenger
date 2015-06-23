package ch.silas.sql;

import java.sql.*;

/**
 * Created by Silas Stegmueller on 23.06.15.
 */
public class SQLClient
{


    Connection c = null;
    Statement statement = null;


    public void dbConnect() {

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/home/silas/IdeaProjects/XMLeaning1/test.db");
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

            /*
            NO COMMIT NEEDED CAUSED OF AUTO COMMIT
            try {
                c.commit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            */

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


    public String readChat() throws SQLException {
        String sqlResult = "";
        statement = c.createStatement();
        ResultSet rs = statement.executeQuery("SELECT creation_ts, sender, message FROM chat;");


        while (rs.next()) {
            String date = rs.getString("creation_ts");
            String sender = rs.getString("sender");
            String message = rs.getString("message");
            sqlResult = sqlResult + date + ": " + sender + "\n" + message + "\n\n";

            // System.out.println(sqlResult);
        }
        rs.close();

        statement.close();
        c.close();

        //   System.out.println("Operation done successfully");
        return sqlResult;

    }


}
