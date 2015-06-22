package ch.silas.mqtt;

/**
 * Created by Silas Stegmueller on 08.05.2015.
 */
public interface SilasMqttReceiver {

    void receive(String topic, String message);
}
