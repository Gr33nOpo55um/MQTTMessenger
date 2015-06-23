package ch.silas.backup;

import java.sql.SQLException;

/**
 * Created by Silas Stegmueller on 08.05.2015.
 */
public interface SilasMqttReceiver {

    void receive(String topic, String message) throws SQLException;
}
