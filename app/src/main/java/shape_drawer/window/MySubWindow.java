package shape_drawer.window;

import java.util.List;
import java.awt.LayoutManager;
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
    private LayoutManager layout;
    private boolean wantLayout;

    /**
     * Constructor for MySubWindow.
     *
     * @param title       The title of the sub-window.
     * @param width       The width of the sub-window.
     * @param height      The height of the sub-window.
     * @param components  A list of JComponents to be added to the sub-window.
     * @param isResizable Whether the sub-window is resizable.
     * @param layout      The layout manager to be used for the sub-window.
     */

    public MySubWindow(JFrame parent, String title, int width, int height, List<JComponent> components,
            boolean isResizable, LayoutManager layout, boolean wantLayout) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.jDialog = new JDialog(parent, title, true);
        this.components = components;
        this.isResizable = isResizable;
        this.layout = layout;
        this.wantLayout = wantLayout;
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
        if (wantLayout) {
            jDialog.getContentPane().setLayout(layout); // Set the layout manager if provided
        }
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
