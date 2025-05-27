package shape_drawer.window;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import shape_drawer.config.MySettings;
import shape_drawer.shape.Circle;
import shape_drawer.shape.DrawnLine;
import shape_drawer.shape.MyShape;
import shape_drawer.shape.Rectangle;
import shape_drawer.usefull.Vector2d;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.io.File;;

public class MyWindow {
    public static enum ButtonPressedType {
        ADD_CIRCLE, ADD_RECTANGLE, SELECT_SHAPE, DRAW_LINE, NONE // Enum to represent different shape types
    }

    String title;
    int width;
    int height;
    private JFrame frame;
    private List<JButton> buttons;
    private List<MyShape> shapes = new ArrayList<>(); // List to hold all shapes
    public ButtonPressedType buttonPressedType = ButtonPressedType.NONE; // Variable to store the type of button
    private final List<MyShape> selectedShapes = new ArrayList<>(); // List to hold selected shapes
    private final Map<MyShape, Point> dragOffsets = new HashMap<>();
    private boolean clickedShape = false; // Flag to check if a shape is clicked
    private Vector2d canvasDimensionsVector2d = new Vector2d(500, 500); // Variable to store mouse position
    private JPanel canva = new JPanel(); // Panel to draw shapes
    private DrawnLine currLine = null;
    private MySettings settings = new MySettings(); // Settings object to manage save path

    public MyWindow() {
        this.title = "Shape Drawer"; // Set the title of the window
        this.width = 400; // Set the width of the window
        this.height = 800; // Set the height of the window
        this.buttons = createButtons(); // Create buttons for the window
        craftWindow();

    }

