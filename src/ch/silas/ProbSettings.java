package ch.silas;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Silas Stegmueller on 12.05.15.
 */
public class ProbSettings {

    Properties mqttSettings = new Properties();
    String mqttPropFilename = "resources/MQTTsettings.properties";


    public String loadProb(String probVal) {

        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(new FileInputStream(mqttPropFilename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            mqttSettings.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mqttSettings.getProperty(probVal);
    }
}

