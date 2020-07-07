package Controllers;

import Models.DataProvider;
import Models.Task;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AllTaskController implements Initializable {
    @FXML
    private VBox ListAllTast;
    @FXML
    private Label NameView;
    public void setViewName(String str){
        NameView.setText(str);
    }
    private ArrayList<Task> list;

    public void setList(ArrayList<Task> list) {
        this.list = list;
    }
    private HomeController homeController;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    public void DelItem(Node n){
        ListAllTast.getChildren().remove(n);
        homeController.refeshData();
    }
    public void fillList() {
        ArrayList<Node> listnode = new ArrayList<>();
        for(Task t : list){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../Views/TaskItem.fxml"));
                Node node = loader.load();
                TaskItem control  = loader.getController();
                control.FillData(t);
                control.setAllTaskController(this);
                control.setHomeController(homeController);
                listnode.add(node);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ListAllTast.getChildren().addAll(listnode);
    }

    public void EditItem() {
        homeController.refeshData();
    }
}
