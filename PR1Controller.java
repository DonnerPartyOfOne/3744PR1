package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import application.PR1Model.HW2Circle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

/**
 * Project 1 controller class. Gets values for all the GUI elements injected
 * by the FXMLLoader.
 * 
 * @author Collin
 *
 */

public class PR1Controller {
	
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
    private TableView<HW2Circle> tv; // Value injected by FXMLLoader

    @FXML // fx:id="tcr"
    private TableColumn<HW2Circle, Double> tcr; // Value injected by FXMLLoader

    @FXML // fx:id="editPaste"
    private MenuItem editPaste; // Value injected by FXMLLoader

    @FXML // fx:id="tcs"
    private TableColumn<?, ?> tcs; // Value injected by FXMLLoader

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
    private TableColumn<HW2Circle, Double> tccx; // Value injected by FXMLLoader

    @FXML // fx:id="tccy"
    private TableColumn<HW2Circle, Double> tccy; // Value injected by FXMLLoader

    @FXML // fx:id="tcc"
    private TableColumn<HW2Circle, Color> tcc; // Value injected by FXMLLoader

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
	
	private Stage stage = null;
	private PR1Model m = null;
	private GraphicsContext gc = null;
	
	public static enum ViewState { NEW, OPEN, OPENED, CLOSE, CLOSED, MODIFIED, SAVE, QUIT, RESIZE};
	private ObjectProperty<ViewState> viewState = null;
	public static enum ClipboardState { IDLE, COPY, PASTE, DELETE};
	private ObjectProperty<ClipboardState> clipboardState = null;
	private ChangeListener<Number> canvasListener = null;
	private File file = null;

	private static final String ABOUT_TITLE = "About Project 1";
	private static final String ABOUT_MESSAGE = "Project 1 version 1.";
	private static final String COLOR_TITLE = "Shape Color";
	private static final String CSS_FILE = "PR1.css";
	
	private static final String CHOICE_TITLE = "File Save";
	private static final String CHOICE_MESSAGE = "File not saved. Proceed?";
	
	private ObjectProperty<HW2Circle> selection = null;
	
	private double mouseLastX = -1;
	private double mouseLastY = -1;
	
