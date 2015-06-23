package ch.silas;

import ch.silas.gui.MainMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Silas Stegmueller on 08.06.15.
 */
public class mainGui extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


        MainMenu mainMenu = new MainMenu();


        stage.setTitle("MQTT Chat Support");
        stage.setScene(new Scene(mainMenu));
        stage.setResizable(true);

        stage.show();


    }

    public void stop() {

        Platform.exit();
        System.exit(0);

    }

}
