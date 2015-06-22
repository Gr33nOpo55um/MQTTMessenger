package ch.silas.xml;

import ch.silas.mqtt.MqttClient;
import ch.silas.mqtt.SilasMqttClient;
import ch.silas.mqtt.SilasMqttReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Silas Stegmueller on 08.05.2015.
 */
public class XmlModule implements SilasMqttReceiver, Runnable {

    private final static int MAX_MESSAGES = 10;
    private final SilasMqttClient mqttClient;
    private final XMLWriter xmlWriter;
    private Map<String, List<String>> messages = new HashMap<>();
    private String END_MESSAGE;
    private String topic;
    private String message;


    public XmlModule(String url, String topic, String clientId, String endMessage, String message) {
        xmlWriter = new XMLWriter();
        END_MESSAGE = endMessage;

        mqttClient = new MqttClient(clientId);

        mqttClient.start(url);
        mqttClient.subscribe(topic, this);
        this.topic = topic;
        messages = new HashMap<>();
        this.message = message;

    }


    @Override
    public void receive(String topic, String message) {
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
                xmlWriter.writeXML(topic, cachedMessages);
                messages.remove(topic);
            }
        }
    }

    @Override
    public void run() {
        this.receive(topic, message);
    }
}
