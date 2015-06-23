package ch.silas.backup;

import ch.silas.Message.unagaMQTTMessage;
import ch.silas.mqtt.SilasMqttClient;
import org.eclipse.paho.client.mqttv3.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Silas Stegmueller on 08.05.2015.
 */
public class MqttClientBAK extends Observable implements SilasMqttClient {

    private static final int QOS = 2;

    private final String clientId;
    private final AtomicBoolean started;
    private final MqttConnectOptions mqttConnectOptions;

    private MqttClientBAK mqttClientBAK = null;


    private MqttAsyncClient asyncClient;

    public MqttClientBAK(String clientId) {
        this.clientId = clientId;

        started = new AtomicBoolean(false);
        mqttConnectOptions = new MqttConnectOptions();

        mqttConnectOptions.setCleanSession(true);
    }

    @java.lang.Override
    public void start(String url) {
        if (started.compareAndSet(false, true)) {
            try {
                asyncClient = new MqttAsyncClient(url, clientId);
                IMqttToken connection = asyncClient.connect(mqttConnectOptions, null, null);
                connection.waitForCompletion();

                System.out.println("Client started.");
            } catch (MqttException e) {
                System.out.println("Unable to start client:" + e.getMessage());
            }
        } else {
            System.out.println("Client is already started.");
        }
    }

    @java.lang.Override
    public void stop() {
        if (started.compareAndSet(true, false)) {
            try {
                if (asyncClient.isConnected()) {
                    IMqttToken connection = asyncClient.disconnect();
                    connection.waitForCompletion();
                }
                asyncClient.close();

                System.out.println("Client started.");
            } catch (MqttException e) {
                System.out.println("Unable to stop client:" + e.getMessage());
            }
            asyncClient = null;
        } else {
            System.out.println("Client is already stopped.");
        }
    }

    @java.lang.Override
    public void subscribe(String topic, SilasMqttReceiver receiver) {
        if (started.get()) {
            try {
                asyncClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable throwable) {

                    }

                    @Override
                    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                        if (mqttMessage != null) {
                            receiver.receive(s, new String(mqttMessage.getPayload(), "UTF-8"));
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                    }
                });
                IMqttToken connection = asyncClient.subscribe(topic, QOS, null, null);
                connection.waitForCompletion();

                System.out.println("Subscribtion started.");
            } catch (MqttException e) {
                System.out.println("Unable to subscribe:" + e.getMessage());
            }
        } else {
            System.out.println("Client not started.");
        }
    }

    @Override
    public void send(String topic, unagaMQTTMessage mqttMessage) {

    }

    // @Override
    public void send(String topic, String mqttMessage) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        pw.println(mqttMessage);
        pw.close();
        MqttMessage message = new MqttMessage(baos.toByteArray());
        try {
            asyncClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }


        //update chat window
        this.notifyObservers();

    }


}
