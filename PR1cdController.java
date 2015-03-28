package application;

import application.PR1Model.HW2Circle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PR1cdController extends BorderPane {

    @FXML
    private BorderPane CDborderpane;

    @FXML
    private Button CDokbutton;

    @FXML
    private ColorPicker CDPicker;
    
    private Stage stage;
    private HW2Circle editShape;
    
    @FXML
    private void initialize(){}
	
	@FXML
    void colorChosen(ActionEvent event) {
		editShape.setColor(CDPicker.getValue());
		stage.close();
    }

	public void setDialogStage(Stage dialogStage) {
		this.stage = dialogStage;
	}

	public void setShape(HW2Circle circle) {
		editShape = circle;
		
	}

}
