package Controllers;

import Models.Confirm;
import Models.DataProvider;
import Models.Task;
import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class DayController implements Initializable {
    @FXML
    private Label daynumber;
    @FXML
    private VBox MyListTask;

    private Stage thisStage = null;
    private HomeController homeController;
    private Calendar thisDay;
    private CenladarController cenladarController;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }

    public void setDay(int num, Calendar thisday){
        daynumber.setText(String.valueOf(num));
        Calendar calendar = Calendar.getInstance();
        this.thisDay=thisday;
        LocalDate localDate = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
        LocalDate localDate2 = LocalDateTime.ofInstant(thisday.toInstant(), thisday.getTimeZone().toZoneId()).toLocalDate();
        if(localDate.equals(localDate2)){
            daynumber.getStyleClass().add("thisToday");
        }

    }
    public void addTask(ArrayList<Task> list){
        for (Task t: list){
            LocalDate day = LocalDateTime.ofInstant(thisDay.toInstant(), thisDay.getTimeZone().toZoneId()).toLocalDate();
            if(t.getDay().isEqual(day)){
                JFXButton btn = new JFXButton(t.getTitle());
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setStyle("-fx-text-fill: #ffffff;-fx-background-color: "+t.getType().getColor());
                final AnimationTimer timer = new AnimationTimer() {
                    private long lastUpdate = 0;
                    @Override
                    public void handle(long time) {
                        if (System.currentTimeMillis() - lastUpdate > 1000) {
                            try {
                                if(DataProvider.getInstance().DelTask(String.valueOf(t.getId()))){
                                    homeController.refeshData();
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            stop();

                        }else {
                            Double num = Double.valueOf(System.currentTimeMillis()-lastUpdate);
                            num= num /1000;
                            cenladarController.SetProgress(num);
                        }
                    }
                    @Override
                    public void start() {
                        super.start();
                        lastUpdate = System.currentTimeMillis();
                    }
                };

                btn.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                            timer.start();
                        } else {
                            timer.stop();
                            cenladarController.SetProgress(Double.valueOf(0));
                        }
                    }
                });
                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getClickCount() == 2){
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../Views/EditTast.fxml"));
                            Parent pr = null;
                            try {
                                pr = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            EditTaskController controller = loader.getController();
                            controller.setTask(t);
                            controller.filldataTask();
                            Scene scene = new Scene(pr,365,470);
                            scene.getStylesheets().add(thisStage.getScene().getStylesheets().get(0));
                            scene.setFill(Color.TRANSPARENT);
                            scene.getStylesheets().add(homeController.getThisStage().getScene().getStylesheets().get(0));
                            Stage stage = new Stage();
                            stage.setResizable(false);
                            stage.setScene(scene);
                            stage.initStyle(StageStyle.TRANSPARENT);
                            stage.initOwner(thisStage);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            controller.setThisStage(stage);
                            controller.setHomeController(homeController);
                            stage.showAndWait();
                        }

                    }
                });
                MyListTask.getChildren().add(btn);
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setCanladarControler(CenladarController cenladarController) {
        this.cenladarController = cenladarController;
    }

    public void AddDay(MouseEvent event) {
        if(!this.daynumber.getText().equals("")){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../Views/AddTast.fxml"));
                Parent pr = loader.load();
                AddTaskController controller = loader.getController();
                controller.setHomeController(homeController);
                Scene scene = new Scene(pr,365,470);
                scene.getStylesheets().add(homeController.getThisStage().getScene().getStylesheets().get(0));
                scene.setFill(Color.TRANSPARENT);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initOwner(thisStage);
                stage.initModality(Modality.APPLICATION_MODAL);
                controller.setThisStage(stage);
                controller.setDay(thisDay);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