	private HW2Circle clipboard = null;
	
	
	@FXML
	void initialize() {
		m = new PR1Model();
		setFile(null);
		selection = new SimpleObjectProperty<HW2Circle>();
		setSelection(null);
		setClipboard(null);
		viewState = new SimpleObjectProperty<ViewState>(ViewState.CLOSE);
		clipboardState = new SimpleObjectProperty<ClipboardState>(ClipboardState.IDLE);
		
		
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
		
		
		fileNew.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		fileOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		fileClose.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
		fileSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		fileQuit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		editCopy.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
		editPaste.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
		editDelete.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		
		gc = canvas.getGraphicsContext2D();
		canvas.heightProperty().set(1105);
		canvas.widthProperty().set(1105);
		canvas.heightProperty().addListener(canvasListener);
		canvas.widthProperty().addListener(canvasListener);
		
		
		tcs.prefWidthProperty().bind(tv.widthProperty().divide(5));
		tccx.prefWidthProperty().bind(tv.widthProperty().divide(5));
		tccy.prefWidthProperty().bind(tv.widthProperty().divide(5));
		tcr.prefWidthProperty().bind(tv.widthProperty().divide(5));
		tcc.prefWidthProperty().bind(tv.widthProperty().divide(5));
	
		
		tccx.setCellValueFactory(new PropertyValueFactory<HW2Circle, Double>("centerX"));
		tccy.setCellValueFactory(new PropertyValueFactory<HW2Circle, Double>("centerY"));
		tcr.setCellValueFactory(new PropertyValueFactory<HW2Circle, Double>("radius"));
		tcc.setCellValueFactory(new PropertyValueFactory<HW2Circle, Color>("color"));
		
		tccx.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tccy.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tcr.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tcc.setCellFactory(ColorTableCell<HW2Circle>::new);
		
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
							getSelection().setRadius(getSelection().getRadius() + 0.25 * (e.getX() - getSelection().getCenterX()));
							viewState.set(ViewState.MODIFIED);
						}
					}
				}
				else if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
					if (e.getButton().equals(MouseButton.PRIMARY)){
						switch (e.getClickCount()) {
						case 1:
							if (getSelection() == null) {
								setSelection(m.add(e.getX(), e.getY()));
							}
							viewState.set(ViewState.MODIFIED);
							break;
						case 2:
							setSelection(m.select(e.getX(), e.getY()));
							getSelection().setColor(showColorDialog(getSelection().getColor()));
							viewState.set(ViewState.MODIFIED);
							break;
						default:
							break;
						}
					}
				}
			}
		});
		
		m.drawDataProperty().addListener(new ListChangeListener<PR1Model.HW2Circle>() {

			/**
			 * Handles the model changes.
			 * Always repaints, regardless of the event object. Inefficient but works!
			 * 
			 * @param e The event object.
			 */
			@Override
			public void onChanged(ListChangeListener.Change<? extends PR1Model.HW2Circle> e) { 
				
				repaint(); 
				reTable();
				
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
								for (PR1Model.HW2Circle c : m.drawDataProperty()) {
									String line = c.getCenterX() + "," +  c.getCenterY() + "," + c.getRadius() + "," +  c.getColor().getRed() + "," + c.getColor().getGreen() + "," + c.getColor().getBlue() + "\n";
									writer.write(line);
								}
								writer.close();
								viewState.set(ViewState.OPENED);
							}
							catch (IOException e) {
								showMessageDialog("File Error", e.toString());
							}
						}
						break;
					case QUIT: // Quit the application
						if (oldValue == ViewState.MODIFIED && !showChoiceDialog(CHOICE_TITLE, CHOICE_MESSAGE)) {
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
				HW2Circle c = null;
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
	private HW2Circle getClipboard() { return clipboard; }

	/**
	 * Sets the clipboard's content.
	 * 
	 * @param c The content.
	 */
	
	private void setClipboard(HW2Circle c) { clipboard = c; }
	
	/**
	 * Shows the color dialog.
	 * 
	 * @param c The color
	 * @return The picked color.
	 */
	public Color showColorDialog(Color c) {
		Color color = null;
		if (c != null) {
			ColorDialog dialog = new ColorDialog(COLOR_TITLE, c);
			color = dialog.pick();			
		}
		return color;
	}
	
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
	 * Creates the color dialog.
	 * 
	 * @author Denis Gracanin
	 * @version 1
	 */
	public class ColorDialog extends Stage {
		private Color color;

		/**
		 * Creates an instance of <code>ColorDialog</code> class.
		 * 
		 * @param title The dialog title.
		 * @param c The dialog color.
		 */
		public ColorDialog(String title, Color c) {
			super();
			color = c;
			setTitle(title);
			setResizable(false);
			initModality(Modality.APPLICATION_MODAL);

			BorderPane root = new BorderPane();
			root.getStyleClass().add("color-dialog");
			ColorPicker colorPicker = new ColorPicker(c);
			root.setCenter(colorPicker);
			root.getStyleClass().add("color-dialog");
			Button okButton = new Button("OK");
			okButton.setOnAction((e) -> { color = colorPicker.getValue(); close(); });
			BorderPane.setAlignment(okButton, Pos.CENTER);
			BorderPane.setMargin(okButton, new Insets(10,10,10,10));
			root.setBottom(okButton);

			Scene scene = new Scene(root, 200, 150);
			try {
				scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
			} catch(Exception e) {
				e.printStackTrace();
			}
			setScene(scene);
		}

		/**
		 * Returns the color value selected in the color picker.
		 * 
		 * @return The selected color value.
		 */
		public Color pick() {
			showAndWait();
			return color;
		}

	}
	
	public class ColorTableCell<HW2Circle> extends TableCell<HW2Circle, Color> {    
	    private final ColorPicker colorPicker;
	 
	    public ColorTableCell(TableColumn<HW2Circle, Color> column) {
		this.colorPicker = new ColorPicker();
		this.colorPicker.editableProperty().bind(column.editableProperty());
		this.colorPicker.disableProperty().bind(column.editableProperty().not());
		this.colorPicker.setOnShowing(event -> {
		    final TableView<HW2Circle> tableView = getTableView();
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
	
    
    @FXML
    void newFilePressed(ActionEvent event) {
    	System.out.println("new file menu item chosen");
    	
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open New File");
		File f = fileChooser.showSaveDialog(stage);
		if (f != null) {
			setFile(f);
			viewState.set(ViewState.NEW);
		}

    }

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
    	showMessageDialog(ABOUT_TITLE, ABOUT_MESSAGE);
    }
    
    public void showMessageDialog(String title, String message) {
		MessageDialog dialog = new MessageDialog(title, message);
		dialog.show();		
	}
    
    /**
	 * Creates the message dialog.
	 * 
	 * @author Denis Gracanin
	 * @version 1
	 */
	public class MessageDialog extends Stage {

		/**
		 * Creates an instance of <code>MessageDialog</code> class.
		 * 
		 * @param title The dialog title.
		 * @param message The dialog message.
		 */
		public MessageDialog(String title, String message) {
			super();
			setTitle(title);
			setResizable(false);
			initModality(Modality.APPLICATION_MODAL);

			BorderPane root = new BorderPane();
			root.getStyleClass().add("message-dialog");
			TextArea messageArea = new TextArea(message);
			messageArea.setEditable(false);
			messageArea.setWrapText(true);
			messageArea.setFocusTraversable(false);
			root.setCenter(messageArea);
			root.getStyleClass().add("message-dialog");
			Button okButton = new Button("OK");
			okButton.setOnAction((e) ->close());
			BorderPane.setAlignment(okButton, Pos.CENTER);
			BorderPane.setMargin(okButton, new Insets(10,10,10,10));
			root.setBottom(okButton);

			Scene scene = new Scene(root, 200, 150);
			try {
				scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
			} catch(Exception e) {
				e.printStackTrace();
			}
			setScene(scene);
		}

	}
	
	public void clear() { gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); }
	
	/**
	 * Draws a circle.
	 * 
	 * @param x The x-coordinate of the center of the circle.
	 * @param y The y-coordinate of the center of the circle.
	 * @param r The radius of the circle
	 * @param c The color of the circle.
	 * @param selection If true, draws the circle, otherwise the selection ring.
	 */
	public void drawCircle(double x, double y, double r, Color c, boolean selection) {
		if (selection) {
			gc.setStroke(Color.RED);
			gc.setLineWidth(3);
			gc.strokeOval(x - r - 3, y - r - 3, 2 * r + 6, 2 * r + 6);
		}
		else {
			gc.setFill(c);
			gc.fillOval(x - r, y - r, 2 * r, 2 * r);
		}
	}
	
	/**
	 * Gets the selected circle.
	 * 
	 * @return The selected circle. 
	 */
	private PR1Model.HW2Circle getSelection() { return selection.get(); }
	
	/**
	 * Selects a circle.
	 * 
	 * @param s The circle to be selected.
	 */
	private void setSelection(PR1Model.HW2Circle s) {
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

	
	
	private void repaint() {
		clear();
		for (PR1Model.HW2Circle c : m.drawDataProperty()) {
			drawCircle(c.getCenterX(), c.getCenterY(), c.getRadius(), c.getColor(), false);
		}
		if (getSelection() != null) {
			drawCircle(getSelection().getCenterX(), getSelection().getCenterY(), getSelection().getRadius(), getSelection().getColor(), true);
		}
	}
	
	private void reTable() {
		ObservableList<HW2Circle> circles = m.drawDataProperty();
		tv.setItems(circles);		
	}
	
    @FXML
    void canvasClicked(ActionEvent e) {

    }

    @FXML
    void canvasDragged(ActionEvent event) {

    }

    @FXML
    void canvasPressed(ActionEvent event) {

    }
	
	public void setFileMenu(boolean n, boolean o, boolean c, boolean s, boolean q) {
		fileNew.setDisable(n);
		fileOpen.setDisable(o);
		fileClose.setDisable(c);
		fileSave.setDisable(s);
		fileQuit.setDisable(q);
	}
	
	
	/**
	 * Creates the choice dialog.
	 * 
	 * @author Denis Gracanin
	 * @version 1
	 */
	public class ChoiceDialog extends Stage {
		private boolean ok = false;

		/**
		 * Creates an instance of <code>MessageDialog</code> class.
		 * 
		 * @param title The dialog title.
		 * @param message The dialog message.
		 */
		public ChoiceDialog(String title, String message) {
			super();
			setTitle(title);
			setResizable(false);
			initModality(Modality.APPLICATION_MODAL);

			BorderPane root = new BorderPane();
			root.getStyleClass().add("choice-dialog");
			TextArea messageArea = new TextArea(message);
			messageArea.setEditable(false);
			messageArea.setWrapText(true);
			messageArea.setFocusTraversable(false);
			root.setCenter(messageArea);
			root.getStyleClass().add("choice-dialog");
			Button okButton = new Button("OK");
			okButton.setOnAction((e) -> { ok = true; close(); });
			Button cancelButton = new Button("Cancel");
			cancelButton.setOnAction((e) -> { ok = false; close(); });
			HBox hbox = new HBox(20);
		    HBox.setHgrow(okButton, Priority.ALWAYS);
		    HBox.setHgrow(cancelButton, Priority.ALWAYS);
		    okButton.setMaxWidth(Double.MAX_VALUE);
		    cancelButton.setMaxWidth(Double.MAX_VALUE);
			hbox.getChildren().addAll(okButton, cancelButton);
			BorderPane.setAlignment(hbox, Pos.CENTER);
			BorderPane.setMargin(hbox, new Insets(10,10,10,10));
			root.setBottom(hbox);

			Scene scene = new Scene(root, 200, 150);
			try {
				scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
			} catch(Exception e) {
				e.printStackTrace();
			}
			setScene(scene);
		}

		/**
		 * Returns the boolean value.
		 * 
		 * @return The boolean.
		 */
		public boolean pick() {
			showAndWait();
			return ok;
		}

	}
	public boolean showChoiceDialog(String title, String message) {
		ChoiceDialog dialog = new ChoiceDialog(title, message);
		return dialog.pick();			
	}
	

}
