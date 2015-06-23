package ch.silas.gui;

import ch.silas.Message.unagaMQTTMessage;
import ch.silas.ProbSettings;
import ch.silas.backup.SilasMqttReceiver;
import ch.silas.mqtt.MqttClient;
import ch.silas.sql.SQLClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by Silas Stegmueller on 01.06.15.
 */
public class MainMenu extends BorderPane implements Observer, SilasMqttReceiver {


    ProbSettings probSettings = new ProbSettings();
    String url = probSettings.loadProb("url");
    String username = probSettings.loadProb("username");
    private SQLClient sqlClient;
    private BorderPane borderPane;
    private HBox horizontal;
    private VBox verticalCenter, verticalLeft;
    private Menu exitM;
    private Button sendChat, sendHWInfo;
    private MenuBar menuBar;
    private Menu Menufile;
    private MenuItem Mmqtt_disconnect, Mexit, Mmqtt_connect;
    private unagaMQTTMessage mqttMessage;
    private MqttClient mqttClient;
    private Label textField;
    private TextField sendTextField;
    private ComboBox comboBox;
    private Label status, chatDivide;

    public MainMenu() {

        /**
         * GUI Stuff
         *
         */

        this.borderPane = new BorderPane();


        //Menu
        this.menuBar = new MenuBar();
        this.menuBar.setUseSystemMenuBar(true);
        this.exitM = new Menu("Exit");
        this.Menufile = new Menu("File");

        this.Mmqtt_connect = new MenuItem("Connect MQTT");
        this.Mmqtt_disconnect = new MenuItem("Disconnect MQTT");
        this.Mexit = new MenuItem("Exit");

        this.Menufile.getItems().addAll(Mmqtt_connect, Mmqtt_disconnect, Mexit);

        this.menuBar.getMenus().addAll(Menufile);


        //Add other stuff
        this.comboBox = new ComboBox<>();


        this.textField = new Label();
        this.sendTextField = new TextField();

        this.sendChat = new Button("Send Message");
        this.sendHWInfo = new Button("Send desired HW Info");

        this.chatDivide = new Label("Please enter message");
        this.status = new Label("dummy");

        this.horizontal = new HBox(10);
        this.verticalCenter = new VBox();
        this.verticalLeft = new VBox();

        this.horizontal.getChildren().addAll(sendTextField);


        this.verticalLeft.getChildren().addAll(this.comboBox, this.sendHWInfo);
        this.verticalCenter.getChildren().addAll(this.textField, this.chatDivide, horizontal, sendChat);


        //Fill Borderpane
        this.setCenter(this.borderPane);
        this.borderPane.setCenter(this.verticalCenter);
        this.borderPane.setRight(this.sendChat);
        this.borderPane.setLeft(this.verticalLeft);
        this.borderPane.setTop(this.menuBar);
        this.borderPane.setBottom(this.status);

        mqttClient = new MqttClient("cliendID");
        mqttClient.start(url);

        mqttClient.subscribe("mqttChat", this);

        /**
         *
         * Event Handler Stuff
         *
         */


        this.sendChat.addEventHandler(ActionEvent.ACTION, event -> {
            try {
                sendChatMessage(username, this.sendTextField.getText());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        //   this.sendChat.setAccelerator(KeyCombination.keyCombination("Enter"));


    }

    public void sendChatMessage(String sender, String message) throws SQLException {

        mqttClient = new MqttClient("sendChatMessage");

        mqttMessage = new unagaMQTTMessage(message, sender);
        mqttClient.start(url);


        //Debug purposes
        //System.out.print(mqttMessage);
        mqttClient.send("mqttChat", mqttMessage);
        mqttClient.stop();


    }


    /**
     * Observer Stuff
     *
     * @param o
     * @param arg
     */

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
            this.status.setText("dummy");
            this.textField.setText(" platzhalter");
        });
    }

    @Override
    public void receive(String topic, String message) throws SQLException {

        //Some tricky substrings, thanks codingbat ^^
        String date, sender, text, sqlStatement;

        sender = message.substring(message.indexOf(";") + 9, message.lastIndexOf(";"));
        text = message.substring(51, message.length() - 1);
        date = message.substring(8, 25).replace('T', ' ');
        //Date: 2015-06-23T15:14:07.518Z;Sender: Silas;Text: test
        // to 15-06-23 15:22:06


        sqlStatement = "INSERT INTO mqttChat (CREATION_TS, SENDER, MESSAGE) VALUES ('15-06-23 15:22:06', 'silas', 'test' )";

        System.out.print(sqlStatement);

        sqlClient.dbConnect();

        System.out.print(sqlClient);

        sqlClient.dbWriter(sqlStatement);
        sqlClient.dbDisconnect();
    }
}


