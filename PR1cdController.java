package cs3744.pr1;

import cs3744.pr1.PR1Model.Shape;
import cs3744.pr1.PR1Model.ShapeType;
import cs3744.pr1.PR1Model.ShapeTypeStringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * Shape Editor controller class.
 * @author Collin Blakley
 *
 */
public class PR1cdController extends BorderPane {
	
	
    @FXML
    private TextField xPosBox;

    @FXML
    private TextField awBox;

    @FXML
    private TextField rBox;
    
    @FXML
    private TextField yPosBox;
    
    @FXML
    private TextField tBox;

    @FXML
    private TextField wBox;

    @FXML
    private TextField hBox;

    @FXML
    private TextField ahBox;

    @FXML
    private BorderPane CDborderpane;

    @FXML
    private Button CDokbutton;

    @FXML
    private ColorPicker CDPicker;
    
    @FXML
    private ComboBox<String> typeBox;
    
    private Stage stage;
    private Shape editShape;
    
    /**
     * Initialize is called when the FXML loads.
     * It adds the possible shape types to the box
     * so the user can see their options.
     */
    @FXML
    private void initialize() {
    	typeBox.getItems().addAll("circle", "oval", "rect", "roundrect", "text");
    }
    
    /**
     * This function sets the option boxes in the GUI to their 
     * corresponding values already set in the model.
     */
    public void setValues() {
    	ShapeTypeStringConverter convert = new ShapeTypeStringConverter();
    	CDPicker.setValue(editShape.getColor());
    	typeBox.setValue(convert.toString(editShape.getType()));
    	xPosBox.setText(Double.toString(editShape.getCenterX()));
    	yPosBox.setText(Double.toString(editShape.getCenterY()));
    	rBox.setText(Double.toString(editShape.getRadius()));
    	wBox.setText(Double.toString(editShape.getWidth()));
    	hBox.setText(Double.toString(editShape.getHeight()));
    	awBox.setText(Double.toString(editShape.getAW()));
    	ahBox.setText(Double.toString(editShape.getAH()));
    	tBox.setText(editShape.getText());
    }
	
    /**
     * This function applies all the changed parameters in the dialog
     * to the shape that was passed to this controller.
     * @param event
     */
	@FXML
    void colorChosen(ActionEvent event) {
		editShape.setColor(CDPicker.getValue());
		if(typeBox.getValue().equals("circle")) editShape.setType(ShapeType.CIRCLE);
		else if (typeBox.getValue().equals("rect")) editShape.setType(ShapeType.RECTANGLE);
		else if (typeBox.getValue().equals("oval")) editShape.setType(ShapeType.OVAL);
		else if (typeBox.getValue().equals("roundrect")) editShape.setType(ShapeType.ROUNDRECT);
		else if (typeBox.getValue().equals("text")) editShape.setType(ShapeType.TEXT);
		else editShape.setType(ShapeType.UNKNOWN);
		
		editShape.setCenterX(Double.parseDouble(xPosBox.getText()));
		editShape.setCenterY(Double.parseDouble(yPosBox.getText()));
		editShape.setRadius(Double.parseDouble(rBox.getText()));
		editShape.setWidth(Double.parseDouble(wBox.getText()));
		editShape.setHeight(Double.parseDouble(hBox.getText()));
		editShape.setArcwidth(Double.parseDouble(awBox.getText()));
		editShape.setArcheight(Double.parseDouble(ahBox.getText()));
		editShape.setText(tBox.getText());
		stage.close();
    }

	/**
	 * sets the stage for this dialog.
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.stage = dialogStage;
	}

	
	/**
	 * sets the shape to be edited by this dialog.
	 */
	public void setShape(Shape shape) {
		editShape = shape;
		
	}

}
