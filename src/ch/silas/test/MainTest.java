package ch.silas.test;

import ch.silas.backup.SQLChat;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Silas Stegmueller on 22.06.15.
 */
public class MainTest {


    public static void main(String[] args) throws SQLException {

        test();

    }


    public static void test() {

        Connection c = null;

        String sender, message;

        sender = "silas";
        message = "test";

        SQLChat sqlChat = new SQLChat();

        try {
            sqlChat.dbConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlChat.dbWriter(sender, message);

        try {
            sqlChat.dbDisconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
