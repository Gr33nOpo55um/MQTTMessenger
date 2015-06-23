package ch.silas.sql;

import ch.silas.backup.MqttClientBAK;
import ch.silas.mqtt.SilasMqttClient;
import ch.silas.backup.SilasMqttReceiver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Silas Stegmueller on 10.05.15.
 */
public class SQLModule implements SilasMqttReceiver, Runnable {

    private final static int MAX_MESSAGES = 10;
    private final SilasMqttClient mqttClient;
    private final SQLWriter sqlWriter;
    private Map<String, List<String>> messages = new HashMap<>();
    private String END_MESSAGE;
    private String topic = "";
    private String message;
    private Connection c;


    public SQLModule(String url, String topic, String clientId, String endMessage, String message) {

        sqlWriter = new SQLWriter();
        END_MESSAGE = endMessage;


        mqttClient = new MqttClientBAK(clientId);

        mqttClient.start(url);
        mqttClient.subscribe(topic, this);
        messages = new HashMap<>();
        this.topic = topic;
        this.message = message;


    }

    @Override
    public void receive(String topic, String message) {

        try {
            sqlWriter.dbConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(topic + ": " + message);

        synchronized (this) {
            List<String> cachedMessages = messages.get(topic);
            if (cachedMessages == null) {
                cachedMessages = new ArrayList<>();
                cachedMessages.add(message);
                messages.put(topic, cachedMessages);
            } else {
                cachedMessages.add(message);
            }

            if (cachedMessages.size() >= MAX_MESSAGES || END_MESSAGE.equalsIgnoreCase(message)) {
                sqlWriter.dbWriter(topic, cachedMessages, c);
                messages.remove(topic);
            }
        }
    }


    @Override
    public void run() {
        this.receive(topic, message);
    }
}
