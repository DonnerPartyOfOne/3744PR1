package application;
	
import java.io.IOException;



import application.PR1Model.Shape;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class PR1 extends Application {
	
	private Stage stage;
	
	private final static String TITLE = "PR1: collintb";
	@Override
	public void start(Stage s) {
		try{
			Parent root = FXMLLoader.load(getClass().getResource("PR1.fxml"));
			stage = s;
			Scene scene = new Scene(root, 400, 500);
			scene.getStylesheets().add(getClass().getResource("PR1.css").toExternalForm());
			stage.setTitle(TITLE); // The application window title is <title>HW2: gracanin</title>
			stage.setScene(scene);
			//goToMainWindow();
			stage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void goToCD(Shape shape) {
		try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(PR1.class.getResource("ColorDialog.fxml"));
	        BorderPane page = loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Shape Editor");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(stage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the shape in the controller so it can be modified.
	        PR1cdController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setShape(shape);
	        controller.setValues();

	        // Show the dialog.
	        dialogStage.show();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void goToAboutDialog() {
		try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(PR1.class.getResource("AboutDialog.fxml"));
	        BorderPane page = loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("About Project 1");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(stage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the stage in the controller so the dialog can close itself.
	        PR1AboutController controller = loader.getController();
	        controller.setDialogStage(dialogStage);

	        // Show the dialog and wait until the user closes it
	        dialogStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void goToErrorDialog() {
		try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(PR1.class.getResource("ErrorDialog.fxml"));
	        BorderPane page = loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("File Error");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(stage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the stage in the controller so the dialog can close itself.
	        PR1AboutController controller = loader.getController();
	        controller.setDialogStage(dialogStage);

	        // Show the dialog and wait until the user closes it
	        dialogStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void goToQuitDialog() {
		try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(PR1.class.getResource("ConfirmQuit.fxml"));
	        BorderPane page = loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("File Save");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(stage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the stage in the controller so the dialog can close itself.
	        PR1QuitController controller = loader.getController();
	        controller.setDialogStage(dialogStage);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();
	        //if the user clicked ok, exit the entire program.
	        if(controller.okClicked()) {
	        	System.exit(0);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
   
}
