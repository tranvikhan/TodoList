package Controllers;

import Models.DataProvider;
import com.jfoenix.controls.JFXProgressBar;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label louisville;
    @FXML
    private JFXProgressBar processBar;
    @FXML
    private FontAwesomeIcon btnShowPassword;

    private String password="";
    private Boolean checkName,checkPass;
    public void CheckLogin(ActionEvent event) throws InterruptedException {
        if(checkName && checkPass){
            Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        processBar.setVisible(true);
                        louisville.setVisible(false);
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                    }
                    return null;
                }
            };
            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent evt) {
                    if(DataProvider.getInstance().checklogin(txtUsername.getText(),password)){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("../Views/Home.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        louisville.setVisible(false);
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(getClass().getResource("../Style/Light.css").toExternalForm());
                        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                        stage.setResizable(true);
                        stage.setTitle("Todo List");
                        stage.setScene(scene);
                        HomeController homeController = loader.getController();
                        homeController.setThisStage(stage);
                        stage.centerOnScreen();
                    }else {
                        louisville.setText("Đăng nhập thất bại!");
                        louisville.setVisible(true);
                        processBar.setVisible(false);
                    }

                }
            });
            new Thread(sleeper).start();
        }else {
            louisville.setText("Vui lòng điền đầy đủ thông tin!");
            louisville.setVisible(true);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        louisville.setVisible(false);
        processBar.setVisible(false);
        btnShowPassword.setVisible(false);
        checkName=false;
        checkPass=false;
        txtUsername.textProperty().addListener((observableValue, oldValue, newValue) ->{
            if(newValue.trim().equals("")){
                checkName = false;
            }else{
                checkName=true;
                louisville.setVisible(false);

            }
        } );
        txtPassword.textProperty().addListener((observableValue, oldValue, newValue) ->{
            password = newValue;
            if(newValue.trim().equals("")){
                checkPass = false;
                btnShowPassword.setVisible(false);
            }else {
                checkPass=true;
                btnShowPassword.setVisible(true);
                louisville.setVisible(false);
            }
        } );
    }

    public void ShowPassword(MouseEvent event) {
        if(btnShowPassword.getGlyphName().equals("EYE")){
            btnShowPassword.setGlyphName("EYE_SLASH");
            txtPassword.setDisable(true);
            txtPassword.setPromptText(txtPassword.getText());
            txtPassword.setText("");
            password= txtPassword.getPromptText();
            btnShowPassword.setVisible(true);
            checkPass = true;
        }else {
            btnShowPassword.setGlyphName("EYE");
            txtPassword.setText(txtPassword.getPromptText());
            txtPassword.setDisable(false);
        }
    }

    public void changeText(KeyEvent keyEvent) {
        if(txtPassword.getText().equals("")){
            txtPassword.setPromptText("Password");
            btnShowPassword.setVisible(false);
        }
    }
}
