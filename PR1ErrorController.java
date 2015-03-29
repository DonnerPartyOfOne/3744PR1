package cs3744.pr1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This class is the controller for the error dialog.
 * @author Collin Blakley
 *
 */
public class PR1ErrorController {
	
	private Stage stage;

    @FXML
    private Button ErrorOK;

    /**
     * This function executes when the OK button is pressed.
     * closes the stage.
     * @param event
     */
    @FXML
    void ErrorOKPressed(ActionEvent event) {
    	stage.close();
    }

    /**
     * Sets the stage for this dialog.
     * @param dialogStage
     */
	public void setDialogStage(Stage dialogStage) {
		this.stage = dialogStage;
	}
}
