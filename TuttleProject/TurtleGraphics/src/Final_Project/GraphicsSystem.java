package Final_Project;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import uk.ac.leedsbeckett.oop.LBUGraphics;

public class GraphicsSystem extends LBUGraphics {

    private JFrame mainFrame;
    private boolean fill;
    private ArrayList<String> list = new ArrayList<String>();
    private int xPos = 400;  // Center x position
    private int yPos = 300;  // Center y position

    public static void main(String[] args) {
        new GraphicsSystem();
    }

    public GraphicsSystem() {
        mainFrame = new JFrame();
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.add(this);
        mainFrame.setSize(820, 445);

        // Add window listener for exit confirmation
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmExit();
            }
        });

        // Set up menu bar with enhanced options
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem createItem = new JMenuItem("Create");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        fileMenu.add(createItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Draw Menu
        JMenu drawMenu = new JMenu("Draw");
        JMenuItem flagItem = new JMenuItem("Flags");
        JMenuItem shapesItem = new JMenuItem("Shapes");
        
        drawMenu.add(flagItem);
        drawMenu.add(shapesItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem helpItem = new JMenuItem("Help Guide");
        
        helpMenu.add(aboutItem);
        helpMenu.add(helpItem);
        
        // Add Color Menu
        JMenu colorMenu = new JMenu("Colors");
        JMenuItem rainbowItem = new JMenuItem("Rainbow Mode");
        JMenuItem customColorItem = new JMenuItem("Custom RGB");
        JMenuItem presetColorsItem = new JMenuItem("Preset Colors");
        
        colorMenu.add(rainbowItem);
        colorMenu.add(customColorItem);
        colorMenu.add(presetColorsItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(drawMenu);
        menuBar.add(helpMenu);
        menuBar.add(colorMenu);
        
        // Add action listeners
        createItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                String fileName = selectedFile.getName().toLowerCase();
                processFile(selectedFile, fileName);
            }
        });
        
        saveItem.addActionListener(e -> save());
        loadItem.addActionListener(e -> load());
        exitItem.addActionListener(e -> confirmExit());
        
        flagItem.addActionListener(e -> {
            String[] flags = {"Nepal", "Japan", "France", "Germany"};
            String selected = (String) JOptionPane.showInputDialog(mainFrame,
                    "Select a flag to draw:",
                    "Flag Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    flags,
                    flags[0]);
            if (selected != null) {
                drawFlag(selected);
            }
        });
        
        shapesItem.addActionListener(e -> {
            String[] shapes = {"Square", "Triangle", "Circle", "Rectangle"};
            String selected = (String) JOptionPane.showInputDialog(mainFrame,
                    "Select a shape to draw:",
                    "Shape Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    shapes,
                    shapes[0]);
            if (selected != null) {
                String size = JOptionPane.showInputDialog(mainFrame, "Enter size:");
                try {
                    int dimension = Integer.parseInt(size);
                    switch (selected) {
                        case "Square": square(dimension); break;
                        case "Triangle": triangle(dimension, dimension, dimension); break;
                        case "Circle": circle(dimension); break;
                        case "Rectangle": rectangle(dimension, dimension * 2); break;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number");
                }
            }
        });
        
        aboutItem.addActionListener(e -> about());
        helpItem.addActionListener(e -> help());
        
        // Add action listeners for color menu
        rainbowItem.addActionListener(e -> {
            String[] options = {"Rainbow Square", "Rainbow Triangle", "Rainbow Circle"};
            String selected = (String) JOptionPane.showInputDialog(mainFrame,
                    "Select a shape for rainbow effect:",
                    "Rainbow Drawing",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (selected != null) {
                String size = JOptionPane.showInputDialog(mainFrame, "Enter size:");
                try {
                    int dimension = Integer.parseInt(size);
                    switch (selected) {
                        case "Rainbow Square": drawRainbowSquare(dimension); break;
                        case "Rainbow Triangle": drawRainbowTriangle(dimension); break;
                        case "Rainbow Circle": drawRainbowCircle(dimension); break;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number");
                }
            }
        });
        
        customColorItem.addActionListener(e -> {
            String r = JOptionPane.showInputDialog(mainFrame, "Enter Red value (0-255):");
            String g = JOptionPane.showInputDialog(mainFrame, "Enter Green value (0-255):");
            String b = JOptionPane.showInputDialog(mainFrame, "Enter Blue value (0-255):");
            try {
                int red = Integer.parseInt(r);
                int green = Integer.parseInt(g);
                int blue = Integer.parseInt(b);
                if (red >= 0 && red <= 255 && green >= 0 && green <= 255 && blue >= 0 && blue <= 255) {
                    setPenColour(new Color(red, green, blue));
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Color values must be between 0 and 255");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter valid numbers");
            }
        });
        
        presetColorsItem.addActionListener(e -> {
            String[] colors = {"Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet", "Pink", "Purple", "Gold"};
            String selected = (String) JOptionPane.showInputDialog(mainFrame,
                    "Select a color:",
                    "Color Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    colors,
                    colors[0]);
            if (selected != null) {
                switch (selected) {
                    case "Red": setPenColour(Color.RED); break;
                    case "Orange": setPenColour(new Color(255, 165, 0)); break;
                    case "Yellow": setPenColour(Color.YELLOW); break;
                    case "Green": setPenColour(Color.GREEN); break;
                    case "Blue": setPenColour(Color.BLUE); break;
                    case "Indigo": setPenColour(new Color(75, 0, 130)); break;
                    case "Violet": setPenColour(new Color(238, 130, 238)); break;
                    case "Pink": setPenColour(Color.PINK); break;
                    case "Purple": setPenColour(new Color(128, 0, 128)); break;
                    case "Gold": setPenColour(new Color(255, 215, 0)); break;
                }
            }
        });
        
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setVisible(true);

        // Convert OK button text to uppercase
        for (Component comp : this.getComponents()) {
            if (comp instanceof javax.swing.JButton) {
                javax.swing.JButton btn = (javax.swing.JButton) comp;
                if (btn.getText().equalsIgnoreCase("ok")) {
                    btn.setText("OK");
                }
            }
        }

        drawOn();
        displayMessage("I AM Gaurav adhikari");
    }

    private void confirmExit() {
        int option = JOptionPane.showConfirmDialog(mainFrame,
                "Do you want to save your work before exiting?",
                "Exit Confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION);

        switch (option) {
            case JOptionPane.YES_OPTION:
                save();
                mainFrame.dispose();
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION:
                mainFrame.dispose();
                System.exit(0);
                break;
            case JOptionPane.CANCEL_OPTION:
            default:
                // Do nothing, stay in application
                break;
        }
    }

    @Override
    public void drawOn() {
        super.drawOn();
        displayMessage("Pen DOWN - drawing enabled");
    }

    @Override
    public void drawOff() {
        super.drawOff();
        displayMessage("Pen UP - drawing disabled");
    }

    @Override
    public void processCommand(String command) {
        try {
            if (command == null || command.trim().isEmpty()) {
                return;
            }

            String[] parts = command.trim().split("\\s+");
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "left":
                    if (parts.length == 2) {
                        int angle = Integer.parseInt(parts[1]);
                        if (angle < 0) {
                            showError("Angle cannot be negative");
                            return;
                        }
                        left(angle);
                        list.add(command);
                    } else {
                        left(90);
                        list.add("left 90");
                    }
                    break;

                case "right":
                    if (parts.length == 2) {
                        int angle = Integer.parseInt(parts[1]);
                        if (angle < 0) {
                            showError("Angle cannot be negative");
                            return;
                        }
                        right(angle);
                        list.add(command);
                    } else {
                        right(90);
                        list.add("right 90");
                    }
                    break;

                case "forward":
                case "move":
                    if (parts.length == 2) {
                        int distance = Integer.parseInt(parts[1]);
                        if (distance < 0) {
                            showError("Distance cannot be negative");
                            return;
                        }
                        forward(distance);
                        list.add(command);
                    } else {
                        showError("Missing distance parameter");
                    }
                    break;

                case "backward":
                case "reverse":
                    if (parts.length == 2) {
                        int distance = Integer.parseInt(parts[1]);
                        if (distance < 0) {
                            showError("Distance cannot be negative");
                            return;
                        }
                        forward(-distance);
                        list.add(command);
                    } else {
                        showError("Missing distance parameter");
                    }
                    break;

                case "square":
                    if (parts.length < 2) throw new IllegalArgumentException("Missing size parameter");
                    int side = Integer.parseInt(parts[1]);
                    square(side);
                    break;

                case "pencolor":
                    if (parts.length == 4) {
                        try {
                            int r = Integer.parseInt(parts[1]);
                            int g = Integer.parseInt(parts[2]);
                            int b = Integer.parseInt(parts[3]);
                            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                                showError("Color values must be between 0 and 255");
                                return;
                            }
                            setPenColour(new Color(r, g, b));
                            list.add(command);
                        } catch (NumberFormatException e) {
                            showError("Invalid color values");
                        }
                    } else {
                        showError("pencolor requires 3 parameters (R G B)");
                    }
                    break;

                case "penwidth":
                    if (parts.length == 2) {
                        int width = Integer.parseInt(parts[1]);
                        if (width <= 0) {
                            showError("Pen width must be positive");
                            return;
                        }
                        setStroke(width);
                        list.add(command);
                    } else {
                        showError("Missing pen width parameter");
                    }
                    break;

                case "triangle":
                    if (parts.length == 2) {
                        int triangleSize = Integer.parseInt(parts[1]);
                        for (int i = 0; i < 3; i++) {
                            forward(triangleSize);
                            left(120);
                        }
                    } else if (parts.length == 4) {
                        int a = Integer.parseInt(parts[1]);
                        int b = Integer.parseInt(parts[2]);
                        int c = Integer.parseInt(parts[3]);
                        if (!isValidTriangle(a, b, c)) {
                            throw new IllegalArgumentException("Invalid triangle sides");
                        }
                        double angleA = Math.toDegrees(Math.acos((b*b + c*c - a*a)/(2.0*b*c)));
                        double angleB = Math.toDegrees(Math.acos((a*a + c*c - b*b)/(2.0*a*c)));
                        forward(a);
                        left((int)(180.0 - angleB));
                        forward(c);
                        left((int)(180.0 - angleA));
                        forward(b);
                    } else {
                        throw new IllegalArgumentException("Triangle requires 1 or 3 parameters");
                    }
                    break;

                case "circle":
                    if (parts.length == 2) {
                        int radius = Integer.parseInt(parts[1]);
                        if (radius <= 0) {
                            showError("Radius must be positive");
                            return;
                        }
                        circle(radius);
                        list.add(command);
                    } else {
                        showError("Missing radius parameter");
                    }
                    break;

                case "rectangle":
                    if (parts.length == 3) {
                        int width = Integer.parseInt(parts[1]);
                        int height = Integer.parseInt(parts[2]);
                        if (width <= 0 || height <= 0) {
                            showError("Width and height must be positive");
                            return;
                        }
                        rectangle(width, height);
                        list.add(command);
                    } else {
                        showError("rectangle requires 2 parameters (width height)");
                    }
                    break;

                case "flag":
                    if (parts.length == 2) {
                        drawFlag(parts[1]);
                        list.add(command);
                    } else {
                        showError("flag requires a country parameter");
                    }
                    break;

                case "black":
                    setPenColour(Color.black);
                    list.add(command);
                    break;
                case "green":
                    setPenColour(Color.green);
                    list.add(command);
                    break;
                case "red":
                    setPenColour(Color.red);
                    list.add(command);
                    break;
                case "white":
                    setPenColour(Color.white);
                    list.add(command);
                    break;
                case "drawoff":
                case "penup":
                    drawOff();
                    list.add(command);
                    break;
                case "drawon":
                case "pendown":
                    drawOn();
                    list.add(command);
                    break;
                case "about":
                    about();
                    list.add(command);
                    break;
                case "reset":
                    reset();
                    drawOn();
                    list.add(command);
                    break;
                case "clear":
                    clear();
                    list.add(command);
                    break;
                case "randomcolor":
                    randomColor();
                    list.add(command);
                    break;
                case "help":
                    help();
                    list.add(command);
                    break;
                case "save":
                    save();
                    break;
                case "load":
                    load();
                    break;
                default:
                    showError("Unknown command: " + command);
            }
        } catch (NumberFormatException e) {
            showError("Invalid number format in command: " + command);
        } catch (Exception e) {
            showError("Error processing command: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void fileloader() {
        int box = JOptionPane.showConfirmDialog(mainFrame, "Want to load file?");
        if (box == JOptionPane.YES_OPTION) {
            String confirm = JOptionPane.showInputDialog(mainFrame, "1. Load Image\n2. Load text commands");
            if (confirm.equals("1")) {
                loadPhoto();
            } else if (confirm.equals("2")) {
                loadInstruct();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Instruct Not Valid", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void load() {
        int warn = JOptionPane.showConfirmDialog(mainFrame, "Want to save this file first?");
        if (warn == JOptionPane.YES_OPTION) {
            save();
            fileloader();
        } else if (warn == JOptionPane.NO_OPTION) {
            fileloader();
        }
    }

    private void loadInstruct() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setDialogTitle("Save As");
        int cfile = chooseFile.showOpenDialog(this);
        if (cfile == JFileChooser.APPROVE_OPTION) {
            Path location = Paths.get(chooseFile.getSelectedFile().getAbsolutePath());
            try {
                reset();
                drawOn();
                Scanner scan = new Scanner(location);
                while (scan.hasNextLine()) {
                    String line = scan.nextLine().trim();
                    if (!line.isEmpty()) {
                        processCommand(line);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, "Error while loading Instruct: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadPhoto() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setDialogTitle("Save as");
        int cfile = chooseFile.showOpenDialog(this);
        if (cfile == JFileChooser.APPROVE_OPTION) {
            String location = chooseFile.getSelectedFile().getAbsolutePath();
            try {
                BufferedImage image = ImageIO.read(new File(location));
                JFrame frame = new JFrame();
                frame.setTitle(new File(location).getName());
                frame.setLayout(new FlowLayout());
                frame.add(new JLabel(new ImageIcon(image)));
                frame.pack();
                frame.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error while loading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void save() {
        int dbox = JOptionPane.showConfirmDialog(mainFrame, "Want to save file?");
        if (dbox == JOptionPane.YES_OPTION) {
            String opt = JOptionPane.showInputDialog(mainFrame, "1. Save Photo\n2. Save text command:");
            if (opt == null) return;

            try {
                int cmd = Integer.parseInt(opt);
                if (cmd == 1) {
                    savePhoto();
                } else if (cmd == 2) {
                    instructSave();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid option", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a number (1 or 2)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void instructSave() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setDialogTitle("Save as");
        int cmd = chooseFile.showSaveDialog(this);
        if (cmd == JFileChooser.APPROVE_OPTION) {
            Path location = Paths.get(chooseFile.getSelectedFile().getAbsolutePath());
            try {
                Files.write(location, list);
                JOptionPane.showMessageDialog(mainFrame, "All commands saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, "Error saving commands: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void savePhoto() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setDialogTitle("Save as");
        int img = chooseFile.showSaveDialog(this);
        if (img == JFileChooser.APPROVE_OPTION) {
            try {
                String path = chooseFile.getSelectedFile().getAbsolutePath();
                if (!path.toLowerCase().endsWith(".png")) {
                    path += ".png";
                }
                ImageIO.write(getBufferedImage(), "png", new File(path));
                JOptionPane.showMessageDialog(mainFrame, "Image saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rectangle(int width, int height) {
        Graphics canvas = getGraphicsContext();
        if (fill) {
            canvas.fillRect(xPos - width / 2, yPos - height / 2, width, height);
        } else {
            canvas.drawRect(xPos - width / 2, yPos - height / 2, width, height);
        }
    }

    private void triangle(int sideA, int sideB, int sideC) {
        Graphics canvas = getGraphicsContext();
        int[] xPoints = {xPos, xPos + sideA, xPos - sideB};
        int[] yPoints = {yPos, yPos + sideC, yPos + sideC};

        if (fill) {
            canvas.fillPolygon(xPoints, yPoints, 3);
        } else {
            canvas.drawPolygon(xPoints, yPoints, 3);
        }
    }

    private void randomColor() {
        Random rgb = new Random();
        setPenColour(new Color(rgb.nextInt(256), rgb.nextInt(256), rgb.nextInt(256)));
    }

    private boolean isValidTriangle(int a, int b, int c) {
        // Triangle inequality theorem: sum of any two sides must be greater than the third side
        return (a + b > c) && (b + c > a) && (a + c > b);
    }

    private void square(int side) {
        // Draw square with animation
        for (int i = 0; i < 4; i++) {
            // Draw each side with animation
            for (int j = 0; j <= side; j += 5) {
                forward(5);  // Move in small increments
                try {
                    Thread.sleep(20);  // Small delay for animation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            left(90);  // Turn 90 degrees for next side
        }
    }

    private void drawFlag(String country) {
        Graphics g = getGraphicsContext();
        clear();
        reset();

        switch (country.toLowerCase()) {
            case "nepal":
                g.setColor(Color.red);
                g.fillPolygon(new int[]{xPos, xPos + 50, xPos}, new int[]{yPos, yPos, yPos - 50}, 3);
                g.fillPolygon(new int[]{xPos, xPos + 50, xPos}, new int[]{yPos, yPos, yPos + 50}, 3);
                break;

            case "japan":
                g.setColor(Color.white);
                g.fillRect(xPos - 75, yPos - 50, 150, 100);
                g.setColor(Color.red);
                g.fillOval(xPos - 25, yPos - 25, 50, 50);
                break;

            case "france":
                g.setColor(Color.blue);
                g.fillRect(xPos - 75, yPos - 50, 50, 100);
                g.setColor(Color.white);
                g.fillRect(xPos - 25, yPos - 50, 50, 100);
                g.setColor(Color.red);
                g.fillRect(xPos + 25, yPos - 50, 50, 100);
                break;

            case "germany":
                g.setColor(Color.black);
                g.fillRect(xPos - 75, yPos - 50, 150, 33);
                g.setColor(Color.red);
                g.fillRect(xPos - 75, yPos - 17, 150, 33);
                g.setColor(Color.yellow);
                g.fillRect(xPos - 75, yPos + 16, 150, 33);
                break;

            default:
                JOptionPane.showMessageDialog(null, "Unsupported country: " + country, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void about() {
        super.about();
        Graphics g = getGraphicsContext();

        // Set larger font
        g.setFont(new Font("Arial", Font.BOLD, 30));

        // Draw each letter with different colors
        String name = "Gaurav Adhikari";
        int x = 150;
        int y = 300;

        // Colors for each letter
        Color[] colors = {
                Color.RED,      // G
                Color.BLUE,     // a
                Color.GREEN,    // u
                Color.ORANGE,   // r
                Color.MAGENTA,  // a
                Color.CYAN,     // v
                Color.YELLOW,   //
                Color.PINK,     // A
                Color.GRAY,     // d
                Color.RED,      // h
                Color.BLUE,     // i
                Color.GREEN,    // k
                Color.ORANGE,   // a
                Color.MAGENTA,  // r
                Color.CYAN      // i
        };

        // Draw each letter with its own color
        for (int i = 0; i < name.length(); i++) {
            g.setColor(colors[i]);
            g.drawString(String.valueOf(name.charAt(i)), x + (i * 25), y);
        }
    }

    private void help() {
        String title = "Turtle Graphics Help Guide";
        String detail = "<html><body style='width: 400px; font-family: Arial;'>" +
                "<h2 style='color: #2c3e50;'>Turtle Graphics Command Reference</h2>" +
                "<div style='background-color: #f8f9fa; padding: 10px; border-radius: 5px; margin-bottom: 15px;'>" +
                "<h3 style='color: #3498db;'>Basic Commands:</h3>" +
                "<ul>" +
                "<li><b>about</b>: Display the turtle dance and author information</li>" +
                "<li><b>drawOff</b>: Lift the pen (movement won't draw)</li>" +
                "<li><b>drawOn</b>: Lower the pen (movement will draw)</li>" +
                "<li><b>clear</b>: Clear the drawing canvas</li>" +
                "<li><b>reset</b>: Reset turtle position and direction</li>" +
                "<li><b>save/load</b>: Save or load commands/images</li>" +
                "<li><b>help</b>: Display this help menu</li>" +
                "</ul></div>" +
                "<div style='background-color: #f8f9fa; padding: 10px; border-radius: 5px; margin-bottom: 15px;'>" +
                "<h3 style='color: #3498db;'>Color Commands:</h3>" +
                "<ul>" +
                "<li><b>black/green/red/white</b>: Set pen color</li>" +
                "<li><b>randomcolor</b>: Random pen color</li>" +
                "<li><b>pencolor R G B</b> (0-255 each): Custom RGB color</li>" +
                "<li><b>penwidth N</b> (N>0): Set pen thickness</li>" +
                "</ul></div>" +
                "<div style='background-color: #f8f9fa; padding: 10px; border-radius: 5px; margin-bottom: 15px;'>" +
                "<h3 style='color: #3498db;'>Drawing Shapes:</h3>" +
                "<ul>" +
                "<li><b>circle radius</b> (radius>0)</li>" +
                "<li><b>rectangle width height</b> (both>0)</li>" +
                "<li><b>square side</b> (side>0)</li>" +
                "<li><b>triangle side</b> (equilateral, side>0)</li>" +
                "<li><b>triangle a b c</b> (three sides, all>0)</li>" +
                "<li><b>flag country</b> (nepal/japan/france/germany)</li>" +
                "</ul></div>" +
                "<div style='background-color: #f8f9fa; padding: 10px; border-radius: 5px; margin-bottom: 15px;'>" +
                "<h3 style='color: #3498db;'>Movement Commands:</h3>" +
                "<ul>" +
                "<li><b>forward N</b>/<b>move N</b> (N>0)</li>" +
                "<li><b>backward N</b>/<b>reverse N</b> (N>0)</li>" +
                "<li><b>left degrees</b> (0-360)</li>" +
                "<li><b>right degrees</b> (0-360)</li>" +
                "</ul></div>" +
                "<div style='background-color: #f8f9fa; padding: 10px; border-radius: 5px; margin-bottom: 15px;'>" +
                "<h3 style='color: #3498db;'>Menu Features:</h3>" +
                "<ul>" +
                "<li><b>File Menu</b>: Create, Save, Load, Exit</li>" +
                "<li><b>Draw Menu</b>: Flags, Shapes</li>" +
                "<li><b>Colors Menu</b>: Rainbow Mode, Custom RGB, Preset Colors</li>" +
                "<li><b>Help Menu</b>: About, Help Guide</li>" +
                "</ul></div>" +
                "<div style='background-color: #f8f9fa; padding: 10px; border-radius: 5px; margin-bottom: 15px;'>" +
                "<h3 style='color: #3498db;'>Rainbow Features:</h3>" +
                "<ul>" +
                "<li><b>Rainbow Square</b>: Each side has a different rainbow color</li>" +
                "<li><b>Rainbow Triangle</b>: Each side has a different rainbow color</li>" +
                "<li><b>Rainbow Circle</b>: Creates a perfect rainbow ring</li>" +
                "</ul></div>" +
                "<div style='background-color: #f8f9fa; padding: 10px; border-radius: 5px; margin-bottom: 15px;'>" +
                "<h3 style='color: #3498db;'>Preset Colors:</h3>" +
                "<ul>" +
                "<li>Red, Orange, Yellow, Green, Blue</li>" +
                "<li>Indigo, Violet, Pink, Purple, Gold</li>" +
                "</ul></div>" +
                "<div style='background-color: #e74c3c; color: white; padding: 10px; border-radius: 5px;'>" +
                "<h3 style='margin-top: 0;'>Bounds and Error Handling:</h3>" +
                "<ul>" +
                "<li>Turtle will <b>wrap around</b> if moved off screen edges</li>" +
                "<li>Negative values for distances/angles <b>not allowed</b></li>" +
                "<li>RGB values must be <b>0-255</b></li>" +
                "<li>Shape dimensions must be <b>positive integers</b></li>" +
                "<li>Invalid commands show <b>error messages</b></li>" +
                "<li>Parameter validation for all commands</li>" +
                "</ul></div>" +
                "<p style='text-align: center; font-style: italic; color: #7f8c8d;'>" +
                "Graphics System by Gaurav Adhikari</p>" +
                "</body></html>";

        JOptionPane.showMessageDialog(null, detail, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void validatePositiveParameter(int value, String paramName) {
        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " must be positive");
        }
    }

    private Point getTurtlePosition() {
        return new Point(xPos, yPos);
    }

    private double getTurtleAngle() {
        return getDirection();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void forward(int distance) {
        // Calculate new position
        int newX = xPos + (int)(distance * Math.sin(Math.toRadians(getDirection())));
        int newY = yPos - (int)(distance * Math.cos(Math.toRadians(getDirection())));

        // Define bounds relative to center (400, 300)
        int centerX = 400;
        int centerY = 300;
        int maxDistance = 250; // Maximum distance from center

        // Calculate distance from center
        double distanceFromCenter = Math.sqrt(
                Math.pow(newX - centerX, 2) +
                        Math.pow(newY - centerY, 2)
        );

        // Check if new position is within bounds
        if (distanceFromCenter > maxDistance) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Warning: Turtle is going out of bounds!\n" +
                            "Current position: (" + xPos + ", " + yPos + ")\n" +
                            "Attempted move to: (" + newX + ", " + newY + ")\n" +
                            "Distance from center: " + (int)distanceFromCenter + " (max allowed: " + maxDistance + ")\n" +
                            "Please stay within " + maxDistance + " units from center",
                    "Out of Bounds Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // If within bounds, proceed with movement
        super.forward(distance);
    }

    private void processFile(File selectedFile, String fileName) {
        try {
            if (fileName.endsWith(".txt")) {
                try (Scanner scanner = new Scanner(selectedFile)) {
                    while (scanner.hasNextLine()) {
                        String command = scanner.nextLine().trim();
                        if (!command.isEmpty()) {
                            processCommand(command);
                        }
                    }
                }
            } else if (fileName.endsWith(".png")) {
                BufferedImage image = ImageIO.read(selectedFile);
                this.setBufferedImage(image);
            } else {
                JOptionPane.showMessageDialog(null, "Unsupported file type", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error processing file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawRainbowSquare(int size) {
        for (int i = 0; i < 4; i++) {
            // Create rainbow effect for each side
            float hue = (float) i / 4;
            Color rainbowColor = Color.getHSBColor(hue, 1.0f, 1.0f);
            setPenColour(rainbowColor);
            
            // Draw side with animation
            for (int j = 0; j <= size; j += 5) {
                forward(5);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            left(90);
        }
    }

    private void drawRainbowTriangle(int size) {
        for (int i = 0; i < 3; i++) {
            // Create rainbow effect for each side
            float hue = (float) i / 3;
            Color rainbowColor = Color.getHSBColor(hue, 1.0f, 1.0f);
            setPenColour(rainbowColor);
            
            // Draw side with animation
            for (int j = 0; j <= size; j += 5) {
                forward(5);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            left(120);
        }
    }

    private void drawRainbowCircle(int radius) {
        int steps = 360;
        for (int i = 0; i < steps; i++) {
            // Create rainbow effect
            float hue = (float) i / steps;
            Color rainbowColor = Color.getHSBColor(hue, 1.0f, 1.0f);
            setPenColour(rainbowColor);
            
            forward(1);
            left(1);
            
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}