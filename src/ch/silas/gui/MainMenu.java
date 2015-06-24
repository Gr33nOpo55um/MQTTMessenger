package ch.silas.gui;

import ch.silas.Message.unagaMQTTMessage;
import ch.silas.ProbSettings;
import ch.silas.backup.SilasMqttReceiver;
import ch.silas.hwINFO.ProvideHWInformation;
import ch.silas.mqtt.MqttClient;
import ch.silas.sql.SQLClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;


/**
 * Created by Silas Stegmueller on 01.06.15.
 */
public class MainMenu extends BorderPane implements SilasMqttReceiver {


    ProbSettings probSettings = new ProbSettings();
    String url = probSettings.loadProb("url");
    String username = probSettings.loadProb("username");
    private SQLClient sqlClient;
    private BorderPane borderPane;
    private HBox horizontal;
    private VBox verticalCenter, verticalLeft;
    private Menu exitM;
    private Button sendChat, sendHWInfo, flushChat;
    private MenuBar menuBar;
    private Menu Menufile;
    private MenuItem Mmqtt_disconnect, Mexit, Mmqtt_connect;
    private unagaMQTTMessage mqttMessage;
    private MqttClient mqttClient;
    private TextArea chatHistory;
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


        this.chatHistory = new TextArea();
        this.chatHistory.setEditable(false);
        this.sendTextField = new TextField();

        this.sendChat = new Button("Send Message");
        this.sendHWInfo = new Button("Send desired HW Info");
        this.flushChat = new Button("Delete Chat history");

        this.chatDivide = new Label("Please enter message");
        this.status = new Label("dummy");

        this.horizontal = new HBox(10);
        this.verticalCenter = new VBox();
        this.verticalLeft = new VBox();

        this.horizontal.getChildren().addAll(sendTextField);
        this.sendTextField.setPrefWidth(getMinWidth());


        this.verticalLeft.getChildren().addAll(this.comboBox, this.sendHWInfo, this.flushChat);
        this.verticalCenter.getChildren().addAll(this.chatHistory, this.chatDivide, horizontal, sendChat);


        //Fill Borderpane
        this.setCenter(this.borderPane);
        this.borderPane.setCenter(this.verticalCenter);
        this.borderPane.setRight(this.sendChat);
        this.borderPane.setLeft(this.verticalLeft);
        this.borderPane.setTop(this.menuBar);
        this.borderPane.setBottom(this.status);

        try {
            this.refreshChatArea();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mqttClient = new MqttClient("cliendID");
        mqttClient.start(url);

        mqttClient.subscribe("mqttChat", this);

        /**
         *
         * Event Handler Stuff
         *
         */


        this.sendChat.addEventHandler(ActionEvent.ACTION, event -> {
            sendChatMessage(username, this.sendTextField.getText());

        });

        this.flushChat.addEventHandler(ActionEvent.ACTION, event -> {
            flushChat();
        });


        this.Mexit.addEventHandler(ActionEvent.ACTION, event -> {
            Platform.exit();
            System.exit(0);
        });

        this.sendHWInfo.addEventHandler(ActionEvent.ACTION, event -> {
            ProvideHWInformation provideHWInformation = new ProvideHWInformation();
        });
        //   this.sendChat.setAccelerator(KeyCombination.keyCombination("Enter"));


    }


    /**
     * Removes all chat DB entries
     */
    private void flushChat() {
        SQLClient sqlClient = new SQLClient();

        String sqlStatement = "DELETE from chat";

        System.out.println(sqlStatement);

        sqlClient.dbConnect();

        System.out.print(sqlClient);

        sqlClient.dbWriter(sqlStatement);
        sqlClient.dbDisconnect();
        try {
            refreshChatArea();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param sender  lock at the properties file
     * @param message textfield
     */
    public void sendChatMessage(String sender, String message) {

        mqttClient = new MqttClient("sendChatMessage");

        mqttMessage = new unagaMQTTMessage(message, sender);
        mqttClient.start(url);


        //Debug purposes
        //System.out.print(mqttMessage);
        mqttClient.send("mqttChat", mqttMessage);
        mqttClient.stop();

        this.sendTextField.setText("");

        try {
            refreshChatArea();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method fetch all chat entries from db and publish it to textfield
     *
     * @throws SQLException
     */

    public void refreshChatArea() throws SQLException {
        SQLClient sqlClient = new SQLClient();


        sqlClient.dbConnect();

        System.out.print(sqlClient);

        this.chatHistory.setText(sqlClient.readChat());

        sqlClient.dbDisconnect();

        chatHistory.selectPositionCaret(chatHistory.getLength());
        chatHistory.deselect(); //removes the highlighting
    }

    /**
     * the main menu implements my mqtt listener therefore its able to listen for new events
     *
     * @param topic
     * @param message
     * @throws SQLException
     */
    @Override
    public void receive(String topic, String message) throws SQLException {

        //Some tricky substrings, thanks codingbat ^^
        String date, sender, text, sqlStatement;

        sender = message.substring(message.indexOf(";") + 9, message.lastIndexOf(";"));
        text = message.substring(51, message.length() - 1);
        date = message.substring(8, 25).replace('T', ' ');
        //Date: 2015-06-23T15:14:07.518Z;Sender: Silas;Text: test
        // to 15-06-23 15:22:06


        SQLClient sqlClient = new SQLClient();

        sqlStatement = "INSERT INTO chat" +
                " (CREATION_TS, SENDER, MESSAGE) VALUES ('" + date + "', '" + sender + "', '" + text + "' )";

        System.out.println(sqlStatement);

        sqlClient.dbConnect();

        System.out.print(sqlClient);

        sqlClient.dbWriter(sqlStatement);
        sqlClient.dbDisconnect();


    }
}


