package ch.silas.Message;

import java.io.Serializable;
import java.time.Instant;

/**
 * Created by Silas Stegmueller on 08.06.15.
 */
public class unagaMQTTMessage implements Serializable {
    private final String text;
    private final Instant date;
    private String sender;

    public unagaMQTTMessage(String text, String sender) {
        this.text = text;
        this.date = Instant.now();
        this.sender = sender;
    }


    public String getText() {
        return text;
    }

    public Instant getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String toString() {

        return "Date: " + date + ";Sender: " + sender + ";Text: " + text + ";Password: ";
    }


}
