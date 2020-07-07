package Controllers;

import Models.Confirm;
import Models.DataProvider;
import Models.Task;
import Models.TypeTask;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private BorderPane mainPanel;
    @FXML
    private VBox leftMenuPanel,VBoxType;
    @FXML
    private ScrollPane labelListPanel,NotificationPanel,SettingPanel;
    @FXML
    private FontAwesomeIcon moreIcon,iconSortDay,iconSortTime;
    @FXML
    private HBox btnAllTast,btnToday,btnCalender2,btnLateTask,SideRight;
    @FXML
    private Label CountTaskToday,CountTaskLate;
    @FXML
    private TextField txtSearch;
    @FXML
    private JFXToggleButton ChangeLayout;

    private int menuindex;
    private String menuTypeTaskLabe;
    private ArrayList<Task> listTasks;
    private ArrayList<Task> TaskTodayList;
    private  ArrayList<Task> SearchList;
    private  ArrayList<Task> lateTasks;
    private int SortDayStatus=0;
    private int SortTimeStatus=0;

    private Calendar currentCeCalendar;

    public void setCurrentCeCalendar(Calendar currentCeCalendar) {
        this.currentCeCalendar = currentCeCalendar;
    }

    private  Stage thisStage;
    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }

    public Stage getThisStage() {
        return thisStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuindex=0;
        refeshData();
        fillTypeList();
        SideRight.setPrefWidth(0);
        SettingPanel.setPrefWidth(0);
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) ->{
            showSearch(newValue);
        } );
        currentCeCalendar = Calendar.getInstance();
    }
    public void showSearch(String str){
        SearchList = new ArrayList<>();
        AllTaskController controller = addToPanel("AllTask").getController();
        controller.setViewName("Search: "+str);
        if(str.trim().equals("")){
            defaultView();
            return;
        }else {
            for(Task t: listTasks){
                if(str.substring(0,1).equals("#") && str.length()>1){
                    String strSub=str.substring(1).toLowerCase();
                    if(t.getType().getName().toLowerCase().matches("(.*)"+strSub+"(.*)")){
                        SearchList.add(t);
                    }
                }else {
                    str=str.toLowerCase();
                    if(t.getTitle().toLowerCase().matches("(.*)"+str+"(.*)") || t.getContent().toLowerCase().matches("(.*)"+str+"(.*)")){
                        SearchList.add(t);
                    }
                }
            }
        }
        controller.setList(SearchList);
        controller.setHomeController(this);
        controller.fillList();
        menuindex=5;
    }
    public void fillTypeList() {
        ArrayList<TypeTask> listType;
        VBoxType.getChildren().clear();
        try {
            listType = DataProvider.getInstance().getListType();
            for (TypeTask typeTask:listType){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../Views/TypeTask.fxml"));
                Node node = loader.load();
                TypeTaskController control  = loader.getController();
                control.setHomeController(this);
                control.fillData(typeTask);
                VBoxType.getChildren().add(node);
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/TypeTask.fxml"));
            Node node = loader.load();
            TypeTaskController control  = loader.getController();
            control.setHomeController(this);
            VBoxType.getChildren().add(node);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public void defaultView() {
        AllTaskController controller = addToPanel("AllTask").getController();
        controller.setList(listTasks);
        controller.setHomeController(this);
        controller.fillList();
        controller.setViewName("All Task");
        menuindex=0;
        btnAllTast.getStyleClass().add("MenuActive");
    }


    public FXMLLoader addToPanel (String layoutName){
        Parent rootPanel = null;
        FXMLLoader loader = new FXMLLoader();
        try{
            loader.setLocation(getClass().getResource("../Views/"+layoutName+".fxml"));
            rootPanel = loader.load();
            for(Node node: leftMenuPanel.getChildren()){
                node.getStyleClass().remove("MenuActive");
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        mainPanel.setCenter(rootPanel);
        return loader;
    }


    public void HideShowMenu(MouseEvent mouseEvent) {
        TranslateTransition openNav=new TranslateTransition(new Duration(350), leftMenuPanel);
        openNav.setToX(0);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), leftMenuPanel);
        closeNav.setOnFinished(event -> {
            leftMenuPanel.setPrefWidth(0);
        });
        if(leftMenuPanel.getTranslateX()!=0){
            leftMenuPanel.setPrefWidth(277);
            leftMenuPanel.setLayoutX(0);
            openNav.play();

        }else{
            closeNav.setToX(-(leftMenuPanel.getWidth()));
            closeNav.play();
        }
    }

    public void HomeBtnClick(MouseEvent event){
        defaultView();
    }

    public void AddTastShow(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/AddTast.fxml"));
            Parent pr = loader.load();
            AddTaskController controller = loader.getController();
            controller.setHomeController(this);
            Scene scene = new Scene(pr,365,470);
            scene.getStylesheets().add(thisStage.getScene().getStylesheets().get(0));
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initOwner(thisStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            controller.setThisStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void ShowAllTast(MouseEvent event){
        AllTaskController controller = addToPanel("AllTask").getController();
        controller.setList(listTasks);
        controller.setHomeController(this);
        controller.setViewName("All Task");
        controller.fillList();
        Node node = (Node) event.getSource();
        node.getStyleClass().add("MenuActive");
        menuindex=0;
    }
    public void ShowCenladar(MouseEvent event){
        CenladarController controller = addToPanel("Cenladar").getController();
        controller.setHomeController(this);
        controller.setCalendar(currentCeCalendar);
        controller.setThisStage(thisStage);
        controller.setList(listTasks);
        controller.showLableDate();
        btnCalender2.getStyleClass().add("MenuActive");
        menuindex=1;
    }
    public void ShowCatalog(MouseEvent event){
        if(labelListPanel.isVisible()){
            labelListPanel.setVisible(false);
            moreIcon.setGlyphName("CHEVRON_RIGHT");

        }else {
            labelListPanel.setVisible(true);
            moreIcon.setGlyphName("CHEVRON_DOWN");
        }
    }
    public void refeshData(){
        try {
            listTasks = DataProvider.getInstance().getListTask(SortDayStatus,SortTimeStatus);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        CheckNotificationTask();
        switch (menuindex){
            case 0:defaultView();break;
            case 1:ShowCenladar(null);break;
            case 3:ShowTodayTask(null);break;
            case 4:ShowTaskInType(menuTypeTaskLabe);break;
            case 5:showSearch(txtSearch.getText());break;
            case 6:ShowLateTask(null);break;
        }
    }
    public void ShowTodayTask(MouseEvent event){
        AllTaskController controller = addToPanel("AllTask").getController();
        controller.setList(TaskTodayList);
        controller.setHomeController(this);
        controller.setViewName("Today");
        controller.fillList();
        menuindex=3;
        btnToday.getStyleClass().add("MenuActive");
    }
    public  void ShowTaskInType(String typeName){
        ArrayList<Task> listTasksType = new ArrayList<>();
        for(Task t:listTasks){
            if(t.getType().getName().equals(typeName)) {
                listTasksType.add(t);
            }
        }
        AllTaskController controller = addToPanel("AllTask").getController();
        controller.setList(listTasksType);
        controller.setHomeController(this);
        controller.setViewName("#"+typeName);
        controller.fillList();
        menuindex=4;
        menuTypeTaskLabe=typeName;
    }

    private void CheckNotificationTask() {
        LocalDate now = LocalDate.now();
        TaskTodayList = new ArrayList<>();
        lateTasks = new ArrayList<>();
        CountTaskToday.setVisible(true);
        CountTaskLate.setVisible(true);
        int countToday=0;
        int countLate=0;
        for(Task t:listTasks){
            if(t.getDay().equals(now)) {
                countToday++;
                TaskTodayList.add(t);
            }
            if(t.getDay().isBefore(now)){
                countLate++;
                lateTasks.add(t);
            }
        }
        if(countToday ==0){
            CountTaskToday.setVisible(false);
        }
        else if(countToday<100){
            CountTaskToday.setText(String.valueOf(countToday));
        }else {
            CountTaskToday.setText("+");
        }
        if(countLate ==0){
            CountTaskLate.setVisible(false);
        }
        else if(countLate<100){
            CountTaskLate.setText(String.valueOf(countLate));
        }else {
            CountTaskLate.setText("+");
        }
    }

    public void ShowLateTask(MouseEvent event) {
        AllTaskController controller = addToPanel("AllTask").getController();
        controller.setList(lateTasks);
        controller.setHomeController(this);
        controller.setViewName("Late Tasks");
        controller.fillList();
        menuindex=6;
        btnLateTask.getStyleClass().add("MenuActive");
    }

    public void SortTimeClick(ActionEvent event) {
        if(SortTimeStatus==0){
            SortTimeStatus=1;
            iconSortTime.setGlyphName("SORT_AMOUNT_DESC");
        }else {
            SortTimeStatus=0;
            iconSortTime.setGlyphName("SORT_AMOUNT_ASC");
        }
        refeshData();
    }

    public void SortDayClick(ActionEvent event) {

        if(SortDayStatus==0){
            SortDayStatus=1;
            iconSortDay.setGlyphName("SORT_AMOUNT_DESC");
        }else {
            SortDayStatus=0;
            iconSortDay.setGlyphName("SORT_AMOUNT_ASC");
        }
        refeshData();
    }

    public void ShowNotification(MouseEvent event) {
        if(SideRight.getPrefWidth()==0){
            SideRight.setPrefWidth(210);
            NotificationPanel.setPrefWidth(210);
        }else {
            if(NotificationPanel.getPrefWidth()==210){
                SideRight.setPrefWidth(0);
                NotificationPanel.setPrefWidth(0);
            }else {
                SettingPanel.setPrefWidth(0);
                NotificationPanel.setPrefWidth(210);
            }
        }
    }
    public void ShowSettings(MouseEvent event) {
        if(SideRight.getPrefWidth()==0){
            SideRight.setPrefWidth(210);
            SettingPanel.setPrefWidth(210);
        }else {
            if(SettingPanel.getPrefWidth()==210){
                SideRight.setPrefWidth(0);
                SettingPanel.setPrefWidth(0);
            }else {
                NotificationPanel.setPrefWidth(0);
                SettingPanel.setPrefWidth(210);
            }
        }
    }

    public void LoginOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/Login.fxml"));
        Scene scene = new Scene(root,900,600);
        scene.getStylesheets().add(getClass().getResource("../Style/Light.css").toExternalForm());
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../Image/2.png")));
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void ChangeLayoutAction(ActionEvent event) {
        Scene scene = thisStage.getScene();
        if(!ChangeLayout.isSelected()){
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("../Style/Light.css").toExternalForm());
        }else {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("../Style/Dark.css").toExternalForm());
        }
    }
}
