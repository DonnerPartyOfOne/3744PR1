package cs3744.pr1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This class is the controller for the quit dialog.
 * @author Collin Blakley
 *
 */
public class PR1QuitController {

	private Stage stage;
	private boolean okPressed = false;
	
    @FXML
    private Button CancelButton;

    @FXML
    private Button OKButton;

    /**
     * This function executes if the OK button is pressed.
     * It sets the okPressed boolean and closes the stage.
     * The boolean is passed back up to the Main class
     * so that the entire program can be exited.
     * @param event
     */
    @FXML
    void OKButtonPressed(ActionEvent event) {
    	okPressed = true;
    	stage.close();
    }

    /**
     * Function executes if the cancel button is pressed.
     * Simply closes the stage.
     * @param event
     */
    @FXML
    void CancelButtonPressed(ActionEvent event) {
    	okPressed = false;
    	stage.close();
    }
    
    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
	public void setDialogStage(Stage dialogStage) {
		this.stage = dialogStage;
	}
	
	/**
	 * function to return the status of the okPressed boolean
	 * up to the Main class so it can close if it needs to.
	 * @return
	 */
	public boolean okClicked() {
		return okPressed;
	}

}
