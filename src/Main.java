import Models.DataProvider;
import Models.SQLConnect;
import Models.TypeTask;
import com.jfoenix.controls.JFXDatePicker;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primarystage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Views/Login.fxml"));
            Scene scene = new Scene(root,900,600);
            scene.getStylesheets().add(getClass().getResource("Style/Light.css").toExternalForm());
            primarystage.setTitle("Login");
            primarystage.setResizable(false);
            primarystage.getIcons().add(new Image(getClass().getResourceAsStream("Image/2.png")));
            primarystage.setScene(scene);
            primarystage.centerOnScreen();
            primarystage.show();

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
