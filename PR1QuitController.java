package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PR1QuitController {

	private Stage stage;
	private boolean okPressed = false;
	
    @FXML
    private Button CancelButton;

    @FXML
    private Button OKButton;

    @FXML
    void OKButtonPressed(ActionEvent event) {
    	okPressed = true;
    	stage.close();
    }

    @FXML
    void CancelButtonPressed(ActionEvent event) {
    	okPressed = false;
    	stage.close();
    }
    
	public void setDialogStage(Stage dialogStage) {
		this.stage = dialogStage;
	}
	
	public boolean okClicked() {
		return okPressed;
	}

}
