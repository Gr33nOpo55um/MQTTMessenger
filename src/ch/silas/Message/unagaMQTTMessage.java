package ch.silas.Message;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;
import java.time.Instant;

/**
 * Created by Silas Stegmueller on 08.06.15.
 */
public class unagaMQTTMessage extends MqttMessage implements Serializable {
    private final String message;
    private final Instant date;
    private String sender;

    public unagaMQTTMessage(String message, String sender) {
        this.message = message;
        this.date = Instant.now();
        this.sender = sender;
    }


    public String getMessage() {
        return message;
    }

    public Instant getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String toString() {

        return "Date: " + date + ";Sender: " + sender + ";Text: " + message;
    }


}
