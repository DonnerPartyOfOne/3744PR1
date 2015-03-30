package cs3744.pr1;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import cs3744.pr1.PR1Model.Shape;
import cs3744.pr1.PR1Model.ShapeType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

/**
 * Project 1 controller class. Gets values for all the GUI elements injected
 * by the FXMLLoader.
 * 
 * @author Collin Blakley
 *
 */

public class PR1Controller extends BorderPane{
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;

    @FXML // fx:id="fileOpen"
    private MenuItem fileOpen; // Value injected by FXMLLoader

    @FXML // fx:id="fileClose"
    private MenuItem fileClose; // Value injected by FXMLLoader

    @FXML // fx:id="scrpaneleft"
    private ScrollPane scrpaneleft; // Value injected by FXMLLoader

    @FXML // fx:id="canvas"
    private Canvas canvas; // Value injected by FXMLLoader

    @FXML // fx:id="tv"
    private TableView<Shape> tv; // Value injected by FXMLLoader

    @FXML // fx:id="tcr"
    private TableColumn<Shape, Double> tcr; // Value injected by FXMLLoader

    @FXML // fx:id="editPaste"
    private MenuItem editPaste; // Value injected by FXMLLoader

    @FXML // fx:id="tcs"
    private TableColumn<Shape, ShapeType> tcs; // Value injected by FXMLLoader

    @FXML // fx:id="borderpane"
    private BorderPane borderpane; // Value injected by FXMLLoader

    @FXML // fx:id="scrpaneright"
    private ScrollPane scrpaneright; // Value injected by FXMLLoader

    @FXML // fx:id="menuFile"
    private Menu menuFile; // Value injected by FXMLLoader

    @FXML // fx:id="about"
    private MenuItem about; // Value injected by FXMLLoader

    @FXML // fx:id="editCopy"
    private MenuItem editCopy; // Value injected by FXMLLoader

    @FXML // fx:id="fileQuit"
    private MenuItem fileQuit; // Value injected by FXMLLoader

    @FXML // fx:id="fileNew"
    private MenuItem fileNew; // Value injected by FXMLLoader

    @FXML // fx:id="fileSave"
    private MenuItem fileSave; // Value injected by FXMLLoader

    @FXML // fx:id="tccx"
    private TableColumn<Shape, Double> tccx; // Value injected by FXMLLoader

    @FXML // fx:id="tccy"
    private TableColumn<Shape, Double> tccy; // Value injected by FXMLLoader

    @FXML // fx:id="tcc"
    private TableColumn<Shape, Color> tcc; // Value injected by FXMLLoader

    @FXML // fx:id="mb"
    private MenuBar mb; // Value injected by FXMLLoader

    @FXML // fx:id="menuEdit"
    private Menu menuEdit; // Value injected by FXMLLoader

    @FXML // fx:id="editDelete"
    private MenuItem editDelete; // Value injected by FXMLLoader

    @FXML // fx:id="sp"
    private SplitPane sp; // Value injected by FXMLLoader

    @FXML // fx:id="menuHelp"
    private Menu menuHelp; // Value injected by FXMLLoader
    
    @FXML 
    private TableColumn<Shape, Double> tcw;
    
    @FXML
    private TableColumn<Shape, Double> tch;
    
    @FXML
    private TableColumn<Shape, Double> tcaw;
    
    @FXML
    private TableColumn<Shape, Double> tcah;
    
    @FXML
    private TableColumn<Shape, String> tct;
    
    @FXML
    private TableColumn<Shape, Boolean> tcd;
    
    @FXML
    private MenuItem zIn;
    
    @FXML
    private MenuItem zOut;
    
    
    private PR1 application;
    
    /**
     * Sets the application so we can pass off to other controllers
     * @param application
     */
    public void setApp(PR1 application){
        this.application = application;
    }
	
	private Stage stage = null;
	private PR1Model m = null;
	private GraphicsContext gc = null;
	
	public static enum ViewState { NEW, OPEN, OPENED, CLOSE, CLOSED, MODIFIED, SAVE, QUIT, RESIZE};
	private ObjectProperty<ViewState> viewState = null;
	public static enum ClipboardState { IDLE, COPY, PASTE, DELETE};
	private ObjectProperty<ClipboardState> clipboardState = null;
	private ChangeListener<Number> canvasListener = null;
	private File file = null;
	
