package Controllers;

import Models.Confirm;
import Models.DataProvider;
import Models.TypeTask;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TypeTaskController {
    @FXML
    private FontAwesomeIcon icon;
    @FXML
    private Label name;
    @FXML
    private FontAwesomeIcon DelIcon;

    private HomeController homeController;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void fillData(TypeTask t){
        icon.setFill(Color.web(t.getColor()));
        icon.setGlyphName(t.getIcon());
        name.setText(t.getName());
    }
    public void ShowType(MouseEvent event){
        if(!name.getText().equals("New Type"))
            homeController.ShowTaskInType(name.getText());
        else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../Views/NewTypeTask.fxml"));
                Parent pr = loader.load();
                NewTypeTask controller = loader.getController();
                controller.setHomeController(homeController);
                Scene scene = new Scene(pr,395,480);
                scene.getStylesheets().add(homeController.getThisStage().getScene().getStylesheets().get(0));
                scene.setFill(Color.TRANSPARENT);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initOwner(((Node)event.getSource()).getScene().getWindow());
                stage.initModality(Modality.APPLICATION_MODAL);
                controller.setThisStage(stage);
                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void ShowDel(MouseEvent event) {
        if(!name.getText().equals("New Type"))
            DelIcon.setVisible(true);
    }

    public void HideDel(MouseEvent event) {
            DelIcon.setVisible(false);
    }

    public void DelTypeAction(MouseEvent event) {
        Stage thisStage =(Stage)((Node)event.getSource()).getScene().getWindow();
        if(DataProvider.getInstance().checkDelType(this.name.getText())){
            Confirm confirm1 = new Confirm("TRASH","#e74c3c","Bạn chắc chắn muốn xóa: "+this.name.getText(),false,"YES","NO",thisStage);
            if(confirm1.ShowDialog()){
                if(DataProvider.getInstance().DelType(this.name.getText())){
                    homeController.defaultView();
                    homeController.fillTypeList();
                }else {
                    Confirm confirm2 = new Confirm("WARNING","#f39c12","Lỗi!",true,"","CLOSE",thisStage);
                    confirm2.ShowDialog();
                }
            }
        }else {
            Confirm confirm3 = new Confirm("WARNING","#f39c12","Không thể xóa: "+this.name.getText(),true,"","CLOSE",thisStage);
            confirm3.ShowDialog();
        }
    }
}
