package Models;

import Controllers.ConfirmController;
import Controllers.NewTypeTask;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Confirm {
    private String Icon;
    private String IconColor;
    private String content;
    private boolean isAlert;
    private String OkBtnName;
    private String CancelBtnName;
    private Stage RootStage;

    private Boolean Status;


    public boolean ShowDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/ConfirmForm.fxml"));
            Parent pr = loader.load();
            ConfirmController controller = loader.getController();
            controller.setConfirm(this);
            controller.SetData(Icon,IconColor,content,isAlert,OkBtnName,CancelBtnName);
            Scene scene = new Scene(pr,321,190);
            scene.getStylesheets().add(RootStage.getScene().getStylesheets().get(0));
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initOwner(RootStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            controller.setThisStage(stage);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Status;
    }

    public Confirm(String icon, String iconColor, String content, boolean isAlert, String okBtnName, String cancelBtnName, Stage rootStage) {
        Icon = icon;
        IconColor = iconColor;
        this.content = content;
        this.isAlert = isAlert;
        OkBtnName = okBtnName;
        CancelBtnName = cancelBtnName;
        RootStage = rootStage;
        Status = false;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getIconColor() {
        return IconColor;
    }

    public void setIconColor(String iconColor) {
        IconColor = iconColor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setAlert(boolean alert) {
        isAlert = alert;
    }

    public String getOkBtnName() {
        return OkBtnName;
    }

    public void setOkBtnName(String okBtnName) {
        OkBtnName = okBtnName;
    }

    public String getCancelBtnName() {
        return CancelBtnName;
    }

    public void setCancelBtnName(String cancelBtnName) {
        CancelBtnName = cancelBtnName;
    }

    public Stage getRootStage() {
        return RootStage;
    }

    public void setRootStage(Stage rootStage) {
        RootStage = rootStage;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }
}