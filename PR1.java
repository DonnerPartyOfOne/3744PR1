package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class PR1 extends Application {
	
	private final static String TITLE = "PR1: collintb";
	@Override
	public void start(Stage stage) {
		try{
			Parent root = FXMLLoader.load(getClass().getResource("PR1.fxml"));
			
			Scene scene = new Scene(root, 400, 500);
			scene.getStylesheets().add(getClass().getResource("PR1.css").toExternalForm());
			stage.setTitle(TITLE); // The application window title is <title>HW2: gracanin</title>
			stage.setScene(scene);
			stage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
