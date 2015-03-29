package cs3744.pr1;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

/**
 * Project 1 model class.
 * Stores a collection of shapes.
 * 
 * @author Denis Gracanin
 * @modified by Collin Blakley
 * @version 1
 */
public class PR1Model {
	private ObservableList<Shape> drawData = null; // An observable list of shape objects.
	private ChangeListener<Number> doubleListener = null; // A listener to the changes in the shape's <code>DoubleProperty</code> properties.
	private ChangeListener<Color> colorListener = null; // A listener to the changes in the shape's <code>ObjecProperty<Color></code> properties.
	private ChangeListener<ShapeType> typeListener = null;
	private ChangeListener<String> textListener = null;
	private ChangeListener<Boolean> deleteListener = null;
	private static final String defaultshape = "DEFAULT SHAPE";

	/**
	 * Creates an instance of <code>HW2Model</code> class with no data.
	 */
	public PR1Model() { 
		drawData = FXCollections.observableArrayList();
		doubleListener = new ChangeListener<Number>() {

			/* (non-Javadoc)
			 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
			 */
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { touch(); }
		};
		colorListener = new ChangeListener<Color>() {

			@Override
			public void changed( ObservableValue<? extends Color> observable, Color oldValue, Color newValue) { touch(); }		
		};
		typeListener = new ChangeListener<ShapeType>() {
			@Override
			public void changed(ObservableValue<? extends ShapeType> observable, ShapeType oldValue, ShapeType newValue) { touch(); }
		};
		textListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { touch(); }
		};
		deleteListener = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) { touch(); }
		};
	}
	
	/**
	 * Generates a list event.
	 */
	private void touch() {
		if (drawData.size() > 0) {
			Shape o = drawData.get(0);
			drawData.remove(0);
			drawData.add(0, o);
		}
	}

	/**
	 * Gets the data property. 
	 * 
	 * @return The observable list of shapes.
	 */
	public ObservableList<Shape> drawDataProperty() { return drawData; }

	/**
	 * Selects a shape that contains a point (<code>null</code> if no shape).
	 * 
	 * @param x The point's x-coordinate.
	 * @param y The point's y-coordinate.
	 * @return The shape.  
	 */
	public Shape select(double x, double y) {
		Shape selection = null;
		for (Shape o : drawData) {
			if (o.contains(x, y) && !o.getText().equals(defaultshape)) {
				selection = o;
			}
		}
		return selection;
	}

	/**
	 * Sets the shape at the end of the list (the last shape drawn).
	 * 
	 * @param c The shape
	 */
	public void setTop(Shape c) {
		if (c != null) {
			int i = drawData.lastIndexOf(c);
			if (i != -1 && i != (drawData.size() - 1)) {
				drawData.remove(i);
				add(c);
			}
		}
	}

	/**
	 * Adds a shape to the model.
	 * 
	 * @param s The string (a comma-separated list of a variable number of values.).
	 */
	public void add(String s) { 
		
		//get list of values
		String[] values = s.split("[ ]*,[ ]*"); 
		
		if(values[0].equals("CIRCLE")) {
			add(new Shape(Double.parseDouble(values[1]), Double.parseDouble(values[2]),
					Double.parseDouble(values[3]), new Color(Double.parseDouble(values[4]), Double.parseDouble(values[5]), Double.parseDouble(values[6]), 1),
					50, 50, 15, 15, "", ShapeType.CIRCLE));
		}
		else if (values[0].equals("RECTANGLE")) {
			add(new Shape(Double.parseDouble(values[1]), Double.parseDouble(values[2]),
					50, new Color(Double.parseDouble(values[5]), Double.parseDouble(values[6]), Double.parseDouble(values[7]), 1),
					Double.parseDouble(values[3]), Double.parseDouble(values[4]), 15, 15, "", ShapeType.RECTANGLE));
		}
		else if (values[0].equals("OVAL")) {
			add(new Shape(Double.parseDouble(values[1]), Double.parseDouble(values[2]),
					50, new Color(Double.parseDouble(values[5]), Double.parseDouble(values[6]), Double.parseDouble(values[7]), 1),
					Double.parseDouble(values[3]), Double.parseDouble(values[4]), 15, 15, "", ShapeType.OVAL));
		}
		else if (values[0].equals("ROUNDRECT")) {
			add(new Shape(Double.parseDouble(values[1]), Double.parseDouble(values[2]),
					50, new Color(Double.parseDouble(values[7]), Double.parseDouble(values[8]), Double.parseDouble(values[9]), 1),
					Double.parseDouble(values[3]), Double.parseDouble(values[4]), 
					Double.parseDouble(values[5]), Double.parseDouble(values[6]), "", ShapeType.ROUNDRECT));
		}
		else if (values[0].equals("TEXT")) {
			add(new Shape(Double.parseDouble(values[1]), Double.parseDouble(values[2]),
					50, new Color(Double.parseDouble(values[3]), Double.parseDouble(values[4]), Double.parseDouble(values[5]), 1),
					50, 50, 15, 15, "textex", ShapeType.TEXT));
		}
		else{
			add(new Shape(0, 0, 0, new Color(0, 0, 0, 1), 0, 0, 0, 0, "", ShapeType.UNKNOWN));
		}
			
	
	}
	
	/**
	 * adds a shape to the list.
	 * This function was needed so that default values could be
	 * provided by the user in the GUI and applied to new shapes.
	 * @param t the shape type.
	 * @param x x-coord center of the shape.
	 * @param y y-coord center of the shape.
	 * @return
	 */
	public Shape add(ShapeType t, double x, double y) {
		Shape c = new Shape(x, y);
		c.setType(t);
		add(c);
		return c;
	}

	/**
	 * Adds a shape to the model.
	 * 
	 * @param x The shape's x-coordinate.
	 * @param y The shape's y-coordinate.
	 * @return The shape.
	 */
	public Shape add(double x, double y) {
		Shape c = new Shape(x, y);
		add(c);
		return c;
	}
	
	/**
	 * Adds a shape to the model and sets up all the listeners.
	 * 
	 * @param c The shape
	 */
	public void add(Shape c) {
		c.centerXProperty().addListener(doubleListener);
		c.centerYProperty().addListener(doubleListener);
		c.radiusProperty().addListener(doubleListener);
		c.colorProperty().addListener(colorListener);
		c.widthProperty().addListener(doubleListener);
		c.heightProperty().addListener(doubleListener);
		c.arcWidthProperty().addListener(doubleListener);
		c.arcHeightProperty().addListener(doubleListener);
		c.textProperty().addListener(textListener);
		c.typeProperty().addListener(typeListener);
		c.deleteProperty().addListener(deleteListener);
		drawData.add(c);
	}
	
	/**
	 * Removes a shape from the model and removes all listeners.
	 * 
	 * @param c The shape.
	 */
	public void remove(Shape c) {
		c.centerXProperty().removeListener(doubleListener);
		c.centerYProperty().removeListener(doubleListener);
		c.radiusProperty().removeListener(doubleListener);
		c.colorProperty().removeListener(colorListener);
		c.widthProperty().removeListener(doubleListener);
		c.heightProperty().removeListener(doubleListener);
		c.arcWidthProperty().removeListener(doubleListener);
		c.arcHeightProperty().removeListener(doubleListener);
		c.textProperty().removeListener(textListener);
		c.typeProperty().removeListener(typeListener);
		c.deleteProperty().removeListener(deleteListener);
		drawData.remove(c);
	}
	
	/**
	 * Clears the model (erases all data).
	 */
	public void clear() { drawData.clear(); }
	
	/**
	 * This enum represents the various types of shapes that can be drawn to the canvas.
	 * @author Collin
	 *
	 */
	public enum ShapeType { RECTANGLE, CIRCLE, OVAL, ROUNDRECT, TEXT, UNKNOWN }
	
	/**
	 * This string converter is necessary to intelligently populate the type
	 * field in the table view.
	 * @author Collin
	 *
	 */
	public static class ShapeTypeStringConverter extends StringConverter<ShapeType> {
		public String toString(ShapeType s) {
			if(s == ShapeType.RECTANGLE) {
				return "rect";
			}
			else if (s == ShapeType.CIRCLE) {
				return "circle";
			}
			else if (s == ShapeType.OVAL) {
				return "oval";
			}
			else if (s == ShapeType.ROUNDRECT) {
				return "roundrect";
			}
			else if (s == ShapeType.TEXT) {
				return "text";
			}
			else return "unknown";
		}
		public ShapeType fromString(String s) {
			if(s == "rect") {
				return ShapeType.RECTANGLE;
			}
			else if (s == "circle") {
				return ShapeType.CIRCLE;
			}
			else if (s == "oval") {
				return ShapeType.OVAL;
			}
			else if (s == "roundrect") {
				return ShapeType.ROUNDRECT;
			}
			else if (s == "text") {
				return ShapeType.TEXT;
			}
			else return ShapeType.UNKNOWN;
		}
	}

	/**
	 * Project 1 shape class.
	 * Stores shape data.
	 * Modified for new functionality from HW2 Circle class.
	 * 
	 * @author Denis Gracanin
	 * @modified Collin Blakley
	 * @version 1
	 */
	public class Shape {
		private DoubleProperty centerX = null;
		private DoubleProperty centerY = null;
		private DoubleProperty radius = null;
		private ObjectProperty<Color> color = null;
		private DoubleProperty width = null;
		private DoubleProperty height = null;
		private DoubleProperty arcwidth = null;
		private DoubleProperty archeight = null;
		private StringProperty text = null;
		private ObjectProperty<ShapeType> type = null;
		private BooleanProperty delete = null;
		
		public static final double WIDTH_MIN = 10;
		
		public static final double HEIGHT_MIN = 10;
		
		public static final double WIDTH_MAX = 500;
		
		public static final double HEIGHT_MAX = 500;
		/**
		 * Minimum shape's radius value.
		 */
		public static final double RADIUS_MIN = 10;
		/**
		 * Maximum shape's radius value.
		 */
		public static final double RADIUS_MAX = 100;
		/**
		 * Default shape's radius value.
		 */
		public static final double RADIUS_DEFAULT = 50;
		/**
		 * Minimum shape's center x-coordinate value.
		 */
		public static final double X_MIN = 0;
		/**
		 * Maximum shape's center x-coordinate value.
		 */
		public static final double X_MAX = 1000;
		/**
		 * Default shape's center x-coordinate value.
		 */
		public static final double X_DEFAULT = 0;
		/**
		 * Minimum shape's center y-coordinate value.
		 */
		public static final double Y_MIN = 0;
		/**
		 * Maximum shape's center y-coordinate value.
		 */
		public static final double Y_MAX = 1000;
		/**
		 * Default shape's center y-coordinate value.
		 */
		public static final double Y_DEFAULT = 0;

		/**
		 * Creates an instance of <code>Shape</code> class with the default values:
		 * <ul>
		 * <li>The center's x-coordinate: 0.</li>
		 * <li>The center's y-coordinate: 0.</li>
		 * <li>The radius: 50.</li>
		 * <li>The color: black.</li>
		 * </ul>
		 */
		public Shape() { this(X_DEFAULT, Y_DEFAULT); }

		/**
		 * Creates an instance of <code>Shape</code> class with the default values:
		 * <ul>
		 * <li>The radius: 50.</li>
		 * <li>The color: black.</li>
		 * </ul>
		 * 
		 * @param x The center's x-coordinate.
		 * @param y The center's y-coordinate.
		 */
		public Shape(double x, double y) { 
			this(x, y, RADIUS_DEFAULT, Color.BLACK, 50, 50, 15, 15, "", ShapeType.CIRCLE); 
		}


		/**
		 * Creates an instance of <code>Shape</code> class.
		 * 
		 * @param c The shape.
		 */
		public Shape(Shape c) { 
			
			this(c.getCenterX(), c.getCenterY(), c.getRadius(), c.getColor(), c.getWidth(), c.getHeight(), c.getAW(), c.getAH(), c.getText(), c.getType()); 
			
		}

		/**
		 * Creates an instance of <code>Shape</code> class.
		 * 
		 * @param x The center's x-coordinate.
		 * @param y The center's y-coordinate.
		 * @param r The radius.
		 * @param c The color.
		 * @param w The width.
		 * @param h The height.
		 * @param aw The arcwidth.
		 * @param ah The archeight.
		 * @param t The text.
		 * @param ty The type of shape.
		 */
		public Shape(double x, double y, double r, Color c, double w, double h, double aw, double ah, String t, ShapeType ty) {
			centerX = new SimpleDoubleProperty(this, "CenterX");
			setCenterX(x);
			centerY = new SimpleDoubleProperty(this, "CenterY");
			setCenterY(y);
			radius = new SimpleDoubleProperty(this, "radius");
			setRadius(r);
			color = new SimpleObjectProperty<Color>(this, "color");
			setColor(c);
			width = new SimpleDoubleProperty(this, "width");
			setWidth(w);
			height = new SimpleDoubleProperty(this, "height");
			setHeight(h);
			arcwidth = new SimpleDoubleProperty(this, "arcWidth");
			setArcwidth(aw);
			archeight = new SimpleDoubleProperty(this, "arcHeight");
			setArcheight(ah);
			text = new SimpleStringProperty(this, "text");
			setText(t);
			type = new SimpleObjectProperty<ShapeType>(this, "type");
			setType(ty);
			delete = new SimpleBooleanProperty(this, "delete");
			setDelete(false);
		}
		
		
		/**
		 * Get the status of the delete boolean.
		 * @return
		 */
		public boolean getDelete() {
			return delete.get();
		}
		
		/**
		 * set the delete boolean.
		 * @param d
		 */
		public void setDelete(boolean d) {
			delete.set(d);
		}

		/**
		 * get the ShapeType associated with this shape.
		 * @return
		 */
		public ShapeType getType() {
			return type.get();
		}
		
		/**
		 * get the text stored in this shape.
		 * @return
		 */
		public String getText() {
			return text.get();
		}
		
		/**
		 * get the archeight stored in this shape.
		 * @return
		 */
		public double getAH() {
			return archeight.get();
		}
		
		/**
		 * get the arcwidth stored in this shape.
		 * @return
		 */
		public double getAW() {
			return arcwidth.get();
		}
		
		/**
		 * get the height of this shape.
		 * @return
		 */
		public double getHeight() {
			return height.get();
		}
		
		/**
		 * get the width of this shape.
		 * @return
		 */
		public double getWidth() {
			return width.get();
		}
		
		/**
		 * set the type of this shape.
		 * @param ty the shape type.
		 */
		public void setType(ShapeType ty) {
			type.set(ty);
		}
		
		/**
		 * set the text of this shape.
		 * @param t The text.
		 */
		public void setText(String t) {
			text.set(t);
		}
		
		/**
		 * set the archeight of this shape.
		 * @param ah the archeight.
		 */
		public void setArcheight(double ah) {
			archeight.set(ah);
		}
		
		/**
		 * set the arcwidth of this shape.
		 * @param aw the arcwidth.
		 */
		public void setArcwidth(double aw) {
			arcwidth.set(aw);
		}
		
		/**
		 * set the height of this shape.
		 * @param h the height.
		 */
		public void setHeight(double h) {
			height.set(clamp(h, HEIGHT_MIN, HEIGHT_MAX));
		}
		
		/**
		 * set the width of this shape.
		 * @param w the width.
		 */
		public void setWidth(double w) {
			width.set(clamp(w, WIDTH_MIN, WIDTH_MAX));
		}
		
		/**
		 * Each of the following methods return the properties
		 * associated with this shape.
		 * @return Properties.
		 */
		public ObjectProperty<ShapeType> typeProperty() {
			return type;
		}
		
		public StringProperty textProperty() {
			return text;
		}
		
		public DoubleProperty arcHeightProperty() {
			return archeight;
		}
		
		public DoubleProperty arcWidthProperty() {
			return arcwidth;
		}
		
		public DoubleProperty heightProperty() {
			return height;
		}
		
		public DoubleProperty widthProperty() {
			return width;
		}
		
		public BooleanProperty deleteProperty() {
			return delete;
		}

		/**
		 * Gets the center's x-coordinate property.
		 * 
		 * @return The x-coordinate property.
		 */
		public DoubleProperty centerXProperty() { 
			return centerX; 	
		}

		/**
		 * Gets the center's x-coordinate.
		 * 
		 * @return The x-coordinate.
		 */
		public double getCenterX() { return centerX.get(); 	}

		/**
		 * Sets the center's x-coordinate.
		 * 
		 * @param x The x-coordinate.
		 */
		public void setCenterX(double x) { centerX.set(clamp(x, X_MIN, X_MAX)); }

		/**
		 * Gets the center's y-coordinate property.
		 * 
		 * @return The y-coordinate property.
		 */
		public DoubleProperty centerYProperty() { return centerY; }

		/**
		 * Gets the center's y-coordinate.
		 * 
		 * @return The y-coordinate.
		 */
		public double getCenterY() { return centerY.get(); }

		/**
		 * Sets the center's y-coordinate.
		 * 
		 * @param y The y-coordinate.
		 */
		public void setCenterY(double y) { centerY.set(clamp(y, Y_MIN, Y_MAX)); }

		/**
		 * Gets the radius property.
		 * 
		 * @return The radius property.
		 */
		public DoubleProperty radiusProperty() { return radius; }

		/**
		 * Gets the cirlce's radius.
		 * 
		 * @return The shape.
		 */
		public double getRadius() { return radius.get(); }

		/**
		 * Sets the shape's radius.
		 * 
		 * @param r The radius.
		 */
		public void setRadius(double r) { radius.set(clamp(r, RADIUS_MIN, RADIUS_MAX)); }

		/**
		 * Gets the color property.
		 * 
		 * @return The color property.
		 */
		public ObjectProperty<Color> colorProperty() { return color; }

		/**
		 * Gets the shape's color.
		 * 
		 * @return The color.
		 */
		public Color getColor() { return color.get(); }

		/**
		 * Sets the shape's color.
		 * 
		 * @param c The color.
		 */
		public void setColor(Color c) { color.set(c); }

		/**
		 * Checks if the shape contains a point.
		 * 
		 * @param x The x-coordinate of the point.
		 * @param y The y-coordinate of the point.
		 * @return <code>true</code> if the point is contained, <code>false</code> otherwise.
		 */
		public boolean contains(double x, double y) { 
			
			if(type.get() == ShapeType.CIRCLE)
				return (Math.pow(x - centerX.get(), 2) + Math.pow(y - centerY.get(), 2)) <= Math.pow(radius.get(), 2); 
			else if (type.get() == ShapeType.RECTANGLE || type.get() == ShapeType.ROUNDRECT || type.get() == ShapeType.OVAL) {
				return ((x >= getCenterX() - getWidth()/2) && (x <= getCenterX() + getWidth()/2) && (y >= getCenterY() - getHeight()/2) && (y <= getCenterY() + getHeight()/2));
			}
			else if (type.get() == ShapeType.TEXT) {
				return ((x >= getCenterX() - getHeight()*.43*getText().length()/2) && (x <= getCenterX() + getHeight()*.43*getText().length()/2) && (y >= getCenterY() - getHeight()/2) && (y <= getCenterY() + getHeight()/2));
			}
			else
				return false;
			
		}

		/**
		 * Clamps the value between <code>min</code> and <code>max</code>.
		 * 
		 * @param v The value.
		 * @param min The minimum value.
		 * @param max The maximum value.
		 * @return The clamped value.
		 */
		private double clamp(double v, double min, double max) { return (v < min ? min : (v > max ? max : v)); }
		
		public void print() {
			System.out.println(centerX.toString());
		}

	}

}