	private ObjectProperty<Shape> selection = null;
	
	private double mouseLastX = -1;
	private double mouseLastY = -1;
	
	private Shape clipboard = null;
	private Shape defaultS = null;
	private static final String defaultshape = "DEFAULT SHAPE";
	
	/**
	 * Initialize is called when the FXML is loaded. set up all our default values,
	 * resizing, bindings, cell factories, event handlers, etc.
	 * 
	 * 	 
	 * 
	 * */
	@FXML
	void initialize() {
		m = new PR1Model();
		setFile(null);
		selection = new SimpleObjectProperty<Shape>();
		application = new PR1();
		setSelection(null);
		setClipboard(null);
		viewState = new SimpleObjectProperty<ViewState>(ViewState.CLOSE);
		clipboardState = new SimpleObjectProperty<ClipboardState>(ClipboardState.IDLE);
		defaultS = m.add(-50, -50);
		defaultS.setText(defaultshape);
		
		
		canvasListener = new ChangeListener<Number>() {

			/**
			 * Handles the canvas resizing.
			 * 
			 * @param event The event object.
			 */
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				viewState.set(ViewState.RESIZE);
				
			}
		};
		

		
		gc = canvas.getGraphicsContext2D();
		canvas.heightProperty().set(1105);
		canvas.widthProperty().set(1105);
		canvas.heightProperty().addListener(canvasListener);
		canvas.widthProperty().addListener(canvasListener);
		
		
		tcs.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tccx.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tccy.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tcr.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tcc.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tcw.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tch.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tcaw.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tcah.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tct.prefWidthProperty().bind(tv.widthProperty().divide(11));
		tcd.prefWidthProperty().bind(tv.widthProperty().divide(11));
		
		tcs.setCellValueFactory(new PropertyValueFactory<Shape, ShapeType>("type"));
		tccx.setCellValueFactory(new PropertyValueFactory<Shape, Double>("centerX"));
		tccy.setCellValueFactory(new PropertyValueFactory<Shape, Double>("centerY"));
		tcr.setCellValueFactory(new PropertyValueFactory<Shape, Double>("radius"));
		tcc.setCellValueFactory(new PropertyValueFactory<Shape, Color>("color"));
		tcw.setCellValueFactory(new PropertyValueFactory<Shape, Double>("width"));
		tch.setCellValueFactory(new PropertyValueFactory<Shape, Double>("height"));
		tcaw.setCellValueFactory(new PropertyValueFactory<Shape, Double>("arcWidth"));
		tcah.setCellValueFactory(new PropertyValueFactory<Shape, Double>("arcHeight"));
		tct.setCellValueFactory(new PropertyValueFactory<Shape, String>("text"));
		tcd.setCellValueFactory(new PropertyValueFactory<Shape, Boolean>("delete"));
		
		ObservableList<ShapeType> shapeValues = FXCollections.observableArrayList(ShapeType.CIRCLE, ShapeType.RECTANGLE, ShapeType.OVAL, ShapeType.ROUNDRECT, ShapeType.TEXT);
		
		tcs.setCellFactory(ComboBoxTableCell.forTableColumn(new PR1Model.ShapeTypeStringConverter(), shapeValues));
		tccx.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tccy.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tcr.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		
		tcw.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tch.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tcaw.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tcah.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		
		tcd.setCellFactory(CheckBoxTableCell.forTableColumn(tcd));
		
		tct.setCellFactory(TextFieldTableCell.forTableColumn());
		
		tcc.setCellFactory(ColorTableCell<Shape>::new);
		
