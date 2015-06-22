package ch.silas.gui;

import ch.silas.Message.unagaMQTTMessage;
import ch.silas.ProbSettings;
import ch.silas.mqtt.MqttClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by Silas Stegmueller on 01.06.15.
 */
public class MainMenu extends BorderPane implements Observer {


    ProbSettings probSettings = new ProbSettings();
    String url = probSettings.loadProb("url");
    String user = probSettings.loadProb("user");
    String password = probSettings.loadProb("password");
    String topic = probSettings.loadProb("topic");
    String END_MESSAGE = probSettings.loadProb("END_MESSAGE");
    ArrayList<String> chatmessage;


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

    private TextArea textField;
    private TextField writeArea;


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


        this.textField = new TextArea();
        this.writeArea = new TextField();

        this.sendChat = new Button("Send Message");
        this.sendHWInfo = new Button("Send desired HW Info");

        this.chatDivide = new Label("Please enter message");
        this.status = new Label("dummy");

        this.horizontal = new HBox(10);
        this.verticalCenter = new VBox();
        this.verticalLeft = new VBox();

        this.horizontal.getChildren().addAll(writeArea);


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


        /**
         *
         * Event Handler Stuff
         *
         */


        this.sendChat.addEventHandler(ActionEvent.ACTION, event -> sendChatMessage("username", this.textField.getText().toString()));
        //   this.sendChat.setAccelerator(KeyCombination.keyCombination("Enter"));


    }

    public void sendChatMessage(String sender, String message) {


        mqttMessage = new unagaMQTTMessage(message, sender);

        mqttClient.send(topic, mqttMessage);
        this.receiveChatMessage();
    }


    public void receiveChatMessage() {

        this.textField.setText("latest chat recieve");


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
}
