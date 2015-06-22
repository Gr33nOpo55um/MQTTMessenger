package ch.silas.backup;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Silas Stegmueller on 09.05.15.
 */
public class TestSender implements Runnable {
    String topic;
    String content;
    int qos;
    String broker;
    String clientId;
    MemoryPersistence persistence;

    public TestSender(String url) {
        topic = "test";
        content = "Message from MqttPublishSample";
        qos = 2;
        broker = url;
        clientId = "JavaSample";
        persistence = new MemoryPersistence();

    }

    @Override
    public void run() {

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            for (int i = 0; i < 25; i++) {

                System.out.println("Publishing message: " + content + i);
                MqttMessage message = new MqttMessage((content + i).getBytes());
                message.setQos(qos);
                sampleClient.publish(topic, message);
                System.out.println("Message published");

                Thread.sleep(1000);
            }
            System.out.println("Publishing message: valarmorgulis");
            MqttMessage message = new MqttMessage("valarmorgulis".getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");

            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