		scrpaneright.setFitToHeight(true);
		scrpaneright.setFitToWidth(true);
		
		
		addMouseListener(new EventHandler<MouseEvent>() {

			/**
			 * Handles the mouse events.
			 * 
			 * @param event The event object.
			 */
			@Override
			public void handle(MouseEvent e) {
				mouseLastX = e.getX();
				mouseLastY = e.getY();
				if (viewState.get() == ViewState.CLOSED) {
					return;
				}
				
				else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					
					setSelection(m.select(e.getX(), e.getY()));
					if (e.getButton() == MouseButton.PRIMARY) {
						if (getSelection() != null) {
							m.setTop(getSelection());
						}
						viewState.set(ViewState.MODIFIED);
					}
				}
				
				if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					if (getSelection() != null) {
						if (e.getButton() == MouseButton.PRIMARY) {
							getSelection().setCenterX(e.getX());
							getSelection().setCenterY(e.getY());
							viewState.set(ViewState.MODIFIED);
						}
						else if (e.getButton() == MouseButton.SECONDARY) {
							if(getSelection().getType() == ShapeType.CIRCLE) {
								getSelection().setRadius(getSelection().getRadius() + 0.25 * (e.getX() - getSelection().getCenterX()));
							}
							else {
								getSelection().setWidth(getSelection().getWidth() + 0.25 * (e.getX() - getSelection().getCenterX()));
								getSelection().setHeight(getSelection().getHeight() + 0.25 * (e.getY() - getSelection().getCenterY()));
							}
							viewState.set(ViewState.MODIFIED);
						}
					}
				}
				else if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
					if (e.getButton().equals(MouseButton.PRIMARY)){
						switch (e.getClickCount()) {
						case 1:
							if (getSelection() == null) {
								setSelection(m.add(defaultS.getType(), e.getX(), e.getY()));
							}
							viewState.set(ViewState.MODIFIED);
							break;
						case 2:
							setSelection(m.select(e.getX(), e.getY()));
							application.goToCD(getSelection());
							viewState.set(ViewState.MODIFIED);
							break;
						default:
							break;
						}
					}
				}
			}
		});
		
		m.drawDataProperty().addListener(new ListChangeListener<PR1Model.Shape>() {

			/**
			 * Handles the model changes.
			 * Always repaints, regardless of the event object. Inefficient but works!
			 * 
			 * @param e The event object.
			 */
			@Override
			public void onChanged(ListChangeListener.Change<? extends PR1Model.Shape> e) { 
				
				repaint(); 
				reTable();
				viewState.set(ViewState.MODIFIED);
				
			}
		});
		
		
		viewState.addListener(new ChangeListener<ViewState>() {

			/**
			 * Handles the view state changes (from File menu and window resizing).
			 * 
			 * @param event The event object.
			 */
			@Override
			public void changed(ObservableValue<? extends ViewState> observable, ViewState oldValue, ViewState newValue) {
				if (!newValue.equals(oldValue)) {
					switch (newValue) {
					case CLOSED: // No file opened (when the application starts or when the current file is closed.
						setFileMenu(false, false, true, true, false); // Configures individual file menu items (enabled/disabled).
						break;
					case NEW: // A new file to be opened.
						if (file != null) {
							if (file.exists()) {
								file.delete(); // Delete the file if it the file with that name already exists (overwrite).
							}
						}
					case OPEN: // An existing file opened.
						if (file != null) {
							Charset charset = Charset.forName("US-ASCII");
							try  {
								file.createNewFile();
								BufferedReader reader = Files.newBufferedReader(file.toPath(), charset);
								String line = null;
								while ((line = reader.readLine()) != null) {
									try {
										m.add(line); // Read the file line by line and add the circle (line) to the model.
									}
									catch (NumberFormatException e) { } // Ignores an incorrectly formatted line.
									catch (ArrayIndexOutOfBoundsException e) { } // Ignores an incorrectly formatted line.
								}
								viewState.set(ViewState.OPENED);
							}
							catch (IOException e) {
								viewState.set(ViewState.CLOSE);
							}
						}
						break;
					case OPENED:  // The file is opened.
						setFileMenu(true, true, false, true, false);  // Configures individual file menu items (enabled/disabled).
						break;
					case CLOSE: // The file has to be closed.
						setFile(null); // Clears the file.
						setSelection(null); // Clears the selection;
						setClipboard(null); // Clears the selection;
						m.clear(); // Clears the model.
						clear(); // Clears the view.
						viewState.set(ViewState.CLOSED);
						break;				
					case MODIFIED: // The file has been modified, needs saving.
						setFileMenu(true, true, true, false, false);
						break;
					case SAVE: // Save the file.
						if (file != null) {
							Charset charset = Charset.forName("US-ASCII");
							try {
								BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset, StandardOpenOption.WRITE);
								for (PR1Model.Shape c : m.drawDataProperty()) {
									//For the project, the line needs to be created in a more complicated way than before,
									//so the work is passed off to a function here.
									String line = lineMaker(c);
									writer.write(line);
								}
								writer.close();
								viewState.set(ViewState.OPENED);
							}
							catch (IOException e) {
								application.goToErrorDialog();
							}
						}
						break;
					case QUIT: // Quit the application
						
						if (oldValue == ViewState.MODIFIED) {
							application.goToQuitDialog();
							//if the application didn't quit, setback the viewState
							viewState.set(oldValue);
							break;
						}
						System.exit(0);
						break;
					case RESIZE: // Redraw the view when the application window resizes.
						repaint();
						viewState.set(oldValue);
						break;
					default:
						break;
					}
				}
			}
		});
		
		
		clipboardState.addListener(new ChangeListener<ClipboardState>() {

			/**
			 * Handles the clipboard changes.
			 * 
			 * @param event The event object.
			 */
			@Override
			public void changed(ObservableValue<? extends ClipboardState> observable, ClipboardState oldValue, ClipboardState newValue) {
				Shape c = null;
				if (getSelection() != null) {
					switch (newValue) {
					case COPY: // Copy the selection to the clipboard. 
						setClipboard(getSelection());
						setEditMenu(false, false, false); // Enable all Edit menu items.			
						break;
					case PASTE: // Paste the clipboard to the view.
						c = m.add(mouseLastX, mouseLastY);
						c.setRadius(getClipboard().getRadius());
						c.setColor(getClipboard().getColor());
						c.setHeight(getClipboard().getHeight());
						c.setWidth(getClipboard().getWidth());
						c.setArcheight(getClipboard().getAH());
						c.setArcwidth(getClipboard().getAW());
						c.setType(getClipboard().getType());
						c.setText(getClipboard().getText());
						viewState.set(ViewState.MODIFIED);
						break;
					case DELETE: // delete the selection.
						c = getSelection();
						setSelection(null);
						m.remove(c);
						setEditMenu(true, true, true); // Disable all Edit menu items.		
						viewState.set(ViewState.MODIFIED);
						break;
					case IDLE:
						break;
					default:
						break;
					}
					clipboardState.set(ClipboardState.IDLE);
				}
			}

		});
		setFileMenu(false, false, true, true, false);
		setEditMenu(true, true, true);
	}
	
	/**
	 * Gets the clipboard's content.
	 * 
	 * @return The content.
	 */
	private Shape getClipboard() { return clipboard; }

	/**
	 * Sets the clipboard's content.
	 * 
	 * @param c The content.
	 */
	
	private void setClipboard(Shape c) { clipboard = c; }
	
	
	/**
	 * Adds the mouse event handler to the canvas.
	 * 
	 * @param eh The mouse event handler.
	 */
	public void addMouseListener(EventHandler<MouseEvent> eh) {
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, eh);
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, eh);
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, eh);
		canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, eh);
		canvas.addEventHandler(MouseEvent.MOUSE_MOVED, eh);
	}
	
	/**
	 * This class is the colorpicker in the table cell. It needed
	 * to be custom since there is no direct way to do this in the JFX libraries.
	 * Thanks to Michael Simmons for the basis of this code.
	 * http://info.michael-simons.eu/2014/10/27/custom-editor-components-in-javafx-tablecells/
	 * 
	 * @author Michael Simmons
	 * @modified by Collin Blakley
	 *
	 * @param <Shape>
	 */
	@SuppressWarnings("hiding")
	public class ColorTableCell<Shape> extends TableCell<Shape, Color> {    
	    private final ColorPicker colorPicker;
	 
	    public ColorTableCell(TableColumn<Shape, Color> column) {
		this.colorPicker = new ColorPicker();
		this.colorPicker.editableProperty().bind(column.editableProperty());
		this.colorPicker.disableProperty().bind(column.editableProperty().not());
		this.colorPicker.setOnShowing(event -> {
		    final TableView<Shape> tableView = getTableView();
		    tableView.getSelectionModel().select(getTableRow().getIndex());
		    tableView.edit(tableView.getSelectionModel().getSelectedIndex(), column);	    
		});
		this.colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
		    if(isEditing()) {
			commitEdit(newValue);
		    }
		});		
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	    }
	 
	    @Override
	    protected void updateItem(Color item, boolean empty) {
		super.updateItem(item, empty);	
	 
		setText(null);	
		if(empty) {	    
		    setGraphic(null);
		} else {	    
		    this.colorPicker.setValue(item);
		    this.setGraphic(this.colorPicker);
		} 
	    }
	}
	
	/**
	 * Sets the current file.
	 * 
	 * @param f The file.
	 */
	public void setFile(File f) { file = f; }
	
	/**
	 * function called when the accelerated menu item to zoom in is pressed.
	 * @param event
	 */
    @FXML
    void zInPressed(ActionEvent event) {
		canvas.setScaleX(canvas.getScaleX()*1.1);
		canvas.setScaleY(canvas.getScaleY()*1.1);
    }
    
    /**
     * function called when the accelerated menu item to zoom out is pressed.
     * @param event
     */

    @FXML
    void zOutPressed(ActionEvent event) {
		canvas.setScaleX(canvas.getScaleX()*0.9);
		canvas.setScaleY(canvas.getScaleY()*0.9);
    }
    
    /**
     * function called when "New" is pressed from the file menu.
     * opens a file chooser and allows the user to select the parameters of the file.
     * @param event
     */
    
    @FXML
    void newFilePressed(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open New File");
		File f = fileChooser.showSaveDialog(stage);
		if (f != null) {
			setFile(f);
			viewState.set(ViewState.NEW);
		}

    }

    /**
     * function called when "Open" is pressed from the file menu.
     * opens a file chooser and allows the user to select the file to open.
     * @param event
     */
    @FXML
    void fileOpenPressed(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		File f = fileChooser.showOpenDialog(stage);
		if (f != null) {
			setFile(f);
			viewState.set(ViewState.OPEN);
		}
    }

    /**
     * function called when "Close" is pressed from the file menu.
     * The FXML-imported functions following this all do mostly the same thing,
     * they simply set the view state so the main logic knows what to do when.
     * @param event
     */
    @FXML
    void fileClosePressed(ActionEvent event) {
    	viewState.set(ViewState.CLOSE);
    }

    @FXML
    void fileSavePressed(ActionEvent event) {
    	viewState.set(ViewState.SAVE);
    }

    @FXML
    void fileQuitPressed(ActionEvent event) {
    	viewState.set(ViewState.QUIT);
    }

    @FXML
    void editCopyPressed(ActionEvent event) {
    	clipboardState.set(ClipboardState.COPY);
    }

    @FXML
    void editPastePressed(ActionEvent event) {
    	clipboardState.set(ClipboardState.PASTE);
    }

    @FXML
    void editDeletePressed(ActionEvent event) {
    	clipboardState.set(ClipboardState.DELETE);
    }

    @FXML
    void HelpAboutPressed(ActionEvent event) {
    	application.goToAboutDialog();
    }
    
	/**
	 * Clears the canvas.
	 */
	public void clear() { gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); }
	
	
	/**
	 * Gets the selected circle.
	 * 
	 * @return The selected circle. 
	 */
	private PR1Model.Shape getSelection() { return selection.get(); }
	
	/**
	 * Selects a circle.
	 * 
	 * @param s The circle to be selected.
	 */
	private void setSelection(PR1Model.Shape s) {
		selection.set(s);
		setEditMenu(getSelection() == null, true, getSelection() == null); // Enable the Edit menu items as needed.
	}
	
	/**
	 * Enables/disables the Edit menu items.
	 * 
	 * @param c The Edit menu Copy menu item disabled.
	 * @param p The Edit menu Paste menu item disabled.
	 * @param d The Edit menu Delete menu item disabled.
	 */
	public void setEditMenu(boolean c, boolean p, boolean d) {
		editCopy.setDisable(c);
		editPaste.setDisable(p);
		editDelete.setDisable(d);
	}
	
	/**
	 * repaints the entire canvas. Loops through the shape list in the model
	 * and draws each.
	 */
	private void repaint() {
		clear();
		for (PR1Model.Shape c : m.drawDataProperty()) {
			if(!c.getText().equals(defaultshape))
				drawShape(c, false);
		}
		if (getSelection() != null) {
			drawShape(getSelection(), true);
		}
	}
	
	
	/**
	 * Draws a shape. 
	 * 
	 * @param s the shape to draw
	 * @param selection If false, draws the circle, otherwise the selection ring.
	 */
	private void drawShape(PR1Model.Shape s, boolean selection) {
		double x = s.getCenterX();
		double y = s.getCenterY();
		double r = s.getRadius();
		Color c = s.getColor();
		double w = s.getWidth();
		double h = s.getHeight();
		double aw = s.getAW();
		double ah = s.getAH();
		String t = s.getText();
		if(selection) {
			gc.setStroke(Color.RED);
			gc.setLineWidth(3);
		} else {
			gc.setFill(c);
		}
		if(s.getType() == ShapeType.CIRCLE) {
			if (selection) {
				gc.strokeOval(x - r - 3, y - r - 3, 2 * r + 6, 2 * r + 6);
			}
			else {
				gc.fillOval(x - r, y - r, 2 * r, 2 * r);
			}
		}
		else if (s.getType() == ShapeType.RECTANGLE) {
			if (selection) {
				gc.strokeRect(x - w/2 - 3, y - h/2 - 3, w + 6, h + 6);
			}
			else {
				gc.fillRect(x- w/2, y-h/2, w, h);
			}
		}
		else if (s.getType() == ShapeType.OVAL) {
			if (selection) {
				gc.strokeOval(x - w/2 - 3, y - h/2 - 3, w + 6, h + 6);
			}
			else {
				gc.fillOval(x - w/2, y - h/2, w, h);
			}
		}
		else if (s.getType() == ShapeType.ROUNDRECT) {
			if (selection) {
				gc.strokeRoundRect(x - w/2 - 3, y - h/2 - 3, 
						w + 6, h + 6, aw, ah);
			}
			else {
				gc.fillRoundRect(x - w/2, y - h/2, w, h, aw, ah);
			}
		}
		else if (s.getType() == ShapeType.TEXT) {
			if(selection) {
				gc.strokeRect(x - h*.43*t.length()/2 - 3, y - h/2 + 3, h*.43*t.length(), h + 10);
			}
			else {
				gc.setFont(new Font("Courier", h));
				gc.fillText(t, x - (h*.43*t.length())/2, y + h/2);
			}
		}
		
	}
	
	/**
	 * Renews the information in the table.
	 */
	private void reTable() {
		ObservableList<Shape> circles = m.drawDataProperty();
		tv.setItems(circles);		
	}
	
	/**
	 * Function to easily disable/enable file menu items.
	 * @param n new
	 * @param o open
	 * @param c close
	 * @param s save
	 * @param q quit
	 */
	public void setFileMenu(boolean n, boolean o, boolean c, boolean s, boolean q) {
		fileNew.setDisable(n);
		fileOpen.setDisable(o);
		fileClose.setDisable(c);
		fileSave.setDisable(s);
		fileQuit.setDisable(q);
	}
	
	/**
	 * function to writeback changes made to the file. 
	 * @param s the shape to writeback to the file.
	 * @return
	 */
	public String lineMaker(Shape s) {
		String returnable;
		ShapeType type = s.getType();
		if(type == ShapeType.CIRCLE) {
			returnable = s.getType() + "," + s.getCenterX() + "," +  s.getCenterY() + "," + s.getRadius() + 
					"," +  s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\n";
		}
		else if ((type == ShapeType.RECTANGLE) || (type == ShapeType.OVAL)) {
			returnable = s.getType() + "," + s.getCenterX() + "," + s.getCenterY() + "," + s.getWidth() + 
					"," + s.getHeight() + "," + s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\n";
		}
		else if (type == ShapeType.ROUNDRECT) {
			returnable = s.getType() + "," + s.getCenterX() + "," + s.getCenterY() + "," + s.getWidth() + 
					"," + s.getHeight() + "," + s.getAW() + "," + s.getAH() + "," + s.getColor().getRed() + "," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "\n";
		}
		else if (type == ShapeType.TEXT) {
			returnable = s.getType() + "," + s.getCenterX() + "," + s.getCenterY() + "," + s.getColor().getRed() +
					"," + s.getColor().getGreen() + "," + s.getColor().getBlue() + "," + s.getText() + "\n";
		}
		else returnable = s.getType() + "\n";
		
		return returnable;
	}
	

}
