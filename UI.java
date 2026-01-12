
package plants_app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;



public class UI extends JPanel{
    private final String plant;
    private static Node currentNode;
    private static final Font labelFont = new Font("Arial", Font.PLAIN, 20);

    public UI (String plant) throws SQLException{
        this.plant = plant;
        setLayout(new BorderLayout()); 

// Create a panel for displaying data
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(0, 1)); // Vertical layout
        
// Make dataPanel focusable and request focus -> So it can listen to the key press (escape)
        dataPanel.setFocusable(true);
        dataPanel.requestFocusInWindow();

        // Set the action for the escape key.
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelManager.ChangeToHome("Name");
            }
        };

        // Setup InputMap and ActionMap
        InputMap inputMap = dataPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = dataPanel.getActionMap();

        // Map Escape key to the action
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "escapeAction");
        actionMap.put("escapeAction", escapeAction);
        
        

        // Create a panel for gallery
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        imagePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        ResultSet imageresult = Database.allImages(plant);
        createList (imageresult);
        loadImage(imageLabel);
  
        
        
        // Create a split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dataPanel, imagePanel);
        splitPane.setDividerLocation(500); 
        add(splitPane, BorderLayout.CENTER); 
        
        JButton returnButton = new JButton("Return");
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(returnButton, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        // Fetch and display data to dataPanel
        
        returnButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelManager.ChangeToHome("Name");
            }
        });
        displayData(dataPanel);

        // Button action listeners
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreviousImage();
                try {
                    loadImage(imageLabel);
                } catch (SQLException ex) {
                    Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NextImage();
                try {
                    loadImage(imageLabel);
                } catch (SQLException ex) {
                    Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    private void displayData(JPanel dataPanel) {
        try{
        ResultSet resultSet = Database.selectAll(plant);

        // Process results
        while (resultSet.next()) {
            String plantName = resultSet.getString("Plant_Name");
            String nickname = resultSet.getString("Nickname");
            String phStart = resultSet.getString("PH_start");
            String phEnd = resultSet.getString("PH_end");
            String frost = resultSet.getString("Frost");
            String perennial = resultSet.getString("Perennial");
            String edible = resultSet.getString("Edible");
            String feedType = resultSet.getString("feed_type");
            String feedTime = resultSet.getString("Feed_time");
            String mulch = resultSet.getString("Mulch");
            String sunlightLevels = resultSet.getString("sunlight_levels");
            String waterLevels = resultSet.getString("water_levels");
            String pruningType = resultSet.getString("Pruning_types");
            // Add data to panel
            dataPanel.add(createLabel("Plant Name: " + plantName));
            dataPanel.add(createLabel("Nickname: " + nickname));
            dataPanel.add(createLabel("pH Range: " + phStart + "->"+phEnd));
            dataPanel.add(createLabel("Frost: " + frost));
            dataPanel.add(createLabel("Perennial: " + perennial));
            dataPanel.add(createLabel("Edible: " + edible));
            dataPanel.add(createLabel("Feed Type: " + feedType));
            dataPanel.add(createLabel("Feed Time: " + feedTime));
            dataPanel.add(createLabel("Mulch: " + mulch));
            dataPanel.add(createLabel("Sunlight Levels: " + sunlightLevels));
            dataPanel.add(createLabel("Water Levels: " + waterLevels));
            dataPanel.add(createLabel("Pruning Type: " + pruningType));
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    private void loadImage(JLabel imageLabel) throws SQLException {
        if (currentNode !=null){
            imageLabel.setIcon(new ImageIcon(currentNode.data));
        }
        else{
             imageLabel.setIcon(new ImageIcon(Database.defaultImage()));
        }
        
               
    }
   
    
    private void PreviousImage() {
        if (currentNode != null){
            currentNode = currentNode.prev;
        }
    }

    private void NextImage() {
        if (currentNode != null){
            currentNode = currentNode.next;
        }
    }
    public static JLabel createLabel (String text){
        JLabel label = new JLabel(text);
        label.setFont(labelFont);
        return label;
    }
    
    private static class Node {
        String data;
        Node next;
        Node prev;

        Node(String data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
    public static void createList(ResultSet items) throws SQLException{
        if (!items.next()) {
            currentNode = null; // handle empty ResultSet
            return;
        }
        
        Node head = new Node(Database.createFilepath(items.getString("filename")));
        Node currentnode = head;
       
        
        while (items.next()){
            currentnode.next = new Node(Database.createFilepath(items.getString("filename")));
            currentnode.next.prev = currentnode;
            currentnode = currentnode.next;
        }
        currentnode.next = head;
        head.prev = currentnode;
        
        System.out.println(head.data);
        System.out.println(head.next.data);
        System.out.println(head.prev.data);
        currentNode = head;
        
    }

}