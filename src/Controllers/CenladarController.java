package Controllers;

import Models.Confirm;
import Models.Task;
import com.jfoenix.controls.JFXProgressBar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CenladarController implements Initializable {
    @FXML
    private GridPane girdCanladar;
    @FXML
    private Label ThisTime;
    @FXML
    private JFXProgressBar ProgressDel;


    private ArrayList<AnchorPane> listDays;
    private ArrayList<Task> list;
    private Calendar calendar;
    private LocalDate localDate;

    private HomeController homeController;
    private Stage thisStage;

    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProgressDel.progressProperty().set(0);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void SetProgress (Double p){
        ProgressDel.progressProperty().set(p);
    }
    public void setList(ArrayList<Task> list) {
        this.list = list;
        listDays= new ArrayList<>();
        fillGrid();
    }

    private void fillGrid() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(calendar.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        int ColdayOfMonth = (cal.get(Calendar.DAY_OF_WEEK)==1)?6:cal.get(Calendar.DAY_OF_WEEK)-2;
        int num=0;
        int days=0;
        girdCanladar.getChildren().removeAll(listDays);
        listDays= new ArrayList<>();
        for(int i=1;i<7;i++){
            for(int j=0;j<7;j++){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../Views/Day.fxml"));

                try {
                    Node node = (Node) loader.load();
                    AnchorPane anchorPane= new AnchorPane();
                    anchorPane = (AnchorPane) node;
                    anchorPane.setMaxWidth(Double.MAX_VALUE);
                    anchorPane.setMaxHeight(Double.MAX_VALUE);
                    if(num>=ColdayOfMonth && days< calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
                        DayController controller = loader.getController();
                        Calendar todays = new GregorianCalendar();
                        todays.setTime(calendar.getTime());
                        days=days+1;
                        todays.set(Calendar.DAY_OF_MONTH,days);
                        controller.setDay(days,todays);
                        controller.addTask(list);
                        controller.setHomeController(homeController);
                        controller.setThisStage(thisStage);
                        controller.setCanladarControler(this);
                    }
                    num++;
                    girdCanladar.add(anchorPane,j,i);
                    listDays.add(anchorPane);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public void plusMonth(MouseEvent event){
        calendar.add(Calendar.MONTH,1);
        homeController.setCurrentCeCalendar(calendar);
        showLableDate();
        fillGrid();
    }
    public void minusMonth(MouseEvent event){
        calendar.add(Calendar.MONTH,-1);
        homeController.setCurrentCeCalendar(calendar);
        showLableDate();
        fillGrid();
    }

    public void showLableDate() {
        System.out.println(calendar.getTime().toString());
        localDate = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
        ThisTime.setText(localDate.format(DateTimeFormatter.ofPattern("MMM, yyyy")));
    }

    public void ShowToday(ActionEvent event) {
        calendar = calendar.getInstance();
        homeController.setCurrentCeCalendar(calendar);
        showLableDate();
        fillGrid();
    }
}
