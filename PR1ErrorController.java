package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PR1ErrorController {
	
	private Stage stage;

    @FXML
    private Button ErrorOK;

    @FXML
    void ErrorOKPressed(ActionEvent event) {
    	stage.close();
    }

	public void setDialogStage(Stage dialogStage) {
		this.stage = dialogStage;
	}
}
