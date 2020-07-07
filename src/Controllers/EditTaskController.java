package Controllers;

import Models.DataProvider;
import Models.Task;
import Models.TypeTask;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.net.ssl.HostnameVerifier;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditTaskController implements Initializable {

    private Task task;

    public void setTask(Task task) {
        this.task = task;
    }
    @FXML
    private JFXTextField txtTitle;
    @FXML
    private JFXComboBox CmbType;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePicker;
    @FXML
    private JFXTextArea txtContent;
    @FXML
    private Label lableTitles;
    @FXML
    private VBox MyForm;

    private Stage thisStage;
    private HomeController homeController;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ArrayList<TypeTask> list = DataProvider.getInstance().getListType();
            for(TypeTask typeTask:list){
                CmbType.getItems().add(typeTask.getName());
            }
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

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void filldataTask() {
        CmbType.setValue(task.getType().getName());
        datePicker.setValue(task.getDay());
        timePicker.set24HourView(true);
        timePicker.setValue(task.getTime());
        this.txtTitle.setText(task.getTitle());
        this.txtContent.setText(task.getContent());
    }

    public void CloseFrame(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();
        Stage stage =(Stage) btn.getScene().getWindow();
        stage.close();

    }
    public void Submit(ActionEvent event) throws SQLException {
        if(checkInput()){
            task.setTitle(txtTitle.getText());
            task.setContent(txtContent.getText());
            task.setType(DataProvider.getInstance().getTypeTask(CmbType.getSelectionModel().getSelectedItem().toString()));
            task.setDay(datePicker.getValue());
            task.setTime(timePicker.getValue());

            if(DataProvider.getInstance().EditTask(task)){
                homeController.refeshData();
                JFXButton btn = (JFXButton) event.getSource();
                Stage stage =(Stage) btn.getScene().getWindow();
                stage.close();
            }
        }
    }

    private boolean checkInput() {
        if(txtTitle.getText().equals("")){
            lableTitles.getStyleClass().add("erro");
            return false;
        }
        return true;
    }
}