    private JPanel canvas(int canvasWidth, int canvasHeight) {
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (MyShape shape : shapes) {
                    shape.draw(g);
                }
            }
        };
        canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight)); // Set the preferred size of the panel
        canvas.setBackground(Color.lightGray); // Set the background color of the panel
        canvas.setLayout(null); // Use null layout for absolute positioning
        canva = canvas; // Assign the canvas to the class variable
        return canvas; // Return the panel
    }

    private JPanel buttonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout()); // Use FlowLayout for the buttons panel
        buttonsPanel.setPreferredSize(new Dimension(200, 500));
        buttonsPanel.setLayout(new GridLayout(0, 1)); // Use GridLayout for the buttons panel
        buttonsPanel.setBackground(Color.lightGray); // Set the background color of the buttons panel
        for (JButton button : buttons) {
            buttonsPanel.add(button); // Add the button to the buttons panel
        }
        return buttonsPanel; // Return the buttons panel
    }

    private void craftWindow() {
        this.frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLayout(new FlowLayout()); // Use FlowLayout for the frame
        frame.add(canvas(500, 500)); // Add the canvas to the frame
        frame.add(buttonsPanel()); // Add the buttons panel to the frame
        frame.pack(); // Pack the frame to fit the components

    }

    private static JButton createButton(String text, int x, int y, int width, int height, Runnable action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height); // Set button position and size
        button.addActionListener(e -> action.run()); // Add action listener to the button
        return button;
    }

    private List<JButton> createButtons() {
        List<JButton> buttons = new ArrayList<>();
        buttons.add(createButton("Add Circle", 100, 50, 100, 30, () -> {
            buttonPressedType = ButtonPressedType.ADD_CIRCLE; // Set the button pressed type to
        }));
        buttons.add(createButton("Add Rectangle", 100, 50, 100, 90, () -> {
            buttonPressedType = ButtonPressedType.ADD_RECTANGLE; // Set the button pressed type
                                                                 // to
        }));
        buttons.add(createButton("Select Shape", 100, 50, 100, 130, () -> {
            buttonPressedType = ButtonPressedType.SELECT_SHAPE; // Set the button pressed type
        }));
        buttons.add(createButton("Move All Shapes", 100, 50, 100, 90, () -> {
            for (MyShape shape : shapes) {
                shape.setX(shape.getX() + 10); // Move all shapes to the right by 10 pixels
                shape.setY(shape.getY() + 10); // Move all shapes down by 10 pixels
            }
            frame.repaint(); // Repaint the frame to update the shapes
        }));
        buttons.add(createButton("Draw Line", 100, 50, 100, 130, () -> {
            buttonPressedType = ButtonPressedType.DRAW_LINE;
        }));
        buttons.add(createButton("Delete All Shapes", 100, 50, 100, 130, () -> {
            shapes.clear(); // Clear the list of shapes
            selectedShapes.clear(); // Clear the list of selected shapes
            currLine = null; // Clear the current line
            frame.repaint(); // Repaint the frame to update the shapes
        }));
        buttons.add(createButton("Export Image", 100, 50, 100, 170, () -> {
            // saveImage(getJSONPath()); // Call the saveImage method to export the image

            SwingUtilities.invokeLater(() -> saveImage(settings.getSavePath())); // Use SwingUtilities to ensure the
                                                                                 // saveImage
            // method is called on the Event Dispatch Thread
        }));
        buttons.add(createButton("change Save Path", 100, 50, 100, 210, () -> {
            java.util.List<JComponent> components = new ArrayList<>();

            // Create a mutable holder for subWindow
            final MySubWindow[] subWindow = new MySubWindow[1];

            components.add(new JLabel("Enter new save path:"));
            JTextField pathField = new JTextField(settings.getSavePath(), 20);
            components.add(pathField);

            JButton saveButton = createButton("Save", 100, 50, 100, 250, () -> {
                String newPath = pathField.getText();
                settings.updateJson(newPath);
                settings.setSavePath(newPath);
                subWindow[0].exit(); // Access the real subWindow object
            });
            components.add(saveButton);

            // Now create and assign the subWindow
            subWindow[0] = new MySubWindow(frame, "Change Save Path", 300, 200, components, false);
            subWindow[0].show(); // Show the subWindow
        }));

        return buttons;
    }

    public void ShowWindow() {
        frame.pack(); // Pack the frame to fit the components
        frame.setVisible(true);
        initializeMouseListeners(); // Call the mouse handler to check for clicks
    }

    public void repaint() {
        frame.repaint(); // Repaint the frame to update the shapes
        canvas(canvasDimensionsVector2d.getX(), canvasDimensionsVector2d.getY()).repaint(); // Repaint the panel to
                                                                                            // update the shapes
    }

    private void saveImage(String savePath) {
        if (canva == null) {
            System.err.println("Error: Canvas is not initialized. Cannot export image.");
            return; // Abort if the canvas is not initialized to avoid crash
        }
        canva.revalidate();
        canva.doLayout();
        canva.repaint();

        Dimension preferredSize = canva.getPreferredSize();

        int width = preferredSize.width; // Get the width of the canvas
        int height = preferredSize.height; // Get the height of the canvas
        if (width <= 0 || height <= 0) {
            System.err.println(
                    "Error: Canvas size is invalid. Cannot export image.\nWidth: " + width + ", Height: " + height);
            return; // Abort if the canvas size is invalid to avoid crash
        }

        // Use preferred size if actual size is not valid yet
        /*
         * if (width <= 0 || height <= 0) {
         * Dimension preferredSize = canva.getPreferredSize();
         * width = preferredSize.width;
         * height = preferredSize.height;
         * 
         * // Still bad? Abort to avoid crash
         * if (width <= 0 || height <= 0) {
         * System.err.println("Error: Canvas size is invalid. Cannot export image.");
         * return;
         * }
         * }
         */

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(canva.getBackground());
        g2d.fillRect(0, 0, width, height);

        // Paint shapes directly
        for (MyShape shape : shapes) {
            shape.draw(g2d);
        }

        g2d.dispose(); // Dispose of the graphics context to release resources

        try {
            ImageIO.write(image, "png", new File(savePath));
            System.out.println("Image saved to " + savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeMouseListeners() {
        canva.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                super.mousePressed(e);
                dragOffsets.clear(); // Clear the drag offsets map
                for (MyShape shape : selectedShapes) {
                    int offsetX = e.getX() - shape.getX(); // Calculate the offset between the mouse position and the
                                                           // shape's position
                    int offsetY = e.getY() - shape.getY(); // Calculate the offset between the mouse position and the
                                                           // shape's position
                    dragOffsets.put(shape, new Point(offsetX, offsetY)); // Store the offset in the drag offsets map

                }
            }

            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                super.mouseReleased(e);
                dragOffsets.clear(); // Clear the drag offsets map when the mouse is releasedW
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switch (buttonPressedType) {
                    case ADD_CIRCLE:
                        addCircle(e.getX(), e.getY(), 30); // Add a circle at the mouse position
                        break;
                    case ADD_RECTANGLE:
                        addRectangle(e.getX(), e.getY()); // Add a rectangle at the mouse position
                        break;
                    case SELECT_SHAPE:
                        clickedShape = false; // Reset the clicked shape flag
                        for (MyShape shape : shapes) {
                            if (shape.isPointInside(e.getX(), e.getY())) { // Check if the mouse click is inside the
                                                                           // shape
                                if (shape.isSelected()) {
                                    shape.deselectShape(); // Deselect the shape
                                    selectedShapes.remove(shape); // Remove the shape from the selected shapes list
                                } else {
                                    shape.selectShape(); // Select the shape
                                    selectedShapes.add(shape); // Add the shape to the selected shapes list
                                }
                                clickedShape = true; // Set the clicked shape flag to true

                            }
                        }

                        if (!clickedShape) {
                            for (MyShape shape : shapes) {
                                shape.deselectShape(); // Deselect all shapes if no shape is clicked
                            }
                            selectedShapes.clear(); // Clear the selected shapes list
                        }
                        repaint(); // Repaint the frame to update the shapes
                        break;
                    default:
                        break;
                }
            }
        });

        canva.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                switch (buttonPressedType) {
                    case SELECT_SHAPE:
                        for (MyShape shape : selectedShapes) {
                            Point offset = dragOffsets.get(shape); // Get the offset for the shape
                            if (offset != null) {
                                shape.setX(e.getX() - offset.x); // Move the selected shape to the mouse position
                                shape.setY(e.getY() - offset.y); // Move the selected shape to the mouse position
                            }
                            repaint(); // Repaint the frame to update the shapes
                        }
                        break;
                    case DRAW_LINE:
                        if (currLine == null) {
                            currLine = new DrawnLine(); // Create a new line if it doesn't exist
                            shapes.add(currLine); // Add the line to the list of lines
                        }
                        currLine.addPoint(e.getX(), e.getY()); // Add the current mouse position to the line
                        repaint(); // Repaint the frame to update the shapes
                        break;
                    default:
                        break;

                }
            }
        });
    }

    public void addCircle(int x, int y, int radius) {
        shapes.add(new Circle(radius, x, y)); // Add a new circle to the list with a radius of 50
        repaint(); // Repaint the frame to update the shapes
    }

    public void addRectangle(int x, int y) {
        shapes.add(new Rectangle(50, 30, x, y)); // Add a new rectangle to the list with width 50 and height 30
        repaint(); // Repaint the frame to update the shapes
    }
}
