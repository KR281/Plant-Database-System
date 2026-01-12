/*
Contains a search bar, order by section, list of plants. Scroll bar to the right?
Search bar at top, order by below it, list of plants below that.

Overall layout must be BorderLayout so that the main bulk can be in the centre.
*/
package plants_app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Home extends JPanel{
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();

    
    public Home(String orderBy) throws SQLException {
/*      Main panel
            ->SearchAndOrder -> Grid 
                -> SearchPanel -> Border
                    -> TextField, Button
                -> orderPanel -> JPanel
                    -> JLabel, JComboBox
            ->PlantPanel -> Grid
                    -> JButton
                        -> Plantname, plant image
        
*/
        // Main layout
        setLayout(new BorderLayout());
        
        JPanel searchAndOrder = new JPanel();
        searchAndOrder.setLayout(new GridLayout(0, 1));
        
        // Search Bar
        
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        
        // Search bar and search button.
        JTextField searchField = new JTextField("");
        JButton searchButton = new JButton("Search");
        
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Order By Section
        JPanel orderPanel = new JPanel();
        JLabel orderByLabel = new JLabel("Order by:");
        String[] orderOptions = {"Name (ASC)", "Name (DESC)", "Min PH", "Max PH"};
        JComboBox<String> orderComboBox = new JComboBox<>(orderOptions);
        orderPanel.add(orderByLabel);
        orderPanel.add(orderComboBox);
        
        searchAndOrder.add(searchPanel);
        searchAndOrder.add(orderPanel);

        // Plant Display Section
        JPanel plantPanel = new JPanel();
        plantPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 0 rows (dynamically made), 4 columns, gap 10 sides.
        
       
        // Add components to the main frame
        add(searchAndOrder, BorderLayout.NORTH);
        add(new JScrollPane(plantPanel), BorderLayout.CENTER);
        
        //Buttons.
        ResultSet result = Database.getButtons(orderBy);
        while(result.next()){
            String name = result.getString("Plant_Name");
            String filepath = Database.createFilepath(result.getString("filepath"));
            addPlantButton(plantPanel,name,filepath);
        }
        searchButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String plant = searchField.getText();
                handleSearch(plant);
            }
        });
        
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String plant = searchField.getText();
                    handleSearch(plant);
                }
            }
            @Override
            public void keyTyped(KeyEvent ke) {
            }
            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        
        orderComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOrder = (String) orderComboBox.getSelectedItem();
                PanelManager.ChangeToHome(selectedOrder);

            }
        });
        
        

        setVisible(true);
    }
    

    private void handleSearch(String plant){
        try {
            if (Database.testPlant(plant) == true){
                PanelManager.ChangeToUI(plant);
            }
            else{
                JOptionPane.showMessageDialog(null, "Plant not found!", "Pop-Up", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void addPlantButton(JPanel panel, String plantName, String imagePath) {
        JButton plantButton = new JButton(plantName);

        //  button size
        int buttonWidth = 350; 
        int buttonHeight = 300;
        ImageIcon icon = imageCache.get(imagePath);
        if (icon == null){
            try {
                // load and resize image.
                BufferedImage originalImage = ImageIO.read(new File(imagePath));
                // Using Graphics 2D (original, 15.56 seconds. G2D = 6.63 seconds) See readme for more details.
                BufferedImage resizedImage = new BufferedImage(buttonWidth, buttonHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resizedImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.drawImage(originalImage, 0, 0, buttonWidth, buttonHeight, null);
                g2d.dispose(); // Clean up the Graphics2D object -> better than auto garbage collection because it is instant

                //Image resizedImage = originalImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
                icon = new ImageIcon(resizedImage);           
                imageCache.put(imagePath, icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        plantButton.setIcon(icon);
        plantButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        plantButton.setHorizontalTextPosition(SwingConstants.CENTER);
        plantButton.setText(plantName);
        plantButton.setVerticalAlignment(SwingConstants.CENTER); 
        plantButton.setHorizontalAlignment(SwingConstants.CENTER);


        plantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelManager.ChangeToUI(plantName);
            }
        });
        
        panel.add(plantButton);
    }
      
}
