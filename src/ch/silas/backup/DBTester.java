package ch.silas.backup;

import ch.silas.sql.SQLWriter;

import java.sql.Connection;

/**
 * Created by Silas Stegmueller on 10.05.15.
 */
public class DBTester {

    public static void main(String[] args) {
        Connection c = null;

        String messages = "hallo schadz ich liebe dich";
        String topic = "Philomene";

        SQLWriter sqlWriter = new SQLWriter();

     /*   try {
            sqlWriter.dbWriter(topic, );
        } catch (SQLException e) {
            e.printStackTrace();
        }

*/
    }
}
