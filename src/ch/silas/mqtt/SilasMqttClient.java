package ch.silas.mqtt;

import ch.silas.Message.unagaMQTTMessage;
import ch.silas.backup.SilasMqttReceiver;

/**
 * Created by Silas Stegmueller on 08.05.2015.
 */
public interface SilasMqttClient {

    void start(String url);

    void stop();

    void subscribe(String topic, SilasMqttReceiver receiverCallback);

    void send(String topic, unagaMQTTMessage mqttMessage);
}
