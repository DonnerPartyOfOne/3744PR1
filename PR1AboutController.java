package cs3744.pr1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller class for the About dialog.
 * @author Collin Blakley
 *
 */
public class PR1AboutController {
	private Stage stage;

    @FXML
    private Button OKButton;

    /**
     * function executes when the OK button is pressed.
     * closes the stage.
     * @param event
     */
    @FXML
    void okButtonPressed(ActionEvent event) {
    	stage.close();
    }
    /**
     * sets the stage for this dialog.
     * @param dialogStage
     */
	public void setDialogStage(Stage dialogStage) {
		this.stage = dialogStage;
	}

}
