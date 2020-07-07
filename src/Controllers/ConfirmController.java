package Controllers;

import Models.Confirm;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmController implements Initializable {

    private Confirm confirm;
    @FXML
    private FontAwesomeIcon Icon;
    @FXML
    private JFXButton BtnOk,BtnCancel;
    @FXML
    private Label Content;
    @FXML
    private HBox BtnGroup;
    @FXML
    private AnchorPane MyForm;
    private Stage thisStage;
    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }

    private static double xOffset = 0;
    private static double yOffset = 0;


    public void setConfirm(Confirm confirm) {
        this.confirm = confirm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyForm.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = thisStage.getX() - event.getScreenX();
                yOffset = thisStage.getY() - event.getScreenY();
            }
        });
        MyForm.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                thisStage.setX(event.getScreenX() + xOffset);
                thisStage.setY(event.getScreenY() + yOffset);
            }
        });
    }
    public void SetData(String icon,String iconColor, String content, boolean isAlert, String okBtnName, String cancelBtnName) {
        this.Icon.setGlyphName(icon);
        this.Icon.setFill(Color.web(iconColor));
        this.Content.setText(content);
        this.BtnOk.setText(okBtnName);
        this.BtnCancel.setText(cancelBtnName);
        if(isAlert){
            BtnGroup.getChildren().remove(BtnOk);
        }
    }

    public void setOk(MouseEvent event) {
        confirm.setStatus(true);
        close(event);
    }

    public void setCancel(MouseEvent event) {
        confirm.setStatus(false);
        close(event);
    }
    private void close(MouseEvent event){
        JFXButton btn = (JFXButton) event.getSource();
        Stage stage =(Stage) btn.getScene().getWindow();
        stage.close();
    }
}
