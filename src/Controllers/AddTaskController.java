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
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;


public class AddTaskController implements Initializable {
    private HomeController homeController;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
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
            CmbType.setValue(list.get(0).getName());
            datePicker.setValue(LocalDate.now());
            timePicker.set24HourView(true);
            timePicker.setValue(LocalTime.of(7,0,0));
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
    public void CloseFrame(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();
        Stage stage =(Stage) btn.getScene().getWindow();
        stage.close();

    }
    public void Submit(ActionEvent event) throws SQLException {
        if(checkInput()){
            Task mytask = new Task();
            mytask.setTitle(txtTitle.getText());
            mytask.setContent(txtContent.getText());
            mytask.setType(DataProvider.getInstance().getTypeTask(CmbType.getSelectionModel().getSelectedItem().toString()));
            mytask.setDay(datePicker.getValue());
            mytask.setTime(timePicker.getValue());

            if(DataProvider.getInstance().AddTask(mytask)){
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

    public void setDay(Calendar thisDay) {
        datePicker.setValue(LocalDateTime.ofInstant(thisDay.toInstant(), thisDay.getTimeZone().toZoneId()).toLocalDate());
    }
}
