package Controllers;

import Models.DataProvider;
import Models.TypeTask;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import de.jensd.fx.glyphs.testapps.AwesomeIconNameComparator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NewTypeTask implements Initializable {

    @FXML
    private FlowPane ListIcon;
    @FXML
    private JFXColorPicker ColorPicker;
    @FXML
    private JFXTextField txtTypeName;
    @FXML
    private Label LableColor;
    @FXML
    private FontAwesomeIcon CheckIcon;
    @FXML
    private VBox MyForm;

    private List<Button> listAwesome;
    private String currentIconName="ANDROID";
    private String currentColor;
    private boolean checked =false;

    private static double xOffset = 0;
    private static double yOffset = 0;
    private Stage thisStage;

    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }

    private HomeController homeController;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColorPicker.valueProperty().addListener((observableValue, color, newColor) ->{
            if(!color.equals(newColor)){
                currentColor= "#"+Integer.toHexString(newColor.hashCode()).substring(0, 6).toUpperCase();
                fillListIcon();
            }
        });
        txtTypeName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue.trim().equals("")){
                checked = false;
                CheckIcon.setVisible(false);
            }else{
                CheckIcon.setVisible(true);
                if(DataProvider.getInstance().checkTypeName(newValue)){
                    checked = true;
                    CheckIcon.setFill(Color.web("#2ecc71"));
                    CheckIcon.setGlyphName("CHECK");
                }else {
                    checked =false;
                    CheckIcon.setFill(Color.web("#e74c3c"));
                    CheckIcon.setGlyphName("REMOVE");
                }
            }
        });
        ColorPicker.valueProperty().set(Color.web("#7670c4"));
        LableColor.setText("Icon: "+currentIconName);
    }
    private void fillListIcon(){
        AwesomeIconNameComparator awesomeIconNameComparator = new AwesomeIconNameComparator();
        this.listAwesome = (List) Stream.of(FontAwesomeIcons.values()).sorted(awesomeIconNameComparator).map((i) -> {
            return createIconButton(i);
        }).collect(Collectors.toList());
        ListIcon.getChildren().clear();
        ListIcon.getChildren().addAll(listAwesome);

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
    private Button createIconButton(FontAwesomeIcons icon) {
        Tooltip tooltip = new Tooltip(String.format("%s: %s", icon.name(), icon.unicodeToString()));
        Text iconText = GlyphsDude.createIcon(icon, "2em");
        iconText.setFill(Color.web(currentColor));

        Button b = new Button();
        b.setContentDisplay(ContentDisplay.CENTER);
        b.setGraphic(iconText);
        b.getStyleClass().add("BntTypeTask");
        b.setTooltip(tooltip);
        b.setPrefWidth(70);
        b.setOnAction((t) -> {
           currentIconName = icon.name();
           LableColor.setText("Icon: "+currentIconName);
        });
        return b;
    }

    public void Submit(ActionEvent event) {
        if(checked){
            TypeTask newType = new TypeTask(txtTypeName.getText(),currentIconName,currentColor);
            if(DataProvider.getInstance().InsertTypeTask(newType)){
                JFXButton btn = (JFXButton) event.getSource();
                Stage stage =(Stage) btn.getScene().getWindow();
                stage.close();
                this.homeController.fillTypeList();
            }
        }

    }

    public void CloseFrame(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();
        Stage stage =(Stage) btn.getScene().getWindow();
        stage.close();
    }
}
