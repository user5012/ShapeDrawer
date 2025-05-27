package shape_drawer.window;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;

public class MySubWindow {
    private String title;
    private int width;
    private int height;
    private JDialog jDialog;
    private List<JComponent> components = new ArrayList<>();
    private boolean isResizable;

    /**
     * Constructor for MySubWindow.
     *
     * @param title       The title of the sub-window.
     * @param width       The width of the sub-window.
     * @param height      The height of the sub-window.
     * @param components  A list of JComponents to be added to the sub-window.
     * @param isResizable Whether the sub-window is resizable.
     */

    public MySubWindow(JFrame parent, String title, int width, int height, List<JComponent> components,
            boolean isResizable) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.jDialog = new JDialog(parent, title, true);
        this.components = components;
        this.isResizable = isResizable;
        initialize();
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.FlowLayout()); // Set a layout manager for the panel
        for (JComponent component : components) {
            panel.add(component);
        }
        return panel;
    }

    private void initialize() {
        jDialog.setTitle(title);
        jDialog.setSize(width, height);
        jDialog.setLocationRelativeTo(null); // Center the window on the screen
        jDialog.setContentPane(createContentPanel());
        jDialog.setResizable(isResizable); // Set the resizable property
        System.out.println("Sub-window initialized: " + title + " with dimensions " + width + "x" + height);
    }

    public void show() {
        System.out.println("Showing sub-window: " + title + " with dimensions " + width + "x" + height);
        jDialog.setVisible(true);
    }

    public void exit() {
        System.out.println("Exiting sub-window: " + title);
        jDialog.dispose();
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
