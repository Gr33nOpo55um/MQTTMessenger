package ch.silas.test;

import ch.silas.ProbSettings;
import ch.silas.sql.SQLModule;
import ch.silas.xml.XmlModule;

import java.sql.SQLException;

/**
 * Created by Silas Stegmueller on 08.05.2015.
 */
public class Main {

    public static void main(String[] args) throws SQLException {

        ProbSettings probSettings = new ProbSettings();
        String url = probSettings.loadProb("url");
        String user = probSettings.loadProb("user");
        String password = probSettings.loadProb("password");
        String topic = probSettings.loadProb("topic");
        String END_MESSAGE = probSettings.loadProb("END_MESSAGE");

        String message = "test";

        System.out.println("Server URL: " + url);


        System.out.println(topic);


        Thread xmlModule = new Thread(new XmlModule(url, topic, "XMLModul", END_MESSAGE, message));

        //Thread testSender = new Thread(new TestSender(url));
        Thread sqlModule = new Thread(new SQLModule(url, topic, "SQLModul", END_MESSAGE, message));


        xmlModule.run();


        //  testSender.start();
        sqlModule.run();

        //   testSender.getState();

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
